package piggott.stockpig.chess;

/**
 * Stores all square and piece data of a chess board.
 * An array of longs is used with each long being an occupancy bitboard of each piece/team.
 * The index of the array represents the piece.
 *
 * @see Piece
 * @see Bitboard
 */
public class Board {

    private final long[] pieceBitboards;

    private Board(final long[] pieceBitboards) {
        this.pieceBitboards = pieceBitboards;
    }

    /**
     * Board with standard set up.
     *
     * @return board with standard starting position
     */
    static Board standard() {
        final long[] pieceBitboards = new long[15];
        //Add unoccupied squares
        pieceBitboards[Piece.UNOCCUPIED] = Bitboard.RANKS[2] | Bitboard.RANKS[3] | Bitboard.RANKS[4] | Bitboard.RANKS[5];

        //Add Black team
        pieceBitboards[Piece.BLACK] = Bitboard.RANKS[6] | Bitboard.RANKS[7];
        pieceBitboards[Piece.KING] = Bitboard.INDEX[60];
        pieceBitboards[Piece.PAWN] = Bitboard.RANKS[6];
        pieceBitboards[Piece.KNIGHT] = Bitboard.INDEX[57] | Bitboard.INDEX[62];
        pieceBitboards[Piece.BISHOP] = Bitboard.INDEX[58] | Bitboard.INDEX[61];
        pieceBitboards[Piece.ROOK] = Bitboard.INDEX[56] | Bitboard.INDEX[63];
        pieceBitboards[Piece.QUEEN] = Bitboard.INDEX[59];

        //Add white team
        pieceBitboards[Piece.WHITE] = Bitboard.RANKS[0] | Bitboard.RANKS[1];
        pieceBitboards[Piece.WHITE | Piece.KING] = Bitboard.INDEX[4];
        pieceBitboards[Piece.WHITE | Piece.PAWN] = Bitboard.RANKS[1];
        pieceBitboards[Piece.WHITE | Piece.KNIGHT] = Bitboard.INDEX[1] | Bitboard.INDEX[6];
        pieceBitboards[Piece.WHITE | Piece.BISHOP] = Bitboard.INDEX[2] | Bitboard.INDEX[5];
        pieceBitboards[Piece.WHITE | Piece.ROOK] = Bitboard.INDEX[0] | Bitboard.INDEX[7];
        pieceBitboards[Piece.WHITE | Piece.QUEEN] = Bitboard.INDEX[3];
        return new Board(pieceBitboards);
    }

    /**
     * Board with no pieces.
     *
     * @return empty board
     */
    static Board empty() {
        final long[] pieceBitboards = new long[15];
        pieceBitboards[Piece.UNOCCUPIED] = Bitboard.ALL;
        return new Board(pieceBitboards);
    }

    /**
     * Decide if the board is a dead position (always stalemate).
     *
     * @return whether the board is in a dead position
     */
    public boolean isDeadPosition() {
        int whiteTeamSize = Long.bitCount(getPieceBitboard(Piece.WHITE));
        int blackTeamSize = Long.bitCount(getPieceBitboard(Piece.BLACK));

        if (whiteTeamSize > 2 || blackTeamSize > 2) return false; //A team has more than two pieces

        //Each team has at most 2 pieces...

        if (whiteTeamSize == 1 && blackTeamSize == 1) return true; //Just the kings

        boolean whiteKnight = (getPieceBitboard(Piece.WHITE | Piece.KNIGHT) != 0);
        boolean whiteBishop = (getPieceBitboard(Piece.WHITE | Piece.BISHOP) != 0);
        boolean blackKnight = (getPieceBitboard(Piece.KNIGHT) != 0);
        boolean blackBishop = (getPieceBitboard(Piece.BISHOP) != 0);

        if (whiteTeamSize == 2 && !whiteKnight && !whiteBishop) return false; //White has a piece that can check
        if (blackTeamSize == 2 && !blackKnight && !blackBishop) return false; //Black has a piece that can check

        if (blackTeamSize + whiteTeamSize == 3) return true; //King vs King+Minor Piece

        //Each team has a minor piece...

        if (whiteKnight || blackKnight) return false;

        //Bishop vs bishop...

        boolean whiteBishopOnBlackSquare = Bitboard.intersects(getPieceBitboard(Piece.WHITE | Piece.BISHOP), Bitboard.BLACK_SQUARES);
        boolean blackBishopOnBlackSquare = Bitboard.intersects(getPieceBitboard(Piece.BISHOP), Bitboard.BLACK_SQUARES);

        return (whiteBishopOnBlackSquare == blackBishopOnBlackSquare); //If they are on the same colour, dead
    }

    // -- Pieces --

    /**
     * Get the occupancy bitboard for a given piece.
     *
     * @param piece piece
     * @return bitboard
     */
    public long getPieceBitboard(final int piece) {
        return pieceBitboards[piece];
    }

    /**
     * Add a piece at bitboard location.
     *
     * @param piece piece
     * @param bitboard bitboard
     */
    void addPiece(final int piece, final long bitboard) {
        pieceBitboards[piece] |= bitboard;
        pieceBitboards[Piece.getTeamOnly(piece)] |= bitboard;
        pieceBitboards[Piece.UNOCCUPIED] &= ~bitboard;
    }

    /**
     * Remove a piece at bitboard location.
     *
     * @param piece piece
     * @param bitboard bitboard
     */
    void removePiece(final int piece, final long bitboard) {
        pieceBitboards[piece] &= ~bitboard;
        pieceBitboards[Piece.getTeamOnly(piece)] &= ~bitboard;
        pieceBitboards[Piece.UNOCCUPIED] |= bitboard;
    }

    /**
     * Get the piece at a given bit.
     *
     * @param bitboard bit to retrieve piece for
     * @return piece
     */
    public int getPieceAtBit(final long bitboard) {
        if (Bitboard.intersects(bitboard, pieceBitboards[Piece.UNOCCUPIED])) return Piece.UNOCCUPIED;

        return Bitboard.intersects(bitboard, pieceBitboards[Piece.WHITE])  ? getPieceAtBitFromTeam(bitboard,true) : getPieceAtBitFromTeam(bitboard,false);
    }

    /**
     * Get the piece at a given bit that is on the given team.
     *
     * @param bitboard bit to retrieve piece for
     * @param team the team that the piece belongs to
     * @return piece
     */
    int getPieceAtBitFromTeam(final long bitboard, final boolean team) {
        return getPieceAtBitFromTeam(bitboard, Piece.getTeam(team));
    }

    /**
     * Get the piece at a given bit that is on the given team.
     *
     * @param bitboard bit to retrieve piece for
     * @param team the team that the piece belongs to
     * @return piece
     */
    int getPieceAtBitFromTeam(final long bitboard, final int team) {
        final int startPiece = team | Piece.KING;
        final int endPiece = startPiece | Piece.QUEEN;

        for (int piece = startPiece; piece < endPiece; piece++) {
            if (Bitboard.intersects(bitboard, pieceBitboards[piece])) return piece;
        }
        return Piece.UNOCCUPIED;
    }

    /**
     * Get the piece at the given square index.
     *
     * @param index square index
     * @return piece
     */
    public int getPieceAtIndex(final int index) {
        return getPieceAtBit(Bitboard.INDEX[index]);
    }

    // -- Moves --

    /**
     * Apply a move to the board.
     *
     * @param move move
     */
    void applyMove(final Move move) {
        // Remove the moving piece from it's start location
        pieceBitboards[move.getMovingPiece()] &= ~move.getFrom();
        pieceBitboards[Piece.UNOCCUPIED] |= move.getFrom();

        // Flip the teams To and From bits
        pieceBitboards[Piece.getTeamOnly(move.getMovingPiece())] ^= (move.getFrom() | move.getTo());

        // Mark the To bit as occupied
        pieceBitboards[Piece.UNOCCUPIED] &= ~move.getTo();

        // Add the correct piece at the To location
        if (move.isPromotion()) {
            pieceBitboards[move.getPromotedToPiece()] |= move.getTo();
        } else {
            pieceBitboards[move.getMovingPiece()] |= move.getTo();
        }

        // Remove any captured pieces, if en passant then the target square is different
        if (move.isEnPassant()) {
            pieceBitboards[move.getCapturedPiece()] &= ~move.getCapturedEnPassantPawn();
            pieceBitboards[Piece.getTeamOnly(move.getCapturedPiece())] &= ~move.getCapturedEnPassantPawn();
            pieceBitboards[Piece.UNOCCUPIED] |= move.getCapturedEnPassantPawn();
        } else if (move.isCapture()) {
            pieceBitboards[move.getCapturedPiece()] &= ~move.getTo();
            pieceBitboards[Piece.getTeamOnly(move.getCapturedPiece())] &= ~move.getTo();
        }

        // Apply the castle move if present
        if (move.isCastle()) {
            pieceBitboards[Piece.getSameTeamRook(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieceBitboards[Piece.getTeamOnly(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieceBitboards[Piece.UNOCCUPIED] ^= move.getCastleRookMove();
        }
    }

    /**
     * Undo move from board.
     *
     * @param move move
     */
    void undoMove(final Move move) {
        // Add the moving piece back where it started
        pieceBitboards[move.getMovingPiece()] |= move.getFrom();
        pieceBitboards[Piece.UNOCCUPIED] &= ~move.getFrom();

        // Flip the teams To and From bits
        pieceBitboards[Piece.getTeamOnly(move.getMovingPiece())] ^= (move.getFrom() | move.getTo());

        // Remove the correct piece from the To location
        if (move.isPromotion()) {
            pieceBitboards[move.getPromotedToPiece()] &= ~move.getTo();
        } else {
            pieceBitboards[move.getMovingPiece()] &= ~move.getTo();
        }
        pieceBitboards[Piece.UNOCCUPIED] |= move.getTo();

        // Undo the capture if present
        if (move.isEnPassant()) {
            pieceBitboards[move.getCapturedPiece()] |= move.getCapturedEnPassantPawn();
            pieceBitboards[Piece.getTeamOnly(move.getCapturedPiece())] |= move.getCapturedEnPassantPawn();
            pieceBitboards[Piece.UNOCCUPIED] &= ~move.getCapturedEnPassantPawn();
        } else if (move.isCapture()) {
            pieceBitboards[move.getCapturedPiece()] |= move.getTo();
            pieceBitboards[Piece.getTeamOnly(move.getCapturedPiece())] |= move.getTo();
            pieceBitboards[Piece.UNOCCUPIED] &= ~move.getTo();
        }

        // Undo the castle move if present
        if (move.isCastle()) {
            pieceBitboards[Piece.getSameTeamRook(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieceBitboards[Piece.getTeamOnly(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieceBitboards[Piece.UNOCCUPIED] ^= move.getCastleRookMove();
        }
    }

    // -- equals --

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Board)) return false;
        final Board board = (Board) obj;
        for (int piece = 0; piece < pieceBitboards.length; piece++) {
            if (pieceBitboards[piece] != board.getPieceBitboard(piece)) return false;
        }
        return true;
    }

    // -- Debug String --

    private static final String LINE_ROW = "+---+---+---+---+---+---+---+---+\n";
    private static final String PADDING_ROW = "|   |   |   |   |   |   |   |   |\n";
    private static final String ROW_START = "| ";
    private static final String ROW_END = " |\n";
    private static final String CELL_BORDER = " | ";

    public String debugString() {
        final StringBuilder boardString = new StringBuilder();

        for (int i = 7; i >= 0; i--) {
            boardString.append(LINE_ROW);
            boardString.append(PADDING_ROW);
            boardString.append(ROW_START);

            long bit = 1L;
            bit <<= ((i * 8));

            for (int j = 0; j < 7; j++) {
                boardString.append(Piece.toChar(getPieceAtBit(bit)));
                boardString.append(CELL_BORDER);
                bit <<= 1;
            }
            boardString.append(Piece.toChar(getPieceAtBit(bit)));

            boardString.append(ROW_END);
            boardString.append(PADDING_ROW);
        }
        boardString.append(LINE_ROW);

        return boardString.toString();
    }

}
