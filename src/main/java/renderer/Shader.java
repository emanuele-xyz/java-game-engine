package renderer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public final class Shader {

    private final String path;
    private final String source;
    private final ShaderType type;

    private int id;

    public Shader(String path, ShaderType type) {
        this.path = path;
        this.type = type;
        this.id = 0;

        try {
            this.source = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            String msg = "Error loading " + type + " shader '" + path + "'";
            assert false : msg;
            throw new IllegalStateException(msg);
        }

    }

    public String getPath() {
        return path;
    }

    public void compile() {
        // ------------------------
        // Load and compile shader
        // ------------------------

        // Create  shader
        id = glCreateShader(type.getType());

        // Pass the shader source to the GPU
        glShaderSource(id, source);

        // Compile shader
        glCompileShader(id);

        // Check for compile errors
        int success = glGetShaderi(id, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(id, GL_INFO_LOG_LENGTH);
            String msg = String.format("ERROR: '%s'\n\t%s shader compilation failed.", path, type);
            System.out.println(msg);
            System.out.println(glGetShaderInfoLog(id, len));

            assert false : "";

            throw new IllegalStateException();
        }
    }

    public int getID() {
        return id;
    }
}
