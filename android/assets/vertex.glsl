attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;
attribute vec4 a_frontColor;
attribute vec4 a_backColor;

uniform mat4 u_projTrans;

varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_frontColor;
varying vec4 v_backColor;

void main() {
    v_color = a_color;
    v_texCoords = a_texCoord0;
    v_frontColor = a_frontColor;
    v_backColor = a_backColor;

    gl_Position = u_projTrans * a_position;
}