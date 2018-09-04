precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

void main() {
    vec2 uv = textureCoordinate.xy;

	vec4 bump = texture2D(inputImageTexture2, uv + iTime * 0.05);

	vec2 vScale = vec2 (0.01, 0.01);
	vec2 newUV = uv + bump.xy * vScale.xy;

	vec4 col = texture2D(inputImageTexture, newUV);

	gl_FragColor = vec4(col.xyz, 1.0);
}