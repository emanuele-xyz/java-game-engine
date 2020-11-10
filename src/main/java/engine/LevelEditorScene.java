package engine;

import components.SpriteRenderer;
import org.joml.Vector2f;
import util.AssetPool;

public final class LevelEditorScene extends Scene {

    public LevelEditorScene() {

    }

    @Override
    public void init() {

        // All resources must be loaded before they are used
        loadResources();

        this.camera = new Camera(new Vector2f());

        GameObject obj1 = new GameObject("Object 1", new Transform(new Vector2f(100, 100), new Vector2f(256, 256)));
        obj1.addComponent(new SpriteRenderer(AssetPool.getTexture("image 1")));
        addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)));
        obj2.addComponent(new SpriteRenderer(AssetPool.getTexture("image 2")));
        addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.loadShader(
                "default",
                "assets/shaders/default-vertex-shader.glsl",
                "assets/shaders/default-fragment-shader.glsl"
        );

        AssetPool.loadTexture("image 1", "assets/images/test-image-1.png");
        AssetPool.loadTexture("image 2", "assets/images/test-image-2.png");
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
