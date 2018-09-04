precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;


void main()
{
	vec2 pixel = textureCoordinate;
    vec2 adjustedPixel = pixel;
    const float intensity = .1;

    adjustedPixel.y = 0.0;
    adjustedPixel.x = pixel.y;

    vec4 noiseTexture = texture2D(inputImageTexture2,adjustedPixel);
    vec4 soundTexture = vec4(1.0);

    noiseTexture = noiseTexture * 2.0;
    pixel.x += sin(noiseTexture.x * iTime) * intensity - .05;
    vec3 videoTexture = texture2D(inputImageTexture, pixel).xyz;
    videoTexture.r -= 1.0 - ((sin(iTime * noiseTexture.y) + 1.0) / 2.);

    videoTexture.g = 0.2;
    videoTexture.b = 0.2;
	//videoTexture.g -= 1.0 - ((sin(iTime * noiseTexture.y) + 1.0) / 2.);
	//videoTexture.b -= 1.0 - ((sin(iTime * noiseTexture.y) + 1.0) / 2.);

	gl_FragColor = vec4(videoTexture, 1.0);
}