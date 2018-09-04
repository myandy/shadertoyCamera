precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

const float PI = 3.1415;
const float TWO_PI = PI * 2.0;
const float SIDES = 10.0;

void main()
{
	vec2 uv = textureCoordinate.xy / iResolution.xy;
    // Center UV
    vec2 p = uv - 0.5;

    // Convert from cartesian coordinates to polar coordinates
    float r = length(p); 		// r = âˆš( x2 + y2 )
    float angle = atan(p.y, p.x);	// Î¸ = tan-1 ( y / x )

    // Kaleidoscope effect
    angle = mod(angle, TWO_PI/SIDES);
    angle = abs(angle - PI/SIDES);

    // Convert from polar coordinates to cartesian coordinates
    p = r * vec2(cos(angle), sin(angle));	// x = r Ã— cos( Î¸ ), y = r Ã— sin( Î¸ )

    float time = iTime;
    gl_FragColor = texture2D(inputImageTexture, p - cos(time)/2.0);

}