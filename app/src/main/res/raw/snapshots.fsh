precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

const float speed = 2.;
void main()
{
	vec2 uv = textureCoordinate.xy;

      float pixelId = mod(floor(textureCoordinate.x),2.) + 2.0 * mod(floor(textureCoordinate.y),2.);
        float timeId = mod(floor(iTime*speed),4.0);
        if(pixelId != timeId) discard;
         if(timeId == 3.) {
     	   gl_FragColor = texture2D(inputImageTexture, uv ) * 1.4;
         } else {
     	   gl_FragColor = texture2D(inputImageTexture, uv ) * 1.4 * vec4(timeId==0.?0.:1.,timeId==1.?0.:1.,timeId==2.?0.:1.,1.);
         }
}