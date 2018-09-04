precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
    vec2 uv = textureCoordinate / iResolution.xy;
    float t = iTime;
    float x = textureCoordinate.x, y = textureCoordinate.y;
    uv.y += sin(t * 2.0 + y / 16.0) * 0.05;
    uv.x += sin(t * 3.0 + x / 16.0) * 0.05;
    vec4 p = texture2D(inputImageTexture, uv);

    gl_FragColor = p;
}