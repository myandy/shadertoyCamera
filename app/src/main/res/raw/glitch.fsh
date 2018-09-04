precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;


float rand(vec2 co)
{
	return fract(sin(dot(co.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

void main()
{
	vec2 uv = textureCoordinate.xy;

	if (mod(iTime, 2.0) > 1.9)
    		uv.x += cos(iTime * 10.0 + uv.y * 1000.0) * 0.01;

        //if (mod(iTime, 4.0) > 3.0)
    	//	uv = floor(uv * 32.0) / 32.0;

    	if (mod(iTime, 5.0) > 3.75)
        	uv += 1.0 / 64.0 * (2.0 * vec2(rand(floor(uv * 32.0) + vec2(32.05,236.0)), rand(floor(uv.y * 32.0) + vec2(-62.05,-36.0))) - 1.0);

    vec4	fragColor = texture2D(inputImageTexture, uv);

        if (rand(vec2(iTime)) > 0.90)
    		fragColor = vec4(dot(fragColor.rgb, vec3(0.25, 0.5, 0.25)));

        fragColor.rgb += 0.25 * vec3(rand(iTime + textureCoordinate / vec2(-213, 5.53)), rand(iTime - textureCoordinate / vec2(213, -5.53)), rand(iTime + textureCoordinate / vec2(213, 5.53))) - 0.125;

   // 	gl_FragColor = finalColor;


    gl_FragColor = fragColor;
}