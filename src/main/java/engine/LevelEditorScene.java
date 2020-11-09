package engine;

import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public final class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        // All resources must be loaded before they are used
        loadResources();

        this.camera = new Camera(new Vector2f());
        int xOffset = 10;
        int yOffset = 10;

        float totalWidth = (600 - xOffset * 2);
        float totalHeight = (300 - yOffset * 2);
        float sizeX = totalWidth / 100.0f;
        float sizeY = totalHeight / 100.0f;

        for (int x = 0; x < 100; x++) {
            for (int y = 0; y < 100; y++) {
                float xPos = xOffset + (x * sizeX);
                float yPos = yOffset + (y * sizeY);

                GameObject go = new GameObject("Obj " + x + " " + y, new Transform(new Vector2f(xPos, yPos), new Vector2f(sizeX, sizeY)));
                go.addComponent(new SpriteRenderer(new Vector4f(xPos / totalWidth, yPos / totalHeight, 1, 1)));
                addGameObjectToScene(go);
            }
        }

    }

    private void loadResources() {
        AssetPool.loadShader(
                "default",
                "assets/shaders/default-vertex-shader.glsl",
                "assets/shaders/default-fragment-shader.glsl"
        );
    }

    @Override
    public void update(float dt) {

        System.out.println("FPS " + (1.0f / dt));

        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }
}
