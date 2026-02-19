package io.github.some_example_name.entities;

import java.util.List;

public abstract class Piece {

    protected final boolean isWhite;
    protected int x, y;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
    }

    public boolean isWhite() {
        return isWhite;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // Met à jour la position de la pièce (après un déplacement)
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract List<Move> getPossibleMoves(Board board);

    // Vérifie si un coup est valide pour cette pièce (en se basant sur les coups possibles)
    public boolean isValidMove(int targetX, int targetY, Board board) {
        List<Move> moves = getPossibleMoves(board);

        for (Move m : moves) {
            if (m.endX == targetX && m.endY == targetY) {
                return true;
            }
        }

        return false;
    }

    public abstract String getSymbol();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + (isWhite ? "White" : "Black") + ")@" + x + "," + y;
    }
}



