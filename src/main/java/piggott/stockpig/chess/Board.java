package piggott.stockpig.chess;

/**
 * Stores all data about the state of pieces on a chess board
 * An array of longs is used with each long being an occupancy bit board of each piece/team, see {@link BitBoard}
 * The index of the array represents the piece, see {@link Piece}
 */
public class Board {

    private final long[] pieces;

    /**
     * Board with standard set up pieces
     *
     * @return board with standard starting position
     */
    public static Board standard() {
        final long[] pieces = new long[15];
        //Add unoccupied squares
        pieces[Piece.UNOCCUPIED] = BitBoard.RANKS[2] | BitBoard.RANKS[3] | BitBoard.RANKS[4] | BitBoard.RANKS[5];

        //Add Black team
        pieces[Piece.BLACK] = BitBoard.RANKS[6] | BitBoard.RANKS[7];
        pieces[Piece.KING] = BitBoard.POSITION[60];
        pieces[Piece.PAWN] = BitBoard.RANKS[6];
        pieces[Piece.KNIGHT] = BitBoard.POSITION[57] | BitBoard.POSITION[62];
        pieces[Piece.BISHOP] = BitBoard.POSITION[58] | BitBoard.POSITION[61];
        pieces[Piece.ROOK] = BitBoard.POSITION[56] | BitBoard.POSITION[63];
        pieces[Piece.QUEEN] = BitBoard.POSITION[59];

        //Add white team
        pieces[Piece.WHITE] = BitBoard.RANKS[0] | BitBoard.RANKS[1];
        pieces[Piece.WHITE | Piece.KING] = BitBoard.POSITION[4];
        pieces[Piece.WHITE | Piece.PAWN] = BitBoard.RANKS[1];
        pieces[Piece.WHITE | Piece.KNIGHT] = BitBoard.POSITION[1] | BitBoard.POSITION[6];
        pieces[Piece.WHITE | Piece.BISHOP] = BitBoard.POSITION[2] | BitBoard.POSITION[5];
        pieces[Piece.WHITE | Piece.ROOK] = BitBoard.POSITION[0] | BitBoard.POSITION[7];
        pieces[Piece.WHITE | Piece.QUEEN] = BitBoard.POSITION[3];
        return new Board(pieces);
    }

    /**
     * Board with no pieces
     *
     * @return empty board
     */
    public static Board empty() {

        final long[] pieces = new long[15];

        pieces[Piece.UNOCCUPIED] = BitBoard.ALL;

        return new Board(pieces);
    }

    private Board(final long[] pieces) {
        this.pieces = pieces;
    }

    /**
     * Decide if the given board is a dead position (always stalemate)
     *
     * @param board board to determine
     * @return whether the board is in a dead state
     */
    public static boolean isDeadPosition(final Board board) {
        int whiteTeamSize = Long.bitCount(board.getPieces(Piece.WHITE));
        int blackTeamSize = Long.bitCount(board.getPieces(Piece.BLACK));

        if (whiteTeamSize > 2 || blackTeamSize > 2) return false; //A team has more than two pieces

        //Each team has at most 2 pieces...

        if (whiteTeamSize == 1 && blackTeamSize == 1) return true; //Just the kings

        boolean whiteKnight = (board.getPieces(Piece.WHITE | Piece.KNIGHT) != 0);
        boolean whiteBishop = (board.getPieces(Piece.WHITE | Piece.BISHOP) != 0);
        boolean blackKnight = (board.getPieces(Piece.KNIGHT) != 0);
        boolean blackBishop = (board.getPieces(Piece.BISHOP) != 0);

        if (whiteTeamSize == 2 && !whiteKnight && !whiteBishop) return false; //White has a piece that can check
        if (blackTeamSize == 2 && !blackKnight && !blackBishop) return false; //Black has a piece that can check

        if (blackTeamSize + whiteTeamSize == 3) return true; //King vs King+Minor Piece

        //Each team has a minor piece...

        if (whiteKnight || blackKnight) return false;

        //Bishop vs bishop...

        boolean whiteBishopOnBlackSquare = BitBoard.intersects(board.getPieces(Piece.WHITE | Piece.BISHOP), BitBoard.BLACK_SQUARES);
        boolean blackBishopOnBlackSquare = BitBoard.intersects(board.getPieces(Piece.BISHOP), BitBoard.BLACK_SQUARES);

        return (whiteBishopOnBlackSquare == blackBishopOnBlackSquare); //If they are on the same colour, dead
    }

    /**
     * Get the occupancy bitboard for a given piece
     *
     * @param piece piece to fetch bit board for
     * @return bitboard
     */
    public long getPieces(final int piece) {
        return pieces[piece];
    }

    public void addPiece(final int piece, final long bit) {
        pieces[piece] |= bit;
        pieces[Piece.getTeamOnly(piece)] |= bit;
        pieces[Piece.UNOCCUPIED] &= ~bit;
    }

    public void removePiece(final int piece, final long bit) {
        pieces[piece] &= ~bit;
        pieces[Piece.getTeamOnly(piece)] &= ~bit;
        pieces[Piece.UNOCCUPIED] |= bit;
    }

    public void applyMove(final Move move) {
        // Remove the moving piece from it's start location
        pieces[move.getMovingPiece()] &= ~move.getFrom();
        pieces[Piece.UNOCCUPIED] |= move.getFrom();

        // Flip the teams To and From bits
        pieces[Piece.getTeamOnly(move.getMovingPiece())] ^= (move.getFrom() | move.getTo());

        // Mark the To bit as occupied
        pieces[Piece.UNOCCUPIED] &= ~move.getTo();

        // Add the correct piece at the To location
        if (move.isPromotion()) {
            pieces[move.getPromotedToPiece()] |= move.getTo();
        } else {
            pieces[move.getMovingPiece()] |= move.getTo();
        }

        // Remove any captured pieces, if en passant then the target square is different
        if (move.isEnPassant()) {
            pieces[move.getCapturedPiece()] &= ~move.getCapturedEnPassantPawn();
            pieces[Piece.getTeamOnly(move.getCapturedPiece())] &= ~move.getCapturedEnPassantPawn();
            pieces[Piece.UNOCCUPIED] |= move.getCapturedEnPassantPawn();
        } else if (move.isCapture()) {
            pieces[move.getCapturedPiece()] &= ~move.getTo();
            pieces[Piece.getTeamOnly(move.getCapturedPiece())] &= ~move.getTo();
        }

        // Apply the castle move if present
        if (move.isCastle()) {
            pieces[Piece.getSameTeamRook(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieces[Piece.getTeamOnly(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieces[Piece.UNOCCUPIED] ^= move.getCastleRookMove();
        }
    }

    public void undoMove(final Move move) {
        // Add the moving piece back where it started
        pieces[move.getMovingPiece()] |= move.getFrom();
        pieces[Piece.UNOCCUPIED] &= ~move.getFrom();

        // Flip the teams To and From bits
        pieces[Piece.getTeamOnly(move.getMovingPiece())] ^= (move.getFrom() | move.getTo());

        // Remove the correct piece from the To location
        if (move.isPromotion()) {
            pieces[move.getPromotedToPiece()] &= ~move.getTo();
        } else {
            pieces[move.getMovingPiece()] &= ~move.getTo();
        }
        pieces[Piece.UNOCCUPIED] |= move.getTo();

        // Undo the capture if present
        if (move.isEnPassant()) {
            pieces[move.getCapturedPiece()] |= move.getCapturedEnPassantPawn();
            pieces[Piece.getTeamOnly(move.getCapturedPiece())] |= move.getCapturedEnPassantPawn();
            pieces[Piece.UNOCCUPIED] &= ~move.getCapturedEnPassantPawn();
        } else if (move.isCapture()) {
            pieces[move.getCapturedPiece()] |= move.getTo();
            pieces[Piece.getTeamOnly(move.getCapturedPiece())] |= move.getTo();
            pieces[Piece.UNOCCUPIED] &= ~move.getTo();
        }

        // Undo the castle move if present
        if (move.isCastle()) {
            pieces[Piece.getSameTeamRook(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieces[Piece.getTeamOnly(move.getMovingPiece())] ^= move.getCastleRookMove();
            pieces[Piece.UNOCCUPIED] ^= move.getCastleRookMove();
        }
    }

    /**
     * Get the piece at a given bit that is on the given team
     *
     * @param bit bit to retrieve piece for
     * @param team the team that the piece belongs to
     * @return type of piece
     */
    public int getPieceAtBitFromTeam(final long bit, final boolean team) {
        return getPieceAtBitFromTeam(bit, Piece.getTeam(team));
    }

    /**
     * Get the piece at a given bit that is on the given team
     *
     * @param bit bit to retrieve piece for
     * @param team the team that the piece belongs to
     * @return type of piece
     */
    public int getPieceAtBitFromTeam(final long bit, final int team) {
        final int startPiece = team | Piece.KING;
        final int endPiece = startPiece | Piece.QUEEN;

        for (int piece = startPiece; piece < endPiece; piece++) {
            if (BitBoard.intersects(bit, pieces[piece])) return piece;
        }
        return Piece.UNOCCUPIED;
    }

    /**
     * Get the piece at a given bit
     *
     * @param bit bit to retrieve piece for
     * @return type of piece
     */
    private int getPieceAtBit(final long bit) {
        if (BitBoard.intersects(bit, pieces[Piece.UNOCCUPIED])) return Piece.UNOCCUPIED;

        return BitBoard.intersects(bit, pieces[Piece.WHITE])  ? getPieceAtBitFromTeam(bit,true) : getPieceAtBitFromTeam(bit,false);
    }

    public String toFen() {

        String fen = "";
        int currentBlankSpaceCount = 0;

        for (int rank = 7; rank >= 0; rank--) {

            for (int file = 0; file < 8; file++) {

                final int position = (rank * 8) + file;
                final int piece = getPieceAtBit(BitBoard.POSITION[position]);

                if (piece == Piece.UNOCCUPIED) {
                    currentBlankSpaceCount++;
                } else {
                    if (currentBlankSpaceCount > 0) fen = fen + currentBlankSpaceCount;
                    currentBlankSpaceCount = 0;
                    fen = fen + Piece.toChar(piece);
                }
            }

            if (currentBlankSpaceCount > 0) fen = fen + currentBlankSpaceCount;
            currentBlankSpaceCount = 0;

            if (rank != 0) fen = fen + "/";
        }

        return fen;
    }

    public static Board fromFen(final String fen) {

        final Board board = Board.empty();

        int square = 56;

        for (int i = 0; i < fen.length(); i ++) {
            final char c = fen.charAt(i);

            if (c == '/') {
                square -= 16;
            } else if (Character.isDigit(c)) {
                 square += Character.digit(c, 10);
            } else {
                 board.addPiece(Piece.fromChar(c), BitBoard.POSITION[square]);
                 square++;
            }
        }

        return board;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Board)) return false;
        final Board board = (Board) obj;
        for (int piece = 0; piece < pieces.length; piece++) {
            if (pieces[piece] != board.getPieces(piece)) return false;
        }
        return true;
    }

    //  ---------------------------------------------- toString ----------------------------------------------

    private static final String LINE_ROW = "+---+---+---+---+---+---+---+---+\n";
    private static final String PADDING_ROW = "|   |   |   |   |   |   |   |   |\n";
    private static final String ROW_START = "| ";
    private static final String ROW_END = " |\n";
    private static final String CELL_BORDER = " | ";

    @Override
    public String toString() {
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
