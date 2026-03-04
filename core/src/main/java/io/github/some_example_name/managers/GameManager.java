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

    // Nombre de pions capturés
    private int whiteCapturedPawns = 0;
    private int blackCapturedPawns = 0;

    public GameManager() {
        this.board = new Board();
    }

    public void startGame() {
        System.out.println("Jeu d'échecs démarré !");
    }

    // Déplace une pièce si le coup est valide
    public boolean movePiece(int startX, int startY, int endX, int endY) {

        Piece piece = board.getPiece(startX, startY);
        if (piece == null) return false;

        if (piece.isWhite() != whiteTurn) return false;

        Move move = new Move(startX, startY, endX, endY);

        if (!isLegalMove(move)) return false;

        Piece captured = board.getPiece(endX, endY);

        if (captured instanceof Pawn) {
            if (piece.isWhite()) {
                whiteCapturedPawns++;
            } else {
                blackCapturedPawns++;
            }
        }

        // appliquer le mouvement
        board.makeMove(move);

        // promotion éventuelle
        promotion(move.endX, move.endY);

        // changer de tour
        whiteTurn = !whiteTurn;

        return true;
    }

    private boolean isLegalMove(Move move) {

        List<Move> legalMoves = board.getAllLegalMoves(whiteTurn);

        for (Move m : legalMoves) {
            if (m.startX == move.startX &&
                m.startY == move.startY &&
                m.endX == move.endX &&
                m.endY == move.endY) {

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

    // Promotion automatique en reine
    private void promotion(int x, int y) {

        Piece piece = board.getPiece(x, y);

        if (piece == null) return;

        // pion blanc arrivé en haut
        if (piece instanceof Pawn && piece.isWhite() && y == 7) {
            board.setPiece(x, y, new Queen(true));
        }

        // pion noir arrivé en bas
        if (piece instanceof Pawn && !piece.isWhite() && y == 0) {
            board.setPiece(x, y, new Queen(false));
        }
    }

    // Affichage du score
    public int getWhiteCapturedPawns() {
        return whiteCapturedPawns;
    }

    public int getBlackCapturedPawns() {
        return blackCapturedPawns;
    }

}
