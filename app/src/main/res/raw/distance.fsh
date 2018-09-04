precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;
        vec4 c = texture2D(inputImageTexture, uv);
        vec4 c2 = texture2D(inputImageTexture, uv + vec2(0.005, 0.005));
        c = c - distance(c, c2);

        gl_FragColor = vec4(c.rgb, 1.0);
}