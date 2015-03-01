uniform mat4 view;
uniform mat4 projection;
uniform vec3 color;

attribute vec3 in_Position;

varying vec3 ex_Color;

void main(void) {
    // Since we are using flat lines, our input only had two points: x and y.
    // Set the Z coordinate to 0 and W coordinate to 1

    gl_Position = projection * view * vec4(in_Position, 1);

    ex_Color = color;
}