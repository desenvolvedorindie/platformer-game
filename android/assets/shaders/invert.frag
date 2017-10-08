varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
	vec4 color = texture2D(u_texture, v_texCoords);
  	gl_FragColor = vec4(1.0 - color.r, 1.0 - color.g, 1.0 - color.b, color.a);
}