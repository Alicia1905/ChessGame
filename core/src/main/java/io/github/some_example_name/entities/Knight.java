package io.github.some_example_name.entities;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        int[] dx = { 2, 2,-2,-2, 1, 1,-1,-1 };
        int[] dy = { 1,-1, 1,-1, 2,-2, 2,-2 };

        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];

            if (!board.isInside(newX, newY)) continue;

            if (board.isEmpty(newX, newY) || board.hasEnemyPiece(newX, newY, isWhite)) {
                moves.add(new Move(x, y, newX, newY));
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "N" : "n"; // N = Knight (notation standard)
    }
}
