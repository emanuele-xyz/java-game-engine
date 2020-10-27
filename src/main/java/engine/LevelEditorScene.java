package engine;

import java.awt.event.KeyEvent;

public final class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2;

    public LevelEditorScene() {
        System.out.println("Inside level editor scene");
    }

    @Override
    public void update(float dt) {

        System.out.println(1.0 / dt + " FPS");

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5;
            Window.get().b -= dt * 5;
            Window.get().g -= dt * 5;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
