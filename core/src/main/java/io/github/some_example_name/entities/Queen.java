package io.github.some_example_name.entities;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Piece {

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        int[][] directions = {
                { 1, 0}, {-1, 0}, { 0, 1}, { 0,-1},
                { 1, 1}, { 1,-1}, {-1, 1}, {-1,-1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            int newX = x + dx;
            int newY = y + dy;

            while (board.isInside(newX, newY)) {
                if (board.isEmpty(newX, newY)) {
                    moves.add(new Move(x, y, newX, newY));
                } else {
                    if (board.hasEnemyPiece(newX, newY, isWhite)) {
                        moves.add(new Move(x, y, newX, newY));
                    }
                    break;
                }
                newX += dx;
                newY += dy;
            }
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "Q" : "q";
    }
}



