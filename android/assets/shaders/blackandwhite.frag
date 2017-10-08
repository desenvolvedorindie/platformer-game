varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main() {
    // Greyscale coeffs: 0.2989, 0.5870, 0.1140
    // http://stackoverflow.com/questions/687261/converting-rgb-to-grayscale-intensity
    vec4 c = texture2D(u_texture, v_texCoords);
    float value = c.r * 0.2989 + c.g * 0.5870 + c.b * 0.1140;

    if(value > 0.5) {
        gl_FragColor.r = 1.0;
        gl_FragColor.g = 1.0;
        gl_FragColor.b = 1.0;
    } else {
        gl_FragColor.r = 0.0;
        gl_FragColor.g = 0.0;
        gl_FragColor.b = 0.0;
    }

    gl_FragColor.a = c.a;
 }