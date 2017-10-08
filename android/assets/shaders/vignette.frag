varying vec2 v_texCoords;

uniform vec2 u_resolution;
uniform sampler2D u_texture;

void main() {
	vec4 color = texture2D(u_texture, v_texCoords);
	vec2 relativePosition = gl_FragCoord.xy / u_resolution - .5f;
	float len = length(relativePosition);
	float vignette = smoothstep(.5, .4, len); 
	color.rgb = mix(color.rgb, color.rgb * vignette, .7);
  	gl_FragColor = color;
}