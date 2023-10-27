package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void addPiece() {
        final Board board = Board.empty();
        board.addPiece(Piece.WHITE | Piece.QUEEN, Bitboard.INDEX[4]);
        board.addPiece(Piece.BLACK | Piece.PAWN, Bitboard.RANKS[5]);

        assertEquals(Bitboard.INDEX[4], board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.INDEX[4], board.getPieceBitboard(Piece.WHITE | Piece.QUEEN));

        assertEquals(Bitboard.RANKS[5], board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[5], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals(Bitboard.ALL ^ (Bitboard.RANKS[5] | Bitboard.INDEX[4]), board.getPieceBitboard(Piece.UNOCCUPIED));
    }

    @Test
    void removePiece() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.PAWN, Bitboard.RANKS[6]);
        board.removePiece(Piece.WHITE | Piece.PAWN, Bitboard.RANKS[1]);

        assertEquals(Bitboard.RANKS[0], board.getPieceBitboard(Piece.WHITE));
        assertEquals(0L, board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals(Bitboard.RANKS[7], board.getPieceBitboard(Piece.BLACK));
        assertEquals(0L, board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals(Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[7]), board.getPieceBitboard(Piece.UNOCCUPIED));
    }

    @Test
    void applyMove_basicMove() {
        final Board board = Board.standard();
        final Move move = Move.basicMove(Bitboard.INDEX[10], Bitboard.INDEX[18], Piece.WHITE | Piece.PAWN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[10]) | Bitboard.RANKS[0] | Bitboard.INDEX[18], board.getPieceBitboard(Piece.WHITE));
        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[10]) | Bitboard.INDEX[18], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals(Bitboard.RANKS[6] | Bitboard.RANKS[7], board.getPieceBitboard(Piece.BLACK));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[10] | Bitboard.INDEX[18]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_basicCapture() {
        final Board board = Board.standard();
        final Move move = Move.basicCapture(Bitboard.INDEX[10], Bitboard.INDEX[53], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[10]) | Bitboard.RANKS[0] | Bitboard.INDEX[53], board.getPieceBitboard(Piece.WHITE));
        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[10]) | Bitboard.INDEX[53], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals((Bitboard.RANKS[6] | Bitboard.RANKS[7]) ^ Bitboard.INDEX[53], board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[6] ^ Bitboard.INDEX[53], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ Bitboard.INDEX[10], board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_pawnPromotion() {
        final Board board = Board.standard();
        board.removePiece(Piece.ROOK, Bitboard.INDEX[56]);
        board.removePiece(Piece.PAWN, Bitboard.INDEX[48]);
        board.removePiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[48]);
        final Move move = Move.pawnPromotion(Bitboard.INDEX[48], Bitboard.INDEX[56], Piece.WHITE | Piece.PAWN, Piece.WHITE | Piece.QUEEN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.RANKS[0] | Bitboard.INDEX[56], board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.RANKS[1] ^ Bitboard.INDEX[8], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));
        assertEquals(Bitboard.INDEX[3] | Bitboard.INDEX[56], board.getPieceBitboard(Piece.WHITE | Piece.QUEEN));

        assertEquals((Bitboard.RANKS[6] | Bitboard.RANKS[7]) ^ (Bitboard.INDEX[56] | Bitboard.INDEX[48]), board.getPieceBitboard(Piece.BLACK));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[8] | Bitboard.INDEX[48]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_pawnPromotionWithCapture() {
        final Board board = Board.standard();
        board.removePiece(Piece.PAWN, Bitboard.INDEX[48]);
        board.removePiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[48]);
        final Move move = Move.pawnPromotionWithCapture(Bitboard.INDEX[48], Bitboard.INDEX[57], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.KNIGHT, Piece.WHITE | Piece.ROOK);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.RANKS[0] | Bitboard.INDEX[57], board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.RANKS[1] ^ Bitboard.INDEX[8], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));
        assertEquals(Bitboard.INDEX[0] | Bitboard.INDEX[7] | Bitboard.INDEX[57], board.getPieceBitboard(Piece.WHITE | Piece.ROOK));

        assertEquals((Bitboard.RANKS[7] ^ Bitboard.INDEX[57]) | (Bitboard.RANKS[6] ^ Bitboard.INDEX[48]), board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.INDEX[62], board.getPieceBitboard(Piece.BLACK | Piece.KNIGHT));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[8] | Bitboard.INDEX[48]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_enPassantCapture() {
        final Board board = Board.standard();
        board.removePiece(Piece.PAWN, Bitboard.INDEX[49]);
        board.addPiece(Piece.PAWN, Bitboard.INDEX[33]);
        board.removePiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, Bitboard.INDEX[32]);
        final Move move = Move.enPassantCapture(Bitboard.INDEX[32], Bitboard.INDEX[41], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN, Bitboard.INDEX[33]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.RANKS[0] | Bitboard.INDEX[41], board.getPieceBitboard(Piece.WHITE));
        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.INDEX[41], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals(Bitboard.RANKS[7] | (Bitboard.RANKS[6] ^ Bitboard.INDEX[49]), board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[6] ^ Bitboard.INDEX[49], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[8] | Bitboard.INDEX[49] | Bitboard.INDEX[41]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_doublePush() {
        final Board board = Board.standard();
        final Move move = Move.doublePush(Bitboard.INDEX[8], Bitboard.INDEX[24], Piece.WHITE | Piece.PAWN, Bitboard.INDEX[16]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.RANKS[0] | Bitboard.INDEX[24], board.getPieceBitboard(Piece.WHITE));
        assertEquals((Bitboard.RANKS[1] ^ Bitboard.INDEX[8]) | Bitboard.INDEX[24], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals(Bitboard.RANKS[7] | Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[8] | Bitboard.INDEX[24]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_white_kingSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[5]);
        board.removePiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[6]);
        final Move move = Move.castle(Bitboard.INDEX[4], Bitboard.INDEX[6], Piece.WHITE | Piece.KING, Bitboard.INDEX[5] | Bitboard.INDEX[7]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(Bitboard.RANKS[1] | Bitboard.RANKS[0] ^ (Bitboard.INDEX[4] | Bitboard.INDEX[7]), board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.INDEX[6], board.getPieceBitboard(Piece.WHITE | Piece.KING));
        assertEquals(Bitboard.INDEX[0] | Bitboard.INDEX[5], board.getPieceBitboard(Piece.WHITE | Piece.ROOK));

        assertEquals(Bitboard.RANKS[7] | Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[7] | Bitboard.INDEX[4]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_white_queenSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[1]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[2]);
        board.removePiece(Piece.WHITE | Piece.QUEEN, Bitboard.INDEX[3]);
        final Move move = Move.castle(Bitboard.INDEX[4], Bitboard.INDEX[2], Piece.WHITE | Piece.KING, Bitboard.INDEX[0] | Bitboard.INDEX[3]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(Bitboard.RANKS[1] | Bitboard.RANKS[0] ^ (Bitboard.INDEX[0] | Bitboard.INDEX[1] | Bitboard.INDEX[4]), board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.INDEX[2], board.getPieceBitboard(Piece.WHITE | Piece.KING));
        assertEquals(Bitboard.INDEX[3] | Bitboard.INDEX[7], board.getPieceBitboard(Piece.WHITE | Piece.ROOK));

        assertEquals(Bitboard.RANKS[7] | Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.RANKS[6], board.getPieceBitboard(Piece.BLACK | Piece.PAWN));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[0] | Bitboard.INDEX[1] | Bitboard.INDEX[4]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_black_kingSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[61]);
        board.removePiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[62]);
        final Move move = Move.castle(Bitboard.INDEX[60], Bitboard.INDEX[62], Piece.BLACK | Piece.KING, Bitboard.INDEX[61] | Bitboard.INDEX[63]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(Bitboard.RANKS[1] | Bitboard.RANKS[0], board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.RANKS[1], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals((Bitboard.RANKS[7] | Bitboard.RANKS[6]) ^ (Bitboard.INDEX[63] | Bitboard.INDEX[60]), board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.INDEX[62], board.getPieceBitboard(Piece.BLACK | Piece.KING));
        assertEquals(Bitboard.INDEX[56] | Bitboard.INDEX[61], board.getPieceBitboard(Piece.BLACK | Piece.ROOK));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[63] | Bitboard.INDEX[60]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_black_queenSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[57]);
        board.removePiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[58]);
        board.removePiece(Piece.BLACK | Piece.QUEEN, Bitboard.INDEX[59]);
        final Move move = Move.castle(Bitboard.INDEX[60], Bitboard.INDEX[58], Piece.BLACK | Piece.KING, Bitboard.INDEX[56] | Bitboard.INDEX[59]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(Bitboard.RANKS[0] | Bitboard.RANKS[1], board.getPieceBitboard(Piece.WHITE));
        assertEquals(Bitboard.RANKS[1], board.getPieceBitboard(Piece.WHITE | Piece.PAWN));

        assertEquals((Bitboard.RANKS[6] | Bitboard.RANKS[7]) ^ (Bitboard.INDEX[56] | Bitboard.INDEX[57] | Bitboard.INDEX[60]), board.getPieceBitboard(Piece.BLACK));
        assertEquals(Bitboard.INDEX[58], board.getPieceBitboard(Piece.BLACK | Piece.KING));
        assertEquals(Bitboard.INDEX[63] | Bitboard.INDEX[59], board.getPieceBitboard(Piece.BLACK | Piece.ROOK));

        assertEquals((Bitboard.ALL ^ (Bitboard.RANKS[0] | Bitboard.RANKS[1] | Bitboard.RANKS[6] | Bitboard.RANKS[7])) ^ (Bitboard.INDEX[56] | Bitboard.INDEX[57] | Bitboard.INDEX[60]), board.getPieceBitboard(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void getPieceAtBitFromTeam() {
        final Board board = Board.standard();
        assertEquals(Piece.WHITE | Piece.ROOK, board.getPieceAtBitFromTeam(Bitboard.INDEX[0], true));
        assertEquals(Piece.WHITE | Piece.KING, board.getPieceAtBitFromTeam(Bitboard.INDEX[4], true));
        assertEquals(Piece.WHITE | Piece.PAWN, board.getPieceAtBitFromTeam(Bitboard.INDEX[15], true));
        assertEquals(Piece.WHITE | Piece.QUEEN, board.getPieceAtBitFromTeam(Bitboard.INDEX[3], true));

        assertEquals(Piece.BLACK | Piece.ROOK, board.getPieceAtBitFromTeam(Bitboard.INDEX[63], false));
        assertEquals(Piece.BLACK | Piece.KING, board.getPieceAtBitFromTeam(Bitboard.INDEX[60], false));
        assertEquals(Piece.BLACK | Piece.PAWN, board.getPieceAtBitFromTeam(Bitboard.INDEX[54], false));
        assertEquals(Piece.BLACK | Piece.QUEEN, board.getPieceAtBitFromTeam(Bitboard.INDEX[59], false));

        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(Bitboard.INDEX[43], true));
        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(Bitboard.INDEX[25], false));
    }

    @Test
    void testGetPieceAtBitFromTeam() {
        final Board board = Board.standard();
        assertEquals(Piece.WHITE | Piece.ROOK, board.getPieceAtBitFromTeam(Bitboard.INDEX[0], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.KING, board.getPieceAtBitFromTeam(Bitboard.INDEX[4], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.PAWN, board.getPieceAtBitFromTeam(Bitboard.INDEX[15], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.QUEEN, board.getPieceAtBitFromTeam(Bitboard.INDEX[3], Piece.WHITE));

        assertEquals(Piece.BLACK | Piece.ROOK, board.getPieceAtBitFromTeam(Bitboard.INDEX[63], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.KING, board.getPieceAtBitFromTeam(Bitboard.INDEX[60], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.PAWN, board.getPieceAtBitFromTeam(Bitboard.INDEX[54], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.QUEEN, board.getPieceAtBitFromTeam(Bitboard.INDEX[59], Piece.BLACK));

        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(Bitboard.INDEX[43], Piece.WHITE));
        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(Bitboard.INDEX[25], Piece.BLACK));
    }

    @Test
    void isDeadPosition() {
        final Board board = Board.empty();

        // Just Kings
        board.addPiece(Piece.WHITE | Piece.KING, Bitboard.INDEX[0]);
        board.addPiece(Piece.BLACK | Piece.KING, Bitboard.INDEX[2]);
        assertTrue(Board.isDeadPosition(board));

        // King + Knight vs King
        board.addPiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[10]);
        board.addPiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[10]);

        // King + Bishop vs King
        board.addPiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[10]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[10]);

        // King + Knight vs King + Knight
        board.addPiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[10]);
        board.addPiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[11]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.KNIGHT, Bitboard.INDEX[11]);
        board.removePiece(Piece.WHITE | Piece.KNIGHT, Bitboard.INDEX[10]);

        // King + Bishop vs King + Bishop (same colour)
        board.addPiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[7]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[23]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[23]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[7]);

        // King + Bishop vs King + Bishop (different colour)
        board.addPiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[7]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[15]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, Bitboard.INDEX[15]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, Bitboard.INDEX[7]);

        // King + Rook vs King
        board.addPiece(Piece.WHITE | Piece.ROOK, Bitboard.INDEX[7]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.ROOK, Bitboard.INDEX[7]);

        // King + Rook vs King
        board.addPiece(Piece.BLACK | Piece.ROOK, Bitboard.INDEX[7]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.ROOK, Bitboard.INDEX[7]);
    }

    @Test
    void fromFen() {
        assertEquals(Board.standard(), Board.fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));

        final Board board = Board.empty();
        assertEquals(board, Board.fromFen("8/8/8/8/8/8/8/8"));
        board.addPiece(Piece.WHITE | Piece.KING, Bitboard.INDEX[1]);
        board.addPiece(Piece.BLACK | Piece.KING, Bitboard.INDEX[62]);
        assertEquals(board, Board.fromFen("6k1/8/8/8/8/8/8/1K6"));
    }

    @Test
    void toFen() {
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", Fen.fromBoard(Board.standard()));

        final Board board = Board.empty();
        assertEquals("8/8/8/8/8/8/8/8", Fen.fromBoard(board));
        board.addPiece(Piece.WHITE | Piece.KING, Bitboard.INDEX[1]);
        board.addPiece(Piece.BLACK | Piece.KING, Bitboard.INDEX[62]);
        assertEquals("6k1/8/8/8/8/8/8/1K6", Fen.fromBoard(board));
    }

    @Test
    void testToString() {
        assertEquals(
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| r | n | b | q | k | b | n | r |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| p | p | p | p | p | p | p | p |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| P | P | P | P | P | P | P | P |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| R | N | B | Q | K | B | N | R |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n", Board.standard().debugString());
    }

}