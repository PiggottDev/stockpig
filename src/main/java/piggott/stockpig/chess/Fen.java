package piggott.stockpig.chess;

/**
 * Provides methods for converting to and from Forsyth–Edwards Notation.
 */
public class Fen {

    public static final String STANDARD_GAME = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1";

    /**
     * Parse a Fen string into a game.
     *
     * @param fen fen
     * @return game
     */
    public static Game toGame(final String fen) {
        final String[] fenParts = fen.split(" ");
        return new Game(
                toBoard(fenParts[0]),
                "w".equals(fenParts[1]),
                Castling.fromString(fenParts[2]),
                AlgebraNotation.toBitboard(fenParts[3]),
                Integer.parseInt(fenParts[4]),
                Integer.parseInt(fenParts[5]));
    }

    /**
     * Convert a game into a full FEN string.
     *
     * @param game game
     * @return fen
     */
    public static String fromGame(final Game game) {
        String fen = fromBoard(game.getBoard());
        fen = fen + (game.isWhiteTurn() ? " w " : " b ");
        fen = fen + Castling.toString(game.getCastlesPossible());
        fen = fen + " " + AlgebraNotation.fromBitboard(game.getEnPassantTarget());
        fen = fen + " " + game.getTurnsSincePushOrCapture() + " " + game.getTurnNumber();
        return fen;
    }

    /**
     * Convert a board into the board part of a FEN string.
     *
     * @param board board
     * @return FEN board part
     */
    public static String fromBoard(final Board board) {
        StringBuilder fen = new StringBuilder();

        int currentBlankSpaceCount = 0;
        for (int rank = 7; rank >= 0; rank--) {

            for (int file = 0; file < 8; file++) {

                final int position = (rank * 8) + file;
                final int piece = board.getPieceAtIndex(position);

                if (piece == Piece.UNOCCUPIED) {
                    currentBlankSpaceCount++;
                } else {
                    if (currentBlankSpaceCount > 0) {
                        fen.append(currentBlankSpaceCount);
                        currentBlankSpaceCount = 0;
                    }
                    fen.append(Piece.toChar(piece));
                }
            }

            if (currentBlankSpaceCount > 0) {
                fen.append(currentBlankSpaceCount);
                currentBlankSpaceCount = 0;
            }
            if (rank != 0) {
                fen.append("/");
            }
        }
        return fen.toString();
    }

    /**
     * Convert the board part of a FEN string into a board.
     *
     * @param fen FEN board part
     * @return board
     */
    public static Board toBoard(final String fen) {
        final Board board = Board.empty();
        int square = 56;

        for (int i = 0; i < fen.length(); i ++) {
            final char c = fen.charAt(i);
            if (c == '/') {
                square -= 16;
            } else if (Character.isDigit(c)) {
                square += Character.digit(c, 10);
            } else {
                board.addPiece(Piece.fromChar(c), Bitboard.INDEX[square]);
                square++;
            }
        }
        return board;
    }


}
