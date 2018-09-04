precision highp float;

uniform float               iTime;
uniform sampler2D           inputImageTexture;
uniform sampler2D           inputImageTexture2;
varying vec2                textureCoordinate;
uniform vec3                iResolution;

float total (vec4 col){
    return col.r+col.g+col.b;
}
float change (vec4 col1, vec4 col2){
    return length(col1-col2);//abs(col1.r-col2.r)+abs(col1.g-col2.g)+abs(col1.b-col2.b);
}
vec4 contrast (vec4 col, float n){
    return 0.5+(col-0.5)*n;
}
bool foundEdge (vec2 uv ,sampler2D inputImageTexture ){
    vec4 camCol = texture2D(inputImageTexture, uv);
    float d = 0.002;
    vec4 top = texture2D(inputImageTexture, vec2(uv.x, uv.y+d));
    vec4 bottom = texture2D(inputImageTexture, vec2(uv.x, uv.y-d));
    vec4 right = texture2D(inputImageTexture, vec2(uv.x+d, uv.y));
    vec4 left = texture2D(inputImageTexture, vec2(uv.x-d, uv.y));

    float c = 1.2;
    camCol = contrast(camCol, c);
    top = contrast(top, c);
    bottom = contrast(bottom, c);
    right = contrast(right, c);
    left = contrast(left, c);


    float check = 0.08;
    if(change(top, camCol)>check||change(bottom, camCol)>check||change(left, camCol)>check||change(right, camCol)>check){
        return true;
    } else {
        return false;
    }
}
void main()
{
    // Normalized pixel coordinates (from 0 to 1)
    vec2 uv = textureCoordinate/iResolution.xy;
	vec4 col = vec4(.8);
    if(foundEdge(uv,inputImageTexture)){
        col = contrast(texture2D(inputImageTexture2, uv), 5.);
        float edgesHit = 0.;
        float dist = 0.002;
        for(float i = 0.; i<360.; i+=10.){
            if(foundEdge(vec2(uv.x+cos(i)*dist, uv.y+sin(i)*dist),inputImageTexture)){
                edgesHit += 1.;
            }
        }
        if(edgesHit<360./10.*0.3){
            col = vec4(0.);
        }
    }
    gl_FragColor = col;
}