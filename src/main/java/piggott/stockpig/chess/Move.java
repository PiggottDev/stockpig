package piggott.stockpig.chess;

/**
 * Provides an object to store chess move data.
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
     * @param from bitboard, primary piece is moving from
     * @param to bitboard, primary piece is moving to
     * @param movingPiece primary moving piece
     * @param capturedPiece piece that has been captured
     * @param promotedToPiece piece that a pawn is being promoted to
     * @param capturedEnPassantPawn bitboard, pawn captured in an en passant move
     * @param enPassantTarget bitboard, if the move is a double pawn push, this is the square that can now be the target of an en passant
     * @param castleRookMove bitboard of rook movement, for castle moves
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

    static Move castle(final long from, final long to, final int movingPiece, final long castleRookMove) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY, castleRookMove);
    }

    static Move doublePush(final long from, final long to, final int movingPiece, final long enPassantTarget) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, Bitboard.EMPTY, enPassantTarget, Bitboard.EMPTY);
    }

    static Move enPassantCapture(final long from, final long to, final int movingPiece, final int capturedPiece, final long capturedEnPassantPawn) {
        return new Move(from, to, movingPiece, capturedPiece, Piece.EMPTY, capturedEnPassantPawn, Bitboard.EMPTY, Bitboard.EMPTY);
    }

    static Move pawnPromotionWithCapture(final long from, final long to, final int movingPiece, final int capturedPiece, final int promotedToPiece) {
        return new Move(from, to, movingPiece, capturedPiece, promotedToPiece, Bitboard.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY);
    }

    static Move pawnPromotion(final long from, final long to, final int movingPiece, final int promotedToPiece) {
        return new Move(from, to, movingPiece, Piece.EMPTY, promotedToPiece, Bitboard.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY);
    }

    static Move basicCapture(final long from, final long to, final int movingPiece, final int capturedPiece) {
        return new Move(from, to, movingPiece, capturedPiece, Piece.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY);
    }

    static Move basicMove(final long from, final long to, final int movingPiece) {
        return new Move(from, to, movingPiece, Piece.EMPTY, Piece.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY, Bitboard.EMPTY);
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
        return capturedEnPassantPawn != Bitboard.EMPTY;
    }

    public long getCapturedEnPassantPawn() {
        return capturedEnPassantPawn;
    }

    public boolean isDoublePawnPush() {
        return enPassantTarget != Bitboard.EMPTY;
    }

    public long getEnPassantTarget() {
        return enPassantTarget;
    }

    public boolean isCastle() {
        return castleRookMove != Bitboard.EMPTY;
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
        return AlgebraNotation.fromBitboard(from) + AlgebraNotation.fromBitboard(to) + (isPromotion() ? Piece.toChar(promotedToPiece) : "");
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
