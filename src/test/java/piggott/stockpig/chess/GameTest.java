package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


class GameTest {

    @Test
    void standard() {
        final Game game = Game.standard();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.toFen());
        assertFalse(game.isGameOver());
        assertFalse(game.isCheckMate());
        assertEquals(0, game.getWinner());
        assertFalse(game.isCheck());
        assertTrue(game.isWhiteTurn());
        assertEquals(20, game.getPossibleMoves().size());
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR", game.getBoard().toFen());
        assertEquals(1, game.getTurnNumber());
        assertEquals(0, game.getTurnsSincePushOrCapture());
        assertEquals(0b1111, game.getCastlesPossible());
        assertEquals(BitBoard.EMPTY, game.getEnPassantTarget());
        assertEquals(BitBoard.RANKS[5] | BitBoard.RANKS[6] | BitBoard.RANKS[7] ^ (BitBoard.POSITION[63] | BitBoard.POSITION[56]), game.getThreatenedSquares());
        assertEquals(BitBoard.ALL ^ (BitBoard.RANKS[0] | BitBoard.RANKS[1]), game.getMovableSquares());
        assertEquals(BitBoard.EMPTY, game.getPinSquares());
    }

    @Test
    void isGameOver() {
        assertTrue(Game.fromFen("k5qr/8/8/8/8/8/8/7K w k - 0 1").isGameOver());
        assertTrue(Game.fromFen("k7/8/8/8/8/8/8/QR5K b - - 0 1").isGameOver());
        assertTrue(Game.fromFen("k5r1/8/8/8/8/8/r7/7K w - - 0 1").isGameOver());
        assertTrue(Game.fromFen("k7/8/8/3n4/8/8/8/7K w - - 0 1").isGameOver()); //Dead
        assertFalse(Game.fromFen("k7/8/8/3n4/8/8/8/3R3K w - - 0 1").isGameOver());
    }

    @Test
    void isCheckMate() {
        assertTrue(Game.fromFen("k5qr/8/8/8/8/8/8/7K w k - 0 1").isCheckMate());
        assertTrue(Game.fromFen("k7/8/8/8/8/8/8/QR5K b - - 0 1").isCheckMate());
        assertFalse(Game.fromFen("k6r/8/8/8/8/8/8/7K w k - 0 1").isCheckMate());
        assertFalse(Game.fromFen("k5r1/8/8/8/8/8/r7/7K w - - 0 1").isCheckMate());
    }

    @Test
    void getWinner() {
        assertEquals(-1, Game.fromFen("k5qr/8/8/8/8/8/8/7K w k - 0 1").getWinner());
        assertEquals(1, Game.fromFen("k7/8/8/8/8/8/8/QR5K b - - 0 1").getWinner());
        assertEquals(0, Game.fromFen("k5r1/8/8/8/8/8/r7/7K w - - 0 1").getWinner());
        assertEquals(0, Game.fromFen("k7/8/8/8/8/8/8/7K w - - 0 1").getWinner());
        assertEquals(0, Game.fromFen("k1r5/8/8/8/8/8/8/3R3K w - - 0 1").getWinner());
    }

    @Test
    void applyAndUndoMove() {
        final Game game = Game.standard();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.toFen());
        game.applyMove(Move.doublePush(AlgebraNotation.toBit("e2"), AlgebraNotation.toBit("e4"), Piece.WHITE | Piece.PAWN, AlgebraNotation.toBit("e3")));
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", game.toFen());
        game.applyMove(Move.doublePush(AlgebraNotation.toBit("c7"), AlgebraNotation.toBit("c5"), Piece.BLACK | Piece.PAWN, AlgebraNotation.toBit("c6")));
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", game.toFen());
        game.applyMove(Move.basicMove(BitBoard.POSITION[6], AlgebraNotation.toBit("f3"), Piece.KNIGHT | Piece.WHITE));
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", game.toFen());
        game.undoLastMove();
        assertEquals("rnbqkbnr/pp1ppppp/8/2p5/4P3/8/PPPP1PPP/RNBQKBNR w KQkq c6 0 2", game.toFen());
        game.undoLastMove();
        assertEquals("rnbqkbnr/pppppppp/8/8/4P3/8/PPPP1PPP/RNBQKBNR b KQkq e3 0 1", game.toFen());
        game.undoLastMove();
        assertEquals("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", game.toFen());
    }

    @Test
    void testToString() {
        final Game game = Game.fromFen("k5qr/8/8/8/8/8/8/7K w k - 0 1");
        assertEquals("+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| k |   |   |   |   |   | q | r |\n" +
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
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   |   |   |   |   |   |   | K |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "\n" +
                "\tWinner: -1\n" +
                "\tCheck\n" +
                "\tWhite Turn\n" +
                "\t\tCastles: k\n" +
                "\t\tEnPassant Target: -\n" +
                "\t\tTurns Since Push/Cap: 0\n" +
                "\t\tTurn Number: 1", game.toString()
        );
    }
}