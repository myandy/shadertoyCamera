precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
    vec2 uv = textureCoordinate / iResolution.xy;
    vec2 webcam_uv = uv;
    vec2 off = 1.0 / iResolution.xy;

    float edge = 0.0;
    edge -= 4.0 * length(texture2D(inputImageTexture, webcam_uv).xyz);
    edge += 1.0 * length(texture2D(inputImageTexture, webcam_uv + vec2(+off.x, 0.0)).xyz);
    edge += 1.0 * length(texture2D(inputImageTexture, webcam_uv + vec2(-off.x, 0.0)).xyz);
    edge += 1.0 * length(texture2D(inputImageTexture, webcam_uv + vec2(0.0, +off.y)).xyz);
    edge += 1.0 * length(texture2D(inputImageTexture, webcam_uv + vec2(0.0, -off.y)).xyz);

    gl_FragColor = vec4(edge);
}