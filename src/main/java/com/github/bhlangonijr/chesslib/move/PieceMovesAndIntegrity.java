package com.github.bhlangonijr.chesslib.move;

import com.github.bhlangonijr.chesslib.Piece;

import java.util.List;

public class PieceMovesAndIntegrity {

    private PieceSquareMoves pieceSquareMoves;

    private Integer ownIntegrity = 0;

    private Integer strangeIntegrity = 0;

    private Integer maxMoves;

    public PieceMovesAndIntegrity(PieceSquareMoves pieceSquareMoves) {
        this.pieceSquareMoves = pieceSquareMoves;
        this.ownIntegrity = Piece.getPieceMaxMoveInteger(pieceSquareMoves.getPiece().getPieceType()) - pieceSquareMoves.getMoves().size();
        this.maxMoves = Piece.getPieceMaxMoveInteger(pieceSquareMoves.getPiece().getPieceType());
    }

    public Integer getOwnIntegrity() {
        return ownIntegrity;
    }

    public Integer getStrangeIntegrity() {
        return strangeIntegrity;
    }

    public void setPieceSquareMoves(PieceSquareMoves pieceSquareMoves) {
        this.pieceSquareMoves = pieceSquareMoves;
    }

    public void setOwnIntegrity(Integer ownIntegrity) {
        this.ownIntegrity = ownIntegrity;
    }

    public void setStrangeIntegrity(Integer strangeIntegrity) {
        this.strangeIntegrity = strangeIntegrity;
        this.ownIntegrity = this.maxMoves - strangeIntegrity;
    }

    public void incrementIntegrity() {
        this.strangeIntegrity = this.strangeIntegrity +1;
        this.ownIntegrity = this.maxMoves - this.strangeIntegrity;
    }

    public void setMoves(List<Move> moves) {
        this.pieceSquareMoves.setMoves(moves);
        this.ownIntegrity = maxMoves - moves.size();
    }


}
