#version 330 core

in vec4 fColor;
in vec2 fTexCoords;
in float fTexId;

// We use 8 instead of the minimum specified (16) so that
// lower end devices don't have to worry about running out
// of texture slots
uniform sampler2D uTextures[8];

out vec4 color;

void main()
{
    if (fTexId > 0) {
        // We are using a texture
        int id = int(fTexId);
        color = fColor * texture(uTextures[id], fTexCoords);
        // color = vec4(fTexCoords, 0, 1);
    } else {
        // We are using a color
        color = fColor;
    }
}