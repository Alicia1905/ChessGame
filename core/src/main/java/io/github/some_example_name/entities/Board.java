package io.github.some_example_name.entities;

import java.util.ArrayList;
import java.util.List;

public class Board {

    public static final int SIZE = 8;
    private final Piece[][] grid;

    public Board() {
        grid = new Piece[SIZE][SIZE];
        setupInitialPosition();
    }

    private Board(boolean empty) {
        grid = new Piece[SIZE][SIZE];
    }

    private void setupInitialPosition() {
        // PIONS
        for (int i = 0; i < SIZE; i++) {
            setPiece(i, 1, new Pawn(true));   // Blancs
            setPiece(i, 6, new Pawn(false));  // Noirs
        }

        // TOURS
        setPiece(0, 0, new Rook(true));
        setPiece(7, 0, new Rook(true));
        setPiece(0, 7, new Rook(false));
        setPiece(7, 7, new Rook(false));

        // CHEVALIERS
        setPiece(1, 0, new Knight(true));
        setPiece(6, 0, new Knight(true));
        setPiece(1, 7, new Knight(false));
        setPiece(6, 7, new Knight(false));

        // FOUS
        setPiece(2, 0, new Bishop(true));
        setPiece(5, 0, new Bishop(true));
        setPiece(2, 7, new Bishop(false));
        setPiece(5, 7, new Bishop(false));

        // REINES
        setPiece(3, 0, new Queen(true));
        setPiece(3, 7, new Queen(false));

        // ROIS
        setPiece(4, 0, new King(true));
        setPiece(4, 7, new King(false));
    }

    public boolean isInside(int x, int y) {
        return x >= 0 && x < SIZE && y >= 0 && y < SIZE;
    }

    public boolean isEmpty(int x, int y) {
        return getPiece(x, y) == null;
    }

    public boolean hasEnemyPiece(int x, int y, boolean isWhite) {
        Piece p = getPiece(x, y);
        return p != null && p.isWhite() != isWhite;
    }

    public Piece getPiece(int x, int y) {
        if (!isInside(x, y)) return null;
        return grid[x][y];
    }

    public void setPiece(int x, int y, Piece piece) {
        if (!isInside(x, y)) return;
        grid[x][y] = piece;
        if (piece != null) piece.setPosition(x, y);
    }

    public void makeMove(Move move) {
        if (move == null) return;
        if (!isInside(move.startX, move.startY) || !isInside(move.endX, move.endY)) return;

        Piece piece = getPiece(move.startX, move.startY);
        if (piece == null) return;

        // retire la pièce de départ
        grid[move.startX][move.startY] = null;

        // pose la pièce 
        setPiece(move.endX, move.endY, piece);
    }

    public Board copy() {
        Board newBoard = new Board(true);

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = grid[x][y];
                if (p != null) {
                    newBoard.setPiece(x, y, copyPiece(p));
                }
            }
        }
        return newBoard;
    }

    private Piece copyPiece(Piece p) {
        if (p instanceof Pawn) return new Pawn(p.isWhite());
        if (p instanceof Rook) return new Rook(p.isWhite());
        if (p instanceof Knight) return new Knight(p.isWhite());
        if (p instanceof Bishop) return new Bishop(p.isWhite());
        if (p instanceof Queen) return new Queen(p.isWhite());
        if (p instanceof King) return new King(p.isWhite());
        return null;
    }

    public boolean isInCheck(boolean white) {
        int kingX = -1, kingY = -1;

        // Trouver le roi
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = grid[x][y];
                if (p instanceof King && p.isWhite() == white) {
                    kingX = x;
                    kingY = y;
                    break;
                }
            }
        }

        // Si on ne trouve pas le roi, on ne peut pas vérifier l'échec correctement
        if (kingX == -1) return false;

        // Vérifier les attaques ennemies
        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = grid[x][y];
                if (p != null && p.isWhite() != white) {
                    // ici, getPossibleMoves ne devrait pas dépendre d'une position interne modifiée pendant l'itération.
                    List<Move> moves = p.getPossibleMoves(this);
                    for (Move m : moves) {
                        if (m.endX == kingX && m.endY == kingY) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheckmate(boolean white) {
        return isInCheck(white) && getAllLegalMoves(white).isEmpty();
    }

    public boolean isStalemate(boolean white) {
        return !isInCheck(white) && getAllLegalMoves(white).isEmpty();
    }

    public List<Move> getAllLegalMoves(boolean white) {
        List<Move> legalMoves = new ArrayList<>();

        for (int x = 0; x < SIZE; x++) {
            for (int y = 0; y < SIZE; y++) {
                Piece p = grid[x][y];
                if (p != null && p.isWhite() == white) {
                    for (Move m : p.getPossibleMoves(this)) {
                        Board copy = this.copy();
                        copy.makeMove(m);
                        if (!copy.isInCheck(white)) {
                            legalMoves.add(m);
                        }
                    }
                }
            }
        }

        return legalMoves;
    }
}
