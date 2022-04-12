package piggott.stockpig.chess;

/**
 * Represents a chess move
 * Storing all the data necessary to both make a move and undo it
 */
public class Move {

    private final long from;
    private final long to;
    private final int movingPiece;
    private final int capturedPiece;
    private final int promotedToPiece;
    private final long capturedEnPassantPawn;
    private final long enPassantTarget;
    private final long castleRookMove;

    /**
     * @param from bit on the board, primary piece is moving from
     * @param to bit on the board, primary piece is moving to
     * @param movingPiece primary moving piece
     * @param capturedPiece piece that has been captured
     * @param promotedToPiece piece that a pawn is being promoted to
     * @param capturedEnPassantPawn pawn captured in an en passant move
     * @param enPassantTarget if the move is a double pawn push, this is the square that can now be the target of an en passant
     * @param castleRookMove for a castle move this represents the rook move
     */
    private Move(final long from, final long to, final int movingPiece, final int capturedPiece, final int promotedToPiece, final long capturedEnPassantPawn, final long enPassantTarget, final long castleRookMove) {
        this.from = from;
        this.to = to;
        this.movingPiece = movingPiece;
        this.capturedPiece = capturedPiece;
        this.promotedToPiece = promotedToPiece;
        this.capturedEnPassantPawn = capturedEnPassantPawn;
        this.enPassantTarget = enPassantTarget;
        this.castleRookMove = castleRookMove;
    }

    public static Move castle(final long from, final long to, final int movingPiece, final long castleRookMove) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY, castleRookMove);
    }

    public static Move doublePush(final long from, final long to, final int movingPiece, final long enPassantTarget) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, BitBoard.EMPTY, enPassantTarget, BitBoard.EMPTY);
    }

    public static Move enPassantCapture(final long from, final long to, final int movingPiece, final int capturedPiece, final long capturedEnPassantPawn) {
        return new Move(from, to, movingPiece, capturedPiece, Piece.EMPTY, capturedEnPassantPawn, BitBoard.EMPTY, BitBoard.EMPTY);
    }

    public static Move pawnPromotionWithCapture(final long from, final long to, final int movingPiece, final int capturedPiece, final int promotedToPiece) {
        return new Move(from, to, movingPiece, capturedPiece, promotedToPiece, BitBoard.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY);
    }

    public static Move pawnPromotion(final long from, final long to, final int movingPiece, final int promotedToPiece) {
        return new Move(from, to, movingPiece, Piece.EMPTY, promotedToPiece, BitBoard.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY);
    }

    public static Move basicCapture(final long from, final long to, final int movingPiece, final int capturedPiece) {
        return new Move(from, to, movingPiece, capturedPiece, Piece.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY);
    }

    public static Move basicMove(final long from, final long to, final int movingPiece) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY, BitBoard.EMPTY);
    }

    public long getFrom() {
        return from;
    }

    public long getTo() {
        return to;
    }

    public int getMovingPiece() {
        return movingPiece;
    }

    public boolean isCapture() {
        return capturedPiece != Piece.EMPTY;
    }

    public int getCapturedPiece() {
        return capturedPiece;
    }

    public boolean isPromotion() {
        return promotedToPiece != Piece.EMPTY;
    }

    public int getPromotedToPiece() {
        return promotedToPiece;
    }

    public boolean isEnPassant() {
        return capturedEnPassantPawn != BitBoard.EMPTY;
    }

    public long getCapturedEnPassantPawn() {
        return capturedEnPassantPawn;
    }

    public boolean isDoublePawnPush() {
        return enPassantTarget != BitBoard.EMPTY;
    }

    public long getEnPassantTarget() {
        return enPassantTarget;
    }

    public boolean isCastle() {
        return castleRookMove != BitBoard.EMPTY;
    }

    public long getCastleRookMove() {
        return castleRookMove;
    }

    public boolean isPawnMove() {
        return Piece.isPawn(movingPiece);
    }

    public boolean isKingMove() {
        return Piece.isKing(movingPiece);
    }

    @Override
    public String toString() {
        return AlgebraNotation.fromBit(from) + AlgebraNotation.fromBit(to) + (isPromotion() ? Piece.toChar(promotedToPiece) : "");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (!(obj instanceof Move)) return false;
        final Move move = (Move) obj;
        return (from == move.getFrom()) && (to == move.getTo()) && (movingPiece == move.getMovingPiece()) && (capturedPiece == move.getCapturedPiece())
                && (promotedToPiece == move.promotedToPiece) && (capturedEnPassantPawn == move.getCapturedEnPassantPawn()) && (enPassantTarget == move.getEnPassantTarget())
                && (castleRookMove == move.getCastleRookMove());
    }

}
