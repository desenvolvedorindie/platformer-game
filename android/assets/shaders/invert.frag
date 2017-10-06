#ifdef GL_ES
precision mediump float; 
#endif

varying vec2 v_texCoord0;

uniform sampler2D u_sampler2D;

void main() {
	vec4 color = texture2D(u_sampler2D, v_texCoord0);
  	gl_FragColor = vec4(1.0 - color.x, 1.0 - color.y, 1.0 - color.z, color.a);
}