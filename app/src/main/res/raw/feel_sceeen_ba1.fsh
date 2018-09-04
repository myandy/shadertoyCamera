//血色
precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
    vec2 uv = textureCoordinate / iResolution.xy;
    vec4 base_color = vec4(0.0);
    vec4 field = texture2D(inputImageTexture, uv);

    float intensity = step(0.1, texture2D(inputImageTexture, uv).r);

    vec2 off = 1.0 / iResolution.xy;

    for (float y = -1.0; y <= 1.0; y++) {
	    for (float x = -1.0; x <= 1.0; x++) {
    		intensity += 0.11 * texture2D(inputImageTexture2, uv + vec2(x, y) * off).r;
        }
    }
    base_color = vec4(clamp(intensity, 0., 1.), 0, 0, 0);

    gl_FragColor = base_color;
}