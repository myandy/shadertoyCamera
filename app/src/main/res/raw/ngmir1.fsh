precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;
    vec4 c = texture2D(inputImageTexture, uv);
    c = sin(uv.x*10.+c*cos(c*6.28+iTime+uv.x)*sin(c+uv.y+iTime)*6.28)*.5+.5;
    c.b+=length(c.rg);
    gl_FragColor = vec4(c.rgb, 1.0);
}