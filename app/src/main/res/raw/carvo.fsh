precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;


void main()
{
	vec2 uv = textureCoordinate;
    uv.x = 1.0 - uv.x;

    vec3 col = texture2D(inputImageTexture,uv).xyz;
    vec3 greyMul = vec3(0.2989, 0.5870, 0.1140);
    vec3 greyCol = col * greyMul;
    float grey = greyCol.x + greyCol.y + greyCol.z;
    float threshold = 0.5 + 0.5 * sin(iTime * 2.0);
    if ( grey > threshold - 0.2 && grey < threshold + 0.2 )
    {
        float u = mod(uv.x * 4.0, 1.0);
        float v = mod(uv.y * 4.0, 1.0);
        col = texture2D(inputImageTexture,vec2(u,v)).xyz * vec3(uv,0.5+0.5*sin(iTime * 4.0));
    }

	gl_FragColor = vec4(col, 1.0);
}