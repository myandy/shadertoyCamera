precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;
        vec4 c = texture2D(inputImageTexture, uv);
         float d = 0.001 / c.g;
          float a = c.r * 15.0 + c.b * 10.0 + sin(iTime*0.001);
          uv.x += d * cos(a);
          uv.y += d * sin(a);

          c.rgb = texture2D(inputImageTexture2, uv).rgb * 0.985  // buffer
            + texture2D(inputImageTexture, uv).rbg * 0.015; // webcam

          c.r = pow(mod(c.r * 1.003 , 1.0), 1.003);
          c.g = pow(mod(c.g * 1.004 , 1.0), 1.004);
          c.b = pow(mod(c.b * 1.005 , 1.0), 1.005);

        gl_FragColor = vec4(c.rgb, 1.0);

       // gl_FragColor = vec4(1.0,0.0,0.0, 1.0);
}