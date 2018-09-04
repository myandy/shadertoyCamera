precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;
uniform vec3                iResolution;


float uPower = 0.2; // barrel power - (values between 0-1 work well)
float uSpeed = 5.0;
float uFrequency = 5.0;

vec2 Distort(vec2 p, float power, float speed, float freq)
{
    float theta  = atan(p.y, p.x);
    float radius = length(p);
    radius = pow(radius, power*sin(radius*freq-iTime*speed)+1.0);
    p.x = radius * cos(theta);
    p.y = radius * sin(theta);
    return 0.5 * (p + 1.0);
}

void main() {

  vec2 xy = 2.0 * textureCoordinate.xy/iResolution.xy - 1.0;
  vec2 uvt;
  float d = length(xy);

  //distance of distortion
  if (d < 1.0 && uPower != 0.0)
  {
    //if power is 0, then don't call the distortion function since there's no reason to do it :)
    uvt = Distort(xy, uPower, uSpeed, uFrequency);
  }
  else
  {
    uvt = textureCoordinate.xy / iResolution.xy;
  }
  vec4 c = texture2D(inputImageTexture, uvt);
  gl_FragColor = c;
}