package renderer;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public final class ShaderProgram {

    private final Shader vertexShader;
    private final Shader fragmentShader;

    private int id;

    public ShaderProgram(String vertexShaderPath, String fragmentShaderPath) {
        this.vertexShader = new Shader(vertexShaderPath, ShaderType.VERTEX);
        this.fragmentShader = new Shader(fragmentShaderPath, ShaderType.FRAGMENT);
        this.id = 0;
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
        glUseProgram(id);
    }

    public void detach() {
        glUseProgram(0);
    }
}
