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

        play.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
    @Override public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
        game.startGame();
    }
});

options.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
    @Override public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
        game.goToOptions();
    }
});

quit.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
    @Override public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
        com.badlogic.gdx.Gdx.app.exit();
    }
});
  