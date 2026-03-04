package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;
import io.github.some_example_name.managers.AudioManager;

public class OptionsScreen extends ScreenAdapter {

    private final Main game;
    private Stage stage;
    private Skin skin;

    public OptionsScreen(Main game) {
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

        Label title = new Label("OPTIONS", skin);
        Slider vol = new Slider(0f, 1f, 0.01f, false, skin);
        vol.setValue(AudioManager.getVolume());

        Label info = new Label("Volume: " + (int)(vol.getValue()*100) + "%", skin);
        TextButton back = new TextButton("Retour", skin);

        t.add(title).pad(20).row();
        t.add(info).pad(10).row();
        t.add(vol).width(300).pad(10).row();
        t.add(back).width(240).pad(20).row();

        vol.addListener(e -> {
            AudioManager.setVolume(vol.getValue());
            info.setText("Volume: " + (int)(vol.getValue()*100) + "%");
            return false;
        });

        back.addListener(e -> {
            if (!back.isPressed()) return false;
            game.goToMenu();
            return true;
        });
    }

    @Override public void render(float delta) { stage.act(delta); stage.draw(); }

    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}