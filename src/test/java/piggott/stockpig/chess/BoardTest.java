package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardTest {

    @Test
    void addPiece() {
        final Board board = Board.empty();
        board.addPiece(Piece.WHITE | Piece.QUEEN, BitBoard.POSITION[4]);
        board.addPiece(Piece.BLACK | Piece.PAWN, BitBoard.RANKS[5]);

        assertEquals(BitBoard.POSITION[4], board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.POSITION[4], board.getPieces(Piece.WHITE | Piece.QUEEN));

        assertEquals(BitBoard.RANKS[5], board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[5], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals(BitBoard.ALL ^ (BitBoard.RANKS[5] | BitBoard.POSITION[4]), board.getPieces(Piece.UNOCCUPIED));
    }

    @Test
    void removePiece() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.PAWN, BitBoard.RANKS[6]);
        board.removePiece(Piece.WHITE | Piece.PAWN, BitBoard.RANKS[1]);

        assertEquals(BitBoard.RANKS[0], board.getPieces(Piece.WHITE));
        assertEquals(0L, board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals(BitBoard.RANKS[7], board.getPieces(Piece.BLACK));
        assertEquals(0L, board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals(BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[7]), board.getPieces(Piece.UNOCCUPIED));
    }

    @Test
    void applyMove_basicMove() {
        final Board board = Board.standard();
        final Move move = Move.basicMove(BitBoard.POSITION[10], BitBoard.POSITION[18], Piece.WHITE | Piece.PAWN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[10]) | BitBoard.RANKS[0] | BitBoard.POSITION[18], board.getPieces(Piece.WHITE));
        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[10]) | BitBoard.POSITION[18], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals(BitBoard.RANKS[6] | BitBoard.RANKS[7], board.getPieces(Piece.BLACK));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[10] | BitBoard.POSITION[18]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_basicCapture() {
        final Board board = Board.standard();
        final Move move = Move.basicCapture(BitBoard.POSITION[10], BitBoard.POSITION[53], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[10]) | BitBoard.RANKS[0] | BitBoard.POSITION[53], board.getPieces(Piece.WHITE));
        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[10]) | BitBoard.POSITION[53], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals((BitBoard.RANKS[6] | BitBoard.RANKS[7]) ^ BitBoard.POSITION[53], board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[6] ^ BitBoard.POSITION[53], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ BitBoard.POSITION[10], board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_pawnPromotion() {
        final Board board = Board.standard();
        board.removePiece(Piece.ROOK, BitBoard.POSITION[56]);
        board.removePiece(Piece.PAWN, BitBoard.POSITION[48]);
        board.removePiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[48]);
        final Move move = Move.pawnPromotion(BitBoard.POSITION[48], BitBoard.POSITION[56], Piece.WHITE | Piece.PAWN, Piece.WHITE | Piece.QUEEN);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.RANKS[0] | BitBoard.POSITION[56], board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.RANKS[1] ^ BitBoard.POSITION[8], board.getPieces(Piece.WHITE | Piece.PAWN));
        assertEquals(BitBoard.POSITION[3] | BitBoard.POSITION[56], board.getPieces(Piece.WHITE | Piece.QUEEN));

        assertEquals((BitBoard.RANKS[6] | BitBoard.RANKS[7]) ^ (BitBoard.POSITION[56] | BitBoard.POSITION[48]), board.getPieces(Piece.BLACK));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[8] | BitBoard.POSITION[48]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_pawnPromotionWithCapture() {
        final Board board = Board.standard();
        board.removePiece(Piece.PAWN, BitBoard.POSITION[48]);
        board.removePiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[48]);
        final Move move = Move.pawnPromotionWithCapture(BitBoard.POSITION[48], BitBoard.POSITION[57], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.KNIGHT, Piece.WHITE | Piece.ROOK);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.RANKS[0] | BitBoard.POSITION[57], board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.RANKS[1] ^ BitBoard.POSITION[8], board.getPieces(Piece.WHITE | Piece.PAWN));
        assertEquals(BitBoard.POSITION[0] | BitBoard.POSITION[7] | BitBoard.POSITION[57], board.getPieces(Piece.WHITE | Piece.ROOK));

        assertEquals((BitBoard.RANKS[7] ^ BitBoard.POSITION[57]) | (BitBoard.RANKS[6] ^ BitBoard.POSITION[48]), board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.POSITION[62], board.getPieces(Piece.BLACK | Piece.KNIGHT));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[8] | BitBoard.POSITION[48]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_enPassantCapture() {
        final Board board = Board.standard();
        board.removePiece(Piece.PAWN, BitBoard.POSITION[49]);
        board.addPiece(Piece.PAWN, BitBoard.POSITION[33]);
        board.removePiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[8]);
        board.addPiece(Piece.WHITE | Piece.PAWN, BitBoard.POSITION[32]);
        final Move move = Move.enPassantCapture(BitBoard.POSITION[32], BitBoard.POSITION[41], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN, BitBoard.POSITION[33]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.RANKS[0] | BitBoard.POSITION[41], board.getPieces(Piece.WHITE));
        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.POSITION[41], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals(BitBoard.RANKS[7] | (BitBoard.RANKS[6] ^ BitBoard.POSITION[49]), board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[6] ^ BitBoard.POSITION[49], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[8] | BitBoard.POSITION[49] | BitBoard.POSITION[41]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_doublePush() {
        final Board board = Board.standard();
        final Move move = Move.doublePush(BitBoard.POSITION[8], BitBoard.POSITION[24], Piece.WHITE | Piece.PAWN, BitBoard.POSITION[16]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.RANKS[0] | BitBoard.POSITION[24], board.getPieces(Piece.WHITE));
        assertEquals((BitBoard.RANKS[1] ^ BitBoard.POSITION[8]) | BitBoard.POSITION[24], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals(BitBoard.RANKS[7] | BitBoard.RANKS[6], board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[6], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[8] | BitBoard.POSITION[24]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_white_kingSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[5]);
        board.removePiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[6]);
        final Move move = Move.castle(BitBoard.POSITION[4], BitBoard.POSITION[6], Piece.WHITE | Piece.KING, BitBoard.POSITION[5] | BitBoard.POSITION[7]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(BitBoard.RANKS[1] | BitBoard.RANKS[0] ^ (BitBoard.POSITION[4] | BitBoard.POSITION[7]), board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.POSITION[6], board.getPieces(Piece.WHITE | Piece.KING));
        assertEquals(BitBoard.POSITION[0] | BitBoard.POSITION[5], board.getPieces(Piece.WHITE | Piece.ROOK));

        assertEquals(BitBoard.RANKS[7] | BitBoard.RANKS[6], board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[6], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[7] | BitBoard.POSITION[4]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_white_queenSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[1]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[2]);
        board.removePiece(Piece.WHITE | Piece.QUEEN, BitBoard.POSITION[3]);
        final Move move = Move.castle(BitBoard.POSITION[4], BitBoard.POSITION[2], Piece.WHITE | Piece.KING, BitBoard.POSITION[0] | BitBoard.POSITION[3]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(BitBoard.RANKS[1] | BitBoard.RANKS[0] ^ (BitBoard.POSITION[0] | BitBoard.POSITION[1] | BitBoard.POSITION[4]), board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.POSITION[2], board.getPieces(Piece.WHITE | Piece.KING));
        assertEquals(BitBoard.POSITION[3] | BitBoard.POSITION[7], board.getPieces(Piece.WHITE | Piece.ROOK));

        assertEquals(BitBoard.RANKS[7] | BitBoard.RANKS[6], board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.RANKS[6], board.getPieces(Piece.BLACK | Piece.PAWN));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[0] | BitBoard.POSITION[1] | BitBoard.POSITION[4]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_black_kingSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[61]);
        board.removePiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[62]);
        final Move move = Move.castle(BitBoard.POSITION[60], BitBoard.POSITION[62], Piece.BLACK | Piece.KING, BitBoard.POSITION[61] | BitBoard.POSITION[63]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(BitBoard.RANKS[1] | BitBoard.RANKS[0], board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.RANKS[1], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals((BitBoard.RANKS[7] | BitBoard.RANKS[6]) ^ (BitBoard.POSITION[63] | BitBoard.POSITION[60]), board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.POSITION[62], board.getPieces(Piece.BLACK | Piece.KING));
        assertEquals(BitBoard.POSITION[56] | BitBoard.POSITION[61], board.getPieces(Piece.BLACK | Piece.ROOK));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[63] | BitBoard.POSITION[60]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void applyMove_castle_black_queenSide() {
        final Board board = Board.standard();
        board.removePiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[57]);
        board.removePiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[58]);
        board.removePiece(Piece.BLACK | Piece.QUEEN, BitBoard.POSITION[59]);
        final Move move = Move.castle(BitBoard.POSITION[60], BitBoard.POSITION[58], Piece.BLACK | Piece.KING, BitBoard.POSITION[56] | BitBoard.POSITION[59]);
        final String beforeString = board.toString();

        board.applyMove(move);

        assertEquals(BitBoard.RANKS[0] | BitBoard.RANKS[1], board.getPieces(Piece.WHITE));
        assertEquals(BitBoard.RANKS[1], board.getPieces(Piece.WHITE | Piece.PAWN));

        assertEquals((BitBoard.RANKS[6] | BitBoard.RANKS[7]) ^ (BitBoard.POSITION[56] | BitBoard.POSITION[57] | BitBoard.POSITION[60]), board.getPieces(Piece.BLACK));
        assertEquals(BitBoard.POSITION[58], board.getPieces(Piece.BLACK | Piece.KING));
        assertEquals(BitBoard.POSITION[63] | BitBoard.POSITION[59], board.getPieces(Piece.BLACK | Piece.ROOK));

        assertEquals((BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[6] | BitBoard.RANKS[7])) ^ (BitBoard.POSITION[56] | BitBoard.POSITION[57] | BitBoard.POSITION[60]), board.getPieces(Piece.UNOCCUPIED));

        board.undoMove(move);
        assertEquals(beforeString, board.toString());
    }

    @Test
    void getPieceAtBitFromTeam() {
        final Board board = Board.standard();
        assertEquals(Piece.WHITE | Piece.ROOK, board.getPieceAtBitFromTeam(BitBoard.POSITION[0], true));
        assertEquals(Piece.WHITE | Piece.KING, board.getPieceAtBitFromTeam(BitBoard.POSITION[4], true));
        assertEquals(Piece.WHITE | Piece.PAWN, board.getPieceAtBitFromTeam(BitBoard.POSITION[15], true));
        assertEquals(Piece.WHITE | Piece.QUEEN, board.getPieceAtBitFromTeam(BitBoard.POSITION[3], true));

        assertEquals(Piece.BLACK | Piece.ROOK, board.getPieceAtBitFromTeam(BitBoard.POSITION[63], false));
        assertEquals(Piece.BLACK | Piece.KING, board.getPieceAtBitFromTeam(BitBoard.POSITION[60], false));
        assertEquals(Piece.BLACK | Piece.PAWN, board.getPieceAtBitFromTeam(BitBoard.POSITION[54], false));
        assertEquals(Piece.BLACK | Piece.QUEEN, board.getPieceAtBitFromTeam(BitBoard.POSITION[59], false));

        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(BitBoard.POSITION[43], true));
        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(BitBoard.POSITION[25], false));
    }

    @Test
    void testGetPieceAtBitFromTeam() {
        final Board board = Board.standard();
        assertEquals(Piece.WHITE | Piece.ROOK, board.getPieceAtBitFromTeam(BitBoard.POSITION[0], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.KING, board.getPieceAtBitFromTeam(BitBoard.POSITION[4], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.PAWN, board.getPieceAtBitFromTeam(BitBoard.POSITION[15], Piece.WHITE));
        assertEquals(Piece.WHITE | Piece.QUEEN, board.getPieceAtBitFromTeam(BitBoard.POSITION[3], Piece.WHITE));

        assertEquals(Piece.BLACK | Piece.ROOK, board.getPieceAtBitFromTeam(BitBoard.POSITION[63], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.KING, board.getPieceAtBitFromTeam(BitBoard.POSITION[60], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.PAWN, board.getPieceAtBitFromTeam(BitBoard.POSITION[54], Piece.BLACK));
        assertEquals(Piece.BLACK | Piece.QUEEN, board.getPieceAtBitFromTeam(BitBoard.POSITION[59], Piece.BLACK));

        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(BitBoard.POSITION[43], Piece.WHITE));
        assertEquals(Piece.UNOCCUPIED, board.getPieceAtBitFromTeam(BitBoard.POSITION[25], Piece.BLACK));
    }

    @Test
    void isDeadPosition() {
        final Board board = Board.empty();

        // Just Kings
        board.addPiece(Piece.WHITE | Piece.KING, BitBoard.POSITION[0]);
        board.addPiece(Piece.BLACK | Piece.KING, BitBoard.POSITION[2]);
        assertTrue(Board.isDeadPosition(board));

        // King + Knight vs King
        board.addPiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[10]);
        board.addPiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[10]);

        // King + Bishop vs King
        board.addPiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[10]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[10]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[10]);

        // King + Knight vs King + Knight
        board.addPiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[10]);
        board.addPiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[11]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.KNIGHT, BitBoard.POSITION[11]);
        board.removePiece(Piece.WHITE | Piece.KNIGHT, BitBoard.POSITION[10]);

        // King + Bishop vs King + Bishop (same colour)
        board.addPiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[7]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[23]);
        assertTrue(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[23]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[7]);

        // King + Bishop vs King + Bishop (different colour)
        board.addPiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[7]);
        board.addPiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[15]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.BISHOP, BitBoard.POSITION[15]);
        board.removePiece(Piece.WHITE | Piece.BISHOP, BitBoard.POSITION[7]);

        // King + Rook vs King
        board.addPiece(Piece.WHITE | Piece.ROOK, BitBoard.POSITION[7]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.WHITE | Piece.ROOK, BitBoard.POSITION[7]);

        // King + Rook vs King
        board.addPiece(Piece.BLACK | Piece.ROOK, BitBoard.POSITION[7]);
        assertFalse(Board.isDeadPosition(board));
        board.removePiece(Piece.BLACK | Piece.ROOK, BitBoard.POSITION[7]);
    }

    @Test
    void fromFen() {
        assertEquals(Board.standard(), Board.fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"));

        final Board board = Board.empty();
        assertEquals(board, Board.fromFen("8/8/8/8/8/8/8/8"));
        board.addPiece(Piece.WHITE | Piece.KING, BitBoard.POSITION[1]);
        board.addPiece(Piece.BLACK | Piece.KING, BitBoard.POSITION[62]);
        assertEquals(board, Board.fromFen("6k1/8/8/8/8/8/8/1K6"));
    }

    @Test
    void toFen() {
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", Board.standard().toFen());

        final Board board = Board.empty();
        assertEquals("8/8/8/8/8/8/8/8", board.toFen());
        board.addPiece(Piece.WHITE | Piece.KING, BitBoard.POSITION[1]);
        board.addPiece(Piece.BLACK | Piece.KING, BitBoard.POSITION[62]);
        assertEquals("6k1/8/8/8/8/8/8/1K6", board.toFen());
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
                "+---+---+---+---+---+---+---+---+\n", Board.standard().toString());
    }

}