package renderer;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {

    private final Shader vertexShader;
    private final Shader fragmentShader;

    private int id;
    private boolean isUsed;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShader = new Shader(vertexShaderPath, ShaderType.VERTEX);
        this.fragmentShader = new Shader(fragmentShaderPath, ShaderType.FRAGMENT);
        this.id = 0;
        this.isUsed = false;
    }

    public void compile() {

        // Compile vertex shader
        vertexShader.compile();

        // Compile fragment shader
        fragmentShader.compile();

        // Link shaders and check for errors
        id = glCreateProgram();
        glAttachShader(id, vertexShader.getID());
        glAttachShader(id, fragmentShader.getID());
        glLinkProgram(id);

        // Check for linking errors
        int success = glGetProgrami(id, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            String msg = "ERROR: shader program linking failed.";
            System.out.println(msg);
            System.out.println(glGetProgramInfoLog(id, len));

            assert false : "";

            throw new IllegalStateException(msg);
        }
    }

    public void use() {
        if (!isUsed) {
            glUseProgram(id);
            isUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        isUsed = false;
    }

    public void uploadMath4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(id, varName);

        // TODO: maybe instead of forcing the program to be used we should throw an error
        use();

        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadMath3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec4f) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform4f(varLocation, vec4f.x, vec4f.y, vec4f.z, vec4f.w);
    }

    public void uploadVec3f(String varName, Vector3f vec3f) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform3f(varLocation, vec3f.x, vec3f.y, vec3f.z);
    }

    public void uploadVec2f(String varName, Vector2f vec2f) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform2f(varLocation, vec2f.x, vec2f.y);
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadTexture(String varName, int slot) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform1i(varLocation, slot);
    }

    public void uploadIntArray(String varName, int[] array) {
        int varLocation = glGetUniformLocation(id, varName);
        use();
        glUniform1iv(varLocation, array);
    }
}
