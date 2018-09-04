precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;

void main()
{
	vec2 uv = textureCoordinate.xy;

    // calculate the intensity bucket for this pixel based on column height (padded at the top)
    const float max_value = 270.0;
    const float buckets = 512.0;
    float bucket_min = log( max_value * floor(uv.y * buckets) / buckets );
    float bucket_max = log( max_value * floor((uv.y * buckets) + 1.0) / buckets );

    // count the count the r,g,b and luma in this column that match the bucket
    vec4 count = vec4(0.0, 0.0, 0.0, 0.0);
    for( int i=0; i < 512; ++i ) {
        float j = float(i) / buckets;
        vec4 pixel = texture2D(inputImageTexture, vec2(uv.x, j )) * 256.0;

        // calculate the Rec.709 luma for this pixel
        pixel.a = pixel.r * 0.2126 + pixel.g * 0.7152 + pixel.b * 0.0722;

        vec4 logpixel = log(pixel);
        if( logpixel.r >= bucket_min && logpixel.r < bucket_max) count.r += 1.0;
        if( logpixel.g >= bucket_min && logpixel.g < bucket_max) count.g += 1.0;
        if( logpixel.b >= bucket_min && logpixel.b < bucket_max) count.b += 1.0;
        if( logpixel.a >= bucket_min && logpixel.a < bucket_max) count.a += 1.0;
    }

    // sum luma into RGB, tweak log intensity for readability
    const float gain = 0.3;
    const float blend = 0.6;
    count.rgb = log( mix(count.rgb, count.aaa, blend) ) * gain;

    // output
    gl_FragColor = count;
}