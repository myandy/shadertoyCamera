precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;

	float drunk = sin(iTime*2.0)*6.0;
	float unitDrunk1 = (sin(iTime*1.2)+1.0)/2.0;
	float unitDrunk2 = (sin(iTime*1.8)+1.0)/2.0;

	vec2 normalizedCoord = mod((textureCoordinate.xy + vec2(0, drunk)) / iResolution.xy, 1.0);
	normalizedCoord.x = pow(normalizedCoord.x, mix(1.25, 0.85, unitDrunk1));
	normalizedCoord.y = pow(normalizedCoord.y, mix(0.85, 1.25, unitDrunk2));

	vec2 normalizedCoord2 = mod((textureCoordinate.xy + vec2(drunk, 0)) / iResolution.xy, 1.0);
	normalizedCoord2.x = pow(normalizedCoord2.x, mix(0.95, 1.1, unitDrunk2));
	normalizedCoord2.y = pow(normalizedCoord2.y, mix(1.1, 0.95, unitDrunk1));

	vec2 normalizedCoord3 = textureCoordinate.xy/iResolution.xy;

	vec4 color = texture2D(inputImageTexture, normalizedCoord);
	vec4 color2 = texture2D(inputImageTexture, normalizedCoord2);
	vec4 color3 = texture2D(inputImageTexture, normalizedCoord3);

	// Mess with colors and test swizzling
	color.x = sqrt(color2.x);
	color2.x = sqrt(color2.x);

	vec4 finalColor = mix( mix(color, color2, mix(0.4, 0.6, unitDrunk1)), color3, 0.4);

	//
	if (length(finalColor) > 1.4)
		finalColor.xy = mix(finalColor.xy, normalizedCoord3, 0.5);
	else if (length(finalColor) < 0.4)
		finalColor.yz = mix(finalColor.yz, normalizedCoord3, 0.5);

	gl_FragColor = finalColor;


    //    gl_FragColor = vec4(1.0,0.0,0.0, 1.0);
}