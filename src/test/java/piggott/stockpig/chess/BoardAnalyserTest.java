package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoardAnalyserTest {

    @Test
    void getThreatened_starting() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        assertEquals(BitBoard.RANKS[5] | BitBoard.RANKS[6] | (BitBoard.RANKS[7] ^ (BitBoard.POSITION[56] | BitBoard.POSITION[63])), analyser.getThreatened());
    }

    @Test
    void getThreatened_rooks() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("1r1r4/2k5/1r1r4/8/8/8/7K/1r1r4"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        assertEquals(BitBoard.RANKS[0] | BitBoard.RANKS[5] | BitBoard.RANKS[7] | BitBoard.FILES[1] | BitBoard.FILES[3], analyser.getThreatened());
    }

    @Test
    void getThreatened_bishops() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("kb1b4/b3b3/7P/4b3/5b2/2b5/1b1P1K1P/4b3"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        assertEquals((BitBoard.BLACK_SQUARES | BitBoard.POSITION[49]) ^ BitBoard.POSITION[4], analyser.getThreatened());
    }

    @Test
    void getThreatened_knights() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("8/2p3p1/k3N3/2N3N1/4N3/8/8/6K1"), false, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {5, 7, 11, 13, 14, 15, 17, 18, 19, 21, 22, 23, 24, 27, 28, 29, 34, 38, 40, 43, 44, 45, 49, 50, 51, 53, 54, 55, 59, 61};
        long threats = 0L;
        for (int square : squares) {
            threats |= BitBoard.POSITION[square];
        }
        assertEquals(threats, analyser.getThreatened());
    }

    @Test
    void getThreatened_queens() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k4Q2/8/8/8/8/8/8/2Q4K"), false, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {9, 11, 14, 15, 16, 20, 25, 38, 43, 47, 52, 54};
        long threats = (BitBoard.RANKS[0] | BitBoard.RANKS[7] | BitBoard.FILES[2] | BitBoard.FILES[5]) ^ (BitBoard.POSITION[2] | BitBoard.POSITION[61]);
        for (int square : squares) {
            threats |= BitBoard.POSITION[square];
        }
        assertEquals(threats, analyser.getThreatened());
    }

    @Test
    void getThreatened_pawns() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k7/8/5p2/p1p1p3/1p1P4/8/8/7K"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {16, 18, 25, 27, 29, 36, 38, 48, 49, 57};
        long threats = 0L;
        for (int square : squares) {
            threats |= BitBoard.POSITION[square];
        }
        assertEquals(threats, analyser.getThreatened());
    }

    @Test
    void getMovableSquares_notCheck() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k7/8/4P3/8/3K4/8/8/1Q3N2"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {1, 5, 27, 44};
        long movable = BitBoard.ALL;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertFalse(analyser.isCheck());
    }

    @Test
    void getMovableSquares_notCheck_blocked() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k2q4/8/3Q4/8/3K4/8/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {27, 43};
        long movable = BitBoard.ALL;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertFalse(analyser.isCheck());
    }

    @Test
    void getMovableSquares_check_rook() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k2r4/8/8/8/3K4/8/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {35, 43, 51, 59};
        long movable = BitBoard.EMPTY;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertTrue(analyser.isCheck());
    }

    @Test
    void getMovableSquares_check_bishop() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k6b/8/5K2/8/8/8/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {54, 63};
        long movable = BitBoard.EMPTY;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertTrue(analyser.isCheck());
    }

    @Test
    void getMovableSquares_check_knight() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k7/8/5n2/4PP2/4K3/8/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {45};
        long movable = BitBoard.EMPTY;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertTrue(analyser.isCheck());
    }

    @Test
    void getMovableSquares_check_pawn() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("k7/8/8/3pPP2/4K3/8/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {35};
        long movable = BitBoard.EMPTY;
        for (int square : squares) {
            movable ^= BitBoard.POSITION[square];
        }
        assertEquals(movable, analyser.getMovableSquares());
        assertTrue(analyser.isCheck());
    }

    @Test
    void getAllPin_horizontal() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("8/8/7k/8/8/8/8/KR5r"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {0};
        long pin = BitBoard.RANKS[0];
        for (int square : squares) {
            pin ^= BitBoard.POSITION[square];
        }
        assertEquals(pin, analyser.getAllPin());
        assertFalse(analyser.isCheck());
    }

    @Test
    void getAllPin_diagonal() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("7b/8/8/4P3/3K4/2R5/8/8"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {36, 45, 54, 63};
        long pin = BitBoard.EMPTY;
        for (int square : squares) {
            pin ^= BitBoard.POSITION[square];
        }
        assertEquals(pin, analyser.getAllPin());
        assertFalse(analyser.isCheck());
    }

    @Test
    void getAllPin_allDirections() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("7b/q2r4/8/2BNP3/r1RKN2q/2QRB3/8/b2r2b1"), true, Castling.ALL_ALLOWED, BitBoard.EMPTY);
        final int[] squares = {0, 3, 6, 9, 11, 13, 18, 19, 20, 34, 35, 36, 41, 43, 45, 48, 51, 54, 63};
        long pin = BitBoard.RANKS[3] ^ BitBoard.POSITION[27];
        for (int square : squares) {
            pin ^= BitBoard.POSITION[square];
        }
        assertEquals(pin, analyser.getAllPin());
        assertFalse(analyser.isCheck());
    }

    // The majority of move generation will be covered in position depth tests
    // These tests are specifically to cover the very rare en passant check move

    @Test
    void generateLegalMoves_illegalEnPassant_true() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("rnbqk1n1/pppp1p2/8/2KPp2r/8/8/PPP1PPPP/RNBQ1BNR"), true, Castling.ALL_ALLOWED, BitBoard.POSITION[44]);
        for (Move move : analyser.generateLegalMoves()) {
            assertNotEquals("d5e6", move.toString());
        }
    }

    @Test
    void generateLegalMoves_illegalEnPassant_false() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("rnbqk1n1/pppp1p2/8/2KPp3/7r/8/PPP1PPPP/RNBQ1BNR"), true, Castling.ALL_ALLOWED, BitBoard.POSITION[44]);
        boolean movePresent = false;
        for (Move move : analyser.generateLegalMoves()) {
            if (move.toString().equals("d5e6")) movePresent = true;
        }
        assertTrue(movePresent);
    }

    @Test
    void generateLegalMoves_illegalEnPassant_pawnCheck() {
        final BoardAnalyser analyser = new BoardAnalyser(Board.fromFen("rnbqk1n1/pppp1p2/8/3Pp3/3K4/8/PPP1PPPP/RNBQ1BNR"), true, Castling.ALL_ALLOWED, BitBoard.POSITION[44]);
        boolean movePresent = false;
        for (Move move : analyser.generateLegalMoves()) {
            if (move.toString().equals("d5e6")) movePresent = true;
        }
        assertTrue(movePresent);
    }

}