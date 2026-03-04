package io.github.some_example_name;

import com.badlogic.gdx.Game;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.MenuScreen;
import io.github.some_example_name.screens.OptionsScreen;
import io.github.some_example_name.utils.Assets;

public class Main extends Game {

    public Assets assets;

    @Override
    public void create() {

        assets = new Assets();
        assets.load();
        assets.finishLoading();

        setScreen(new MenuScreen(this));
    }

    // démarrer la partie
    public void startGame() {
        setScreen(new GameScreen(this));
    }

    // aller au menu
    public void goToMenu() {
        setScreen(new MenuScreen(this));
    }

    // aller aux options
    public void goToOptions() {
        setScreen(new OptionsScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) assets.dispose();
    }
}