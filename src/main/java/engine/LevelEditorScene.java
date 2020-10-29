package engine;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.ShaderProgram;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class LevelEditorScene extends Scene {

    private ShaderProgram defaultShader;

    private int vertexID;
    private int fragmentID;

    private float[] vertexArray = {
            // x y    z    // red  green  blue  alpha                      // index
            100, 0,   0,      1.0f, 0.0f, 0.0f, 1.0f,     // bottom right       0
            0,   100, 0,      0.0f, 1.0f, 0.0f, 1.0f,     // top    left        1
            100, 100, 0,      0.0f, 0.0f, 1.0f, 1.0f,     // top    right       2
            0,   0,   0,      1.0f, 1.0f, 0.0f, 1.0f,     // bottom left        3
    };

    // IMPORTANT: Must be counter clockwise order
    private int[] elementArray = {
            2, 1, 0, // top    right triangle
            0, 1, 3, // bottom left  triangle
    };

    private int vaoID, vboID, eboID;

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        this.camera = new Camera(new Vector2f());

        // Create shader program
        defaultShader = new ShaderProgram(
                "assets/shaders/default-vertex-shader.glsl",
                "assets/shaders/default-fragment-shader.glsl"
        );
        defaultShader.compile();

        // ---------------------------------------------------------------
        // Generate VAO, VBO, EBO buffer objects and send them to the GPU
        // ---------------------------------------------------------------

        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create VBO upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionsSize = 3;
        int colorSize = 4;
        int floatSizeBytes = 4;
        int vertexSizeBytes = (positionsSize + colorSize) * floatSizeBytes;

        // Position attribute
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);
    }

    @Override
    public void update(float dt) {

        camera.position.x -= dt * 50.0f;

        // Bind shader program
        defaultShader.use();

        defaultShader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMath4f("uView", camera.getViewMatrix());

        // Bind the VAO
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        defaultShader.detach();
    }
}
