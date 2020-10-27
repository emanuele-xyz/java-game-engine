package engine;

import static org.lwjgl.glfw.GLFW.*;

public final class KeyListener {

    private static KeyListener instance;

    private final boolean[] keyPressed;

    private KeyListener() {
        this.keyPressed = new boolean[GLFW_KEY_LAST + 1];
    }

    public static KeyListener get() {
        if (instance == null) {
            instance = new KeyListener();
        }

        return instance;
    }

    public static void keyCallback(long window, int key , int scancode, int action, int mods) {
        if (action == GLFW_PRESS) {
            get().keyPressed[key] = true;
        } else if (action == GLFW_RELEASE) {
            get().keyPressed[key] = false;
        }
    }

    public static boolean isKeyPressed(int keyCode) {
        return get().keyPressed[keyCode];
    }
}
