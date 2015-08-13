#ifdef GL_ES
#define LOWP lowp
    precision mediump float;
#else
    #define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
varying vec4 v_backColor;
varying vec4 v_frontColor;

//attribute vec4 a_backColor;
//attribute vec4 a_frontColor;

uniform sampler2D u_texture;

void main()
{
    
    vec4 switchColor = v_color * texture2D(u_texture, v_texCoords);
    if(switchColor.r == 1.0f && switchColor.g == 1.0f && switchColor.b == 1.0f) {
        gl_FragColor = v_frontColor;
    }
    else {
        gl_FragColor = v_backColor;
    }
}