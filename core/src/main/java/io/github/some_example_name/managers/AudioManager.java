package io.github.some_example_name.managers;

public class AudioManager {

    private static float volume = 0.7f;

    public static void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
    }

    public static float getVolume() {
        return volume;
    }
}