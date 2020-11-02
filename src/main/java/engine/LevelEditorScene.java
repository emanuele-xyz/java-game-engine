package engine;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.ShaderProgram;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class LevelEditorScene extends Scene {

    private ShaderProgram defaultShader;
    private Texture testTexture;

    private GameObject testObj;
    private boolean firstTime = true;

    private int vertexID;
    private int fragmentID;

    private float[] vertexArray = {
            // x y    z    // red  green  blue  alpha   // uv                        // index
            100, 0,   0,      1.0f, 0.0f, 0.0f, 1.0f,     1, 1,     // bottom right       0
            0,   100, 0,      0.0f, 1.0f, 0.0f, 1.0f,     0, 0,     // top    left        1
            100, 100, 0,      1.0f, 0.0f, 1.0f, 1.0f,     1, 0,     // top    right       2
            0,   0,   0,      1.0f, 1.0f, 0.0f, 1.0f,     0, 1,     // bottom left        3
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

        System.out.println("Creating test object");
        this.testObj = new GameObject("test object");
        this.testObj.addComponent(new SpriteRenderer());
        this.testObj.addComponent(new FontRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f(-200, -300));

        // Create shader program
        defaultShader = new ShaderProgram(
                "assets/shaders/default-vertex-shader.glsl",
                "assets/shaders/default-fragment-shader.glsl"
        );
        defaultShader.compile();

        testTexture = new Texture("assets/images/test-image.png");

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
        int uvSize = 2;
        int floatSizeBytes = Float.BYTES;
        int vertexSizeBytes = (positionsSize + colorSize + uvSize) * floatSizeBytes;

        // Position attribute
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        // Color attribute
        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizeBytes, positionsSize * floatSizeBytes);
        glEnableVertexAttribArray(1);

        // UV coordinates attribute
        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionsSize + colorSize) * floatSizeBytes);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {

        camera.position.x -= dt * 50.0f;

        // Bind shader program
        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        // Activate slot 0
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMath4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMath4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

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

        if (firstTime) {
            System.out.println("Creating game object");
            GameObject go = new GameObject("Game TEst 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = false;
        }

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }
    }
}
