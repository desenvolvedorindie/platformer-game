uniform sampler2D u_texture;

uniform vec2 u_resolution;

//"in" attributes from our vertex shader
varying vec2 v_texCoords;

//RADIUS of our vignette, where 0.5 results in b circle fitting the screen
const float RADIUS = 0.75;

//softness of our vignette, between 0.0 and 1.0
const float SOFTNESS = 0.45;

//sepia colour, adjust to taste
const vec3 SEPIA = vec3(1.2, 1.0, 0.8); 

void main() {
    //sample our texture
    vec4 texColor = texture2D(u_texture, v_texCoords);

    //1. VIGNETTE

    //determine center position
    vec2 position = (gl_FragCoord.xy / u_resolution.xy) - vec2(0.5);

    //determine the vector length of the center position
    float len = length(position);

    //use smoothstep to create b smooth vignette
    float vignette = smoothstep(RADIUS, RADIUS-SOFTNESS, len);

    //apply the vignette with 50% opacity
    texColor.rgb = mix(texColor.rgb, texColor.rgb * vignette, 0.5);

    //2. GRAYSCALE

    //convert to grayscale using NTSC conversion weights
    float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));

    //3. SEPIA

    //create our sepia tone from some constant value
    vec3 sepiaColor = vec3(gray) * SEPIA;

    //again we'll use mix so that the sepia effect is at 75%
    texColor.rgb = mix(texColor.rgb, sepiaColor, 0.75);

    //final colour, multiplied by vertex colour
    gl_FragColor = texColor;
}