package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import io.github.some_example_name.Main;

public class EndScreen implements Screen {

    private final Main game;
    private final String resultMessage;

    private final int whiteScore;
    private final int blackScore;

    private int bestScore;

    private SpriteBatch batch;
    private BitmapFont font;

    public EndScreen(Main game, String resultMessage, int whiteScore, int blackScore) {

        this.game = game;
        this.resultMessage = resultMessage;
        this.whiteScore = whiteScore;
        this.blackScore = blackScore;

        batch = new SpriteBatch();
        font = new BitmapFont();

        loadBestScore();
        updateBestScore();
    }

    // Charger le meilleur score sauvegardé
    private void loadBestScore() {
        Preferences prefs = Gdx.app.getPreferences("ChessScores");
        bestScore = prefs.getInteger("bestScore", 0);
    }

    // Mettre à jour le record si battu
    private void updateBestScore() {

        int currentBest = Math.max(whiteScore, blackScore);

        if (currentBest > bestScore) {

            bestScore = currentBest;

            Preferences prefs = Gdx.app.getPreferences("ChessScores");
            prefs.putInteger("bestScore", bestScore);
            prefs.flush();
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        font.draw(batch, resultMessage, 350, 500);

        font.draw(batch, "Blanc a capturé : " + whiteScore, 330, 420);
        font.draw(batch, "Noir a capturé : " + blackScore, 330, 380);

        font.draw(batch, "Best capture score : " + bestScore, 330, 320);

        font.draw(batch, "Cliquez sur ENTER pour retourner au menu", 300, 250);

        batch.end();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.setScreen(new MenuScreen(game));
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}