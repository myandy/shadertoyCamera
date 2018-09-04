precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

float rand2(vec2 co){
    return rand(co)-rand(vec2(co.x+1.0,co.y+1.0))/2.0;
}

const float COLOR_DEPTH =2.0;

void main()
{
    float mousePos = 0.0;
	vec2 uv = textureCoordinate.xy / iResolution.xy;
    float value = (texture2D(inputImageTexture,uv).r*0.2125+texture2D(inputImageTexture,uv).g*0.715+texture2D(inputImageTexture,uv).b*0.0721);
    if(uv.x<mousePos-0.002){
		gl_FragColor = vec4(floor(value*COLOR_DEPTH)/(COLOR_DEPTH-1.0),
                         floor(value*COLOR_DEPTH)/(COLOR_DEPTH-1.0),
                         floor(value*COLOR_DEPTH)/(COLOR_DEPTH-1.0),
                         1.0);
    }else if(uv.x>mousePos+0.002){
		gl_FragColor = vec4(floor(value*(COLOR_DEPTH-.5)+rand2(textureCoordinate+iTime)*1.0)/(COLOR_DEPTH-1.0),
                         floor(value*(COLOR_DEPTH-.5)+rand2(textureCoordinate+iTime)*1.0)/(COLOR_DEPTH-1.0),
                         floor(value*(COLOR_DEPTH-.5)+rand2(textureCoordinate+iTime)*1.0)/(COLOR_DEPTH-1.0),
                         1.0);
    }else{
     	gl_FragColor = vec4(0.0,0.0,0.0,1.0);
    }
}