#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main()
{
    float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78.233))) * 43758.5453);
    color = fColor * noise;
}