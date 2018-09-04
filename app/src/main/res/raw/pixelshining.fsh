precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;
uniform vec3                iResolution;

const float PI = 3.1416;

vec2 pixelate ( vec2 pixel, vec2 details ) { return floor(pixel * details) / details; }
float luminance ( vec3 color ) { return (color.r + color.g + color.b) / 3.0; }

void main()
{
   	vec2 uv = textureCoordinate.xy;

    // Maths infos about the current pixel position
    vec2 center = uv - vec2(0.5);
    float angle = atan(center.y, center.x);
    float radius = length(center);
    float ratioAngle = (angle / PI) * 0.5 + 0.5;

    // Displacement from noise
    vec2 angleUV = mod(abs(vec2(0, angle / PI)), 1.0);
    float offset = texture2D(inputImageTexture2, angleUV).r * 0.5;

    // Displaced pixel color
    vec2 p = vec2(cos(angle), sin(angle)) * offset + vec2(0.5);

    // Apply displacement
    uv = mix(uv, p, step(offset, radius));

    // Get color from texture
    vec3 color = texture2D(inputImageTexture, uv).rgb;

    // Treshold color from luminance
    float lum = luminance(color);
    color = mix(vec3(0.0,0.0,0.0), vec3(1.0,0.0,0.0), step(0.45, lum));
    color = mix(color, vec3(1.0,1.0,0.0), step(0.65, lum));
    color = mix(color, vec3(1.0,1.0,1.0), step(0.85, lum));

    // Voila
	gl_FragColor = vec4(color,1.0);
}