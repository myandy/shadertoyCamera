precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

// Implemented from this blog site post-> http://www.gamedev.net/topic/590070-glsl-droste/

const float TWO_PI = 3.141592*2.0;

//ADJUSTABLE PARAMETERS:
const float branches = 3.0;
const float scale = 0.6125; // try different values :)
const float speed = 10.0;

//Complex Math:
vec2 complexExp(in vec2 z){
	return vec2(exp(z.x)*cos(z.y),exp(z.x)*sin(z.y));
}
vec2 complexLog(in vec2 z){
	return vec2(log(length(z)), atan(z.y, z.x));
}
vec2 complexMult(in vec2 a,in vec2 b){
	return vec2(a.x*b.x - a.y*b.y, a.x*b.y + a.y*b.x);
}
float complexMag(in vec2 z){
	return float(pow(length(z), 2.0));
}
vec2 complexReciprocal(in vec2 z){
	return vec2(z.x / complexMag(z), -z.y / complexMag(z));
}
vec2 complexDiv(in vec2 a,in vec2 b){
	return complexMult(a, complexReciprocal(b));
}
vec2 complexPower(in vec2 a, in vec2 b){
	return complexExp( complexMult(b,complexLog(a))  );
}
//Misc Functions:
float nearestPower(in float a, in float base){
	return pow(base,  ceil(  log(abs(a))/log(base)  )-1.0 );
}
float map(float value, float istart, float istop, float ostart, float ostop) {
	   return ostart + (ostop - ostart) * ((value - istart) / (istop - istart));
}

void main()
{
	vec2 uv = textureCoordinate;

	//SHIFT AND SCALE COORDINATES TO <-1,1>
	vec2 z = (uv-0.5+sin(iTime*0.3)*0.35)*2.0;

	//ESCHER GRID TRANSFORM:
	float factor = pow(1.0/scale,branches);
	z = complexPower(z, complexDiv(vec2( log(factor) ,TWO_PI), vec2(0.0,TWO_PI) ) );

	//RECTANGULAR DROSTE EFFECT:
	z *= 1.0+fract(iTime*speed)*(scale-1.0);
	float npower = max(nearestPower(z.x,scale),nearestPower(z.y,scale));
	z.x = map(z.x,-npower,npower,-1.0,.40);
	z.y = map(z.y,-npower,npower,-1.0,.20);

	//UNDO SHIFT AND SCALE:
	z = z*0.5+0.5;

	gl_FragColor = vec4(texture2D(inputImageTexture,z).rgb, 1.0);

	//gl_FragColor = vec4(1.0,1.0,0.0,1.0);
}