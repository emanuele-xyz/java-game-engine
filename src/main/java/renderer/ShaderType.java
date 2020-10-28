package renderer;

import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;

public enum ShaderType {

    VERTEX(GL_VERTEX_SHADER),
    FRAGMENT(GL_FRAGMENT_SHADER);

    private final int type;

    ShaderType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return switch (this) {
            case VERTEX -> "vertex";
            case FRAGMENT -> "fragment";
        };
    }
}
