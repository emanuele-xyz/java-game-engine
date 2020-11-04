package engine;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;
    protected final List<GameObject> gameObjects = new ArrayList<>();
    protected  Renderer renderer = new Renderer();
    private boolean isRunning = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
            renderer.add(go);
        }

        isRunning = true;
    }

    public abstract void update(float dt);

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);
        renderer.add(gameObject);

        if (isRunning) {
            gameObject.start();
        }
    }

    public Camera getCamera() {
        return camera;
    }
}
