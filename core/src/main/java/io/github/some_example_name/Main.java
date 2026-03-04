package io.github.some_example_name;

import com.badlogic.gdx.Game;
import io.github.some_example_name.screens.MenuScreen;
import io.github.some_example_name.utils.Assets;

public class Main extends Game {

    public Assets assets;

    @Override
    public void create() {
        assets = new Assets();     // ton Assets.java existe déjà
        setScreen(new MenuScreen(this)); // on démarre par le menu
    }

    @Override
    public void dispose() {
        super.dispose();
        if (assets != null) assets.dispose();
    }
}