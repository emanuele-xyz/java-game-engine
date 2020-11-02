package engine;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Camera camera;

    protected final List<GameObject> gameObjects = new ArrayList<>();
    private boolean isRunning = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject go : gameObjects) {
            go.start();
        }

        isRunning = true;
    }

    public abstract void update(float dt);

    public void addGameObjectToScene(GameObject gameObject) {
        gameObjects.add(gameObject);

        if (isRunning) {
            gameObject.start();
        }
    }
}
