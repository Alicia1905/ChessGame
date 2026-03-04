package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import io.github.some_example_name.managers.AudioManager;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.MenuScreen;
import io.github.some_example_name.screens.OptionsScreen;
import io.github.some_example_name.utils.Assets;

public class Main extends Game {

    public Assets assets;
    public AudioManager audio;

    @Override
    public void create() {
        assets = new Assets();
        assets.load();
        assets.finishLoading();
        audio = new AudioManager();
        audio.load();
        audio.playBgm();

        setScreen(new MenuScreen(this));
    }

    /** Change d'écran proprement (évite stages/UI “fantômes”) */
    private void setScreenSafe(Screen next) {
        Screen current = getScreen();
        setScreen(next);
        if (current != null) current.dispose();
    }

    // démarrer la partie
    public void startGame() {
        setScreenSafe(new GameScreen(this));
    }

    // aller au menu
    public void goToMenu() {
        setScreenSafe(new MenuScreen(this));
    }

    // aller aux options
    public void goToOptions() {
        setScreenSafe(new OptionsScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (audio != null) audio.dispose();
        if (assets != null) assets.dispose();
    }
}