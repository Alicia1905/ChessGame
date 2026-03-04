package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class MenuScreen extends ScreenAdapter {
    
    private final Main game;
    private Stage stage;
    private Skin skin;

    public MenuScreen(Main game) {
        this.game = game;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table t = new Table();
        t.setFillParent(true);
        stage.addActor(t);

        t.add(new Label("CHESS GAME", skin)).pad(20).row();

        TextButton play = new TextButton("Jouer", skin);
        TextButton options = new TextButton("Options", skin);
        TextButton quit = new TextButton("Quitter", skin);

        t.add(play).width(240).pad(10).row();
        t.add(options).width(240).pad(10).row();
        t.add(quit).width(240).pad(10).row();

        play.addListener(e -> { if (!play.isPressed()) return false; game.startGame(); return true; });
        options.addListener(e -> { if (!options.isPressed()) return false; game.goToOptions(); return true; });
        quit.addListener(e -> { if (!quit.isPressed()) return false; Gdx.app.exit(); return true; });
    }

    @Override public void render(float delta) { stage.act(delta); stage.draw(); }

    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}
