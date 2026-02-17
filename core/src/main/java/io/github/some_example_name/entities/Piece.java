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

    // Idéalement: ne l’appeler QUE depuis Board.setPiece / Board.makeMove
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public abstract List<Move> getPossibleMoves(Board board);

    public abstract String getSymbol();

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" + (isWhite ? "White" : "Black") + ")@" + x + "," + y;
    }
}


