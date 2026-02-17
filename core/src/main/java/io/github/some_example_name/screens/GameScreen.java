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
import io.github.some_example_name.utils.Assets;

public class GameScreen implements Screen {

    private final Main game;
    private final Assets assets;

    private final SpriteBatch batch;
    private final Board board;

    private final OrthographicCamera camera;
    private final Viewport viewport;

    private final ShapeRenderer shapeRenderer;

    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private static final int WORLD_WIDTH  = BOARD_SIZE * TILE_SIZE;
    private static final int WORLD_HEIGHT = BOARD_SIZE * TILE_SIZE;

    // --- Interaction ---
    private boolean whiteToMove = true;
    private Piece selectedPiece = null;
    private List<Move> selectedMoves = new ArrayList<>();

    private boolean wasLeftDown = false; // pour détecter "clic" 
    private final Vector3 tmpVec = new Vector3();

    public GameScreen(Main game) {
        this.game = game;
        this.assets = game.assets; // textures chargées via AssetManager dans Main

        this.batch = new SpriteBatch();
        this.board = new Board();

        this.camera = new OrthographicCamera();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        this.viewport.apply();
        this.camera.position.set(WORLD_WIDTH / 2f, WORLD_HEIGHT / 2f, 0);

        this.shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        handleInput();

        Gdx.gl.glClearColor(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        // 1) Dessin plateau + pièces (SpriteBatch)
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        drawBoard();
        drawPieces();
        batch.end();

        // 2) Marqueurs (ShapeRenderer)
        shapeRenderer.setProjectionMatrix(camera.combined);
        drawHighlights();
    }

    private void handleInput() {
        boolean leftDown = Gdx.input.isButtonPressed(Input.Buttons.LEFT);

        // on ne traite que le moment où le bouton passe de "pas pressé" à "pressé"
        if (leftDown && !wasLeftDown) {
            // convertir coordonnées écran 
            tmpVec.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(tmpVec);

            int boardX = (int) (tmpVec.x / TILE_SIZE);
            int boardY = (int) (tmpVec.y / TILE_SIZE);

            if (!board.isInside(boardX, boardY)) {
                clearSelection();
            } else {
                onBoardClick(boardX, boardY);
            }
        }

        wasLeftDown = leftDown;

        // clic droit pour annuler (optionnel)
        if (Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            clearSelection();
        }
    }

    private void onBoardClick(int x, int y) {
        // 1) si une pièce est sélectionnée et on clique une destination valide 
        if (selectedPiece != null) {
            Move chosen = findMoveTo(x, y, selectedMoves);
            if (chosen != null) {
                board.makeMove(chosen);
                clearSelection();
                whiteToMove = !whiteToMove;
                return;
            }
        }

        // 2) sinon : essayer de sélectionner une pièce
        Piece p = board.getPiece(x, y);

        if (p == null) {
            clearSelection();
            return;
        }

        // ne sélectionner que la couleur qui doit jouer
        if (p.isWhite() != whiteToMove) {
            clearSelection();
            return;
        }

        selectedPiece = p;

        // coups légaux (filtrés pour ne pas mettre son roi en échec)
        selectedMoves = computeLegalMovesForSelectedPiece();
        if (selectedMoves.isEmpty()) {
            clearSelection();
        }
    }

    private List<Move> computeLegalMovesForSelectedPiece() {
        List<Move> legal = new ArrayList<>();
        if (selectedPiece == null) return legal;

        // coups pseudo-légaux de la pièce
        List<Move> pseudo = selectedPiece.getPossibleMoves(board);

        // filtrer en simulant pour ne pas mettre son roi en échec
        boolean white = selectedPiece.isWhite();
        for (Move m : pseudo) {
            Board copy = board.copy();
            copy.makeMove(m);
            if (!copy.isInCheck(white)) {
                legal.add(m);
            }
        }
        return legal;
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
                Texture tile = ((x + y) % 2 == 0) ? assets.lightTile : assets.darkTile;
                batch.draw(tile, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPieces() {
        for (int x = 0; x < BOARD_SIZE; x++) {
            for (int y = 0; y < BOARD_SIZE; y++) {
                Piece piece = board.getPiece(x, y);
                if (piece == null) continue;

                Texture texture = getTextureForPiece(piece);
                if (texture == null) continue;

                batch.draw(texture, x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    // Dessiner les surlignages (ShapeRenderer)
    private void drawHighlights() {
        if (selectedPiece == null) return;

        Gdx.gl.glEnable(GL20.GL_BLEND);

        // 1) Contour vert autour de la case sélectionnée
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        float sx = selectedPiece.getX() * TILE_SIZE;
        float sy = selectedPiece.getY() * TILE_SIZE;

        // petit padding pour éviter que ça colle pile sur le bord
        shapeRenderer.rect(sx + 1, sy + 1, TILE_SIZE - 2, TILE_SIZE - 2);
        shapeRenderer.end();

        // 2) Points verts au centre des cases destinations
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.GREEN);

        float radius = TILE_SIZE * 0.12f; // taille du point
        for (Move m : selectedMoves) {
            float cx = m.endX * TILE_SIZE + TILE_SIZE / 2f;
            float cy = m.endY * TILE_SIZE + TILE_SIZE / 2f;
            shapeRenderer.circle(cx, cy, radius);
        }

        shapeRenderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);
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

