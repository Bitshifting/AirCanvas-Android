uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 color;

attribute vec3 vertices;
attribute vec3 normals;

varying vec4 vColor;

void main() {
   vColor = vec4(color, 1);

   gl_Position = projection * view * model * vec4(vertices, 1);
}