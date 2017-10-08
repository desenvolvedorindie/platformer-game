varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
    // Greyscale coeffs: 0.2989, 0.5870, 0.1140
    // http://stackoverflow.com/questions/687261/converting-rgb-to-grayscale-intensity
    vec4 c = texture2D(u_texture, v_texCoords);
    float value = c.r * 0.2989 + c.g * 0.5870 + c.b * 0.1140;
    gl_FragColor.r = value;
    gl_FragColor.g = value;
    gl_FragColor.b = value;
    gl_FragColor.a = c.a;
}