attribute vec3 a_position;
attribute vec2 a_texCoord0;

varying vec4 v_color;
varying vec2 v_texCoord0;

void main() {
   v_texCoord0 = a_texCoord0;
   gl_Position =  vec4(a_position, 1.0);
}