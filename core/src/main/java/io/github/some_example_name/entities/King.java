package io.github.some_example_name.entities;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {

        List<Move> moves = new ArrayList<>();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {

                if (dx == 0 && dy == 0) continue;

                int newX = x + dx;
                int newY = y + dy;

                if (board.isInside(newX, newY)) {

                    if (board.isEmpty(newX, newY) ||
                        board.hasEnemyPiece(newX, newY, isWhite)) {

                        moves.add(new Move(x, y, newX, newY));
                    }
                }
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "K" : "k";
    }
}


