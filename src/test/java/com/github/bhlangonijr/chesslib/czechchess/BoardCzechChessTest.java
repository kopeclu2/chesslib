package com.github.bhlangonijr.chesslib.czechchess;

import com.github.bhlangonijr.chesslib.Board;
import com.github.bhlangonijr.chesslib.Piece;
import com.github.bhlangonijr.chesslib.Square;
import com.github.bhlangonijr.chesslib.move.PieceMovesAndIntegrity;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class BoardCzechChessTest {

    @Test
    public void testMoves() {
        String fe = "rnb2bnr/8/8/8/8/8/8/RNB2BNR w - - 1 11";
        Board b = new Board();
        b.loadFromFen(fe);
        boolean b1 = b.setPieceCzechChess(Piece.WHITE_PAWN, Square.D3);
        boolean b2 = b.setPieceCzechChess(Piece.WHITE_PAWN, Square.A3);
        Assert.assertFalse(b1);
        Assert.assertTrue(b2);
        System.out.println();
    }

    @Test
    public void testMoveForPiece() {
        String fe = "rnb2bnr/8/8/8/8/8/8/RNB2BNR w - - 1 11";
        Board b = new Board();
        b.loadFromFen(fe);
        List<PieceMovesAndIntegrity> pieceMovesAndIntegrities = b.generateAllPiecesIntegrity();
        System.out.println();
    }
}
