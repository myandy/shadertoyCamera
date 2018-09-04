precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;
    vec4 c = texture2D(inputImageTexture,uv)*2.0;
    uv.xy+=c.bg*(0.6/iResolution.x-.5);
    uv-=.5;
    float a = atan(uv.y,uv.x);
    float d = length(uv);
    a+=c.r*(0.6/iResolution.y-.5)*12.0;
    uv.x = cos(a)*d;
    uv.y = sin(a)*d;
    uv+=.5;
    c = texture2D(inputImageTexture,uv)*2.0;
    gl_FragColor = vec4(c.rgb, 1.0);
}