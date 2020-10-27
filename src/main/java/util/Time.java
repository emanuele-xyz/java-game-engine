package util;

public final class Time {

    // TODO: probably should use glfwGetTime(), but what do i know?

    public static float timeStarted = System.nanoTime();

    public static float getTime() {
        return (float) ((System.nanoTime() - timeStarted) * 1E-9);
    }
}
