precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;
uniform vec3                iResolution;

void main()
{
    vec2 uv = textureCoordinate/iResolution.xy-0.5;
    uv *= 0.7 - mod(uv.x+uv.y,uv.x*0.5)*1.5;
    gl_FragColor = texture2D(inputImageTexture,uv+0.5);

}