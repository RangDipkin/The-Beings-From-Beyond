#version 150 core

in vec4 in_Position;
in vec4 in_Color;
in vec2 in_TextureCoord;

out vec4 pass_Color;
out vec2 pass_TextureCoord;

void main(void) {
    //Trivial vertex shader, simply return the vertex as it is
    gl_Position = in_Position;
    
    //Pass these values on to the fragment shader
    pass_Color = in_Color;
    pass_TextureCoord = in_TextureCoord;
}