package piggott.stockpig.chess;

/**
 * Class for converting to and from algebra notation (a1, h8, etc)
 */
public class AlgebraNotation {

    /**
     * Convert given algebra notation to bit board.
     *
     * @param algebra algebra representation of chess square [a-h][1-8]
     * @return bit board with single bit flipped
     */
    public static long toBit(final String algebra) {
        if ("-".equals(algebra)) return 0L;
        final int file = indexOfFile(algebra.charAt(0));
        final int rank = Character.digit(algebra.charAt(1), 10) - 1;
        return BitBoard.POSITION[(rank * 8) + file];
    }

    /**
     * Convert the given bit board to algebra notation.
     * If a bit board has more than one bit flipped, the least significant bit will be converted.
     *
     * @param bit bit board with only one bit flipped
     * @return algebra notation [a-h][1-8]
     */
    public static String fromBit(final long bit) {
        if (bit == 0L) return "-";

        final int position = Long.numberOfTrailingZeros(bit);
        return charFromFile(position % 8) + "" + ((position / 8)+ 1);
    }

    private static int indexOfFile(final char file) {
        return ((int) file - 97);
    }

    private static char charFromFile(final int file) {
        return (char) (file + 97);
    }

}
