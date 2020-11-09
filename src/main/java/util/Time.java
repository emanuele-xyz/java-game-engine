package util;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public final class Time {

    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) glfwGetTime();
    }
}
