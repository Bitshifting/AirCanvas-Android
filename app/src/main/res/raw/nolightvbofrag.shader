varying  vec3 ex_Color;

void main(void) {
    // Pass through our original color with full opacity.
    gl_FragColor = vec4(ex_Color,1.0);
}