precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
	float t = iTime;

    vec4 color = vec4(1.0,1.0,0.0,1.0);
        color.bgr = vec3(1.0) - texture2D(inputImageTexture, textureCoordinate).brg / length(textureCoordinate) * 2.0;
    	color.r += texture2D(inputImageTexture, textureCoordinate).r;
        color.b *= texture2D(inputImageTexture2, textureCoordinate + t*0.02).r;
        color.g /= texture2D(inputImageTexture2, textureCoordinate + t*0.02).r;

        color.rgb += vec3(1.0) * texture2D(inputImageTexture, textureCoordinate).r;

    gl_FragColor = color;
}