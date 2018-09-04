precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

const float TESTS = 50.0;
void main()
{
	vec2 uv = textureCoordinate.xy;

      vec4 c = vec4(0.0);
      for(float i = 0.; i< TESTS; i++){
      	c.rgb = max(c.rgb,
                  sin(i/40.+
                      6.28*(vec3(0.,.33,.66)+
                         texture2D(
                              inputImageTexture,vec2(
                                  uv.x,uv.y-(i/1.0))
                          ).rgb
                      ))*.5+.5);
      }
     	c.rgb = sin(( vec3(0.,.33,.66)+c.rgb+uv.y)*6.28)*.5+.5;
      c.a = 1.0;
  	gl_FragColor = c;
}