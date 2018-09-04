precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

vec2 size = vec2(50.0, 50.0);
vec2 distortion = vec2(20.0, 20.0);
float speed = 0.75;

void main()
{
    vec2 transformed = vec2(
        textureCoordinate.x + sin(textureCoordinate.y / size.x + iTime * speed) * distortion.x,
        textureCoordinate.y + cos(textureCoordinate.x / size.y + iTime * speed) * distortion.y
    );
    vec2 relCoord = textureCoordinate.xy / iResolution.xy;
    gl_FragColor = texture2D(inputImageTexture, transformed / iResolution.xy) + vec4(
        (cos(relCoord.x + iTime * speed * 4.0) + 1.0) / 2.0,
        (relCoord.x + relCoord.y) / 2.0,
        (sin(relCoord.y + iTime * speed) + 1.0) / 2.0,
        0
    );

}