uniform mat4 MVP;

attribute vec3 pos;
attribute vec3 color;
attribute vec3 normal;

varying vec4 vColor;

void main() {
   vColor = color;

   gl_Position = MVP * vec4(pos, 1);
}