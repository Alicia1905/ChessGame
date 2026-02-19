package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;

import io.github.some_example_name.Main;
import io.github.some_example_name.entities.Board;
import io.github.some_example_name.entities.Move;
import io.github.some_example_name.entities.Piece;
import io.github.some_example_name.managers.GameManager;
import io.github.some_example_name.utils.Assets;

public class GameScreen implements Screen {
    private final Main game;
    private final Assets assets;

    private final SpriteBatch batch;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    private final ShapeRenderer shapeRenderer;

    private final GameManager gameManager;

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;
    private static final int WORLD_WIDTH  = BOARD_SIZE * TILE_SIZE;
    private static final int WORLD_HEIGHT = BOARD_SIZE * TILE_SIZE;

    // L'interaction et sélection
    private Piece selectedPiece = null;
    private List<Move> selectedMoves = new ArrayList<>();

    private boolean wasLeftDown = false;
    private final Vector3 tmpVec = new Vector3();

    public GameScreen(Main game) {
        this.game = game;
        this.assets = game.assets;

        this.batch = new SpriteBatch();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.viewport.apply();
        this.camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        this.shapeRenderer = new ShapeRenderer();

        this.gameManager = new GameManager();
        this.gameManager.startGame();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawBoard();
        drawPieces();
        batch.end();

        // Dessin des contours + points
        shapeRenderer.setProjectionMatrix(camera.combined);
        drawOverlays();
    }

    private Board board() {
        return gameManager.getBoard();
    }

    private void handleInput() {
        boolean leftDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        if (leftDown && !wasLeftDown) {
            tmpVec.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tmpVec);

            int boardX = (int) (tmpVec.x / TILE_SIZE);
            int boardY = (int) (tmpVec.y / TILE_SIZE);

            if (!board().isInside(boardX, boardY)) {
                clearSelection();
            } else {
                onBoardClick(boardX, boardY);
            }
        }

        wasLeftDown = leftDown;

        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            clearSelection();
        }
    }

    private void onBoardClick(int x, int y) {
        // tenter un move si une pièce est sélectionnée
        if (selectedPiece != null) {
            Move chosen = findMoveTo(x, y, selectedMoves);
            if (chosen != null) {
                boolean moved = gameManager.movePiece(chosen.startX, chosen.startY, chosen.endX, chosen.endY);
                if (moved) {
                    clearSelection();
                    return;
                }
            }
        }

        // sélectionner une pièce si c'est la bonne couleur
        Piece p = board().getPiece(x, y);
        if (p == null) {
            clearSelection();
            return;
        }

        boolean whiteTurn = gameManager.getCurrentPlayer();
        if (p.isWhite() != whiteTurn) {
            clearSelection();
            return;
        }

        selectedPiece = p;
        selectedMoves = computeLegalMovesForSelectedPiece();

        if (selectedMoves.isEmpty()) {
            clearSelection();
        }
    }

    private List<Move> computeLegalMovesForSelectedPiece() {
        List<Move> result = new ArrayList<>();
        if (selectedPiece == null) return result;

        boolean whiteTurn = gameManager.getCurrentPlayer();
        List<Move> legalMoves = board().getAllLegalMoves(whiteTurn);

        int sx = selectedPiece.getX();
        int sy = selectedPiece.getY();

        for (Move m : legalMoves) {
            if (m.startX == sx && m.startY == sy) {
                result.add(m);
            }
        }

        return result;
    }

    private Move findMoveTo(int x, int y, List<Move> moves) {
        for (Move m : moves) {
            if (m.endX == x && m.endY == y) return m;
        }
        return null;
    }

    private void clearSelection() {
        selectedPiece = null;
        selectedMoves.clear();
    }

    private void drawBoard() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                Texture tile = ((x + y) % 2 == 0) ? assets.light : assets.dark;
                batch.draw(tile, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPieces() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                Piece piece = board().getPiece(x, y);
                if (piece == null) continue;

                Texture texture = getTextureForPiece(piece);
                if (texture == null) continue;

                batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // Tout ce qui est "dessiné par-dessus" (contours + points + roi en échec)
    private void drawOverlays() {
        Gdx.gl.glEnable(GL20.GL_BLEND);

        // contours (sélection + roi en échec)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        drawSelectedOutline(); // vert
        drawCheckOutlines();   // rouge

        shapeRenderer.end();

        // points de déplacement
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawMoveDots(); // verts
        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void drawSelectedOutline() {
        if (selectedPiece == null) return;

        shapeRenderer.setColor(Color.GREEN);

        float sx = selectedPiece.getX() * TILE_SIZE;
        float sy = selectedPiece.getY() * TILE_SIZE;

        shapeRenderer.rect(sx + 1, sy + 1, TILE_SIZE - 2, TILE_SIZE - 2);
    }

    private void drawMoveDots() {
        if (selectedPiece == null) return;

        shapeRenderer.setColor(Color.GREEN);

        float radius = TILE_SIZE * 0.12f;
        for (Move m : selectedMoves) {
            float cx = m.endX * TILE_SIZE + TILE_SIZE / 2f;
            float cy = m.endY * TILE_SIZE + TILE_SIZE / 2f;
            shapeRenderer.circle(cx, cy, radius);
        }
    }

    // Contours rouges sur le(s) roi(s) en échec
    private void drawCheckOutlines() {
        Board b = board();

        if (b.isInCheck(true)) {
            Piece king = findKing(true);
            if (king != null) drawRedKingOutline(king.getX(), king.getY());
        }

        if (b.isInCheck(false)) {
            Piece king = findKing(false);
            if (king != null) drawRedKingOutline(king.getX(), king.getY());
        }
    }

    private Piece findKing(boolean white) {
        Board b = board();
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                Piece p = b.getPiece(x, y);
                if (p != null && p.isWhite() == white && "King".equals(p.getClass().getSimpleName())) {
                    return p;
                }
            }
        }
        return null;
    }

    private void drawRedKingOutline(int kingX, int kingY) {
        shapeRenderer.setColor(Color.RED);

        float x = kingX * TILE_SIZE;
        float y = kingY * TILE_SIZE;

        // double rectangle 
        shapeRenderer.rect(x + 1, y + 1, TILE_SIZE - 2, TILE_SIZE - 2);
        shapeRenderer.rect(x + 2, y + 2, TILE_SIZE - 4, TILE_SIZE - 4);
    }

    private Texture getTextureForPiece(Piece piece) {
        String name = piece.getClass().getSimpleName();
        boolean isWhite = piece.isWhite();

        switch (name) {
            case "Pawn":   return isWhite ? assets.wPawn : assets.bPawn;
            case "Rook":   return isWhite ? assets.wRook : assets.bRook;
            case "Knight": return isWhite ? assets.wKnight : assets.bKnight;
            case "Bishop": return isWhite ? assets.wBishop : assets.bBishop;
            case "Queen":  return isWhite ? assets.wQueen : assets.bQueen;
            case "King":   return isWhite ? assets.wKing : assets.bKing;
            default:       return null;
        }
    }

    @Override public void show() {}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
    }
}



