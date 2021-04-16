package com.github.bhlangonijr.chesslib.move;

import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;

import java.util.List;

public class PieceSquareMoves {

    private Piece piece;

    private Square square;

    private List<Move> moves;

    public PieceSquareMoves(Piece piece, Square square, List<Move> moves) {
        this.piece = piece;
        this.square = square;
        this.moves = moves;

    }

    public Piece getPiece() {
        return piece;
    }

    public Square getSquare() {
        return square;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public void setMoves(List<Move> moves) {
        this.moves = moves;
    }
}
