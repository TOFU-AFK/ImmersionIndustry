#define HIGHP

#define S1 vec3(46.0, 49.0, 83.0) / 100.0
#define S2 vec3(54.0, 63.0, 95.0) / 100.0
#define NSCALE 100.0 / 2.0

uniform sampler2D u_texture;
uniform sampler2D u_noise;

uniform vec2 u_campos;
uniform vec2 u_resolution;
uniform float u_time;

varying vec2 v_texCoords;

const float mscl = 40.0;
const float mth = 7.0;

void main(){
    vec2 c = v_texCoords.xy;
    vec2 v = vec2(1.0/u_resolution.x, 1.0/u_resolution.y);
	vec2 coords = vec2(c.x / v.x, c.y / v.y);

    float btime = u_time / 5000.0;
    float wave = abs(sin(coords.x * 1.1 + coords.y) + 0.1 * sin(2.5 * coords.x) + 0.15 * sin(3.0 * coords.y)) / 30.0;
    float noise = wave + (texture2D(u_noise, (coords) / NSCALE + vec2(btime) * vec2(-0.2, 0.8)).r + texture2D(u_noise, (coords) / NSCALE + vec2(btime * 1.1) * vec2(0.8, -1.0)).r) / 2.0;
    vec4 color = texture2D(u_texture, c);

    if(noise > 0.54 && noise < 0.57){
        color.rgb = S2;
    }else if (noise > 0.49 && noise < 0.62){
        color.rgb = S1;
    }
    
    float tester = mod((coords.x + coords.y*1.1 + sin(btime / 8.0 + coords.x/5.0 - coords.y/100.0)*2.0) +
      sin(btime / 20.0 + coords.y/3.0) * 1.0 +
      sin(btime / 10.0 - coords.y/2.0) * 2.0 +
      sin(btime / 7.0 + coords.y/1.0) * 0.5 +
      sin(coords.x / 3.0 + coords.y / 2.0) +
      sin(btime / 20.0 + coords.x/4.0) * 1.0, mscl);

    if(tester < mth){
        color *= 1.2;
    }

    gl_FragColor = color;
}