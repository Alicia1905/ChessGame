package io.github.some_example_name.managers;

import io.github.some_example_name.entities.Board;
import io.github.some_example_name.entities.Move;
import io.github.some_example_name.entities.Piece;

import java.util.List;

public class GameManager {

    private Board board;
    private boolean isWhiteTurn = true;  // Blanc joue en premier

    public GameManager() {
        board = new Board();
    }

    // Démarre le jeu (ici juste un log pour debug) et affiche le plateau
    public void startGame() {
        System.out.println("Jeu d'échecs démarré !");
        // Si tu veux afficher le plateau dans la console
        // board.printBoard();
    }

    // Tente de déplacer une pièce de (startX,startY) vers (endX,endY) 
    public boolean movePiece(int startX, int startY, int endX, int endY) {
        Piece piece = board.getPiece(startX, startY);
        if (piece == null) return false;

        // Vérifie que c’est bien le tour du joueur
        if (piece.isWhite() != isWhiteTurn) return false;

        // Vérifie que le coup est possible selon les règles de la pièce
        if (!piece.isValidMove(endX, endY, board)) return false;

        // Applique le coup
        board.setPiece(endX, endY, piece);
        board.setPiece(startX, startY, null);
        piece.setPosition(endX, endY);

        // Change le tour
        isWhiteTurn = !isWhiteTurn;

        return true;
    }

    // Renvoie le plateau actuel 
    public Board getBoard() {
        return board;
    }

    // Renvoie le joueur qui doit jouer (true = blanc, false = noir) 
    public boolean getCurrentPlayer() {
        return isWhiteTurn;
    }

    // Renvoie tous les coups possibles pour le joueur actuel 
    public List<Move> getLegalMoves() {
        return board.getAllLegalMoves(isWhiteTurn);
    }

    // Méthode de debug : fait avancer le pion blanc de (0,1) à (0,3) si possible
    public void debugMovePawn() {
        List<Move> moves = getLegalMoves();
        for (Move m : moves) {
            if (m.startX == 0 && m.startY == 1 && m.endX == 0 && m.endY == 3) {
                movePiece(m.startX, m.startY, m.endX, m.endY);
                break;
            }
        }
    }
}


