package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.some_example_name.Main;

public class EndScreen extends ScreenAdapter 
{
    private final Main game;
    private final boolean win;
    private final int score;

    private Stage stage;
    private Skin skin;

    public EndScreen(Main game, boolean win, int score) {
        this.game = game;
        this.win = win;
        this.score = score;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        skin = new Skin(Gdx.files.internal("uiskin.json"));

        Table t = new Table();
        t.setFillParent(true);
        stage.addActor(t);

        Label msg = new Label(win ? "TU AS GAGNÉ !" : "TU AS PERDU !", skin);
        Label sc = new Label("Score: " + score, skin);

        TextButton replay = new TextButton("Rejouer", skin);
        TextButton menu = new TextButton("Menu", skin);

        t.add(msg).pad(20).row();
        t.add(sc).pad(10).row();
        t.add(replay).width(240).pad(10).row();
        t.add(menu).width(240).pad(10).row();

        replay.addListener(e -> { if (!replay.isPressed()) return false; game.startGame(); return true; });
        menu.addListener(e -> { if (!menu.isPressed()) return false; game.goToMenu(); return true; });
    }

    @Override public void render(float delta) { stage.act(delta); stage.draw(); }

    @Override public void dispose() { stage.dispose(); skin.dispose(); }
}