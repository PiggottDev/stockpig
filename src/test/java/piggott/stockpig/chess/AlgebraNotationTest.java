package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AlgebraNotationTest {

    private static final char[] FILES = new char[] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};

    @Test
    void toBit() {

        assertEquals(0L, AlgebraNotation.toBitboard("-"));

        int position = 0;

        for (int rank = 1; rank < 9; rank ++) { // For each rank...

            for (char file : FILES) { // ... and each file...

                final String algebra = file + "" + rank; // ... construct the algebra notation...

                assertEquals(Bitboard.INDEX[position], AlgebraNotation.toBitboard(algebra)); // ... and check it's the same as BitBoard

                position++;
            }
        }
    }

    @Test
    void fromBit() {

        assertEquals("-", AlgebraNotation.fromBitboard(0L));

        int position = 0;

        for (int rank = 1; rank < 9; rank ++) { // For each rank...

            for (char file : FILES) { // ... and each file...

                final String algebra = file + "" + rank; // ... construct the algebra notation...

                assertEquals(algebra, AlgebraNotation.fromBitboard(Bitboard.INDEX[position]));

                position++;
            }
        }
    }
}