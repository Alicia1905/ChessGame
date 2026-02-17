package io.github.some_example_name.entities;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    public List<Move> getPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        int direction = isWhite ? 1 : -1;          // blancs montent (y+), noirs descendent (y-)
        int startRow  = isWhite ? 1 : 6;           // ligne de départ des pions

        int oneStepY = y + direction;

        // 1) Avancer d'une case
        if (board.isInside(x, oneStepY) && board.isEmpty(x, oneStepY)) {
            moves.add(new Move(x, y, x, oneStepY));

            // 2) Avancer de deux cases depuis la position initiale
            int twoStepY = y + 2 * direction;
            if (y == startRow && board.isInside(x, twoStepY) && board.isEmpty(x, twoStepY)) {
                moves.add(new Move(x, y, x, twoStepY));
            }
        }

        // 3) Captures diagonales
        int diagLeftX = x - 1;
        int diagRightX = x + 1;

        if (board.isInside(diagLeftX, oneStepY) && board.hasEnemyPiece(diagLeftX, oneStepY, isWhite)) {
            moves.add(new Move(x, y, diagLeftX, oneStepY));
        }

        if (board.isInside(diagRightX, oneStepY) && board.hasEnemyPiece(diagRightX, oneStepY, isWhite)) {
            moves.add(new Move(x, y, diagRightX, oneStepY));
        }

        return moves;
    }

    @Override
    public String getSymbol() {
        return isWhite ? "P" : "p";
    }
}
