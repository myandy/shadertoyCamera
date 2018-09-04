precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

const float AA_ITER_COUNT = 1.0;
const float AA_ITER_COUNT_SQRT = 1.0;

vec2 hash( float n )
{
    return fract( sin( vec2( n, n + 1.0 ) ) * 2342.2343 );
}

vec3 render(float iTime, vec2 uv, sampler2D inputImageTexture)
{
    vec2 og_uv = uv;

    float time = iTime;
    time += 50.0 * smoothstep( 0.0, 4.0, iTime );

    vec3 col = texture2D(
        inputImageTexture,
        uv + vec2( sin( uv.x * 1.0 * time ) * .1,
                   cos( uv.y * 1.0 * time ) * .12 )
    ).rgb;

    float a = mix( 100.0, 200.0, .5 + 0.5 * sin( time * 0.31 ) );
    float b = mix( 22.0, 50.0, .5 + 0.5 * sin( time * 0.2 ) );
    float f = mix( a, b, smoothstep( 0.5, 0.0, abs( ( uv.x - 0.5 ) * ( uv.y ) ) ) );
    float t = cos( uv.x * f ) * sin( uv.y * f );
    col = 0.95 * col + 0.05 * step( -0.3, t ) * t;

    float x = mix( 0.01, 1.0, 0.5 + 0.5 * sin( 1000.0 * time ) );
    uv += x * ( -1.0 + 2.0 * hash( 234.0 + time * uv.x + uv.y * 1000.0 ) );
    float tt = 0.5 + 0.5 * sin( 0.1 * time );
    col = mix( texture2D( inputImageTexture, uv ).rgb, col, tt );

    col.r *= 3.0;
    col.g *= 2.1;
    col.b += 0.1;

    float edge = smoothstep( 0.95 * sqrt( 2.0 ), 0.0, length( -1.0 + 2.0 * og_uv ) );
    col *= 0.5 + 0.5 * edge;

    col = 0.8 * col + 0.2 * texture2D( inputImageTexture2, uv + vec2( 0.2, 0.3 ) * time ).rgb;

    col -= 0.5 * ( 0.5 + 0.5 * sin( og_uv.y * 700.0 ) * sin( 300.0 * og_uv.x + 34234.0 ) );

    col *= 0.95 + 0.2 * smoothstep( 0.1, sqrt( 2.0 ), edge );

    col.r += 0.21 * smoothstep( 0.0, 1.0, og_uv.x );
    col.g += 0.08 * smoothstep( 0.0, 0.3, 1.0 - og_uv.x * og_uv.y );
    col.b -= 0.32 * smoothstep( 0.25, 1.0, og_uv.x * og_uv.y );

    uv = og_uv + .1 * hash( floor( ( 0.01 * time + 1.0 + og_uv.y ) * 10.0 ) * floor( ( 1.0 + og_uv.x ) * 10.0 ) );
    col *= 1.0 - 0.2 * texture2D( inputImageTexture2, uv ).rgb;

    return col;
}


void main()
{
    vec3 col = vec3( 0.0 );

     float aspect = iResolution.x / iResolution.y;

        int i = 0;
            vec2 offset = vec2( float(i) / AA_ITER_COUNT_SQRT, mod( float(i), AA_ITER_COUNT_SQRT ) ) / AA_ITER_COUNT_SQRT;
            vec2 uvs = ( textureCoordinate.xy + offset ) / iResolution.xy;
            vec2 uv = textureCoordinate.xy / iResolution.xy;
           col += render(iTime, uvs,inputImageTexture );

        col /= float( AA_ITER_COUNT );

    gl_FragColor = vec4( col, 1.0 );

    //gl_FragColor = vec4(1.0,1.0,0.0,1.0);
}