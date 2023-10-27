package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {

    @Test
    void castle() {
        final Move move = Move.castle(Bitboard.INDEX[4], Bitboard.INDEX[6], Piece.WHITE | Piece.KING, Bitboard.INDEX[5] | Bitboard.INDEX[7]);

        assertEquals(Bitboard.INDEX[4], move.getFrom());
        assertEquals(Bitboard.INDEX[6], move.getTo());
        assertEquals(Piece.WHITE | Piece.KING, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.INDEX[5] | Bitboard.INDEX[7], move.getCastleRookMove());
        assertEquals(false, move.isCapture());
        assertEquals(false, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(true, move.isCastle());
        assertEquals(false, move.isPawnMove());
        assertEquals(true, move.isKingMove());
    }

    @Test
    void doublePush() {
        final Move move = Move.doublePush(Bitboard.INDEX[10], Bitboard.INDEX[26], Piece.WHITE | Piece.PAWN, Bitboard.INDEX[18]);

        assertEquals(Bitboard.INDEX[10], move.getFrom());
        assertEquals(Bitboard.INDEX[26], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.INDEX[18], move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(false, move.isCapture());
        assertEquals(false, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(true, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(true, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void enPassantCapture() {
        final Move move = Move.enPassantCapture(Bitboard.INDEX[24], Bitboard.INDEX[33], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN, Bitboard.INDEX[25]);

        assertEquals(Bitboard.INDEX[24], move.getFrom());
        assertEquals(Bitboard.INDEX[33], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.BLACK | Piece.PAWN, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(Bitboard.INDEX[25], move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(true, move.isCapture());
        assertEquals(false, move.isPromotion());
        assertEquals(true, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(true, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void pawnPromotionWithCapture() {
        final Move move = Move.pawnPromotionWithCapture(Bitboard.INDEX[54], Bitboard.INDEX[63], Piece.WHITE | Piece.PAWN, Piece.KNIGHT, Piece.WHITE | Piece.ROOK);

        assertEquals(Bitboard.INDEX[54], move.getFrom());
        assertEquals(Bitboard.INDEX[63], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.BLACK | Piece.KNIGHT, move.getCapturedPiece());
        assertEquals(Piece.WHITE | Piece.ROOK, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(true, move.isCapture());
        assertEquals(true, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(true, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void pawnPromotion() {
        final Move move = Move.pawnPromotion(Bitboard.INDEX[15], Bitboard.INDEX[7], Piece.PAWN, Piece.KNIGHT);

        assertEquals(Bitboard.INDEX[15], move.getFrom());
        assertEquals(Bitboard.INDEX[7], move.getTo());
        assertEquals(Piece.BLACK | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.BLACK | Piece.KNIGHT, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(false, move.isCapture());
        assertEquals(true, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(true, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void basicCapture() {
        final Move move = Move.basicCapture(Bitboard.INDEX[42], Bitboard.INDEX[32], Piece.QUEEN, Piece.WHITE | Piece.BISHOP);

        assertEquals(Bitboard.INDEX[42], move.getFrom());
        assertEquals(Bitboard.INDEX[32], move.getTo());
        assertEquals(Piece.BLACK | Piece.QUEEN, move.getMovingPiece());
        assertEquals(Piece.WHITE | Piece.BISHOP, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(true, move.isCapture());
        assertEquals(false, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(false, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void basicMove() {
        final Move move = Move.basicMove(Bitboard.INDEX[57], Bitboard.INDEX[12], Piece.QUEEN);

        assertEquals(Bitboard.INDEX[57], move.getFrom());
        assertEquals(Bitboard.INDEX[12], move.getTo());
        assertEquals(Piece.BLACK | Piece.QUEEN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(Bitboard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(Bitboard.EMPTY, move.getEnPassantTarget());
        assertEquals(Bitboard.EMPTY, move.getCastleRookMove());
        assertEquals(false, move.isCapture());
        assertEquals(false, move.isPromotion());
        assertEquals(false, move.isEnPassant());
        assertEquals(false, move.isDoublePawnPush());
        assertEquals(false, move.isCastle());
        assertEquals(false, move.isPawnMove());
        assertEquals(false, move.isKingMove());
    }

    @Test
    void testEquals() {
        final Move move = Move.castle(Bitboard.INDEX[4], Bitboard.INDEX[6], Piece.WHITE | Piece.KING, Bitboard.INDEX[5] | Bitboard.INDEX[7]);
        assertEquals(false, move.equals(null));
        assertEquals(false, move.equals(Long.valueOf(2)));
        assertEquals(false, move.equals(Move.castle(Bitboard.INDEX[3], Bitboard.INDEX[6], Piece.WHITE | Piece.KING, Bitboard.INDEX[5] | Bitboard.INDEX[7])));
        assertEquals(true, move.equals(Move.castle(Bitboard.INDEX[4], Bitboard.INDEX[6], Piece.WHITE | Piece.KING, Bitboard.INDEX[5] | Bitboard.INDEX[7])));
    }

    @Test
    void toString_test() {
        assertEquals("a1h8", Move.basicMove(Bitboard.INDEX[0], Bitboard.INDEX[63], Piece.WHITE | Piece.PAWN).toString());
        assertEquals("a1h8Q", Move.pawnPromotion(Bitboard.INDEX[0], Bitboard.INDEX[63], Piece.WHITE | Piece.PAWN, Piece.WHITE | Piece.QUEEN).toString());
    }

}