package renderer;

import components.SpriteRenderer;
import engine.Window;
import org.joml.Vector4f;
import util.AssetPool;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public final class RenderBatch {

    // Vertex layout
    // ====================
    // position         color
    // float, float     float, float, float, float

    private static final int POS_SIZE = 2;
    private static final int COLOR_SIZE = 2;
    private static final int POS_OFFSET = 0;
    private static final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private static final int VERTEX_SIZE = 6;
    private static final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasRoom;
    private float[] vertices;

    private int vaoID;
    private int vboID;
    private int maxBatchSize;
    private final ShaderProgram shader;

    public RenderBatch(int maxBatchSize) {
        this.shader = AssetPool.getShader("default");

        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        // how many floats in total?
        // 4 = vertices per quad
        this.vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasRoom = true;
    }

    // Create all the data on the GPU
    public void start() {
        // Generate and bind a Vertex Array Object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Allocate space for vertices
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        // Create and upload indices
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute pointer
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);
    }

    public void addSprite(SpriteRenderer spriteRenderer) {
        if (!hasRoom) {
            return;
        }

        // Get index and add renderObject
        int index = numSprites;
        sprites[index] = spriteRenderer;
        numSprites++;

        // Add properties to local vertices array
        loadVertexProperties(index);

        if (numSprites >= maxBatchSize) {
            hasRoom = false;
        }
    }

    public void render() {

        // For now we will re-buffer all data every frame
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);

        // Use shader
        shader.use();
        shader.uploadMath4f("uProjection", Window.getCurrentScene().getCamera().getProjectionMatrix());
        shader.uploadMath4f("uView", Window.getCurrentScene().getCamera().getViewMatrix());

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, numSprites * 6 , GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        shader.detach();
    }

    public boolean hasRoom() {
        return this.hasRoom;
    }

    private int[] generateIndices() {
        // 6 indices per quad (3 per triangle)
        int[] elements = new int[6 * maxBatchSize];

        for (int i = 0; i < maxBatchSize; i++) {
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int i) {
        int offsetArrayIndex = 6 * i;
        int offset = 4 * i;

        // 3, 2, 0, 0, 2, 1    7, 6, 4, 4, 6, 5

        // Triangle 1
        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        // Triangle 2
        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;

    }

    private void loadVertexProperties(int index) {
        // Create four vertices per quad
        SpriteRenderer sprite = sprites[index];

        // Find offset within array
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();

        // Add vertices with appropriate properties

        // (0, 1)   (1, 1)
        //
        //
        // (0, 0)   (1, 0)

        float xAdd = 1;
        float yAdd = 1;
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 1 -> yAdd = 0;
                case 2 -> xAdd = 0;
                case 3 -> yAdd = 1;
            }

            // Load Position
            vertices[offset] = sprite.getParent().transform.position.x + (xAdd * sprite.getParent().transform.scale.x);
            vertices[offset + 1] = sprite.getParent().transform.position.y + (yAdd * sprite.getParent().transform.scale.y);

            // Load Color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            offset += VERTEX_SIZE;
        }
    }

}
