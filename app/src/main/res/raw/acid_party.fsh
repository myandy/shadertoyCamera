precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

vec2 distort( vec2 p )
{
  float theta  = atan(p.y, p.x);
  float radius = length(p);
  radius = pow(radius, 2.41);
  theta += sin(iTime) * 3.14;
  p.x = radius * cos(theta);
  p.y = radius * sin(theta);
  return 0.5 * (p + 1.0);
}

void main()
{
  vec2 uv = (textureCoordinate.xy / iResolution.xy) - vec2(0.5, 0.5);
  vec4 fragColor = texture2D(inputImageTexture, distort(uv * 2.0), 1.0 + sin(iTime) * 5.0 );
  fragColor.r *= abs(sin(iTime * 10.0));
  fragColor.g *= abs(sin(iTime * 10.0 + 1.0));
  fragColor.b *= abs(sin(iTime * 10.0 + 2.0));

  gl_FragColor = fragColor;
}