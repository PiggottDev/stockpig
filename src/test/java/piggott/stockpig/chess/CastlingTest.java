package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CastlingTest {

    @Test
    void getCastlesAllowedAfterMove_white_takeKingSideRook() {
        final Move move = Move.basicCapture(BitBoard.POSITION[54], BitBoard.POSITION[63], Piece.WHITE | Piece.QUEEN, Piece.BLACK | Piece.ROOK);

        assertEquals(0b1110, Castling.getCastlesAllowedAfterMove(Castling.ALL_ALLOWED, move, true));

        assertEquals(0b1110, Castling.getCastlesAllowedAfterMove(0b1110, move, true));
        assertEquals(0b1100, Castling.getCastlesAllowedAfterMove(0b1100, move, true));
        assertEquals(0b1000, Castling.getCastlesAllowedAfterMove(0b1000, move, true));

        assertEquals(0b1010, Castling.getCastlesAllowedAfterMove(0b1011, move, true));
        assertEquals(0b1100, Castling.getCastlesAllowedAfterMove(0b1101, move, true));
        assertEquals(0b0100, Castling.getCastlesAllowedAfterMove(0b0101, move, true));

        assertEquals(0, Castling.getCastlesAllowedAfterMove(0, move, true));
    }

    @Test
    void getCastlesAllowedAfterMove_white_takeQueenSideRook() {
        final Move move = Move.basicCapture(BitBoard.POSITION[32], BitBoard.POSITION[56], Piece.WHITE | Piece.BISHOP, Piece.BLACK | Piece.ROOK);

        assertEquals(0b1101, Castling.getCastlesAllowedAfterMove(Castling.ALL_ALLOWED, move, true));

        assertEquals(0b1100, Castling.getCastlesAllowedAfterMove(0b1100, move, true));
        assertEquals(0b1101, Castling.getCastlesAllowedAfterMove(0b1101, move, true));
        assertEquals(0b1001, Castling.getCastlesAllowedAfterMove(0b1001, move, true));

        assertEquals(0b1001, Castling.getCastlesAllowedAfterMove(0b1011, move, true));
        assertEquals(0b1100, Castling.getCastlesAllowedAfterMove(0b1110, move, true));
        assertEquals(0b0100, Castling.getCastlesAllowedAfterMove(0b0110, move, true));

        assertEquals(0, Castling.getCastlesAllowedAfterMove(0, move, true));
    }

    @Test
    void getCastlesAllowedAfterMove_white_moveKing() {
        final Move move = Move.basicMove(BitBoard.POSITION[4], BitBoard.POSITION[12], Piece.WHITE | Piece.KING);

        assertEquals(0b0011, Castling.getCastlesAllowedAfterMove(Castling.ALL_ALLOWED, move, true));

        assertEquals(0b0001, Castling.getCastlesAllowedAfterMove(0b1101, move, true));
        assertEquals(0b0010, Castling.getCastlesAllowedAfterMove(0b1110, move, true));
        assertEquals(0b0011, Castling.getCastlesAllowedAfterMove(0b1011, move, true));

        assertEquals(0b0001, Castling.getCastlesAllowedAfterMove(0b0001, move, true));
        assertEquals(0b0011, Castling.getCastlesAllowedAfterMove(0b0011, move, true));
        assertEquals(0b0010, Castling.getCastlesAllowedAfterMove(0b0010, move, true));

        assertEquals(0, Castling.getCastlesAllowedAfterMove(0, move, true));
    }

    @Test
    void getCastlesAllowedAfterMove_white_moveKingSideRook() {
        final Move move = Move.basicMove(BitBoard.POSITION[7], BitBoard.POSITION[19], Piece.WHITE | Piece.ROOK);

        assertEquals(0b1011, Castling.getCastlesAllowedAfterMove(Castling.ALL_ALLOWED, move, true));

        assertEquals(0b1001, Castling.getCastlesAllowedAfterMove(0b1101, move, true));
        assertEquals(0b0000, Castling.getCastlesAllowedAfterMove(0b0100, move, true));
        assertEquals(0b0001, Castling.getCastlesAllowedAfterMove(0b0101, move, true));

        assertEquals(0b1010, Castling.getCastlesAllowedAfterMove(0b1010, move, true));
        assertEquals(0b0011, Castling.getCastlesAllowedAfterMove(0b0011, move, true));
        assertEquals(0b1010, Castling.getCastlesAllowedAfterMove(0b1010, move, true));

        assertEquals(0, Castling.getCastlesAllowedAfterMove(0, move, true));
    }

    @Test
    void getCastlesAllowedAfterMove_white_moveQueenSideRook() {
        final Move move = Move.basicMove(BitBoard.POSITION[0], BitBoard.POSITION[12], Piece.WHITE | Piece.ROOK);

        assertEquals(0b0111, Castling.getCastlesAllowedAfterMove(Castling.ALL_ALLOWED, move, true));

        assertEquals(0b0101, Castling.getCastlesAllowedAfterMove(0b1101, move, true));
        assertEquals(0b0000, Castling.getCastlesAllowedAfterMove(0b1000, move, true));
        assertEquals(0b0001, Castling.getCastlesAllowedAfterMove(0b1001, move, true));

        assertEquals(0b0010, Castling.getCastlesAllowedAfterMove(0b0010, move, true));
        assertEquals(0b0011, Castling.getCastlesAllowedAfterMove(0b0011, move, true));
        assertEquals(0b0110, Castling.getCastlesAllowedAfterMove(0b0110, move, true));

        assertEquals(0, Castling.getCastlesAllowedAfterMove(0, move, true));
    }

    @Test
    void getKingSideCastleIfPossible_white() {
        // Empty board
        assertNotNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.EMPTY));
        assertNotNull(Castling.getKingSideCastleIfPossible(0b0100, true, BitBoard.ALL ^ (BitBoard.POSITION[4] | BitBoard.POSITION[7]), BitBoard.EMPTY));

        // Limit
        assertNotNull(Castling.getKingSideCastleIfPossible(0b0101, true, BitBoard.EMPTY ^ (BitBoard.POSITION[5] | BitBoard.POSITION[6]),
                BitBoard.ALL ^ (BitBoard.POSITION[4] | BitBoard.POSITION[5] | BitBoard.POSITION[6])));

        // Not allowed due to occupancy
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ BitBoard.POSITION[5], BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ BitBoard.POSITION[6], BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ (BitBoard.POSITION[5] | BitBoard.POSITION[6]), BitBoard.EMPTY));

        // Not allowed due to check
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[4]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[5]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[6]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[6] | BitBoard.POSITION[4]));

        // Not allowed due to bitmap
        assertNull(Castling.getKingSideCastleIfPossible(0, true, BitBoard.ALL, BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(0b1011, true, BitBoard.ALL, BitBoard.EMPTY));
    }

    @Test
    void getKingSideCastleIfPossible_black() {
        // Empty board
        assertNotNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.EMPTY));
        assertNotNull(Castling.getKingSideCastleIfPossible(0b0001, false, BitBoard.ALL ^ (BitBoard.POSITION[60] | BitBoard.POSITION[63]), BitBoard.EMPTY));

        // Limit
        assertNotNull(Castling.getKingSideCastleIfPossible(0b0101, false, BitBoard.EMPTY ^ (BitBoard.POSITION[61] | BitBoard.POSITION[62]),
                BitBoard.ALL ^ (BitBoard.POSITION[60] | BitBoard.POSITION[61] | BitBoard.POSITION[62])));

        // Not allowed due to occupancy
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ BitBoard.POSITION[61], BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ BitBoard.POSITION[62], BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ (BitBoard.POSITION[61] | BitBoard.POSITION[62]), BitBoard.EMPTY));

        // Not allowed due to check
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[60]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[61]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[62]));
        assertNull(Castling.getKingSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[60] | BitBoard.POSITION[62]));

        // Not allowed due to bitmap
        assertNull(Castling.getKingSideCastleIfPossible(0, false, BitBoard.ALL, BitBoard.EMPTY));
        assertNull(Castling.getKingSideCastleIfPossible(0b1110, false, BitBoard.ALL, BitBoard.EMPTY));
    }

    @Test
    void getQueenSideCastleIfPossible_white() {
        // Empty board
        assertNotNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.EMPTY));
        assertNotNull(Castling.getQueenSideCastleIfPossible(0b1000, true, BitBoard.ALL ^ (BitBoard.POSITION[4] | BitBoard.POSITION[0]), BitBoard.EMPTY));

        // Limit
        assertNotNull(Castling.getQueenSideCastleIfPossible(0b1001, true, BitBoard.EMPTY ^ (BitBoard.POSITION[1] | BitBoard.POSITION[2] | BitBoard.POSITION[3]),
                BitBoard.ALL ^ (BitBoard.POSITION[2] | BitBoard.POSITION[3] | BitBoard.POSITION[4])));

        // Not allowed due to occupancy
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ BitBoard.POSITION[1], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ BitBoard.POSITION[2], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ BitBoard.POSITION[3], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL ^ (BitBoard.POSITION[1] | BitBoard.POSITION[3]), BitBoard.EMPTY));

        // Not allowed due to check
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[2]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[3]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[4]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, true, BitBoard.ALL, BitBoard.POSITION[2] | BitBoard.POSITION[4]));

        // Not allowed due to bitmap
        assertNull(Castling.getQueenSideCastleIfPossible(0, true, BitBoard.ALL, BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(0b0111, true, BitBoard.ALL, BitBoard.EMPTY));
    }

    @Test
    void getQueenSideCastleIfPossible_black() {
        // Empty board
        assertNotNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.EMPTY));
        assertNotNull(Castling.getQueenSideCastleIfPossible(0b0010, false, BitBoard.ALL ^ (BitBoard.POSITION[60] | BitBoard.POSITION[56]), BitBoard.EMPTY));

        // Limit
        assertNotNull(Castling.getQueenSideCastleIfPossible(0b0110, false, BitBoard.EMPTY ^ (BitBoard.POSITION[57] | BitBoard.POSITION[58] | BitBoard.POSITION[59]),
                BitBoard.ALL ^ (BitBoard.POSITION[58] | BitBoard.POSITION[59] | BitBoard.POSITION[60])));

        // Not allowed due to occupancy
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ BitBoard.POSITION[57], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ BitBoard.POSITION[58], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ BitBoard.POSITION[59], BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL ^ (BitBoard.POSITION[57] | BitBoard.POSITION[59]), BitBoard.EMPTY));

        // Not allowed due to check
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[58]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[59]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[60]));
        assertNull(Castling.getQueenSideCastleIfPossible(Castling.ALL_ALLOWED, false, BitBoard.ALL, BitBoard.POSITION[58] | BitBoard.POSITION[60]));

        // Not allowed due to bitmap
        assertNull(Castling.getQueenSideCastleIfPossible(0, false, BitBoard.ALL, BitBoard.EMPTY));
        assertNull(Castling.getQueenSideCastleIfPossible(0b1101, false, BitBoard.ALL, BitBoard.EMPTY));
    }

    @Test
    void fromString() {
        assertEquals(Castling.ALL_ALLOWED, Castling.fromString("KQkq"));
        assertEquals(0b0111, Castling.fromString("Kkq"));
        assertEquals(0b1011, Castling.fromString("Qkq"));
        assertEquals(0b1110, Castling.fromString("KQq"));
        assertEquals(0b1101, Castling.fromString("KQk"));

        assertEquals(0b1100, Castling.fromString("KQ"));
        assertEquals(0b1010, Castling.fromString("Qq"));
        assertEquals(0b0101, Castling.fromString("Kk"));
        assertEquals(0b0011, Castling.fromString("kq"));
        assertEquals(0b0010, Castling.fromString("q"));

        assertEquals(0b0000, Castling.fromString(""));
    }

    @Test
    void testToString() {
        assertEquals("KQkq", Castling.toString(Castling.ALL_ALLOWED));
        assertEquals("Kkq", Castling.toString(0b0111));
        assertEquals("Qkq", Castling.toString(0b1011));
        assertEquals("KQq", Castling.toString(0b1110));
        assertEquals("KQk", Castling.toString(0b1101));

        assertEquals("KQ", Castling.toString(0b1100));
        assertEquals("Qq", Castling.toString(0b1010));
        assertEquals("Kk", Castling.toString(0b0101));
        assertEquals("kq", Castling.toString(0b0011));
        assertEquals("q", Castling.toString(0b0010));

        assertEquals("-", Castling.toString(0b0000));
    }
}