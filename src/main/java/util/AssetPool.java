package util;

import renderer.ShaderProgram;
import renderer.Texture;

import java.util.HashMap;
import java.util.Map;

public final class AssetPool {

    private static final Map<String, ShaderProgram> shaders = new HashMap<>();
    private static final Map<String, Texture> textures = new HashMap<>();

    public static ShaderProgram getShader(String id) {
        if (shaders.containsKey(id)) {
            return shaders.get(id);
        } else {
            String msg = String.format("Shader not found -- id: %s", id);
            assert false : msg;
            throw new IllegalArgumentException(msg);
        }
    }

    public static void loadShader(String id, String vertexShaderPath, String fragmentShaderPath) {
        if (!shaders.containsKey(id)) {
            ShaderProgram shader = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
            shader.compile();
            shaders.put(id, shader);
        } else {
            String msg = String.format("Shader already present -- id: %s", id);
            assert false : msg;
            throw new IllegalArgumentException(msg);
        }
    }

    public static Texture getTexture(String id) {
        if (textures.containsKey(id)) {
            return textures.get(id);
        } else {
            String msg = String.format("Texture not found -- id: %s", id);
            assert false : msg;
            throw new IllegalArgumentException(msg);
        }
    }

    public static Texture loadTexture(String id, String path) {
        if (!textures.containsKey(id)) {
            Texture texture = new Texture(path);
            textures.put(id, texture);
            return texture;
        } else {
            String msg = String.format("Texture already present -- id: %s", id);
            assert false : msg;
            throw new IllegalArgumentException(msg);
        }
    }

    private AssetPool() {
    }
}
