package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BitBoardTest {

    @Test
    void intersects() {
        assertTrue(BitBoard.intersects(1L, 1L));
        assertTrue(BitBoard.intersects(2L, 2L));
        assertTrue(BitBoard.intersects(1L, 3L));
        assertTrue(BitBoard.intersects(8L, 10L));
        assertTrue(BitBoard.intersects(BitBoard.RANKS[0], BitBoard.FILES[0]));

        assertFalse(BitBoard.intersects(1L, 2L));
        assertFalse(BitBoard.intersects(3L, 4L));
        assertFalse(BitBoard.intersects(BitBoard.RANKS[0], BitBoard.RANKS[1]));
    }

    @Test
    void contains() {
        assertTrue(BitBoard.contains(BitBoard.RANKS[0], BitBoard.RANKS[0]));
        assertTrue(BitBoard.contains(BitBoard.RANKS[0], BitBoard.POSITION[1]));
        assertTrue(BitBoard.contains(BitBoard.RANKS[0], BitBoard.POSITION[5]));
        assertTrue(BitBoard.contains(BitBoard.RANKS[0], BitBoard.POSITION[5] | BitBoard.POSITION[6]));
        assertTrue(BitBoard.contains(BitBoard.ALL, BitBoard.BLACK_SQUARES));
        assertTrue(BitBoard.contains(BitBoard.ALL, ~BitBoard.BLACK_SQUARES));
        assertTrue(BitBoard.contains(BitBoard.ALL, BitBoard.EMPTY));
        assertTrue(BitBoard.contains(BitBoard.POSITION[1], BitBoard.POSITION[1]));

        assertFalse(BitBoard.contains(BitBoard.POSITION[1], BitBoard.POSITION[1] | BitBoard.POSITION[2]));
        assertFalse(BitBoard.contains(BitBoard.RANKS[2], BitBoard.FILES[2]));
        assertFalse(BitBoard.contains(BitBoard.BLACK_SQUARES, BitBoard.ALL));
        assertFalse(BitBoard.contains(BitBoard.BLACK_SQUARES, BitBoard.ALL));
    }

    @Test
    void fill() {
        assertEquals(BitBoard.FILES[0], BitBoard.fill(BitBoard.POSITION[0], BitBoard.NORTH, BitBoard.ALL));
        assertEquals(BitBoard.RANKS[0], BitBoard.fill(BitBoard.POSITION[0], BitBoard.EAST, BitBoard.ALL));
        assertEquals(BitBoard.FILES[7], BitBoard.fill(BitBoard.POSITION[63], BitBoard.SOUTH, BitBoard.ALL));
        assertEquals(BitBoard.RANKS[7], BitBoard.fill(BitBoard.POSITION[63], BitBoard.WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[14] | BitBoard.POSITION[23], BitBoard.fill(BitBoard.POSITION[14], BitBoard.NORTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[7] | BitBoard.POSITION[14], BitBoard.fill(BitBoard.POSITION[14], BitBoard.SOUTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[6] | BitBoard.POSITION[13] | BitBoard.POSITION[20] | BitBoard.POSITION[27], BitBoard.fill(BitBoard.POSITION[6], BitBoard.NORTH_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2] | BitBoard.RANKS[3]));
        assertEquals(BitBoard.POSITION[63] | BitBoard.POSITION[54], BitBoard.fill(BitBoard.POSITION[63], BitBoard.SOUTH_WEST, BitBoard.POSITION[54]));
        assertEquals(BitBoard.ALL, BitBoard.fill(BitBoard.FILES[0], BitBoard.EAST, BitBoard.ALL));
        assertEquals(BitBoard.FILES[0], BitBoard.fill(BitBoard.FILES[0], BitBoard.WEST, BitBoard.ALL));
    }

    @Test
    void oppositeDirectionalShift() {
        assertEquals(BitBoard.POSITION[11], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH));
        assertEquals(BitBoard.POSITION[18], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.EAST));
        assertEquals(BitBoard.POSITION[27], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH));
        assertEquals(BitBoard.POSITION[20], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.WEST));
        assertEquals(BitBoard.POSITION[10], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_EAST));
        assertEquals(BitBoard.POSITION[26], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_EAST));
        assertEquals(BitBoard.POSITION[28], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_WEST));
        assertEquals(BitBoard.POSITION[12], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_WEST));
        assertEquals(BitBoard.POSITION[2], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_EAST));
        assertEquals(BitBoard.POSITION[9], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_EAST_EAST));
        assertEquals(BitBoard.POSITION[25], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_EAST_EAST));
        assertEquals(BitBoard.POSITION[34], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_EAST));
        assertEquals(BitBoard.POSITION[36], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_WEST));
        assertEquals(BitBoard.POSITION[29], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.SOUTH_WEST_WEST));
        assertEquals(BitBoard.POSITION[13], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_WEST_WEST));
        assertEquals(BitBoard.POSITION[4], BitBoard.oppositeDirectionalShift(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_WEST));
    }

    @Test
    void directionalShiftWithinArea() {
        assertEquals(BitBoard.POSITION[27], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[20], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[11], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[18], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[28], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[12], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[10], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[26], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[36], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[29], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_EAST_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[13], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_EAST_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[4], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[2], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[9], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_WEST_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[25], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_WEST_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[34], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_WEST, BitBoard.ALL));

        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[20], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[11], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[18], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[12], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[10], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_EAST_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[13], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_EAST_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[4], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_EAST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[2], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_SOUTH_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.POSITION[9], BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.SOUTH_WEST_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_WEST_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftWithinArea(BitBoard.POSITION[19], BitBoard.NORTH_NORTH_WEST, BitBoard.RANKS[0] | BitBoard.RANKS[1] | BitBoard.RANKS[2]));
    }

    @Test
    void directionalShiftBounded() {
        assertEquals(BitBoard.POSITION[23], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.EAST));
        assertEquals(BitBoard.POSITION[7], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH));
        assertEquals(BitBoard.POSITION[14], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.WEST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_EAST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_EAST));
        assertEquals(BitBoard.POSITION[6], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_WEST));
        assertEquals(BitBoard.POSITION[22], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_WEST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_EAST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_EAST_EAST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_EAST_EAST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_EAST));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_WEST));
        assertEquals(BitBoard.POSITION[5], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.SOUTH_WEST_WEST));
        assertEquals(BitBoard.POSITION[21], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_WEST_WEST));
        assertEquals(BitBoard.POSITION[30], BitBoard.directionalShiftBounded(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_WEST));
    }

    @Test
    void directionalShiftBoundedWithinArea() {
        assertEquals(BitBoard.POSITION[23], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[7], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[14], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.WEST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[6], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[22], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_EAST_EAST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_EAST_EAST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_EAST, BitBoard.ALL));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[5], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_WEST_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[21], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_WEST_WEST, BitBoard.ALL));
        assertEquals(BitBoard.POSITION[30], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_WEST, BitBoard.ALL));

        assertEquals(BitBoard.POSITION[23], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.POSITION[7], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.POSITION[14], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.POSITION[6], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.POSITION[22], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_EAST_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_EAST_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_EAST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_SOUTH_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.SOUTH_WEST_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.EMPTY, BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_WEST_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
        assertEquals(BitBoard.POSITION[30], BitBoard.directionalShiftBoundedWithinArea(BitBoard.POSITION[15], BitBoard.NORTH_NORTH_WEST, BitBoard.FILES[6] | BitBoard.FILES[7]));
    }

    @Test
    void string() {
        assertEquals(
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   | x |   | x |   | x |   | x |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| x |   | x |   | x |   | x |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   | x |   | x |   | x |   | x |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| x |   | x |   | x |   | x |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   | x |   | x |   | x |   | x |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| x |   | x |   | x |   | x |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "|   | x |   | x |   | x |   | x |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "| x |   | x |   | x |   | x |   |\n" +
                "|   |   |   |   |   |   |   |   |\n" +
                "+---+---+---+---+---+---+---+---+\n", BitBoard.toString(BitBoard.BLACK_SQUARES));
    }

}