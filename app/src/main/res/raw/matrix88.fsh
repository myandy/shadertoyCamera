precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;


// inputs:
//  iChannel0: webcam image
//  iChannel1: matrix characters image.
//             should be a small b&w image with a row of pixelated matrix-font characters.
//             for example, a 64 x 16 image with a row of 8 x 16 characters.


const float N_CHARS = 8.0;   // how many characters are in the character image
const float Y_PIXELS = 18.0; // reduce input image to this many mega-pixels across
const float DROP_SPEED = 0.15;
const float ASPECT = 2.7;    // aspect ratio of input webcam image
                      // can play with this to get non-square matrix characters
const float MIN_DROP_SPEED = 0.2; // range 0-1.  is added to column speeds to avoid stopped columns.
const float STATIC_STRENGTH = 0.1; // range 0-1.  how intense is the tv static
const float SCANLINE_STRENGTH = 0.4; // range 0-1.  how dark are the tv scanlines
const float NUM_SCANLINES = 70.0; // how many scanlines

// random functions adapted from https://www.shadertoy.com/view/lsXSDn
float rand2d(vec2 v){
    return fract(sin(dot(v.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float rand(float x) {
    return fract(sin(x) * 3928.2413);
}

void main()
{
    vec2 uv = textureCoordinate.xy / iResolution.xy;

    // pixelate webcam image into mega-pixels
    float xPix = floor(uv.x * Y_PIXELS * ASPECT) / Y_PIXELS / ASPECT;
    float yOffs = mod(iTime * DROP_SPEED * (rand(xPix) + MIN_DROP_SPEED), 1.0);
    float yPix = floor((uv.y + yOffs) * Y_PIXELS) / Y_PIXELS - yOffs;
    vec2 uvPix = vec2(xPix, yPix);
    // ideally we should blur the input image to reduce flickering
    vec4 pixelColor = texture2D(inputImageTexture, uvPix);

    // compute uv within each mega-pixel
    vec2 uvInPix = vec2(
    	mod(uv.x * Y_PIXELS, 1.0),
    	mod((uv.y + yOffs) * Y_PIXELS, 1.0)
    );

    // offset char image to appropriate char
    float charOffset = floor(pixelColor.r * N_CHARS) / N_CHARS;
    uvInPix.x = uvInPix.x / N_CHARS + charOffset;
    vec4 charColor = texture2D(inputImageTexture2, uvInPix);

    // multiply char images and webcam mega-pixels
    float result = charColor.r * pixelColor.r;

    // add scanlines
    result *= 1.0 - SCANLINE_STRENGTH * (sin(uv.y * NUM_SCANLINES*3.14159*2.0)/2.0+0.5);

	// and map to a black->green->white gradient
    vec4 outColor = vec4(
        max(0.0, result*3.0-1.2),
        result*1.6,
        max(0.0, result*3.0-1.5),
        1.0
    );

    // add tv static
    //float stat = rand(uv.x * uv.y * 0.12798 + iTime * 0.1) * STATIC_STRENGTH;
    float stat = rand2d(uv * vec2(0.0005,1.0) + iTime * 0.1) * STATIC_STRENGTH;
    outColor += stat;

    gl_FragColor = outColor;
}