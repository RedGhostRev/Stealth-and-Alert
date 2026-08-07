#version 330

in vec3 Position;
in vec4 Color;

uniform mat4 ModelViewMat;
uniform mat4 ProjMat;

// 诊断：>= -1000 时强制所有顶点 Y = DebugForceY（验证 ModelView/投影链路）
uniform float DebugForceY;

out vec4 vertexColor;

void main() {
    vec3 projectedPos = Position;

    // 诊断覆盖（默认 debugForceY=-9999 关闭）
    if (DebugForceY >= -1000.0) {
        projectedPos.y = DebugForceY;
    }

    gl_Position = ProjMat * ModelViewMat * vec4(projectedPos, 1.0);
    vertexColor = Color;
}
