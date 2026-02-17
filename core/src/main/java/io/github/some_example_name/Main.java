package io.github.some_example_name;

import com.badlogic.gdx.Game;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.utils.Assets;

public class Main extends Game {

    public Assets assets;

    @Override
    public void create() {
        assets = new Assets();
        assets.load();
        assets.finishLoading();

        setScreen(new GameScreen(this));
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) assets.dispose();
    }
}
