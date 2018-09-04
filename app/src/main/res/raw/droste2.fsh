precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
uniform vec3                iResolution;
varying vec2                textureCoordinate;

float inv_webcam_aspect = 480.0/640.0,
    two_pi = 6.28318530718,
    inner_radius = 0.1,
    outer_radius = 0.5,
	spiral_factor = 1.0;

vec2 wrap(vec2 pos, float r1, float r2) {
    float theta = pos.x * two_pi,
        r = pos.y * (r2-r1) + r1;
    return vec2(0.5 + inv_webcam_aspect * r * cos(theta), 0.5 + r * sin(theta));
}

vec2 unwrap(vec2 pos, float factor) {
    vec2 centred = pos - vec2(0.5, 0.5);
    float theta = atan(centred.y, centred.x),
        phi = theta / two_pi,
        r2 = dot(centred, centred),
        logr = 0.5 * log(r2) * factor,
        y = logr - phi;

 	return vec2(phi, y - floor(y));
}

void main()
{
	vec2 uv = (textureCoordinate.xy - 0.5 * iResolution.xy) / iResolution.y + vec2(0.5, 0.5);
	gl_FragColor = texture2D(inputImageTexture, wrap(unwrap(uv, spiral_factor), inner_radius, outer_radius));

}