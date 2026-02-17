package io.github.some_example_name.utils;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Assets {

    public final AssetManager manager = new AssetManager();

    // Textures plateau
    public Texture darkTile;
    public Texture lightTile;

    // Pièces blanches
    public Texture wPawn, wRook, wKnight, wBishop, wQueen, wKing;

    // Pièces noires
    public Texture bPawn, bRook, bKnight, bBishop, bQueen, bKing;

    public void load() {
        // Plateau
        manager.load("pieces/square_brown_dark.png", Texture.class);
        manager.load("pieces/square_brown_light.png", Texture.class);

        // Blanc
        manager.load("pieces/w_pawn.png", Texture.class);
        manager.load("pieces/w_rook.png", Texture.class);
        manager.load("pieces/w_knight.png", Texture.class);
        manager.load("pieces/w_bishop.png", Texture.class);
        manager.load("pieces/w_queen.png", Texture.class);
        manager.load("pieces/w_king.png", Texture.class);

        // Noir
        manager.load("pieces/b_pawn.png", Texture.class);
        manager.load("pieces/b_rook.png", Texture.class);
        manager.load("pieces/b_knight.png", Texture.class);
        manager.load("pieces/b_bishop.png", Texture.class);
        manager.load("pieces/b_queen.png", Texture.class);
        manager.load("pieces/b_king.png", Texture.class);
    }

    public void finishLoading() {
        manager.finishLoading();

        // Récupération des assets
        darkTile  = manager.get("pieces/square_brown_dark.png", Texture.class);
        lightTile = manager.get("pieces/square_brown_light.png", Texture.class);

        wPawn   = manager.get("pieces/w_pawn.png", Texture.class);
        wRook   = manager.get("pieces/w_rook.png", Texture.class);
        wKnight = manager.get("pieces/w_knight.png", Texture.class);
        wBishop = manager.get("pieces/w_bishop.png", Texture.class);
        wQueen  = manager.get("pieces/w_queen.png", Texture.class);
        wKing   = manager.get("pieces/w_king.png", Texture.class);

        bPawn   = manager.get("pieces/b_pawn.png", Texture.class);
        bRook   = manager.get("pieces/b_rook.png", Texture.class);
        bKnight = manager.get("pieces/b_knight.png", Texture.class);
        bBishop = manager.get("pieces/b_bishop.png", Texture.class);
        bQueen  = manager.get("pieces/b_queen.png", Texture.class);
        bKing   = manager.get("pieces/b_king.png", Texture.class);
    }

    public void dispose() {
        manager.dispose(); // dispose toutes les textures chargées
    }
}

