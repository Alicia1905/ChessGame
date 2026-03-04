package io.github.some_example_name.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Music;

public class AudioManager {

    private Music bgm;
    private float volume;

    private final Preferences prefs = Gdx.app.getPreferences("chess_settings");

    public void load() {
        volume = prefs.getFloat("volume", 0.7f);

        bgm = Gdx.audio.newMusic(Gdx.files.internal("son/041415calmbgm_0.ogg"));
        bgm.setLooping(true);
        bgm.setVolume(volume);
    }

    public void playBgm() {
        if (bgm != null && !bgm.isPlaying()) bgm.play();
    }

    public void setVolume(float v) {
        volume = Math.max(0f, Math.min(1f, v));
        if (bgm != null) bgm.setVolume(volume);

        prefs.putFloat("volume", volume);
        prefs.flush();
    }

    public float getVolume() {
        return volume;
    }

    public void dispose() {
        if (bgm != null) bgm.dispose();
    }
}