/*
 * Copyright 2017 Ben-Hur Carlos Vieira Langoni Junior
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.bhlangonijr.chesslib.move;

import com.github.bhlangonijr.chesslib.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.bhlangonijr.chesslib.Bitboard.bitScanForward;
import static com.github.bhlangonijr.chesslib.Bitboard.extractLsb;

/**
 * The Move generator.
 */
public class MoveGenerator {

    private MoveGenerator() {
    }

    /**
     * Generate All pawn moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generatePawnCaptures(Board board, List<Move> moves) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.PAWN));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getPawnCaptures(side, sqSource,
                    board.getBitboard(), board.getEnPassantTarget()) & ~board.getBitboard(side);
            while (attacks != 0L) {
                int targetIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(targetIndex);
                addPromotions(moves, side, sqTarget, sqSource);
            }
        }
    }

    /**
     * Generate All pawn captures
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generatePawnMoves(Board board, List<Move> moves) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.PAWN));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getPawnMoves(side, sqSource, board.getBitboard());
            while (attacks != 0L) {
                int targetIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(targetIndex);
                addPromotions(moves, side, sqTarget, sqSource);
            }
        }
    }

    private static void addPromotions(List<Move> moves, Side side, Square sqTarget, Square sqSource) {

        if (Side.WHITE.equals(side) && Rank.RANK_8.equals(sqTarget.getRank())) {
            moves.add(new Move(sqSource, sqTarget, Piece.WHITE_QUEEN));
            moves.add(new Move(sqSource, sqTarget, Piece.WHITE_ROOK));
            moves.add(new Move(sqSource, sqTarget, Piece.WHITE_BISHOP));
            moves.add(new Move(sqSource, sqTarget, Piece.WHITE_KNIGHT));
        } else if (Side.BLACK.equals(side) && Rank.RANK_1.equals(sqTarget.getRank())) {
            moves.add(new Move(sqSource, sqTarget, Piece.BLACK_QUEEN));
            moves.add(new Move(sqSource, sqTarget, Piece.BLACK_ROOK));
            moves.add(new Move(sqSource, sqTarget, Piece.BLACK_BISHOP));
            moves.add(new Move(sqSource, sqTarget, Piece.BLACK_KNIGHT));
        } else {
            moves.add(new Move(sqSource, sqTarget, Piece.NONE));
        }
    }

    /**
     * generate knight moves on the target squares allowed in mask param
     *
     * @param board the board
     * @param moves the moves
     * @param mask  mask of allowed targets
     */
    public static void generateKnightMoves(Board board, List<Move> moves, long mask) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.KNIGHT));
        while (pieces != 0L) {
            int knightIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(knightIndex);
            long attacks = Bitboard.getKnightAttacks(sqSource, mask);
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                moves.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
    }

    /**
     * Get knight moves and captures
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateKnightMoves(Board board, List<Move> moves) {

        generateKnightMoves(board, moves, ~board.getBitboard(board.getSideToMove()));
    }

    /**
     * generate bishop moves on the target squares allowed in mask param
     *
     * @param board the board
     * @param moves the moves
     * @param mask  mask of allowed targets
     */
    public static void generateBishopMoves(Board board, List<Move> moves, long mask) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.BISHOP));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getBishopAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                moves.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
    }

    /**
     * Get Bishop moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateBishopMoves(Board board, List<Move> moves) {

        generateBishopMoves(board, moves, ~board.getBitboard(board.getSideToMove()));
    }

    /**
     * generate rook moves on the target squares allowed in mask param
     *
     * @param board the board
     * @param moves the moves
     * @param mask  mask of allowed targets
     */
    public static void generateRookMoves(Board board, List<Move> moves, long mask) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.ROOK));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getRookAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                moves.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
    }

    /**
     * Get Rook moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateRookMoves(Board board, List<Move> moves) {

        generateRookMoves(board, moves, ~board.getBitboard(board.getSideToMove()));
    }

    /**
     * generate queen moves on the target squares allowed in mask param
     *
     * @param board the board
     * @param moves the moves
     * @param mask  mask of allowed targets
     */
    public static void generateQueenMoves(Board board, List<Move> moves, long mask) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.QUEEN));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getQueenAttacks(board.getBitboard(), sqSource) & mask;
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                moves.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
    }

    /**
     * Get Queen moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateQueenMoves(Board board, List<Move> moves) {

        generateQueenMoves(board, moves, ~board.getBitboard(board.getSideToMove()));
    }

    /**
     * generate king moves on the target squares allowed in mask param
     *
     * @param board the board
     * @param moves the moves
     * @param mask  mask of allowed targets
     */
    public static void generateKingMoves(Board board, List<Move> moves, long mask) {
        Side side = board.getSideToMove();
        long pieces = board.getBitboard(Piece.make(side, PieceType.KING));
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            pieces = extractLsb(pieces);
            Square sqSource = Square.squareAt(sourceIndex);
            long attacks = Bitboard.getKingAttacks(sqSource, mask);
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                attacks = extractLsb(attacks);
                Square sqTarget = Square.squareAt(attackIndex);
                moves.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
    }

    /**
     * Get King moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateKingMoves(Board board, List<Move> moves) {
        generateKingMoves(board, moves, ~board.getBitboard(board.getSideToMove()));
    }

    /**
     * Generate all castle moves - always legal moves
     *
     * @param board the board
     * @param moves the moves
     */
    public static void generateCastleMoves(Board board, List<Move> moves) {
        Side side = board.getSideToMove();
        if (board.isKingAttacked()) {
            return;
        }
        if (board.getCastleRight(side).equals(CastleRight.KING_AND_QUEEN_SIDE) ||
                (board.getCastleRight(side).equals(CastleRight.KING_SIDE))) {
            if ((board.getBitboard() & board.getContext().getooAllSquaresBb(side)) == 0L) {
                if (!board.isSquareAttackedBy(board.getContext().getooSquares(side), side.flip())) {
                    moves.add(board.getContext().getoo(side));
                }
            }
        }
        if (board.getCastleRight(side).equals(CastleRight.KING_AND_QUEEN_SIDE) ||
                (board.getCastleRight(side).equals(CastleRight.QUEEN_SIDE))) {
            if ((board.getBitboard() & board.getContext().getoooAllSquaresBb(side)) == 0L) {
                if (!board.isSquareAttackedBy(board.getContext().getoooSquares(side), side.flip())) {
                    moves.add(board.getContext().getooo(side));
                }
            }
        }
    }

    public static List<Move> generateMovesForPiece(Board board, Square square, Side side, PieceType pieceType) {
        // Make Piece instance
        Piece piece = Piece.make(side, pieceType);
        // MAYBE get all pieces
        long pieces = board.getBitboard(piece);
        // get occupied -> occupation of the board, but its LONG "IDK"
        long occpuied = ~board.getBitboard(piece.getPieceSide());
        // Init liost of moves for piece
        List<Move> movesForPiece = new ArrayList<>();
        // Are some pieces found
        while (pieces != 0L) {
            int sourceIndex = bitScanForward(pieces);
            // MAYBE extract this pieces from pieces
            pieces = extractLsb(pieces);
            // Get Square
            Square sqSource = Square.squareAt(sourceIndex);
            // Is this square what we want for this piece ?
            // Square must eq @param square, if true, this is EXACT piece what we want
            if (!sqSource.equals(square)) {
                // IF NOT equals, go to next piece of this type
                continue;
            }
            long attacks = 0L;
            // GET attacks for piece by PIECE.type
            switch (pieceType){
                case PAWN:
                    attacks = Bitboard.getPawnMoves(side, sqSource, board.getBitboard());
                    break;
                case KNIGHT:
                    attacks = Bitboard.getKnightAttacks(sqSource,  occpuied);
                    break;
                case BISHOP:
                    attacks = Bitboard.getBishopAttacks(board.getBitboard(), sqSource) & occpuied;
                    break;
                case ROOK:
                    attacks = Bitboard.getRookAttacks(board.getBitboard(), sqSource) & occpuied;
                    break;
            }
            // Iterate attacks
            while (attacks != 0L) {
                int attackIndex = bitScanForward(attacks);
                // DELETE attack from attacks
                attacks = extractLsb(attacks);
                // Get Square
                Square sqTarget = Square.squareAt(attackIndex);
                // Finally add new move to move list for piece
                movesForPiece.add(new Move(sqSource, sqTarget, Piece.NONE));
            }
        }
        return movesForPiece;
    }

    /**
     * Generate all pseudo-legal moves
     *
     * @param board the board
     * @return move list
     */
    public static List<Move> generatePseudoLegalMoves(Board board) {
        List<Move> moves = new LinkedList<>();
        generatePawnCaptures(board, moves);
        generatePawnMoves(board, moves);
        generateKnightMoves(board, moves);
        generateBishopMoves(board, moves);
        generateRookMoves(board, moves);
        generateQueenMoves(board, moves);
        generateKingMoves(board, moves);
        //generateCastleMoves(board, moves);
        return moves;
    }

    /**
     * Generate all pseudo-legal captures
     *
     * @param board the board
     * @return move list
     */
    public static List<Move> generatePseudoLegalCaptures(Board board) {
        List<Move> moves = new LinkedList<>();
        Side other = board.getSideToMove().flip();
        generatePawnCaptures(board, moves);
        generateKnightMoves(board, moves, board.getBitboard(other));
        generateBishopMoves(board, moves, board.getBitboard(other));
        generateRookMoves(board, moves, board.getBitboard(other));
        generateQueenMoves(board, moves, board.getBitboard(other));
        generateKingMoves(board, moves, board.getBitboard(other));
        return moves;
    }

    public static boolean integrityLegalMove(Board board, Move move, PieceMovesAndIntegrity pieceMovesAndIntegrity) {

        //Get from piece
        final Piece fromPiece = board.getPiece(move.getFrom());

        // Is piece on dest square ? If not Piece.NONE
        final Piece capturedPiece = board.getPiece(move.getTo());

        if (Piece.NONE.equals(fromPiece)) {
            throw new RuntimeException("From piece cannot be null");
        }

        // If Piece.NONE and Piece from and Piece dest have diff color, it means strange integrity.
        // Count++ integrity limit and return false, it means move gonna be deleted
        if (!capturedPiece.equals(Piece.NONE) && !fromPiece.getPieceSide().equals(capturedPiece.getPieceSide())) {
           pieceMovesAndIntegrity.incrementIntegrity();
           return false;
        }

        return true;
    }

    /**
     * Generate move integrity limit with moves
     */
    public static PieceMovesAndIntegrity generateLimitMoves(Board board, PieceSquareMoves pieceSquareMoves) {
        // Creeate instance of Piece with Square and all posiuble moves
        PieceMovesAndIntegrity pieceMovesAndIntegrity = new PieceMovesAndIntegrity(pieceSquareMoves);
        // Get all moves for Piece
        List<Move> moves = pieceSquareMoves.getMoves();
        // Test for legal Move, Move.to must be free. Or check Strange integrity. Delete move if not legal
        moves.removeIf(m -> !integrityLegalMove(board, m, pieceMovesAndIntegrity));
        // Set only legal moves to entity, and recalculate own initegriy maxMove for piece - legalMoves.size
        pieceMovesAndIntegrity.setMoves(moves);
        return pieceMovesAndIntegrity;
    }
    /**
     * Generate Legal Moves
     *
     * @param board the board
     * @return move list
     * @throws MoveGeneratorException the move generator exception
     */
    public static List<Move> generateLegalMoves(Board board) throws MoveGeneratorException {
        try {
            List<Move> moves = generatePseudoLegalMoves(board);
            moves.removeIf(move -> !board.isMoveLegal(move, false));
            return moves;
        } catch (Exception e) {
            throw new MoveGeneratorException("Couldn't generate Legal moves: ", e);
        }
    }

}
