package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;

public final class Texture {

    private final String path;
    private int id;

    public Texture(String path) {
        this.path = path;

        // Generate texture on GPU
        id = glGenTextures();

        // Bind texture
        glBindTexture(GL_TEXTURE_2D, id);

        // Set texture parameters

        // Repeat image in both directions
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        // When stretching the image, pixelate texture
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);

        // When shrinking an image, pixelate
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        // Load image
        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        ByteBuffer image = stbi_load(path, width, height, channels, 0);

        if (image != null) {
            int internalFormat;
            switch (channels.get(0)) {
                case 3 -> internalFormat = GL_RGB;
                case 4 -> internalFormat = GL_RGBA;
                default -> {
                    String msg = String.format("Error: (Texture) unknown number of channels '%d'", channels.get(0));
                    assert false : msg;
                    throw new IllegalStateException(msg);
                }
            }

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    internalFormat,
                    width.get(0), height.get(0),
                    0,
                    internalFormat,
                    GL_UNSIGNED_BYTE,
                    image
            );
        } else {
            String msg = String.format("Error: (Texture) could not load image '%s'", path);
            assert false : msg;
            throw new IllegalStateException(msg);
        }

        stbi_image_free(image);
    }

    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    public void unbind() {
        glBindTexture(GL_TEXTURE_2D, 0);
    }
}
