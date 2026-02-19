package io.github.some_example_name.managers;

import io.github.some_example_name.entities.Board;
import io.github.some_example_name.entities.Move;
import io.github.some_example_name.entities.Pawn;
import io.github.some_example_name.entities.Piece;
import io.github.some_example_name.entities.Queen;


import java.util.List;

public class GameManager {
    private final Board board;
    private boolean whiteTurn = true; // Blanc commence

    public GameManager() {
        this.board = new Board();
    }

    public void startGame() {
        System.out.println("Jeu d'échecs démarré !");
    }

    // Déplace une pièce si le coup est le bon
    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece == null) return false;

        // tour du joueur
        if (piece.isWhite() != whiteTurn) return false;

        Move move = new Move(startX, startY, endX, endY);

        // vérifier que le coup est légal (ne met pas son roi en échec)
        if (!isLegalMove(move)) return false;

        // appliquer via Board 
        board.makeMove(move);
        Promotion(move.endX, move.endY);


        // changer de tour
        whiteTurn = !whiteTurn;

        return true;
    }

    private boolean isLegalMove(Move move) {
        // On prend tous les coups légaux du joueur et on compare
        List<Move> legalMoves = board.getAllLegalMoves(whiteTurn);

        for (Move m : legalMoves) {
            if (m.startX == move.startX && m.startY == move.startY
                    && m.endX == move.endX && m.endY == move.endY) {
                return true;
            }
        }

        return false;
    }

    public Board getBoard() {
        return board;
    }

    public boolean getCurrentPlayer() {
        return whiteTurn;
    }

    public List<Move> getLegalMoves() {
        return board.getAllLegalMoves(whiteTurn);
    }

    private void Promotion(int x, int y) {
        Piece piece = board.getPiece(x, y);

        if (piece == null) return;

        // Si c'est un pion blanc arrivé en haut
        if (piece instanceof Pawn && piece.isWhite() && y == 7) {
            board.setPiece(x, y, new Queen(true));
        }

        // Si c'est un pion noir arrivé en bas
        if (piece instanceof Pawn && !piece.isWhite() && y == 0) {
            board.setPiece(x, y, new Queen(false));
        }
    }

}
