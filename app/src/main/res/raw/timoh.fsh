precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

highp float rand(vec2 co)
{
    highp float a = 2.9898;
    highp float b = 0.3;
    highp float c = 1.3;
    highp float dt= dot(co.xy ,vec2(a+b*3.23,b*c));
    highp float sn= mod(dt,0.4);
    return fract(tan(sn) * c);
}

void main()
{
	vec2 uv = textureCoordinate.xy / iResolution.xy;
	// Flip Y Axis
	//uv.y = -uv.y;

	highp float magnitude = 0.04;

	// Set up offset
	vec2 offsetRedUV = uv;
	offsetRedUV.x = uv.x + rand(vec2(iTime*0.03,uv.y*2.42)) * 0.02;
	offsetRedUV.x += cos(rand(vec2(iTime*0.2, uv.y)))*magnitude*uv.x;

	vec2 offsetGreenUV = uv;
	offsetGreenUV.x = uv.x + rand(vec2(iTime*0.4,uv.y*uv.x*100000.0)) * 0.4;
	offsetGreenUV.x += cos(iTime*9.0)*magnitude*uv.y;

	vec2 offsetBlueUV = uv;
	offsetBlueUV.x =  uv.x + rand(vec2(iTime/0.4,uv.y*uv.x*100000.0)) * 0.4;
	offsetBlueUV.x += rand(vec2(cos(iTime*0.1),sin(uv.x*uv.y)));

	// Load Texture
	float r = texture2D(inputImageTexture, offsetRedUV).r;
	float g = texture2D(inputImageTexture, offsetGreenUV).g;
	float b = texture2D(inputImageTexture, uv).b;

	gl_FragColor = vec4(r,g,b,0);
}