package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MoveTest {

    @Test
    void castle() {
        final Move move = Move.castle(BitBoard.POSITION[4], BitBoard.POSITION[6], Piece.WHITE | Piece.KING, BitBoard.POSITION[5] | BitBoard.POSITION[7]);

        assertEquals(BitBoard.POSITION[4], move.getFrom());
        assertEquals(BitBoard.POSITION[6], move.getTo());
        assertEquals(Piece.WHITE | Piece.KING, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.POSITION[5] | BitBoard.POSITION[7], move.getCastleRookMove());
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
        final Move move = Move.doublePush(BitBoard.POSITION[10], BitBoard.POSITION[26], Piece.WHITE | Piece.PAWN, BitBoard.POSITION[18]);

        assertEquals(BitBoard.POSITION[10], move.getFrom());
        assertEquals(BitBoard.POSITION[26], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.POSITION[18], move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.enPassantCapture(BitBoard.POSITION[24], BitBoard.POSITION[33], Piece.WHITE | Piece.PAWN, Piece.BLACK | Piece.PAWN, BitBoard.POSITION[25]);

        assertEquals(BitBoard.POSITION[24], move.getFrom());
        assertEquals(BitBoard.POSITION[33], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.BLACK | Piece.PAWN, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(BitBoard.POSITION[25], move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.pawnPromotionWithCapture(BitBoard.POSITION[54], BitBoard.POSITION[63], Piece.WHITE | Piece.PAWN, Piece.KNIGHT, Piece.WHITE | Piece.ROOK);

        assertEquals(BitBoard.POSITION[54], move.getFrom());
        assertEquals(BitBoard.POSITION[63], move.getTo());
        assertEquals(Piece.WHITE | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.BLACK | Piece.KNIGHT, move.getCapturedPiece());
        assertEquals(Piece.WHITE | Piece.ROOK, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.pawnPromotion(BitBoard.POSITION[15], BitBoard.POSITION[7], Piece.PAWN, Piece.KNIGHT);

        assertEquals(BitBoard.POSITION[15], move.getFrom());
        assertEquals(BitBoard.POSITION[7], move.getTo());
        assertEquals(Piece.BLACK | Piece.PAWN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.BLACK | Piece.KNIGHT, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.basicCapture(BitBoard.POSITION[42], BitBoard.POSITION[32], Piece.QUEEN, Piece.WHITE | Piece.BISHOP);

        assertEquals(BitBoard.POSITION[42], move.getFrom());
        assertEquals(BitBoard.POSITION[32], move.getTo());
        assertEquals(Piece.BLACK | Piece.QUEEN, move.getMovingPiece());
        assertEquals(Piece.WHITE | Piece.BISHOP, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.basicMove(BitBoard.POSITION[57], BitBoard.POSITION[12], Piece.QUEEN);

        assertEquals(BitBoard.POSITION[57], move.getFrom());
        assertEquals(BitBoard.POSITION[12], move.getTo());
        assertEquals(Piece.BLACK | Piece.QUEEN, move.getMovingPiece());
        assertEquals(Piece.EMPTY, move.getCapturedPiece());
        assertEquals(Piece.EMPTY, move.getPromotedToPiece());
        assertEquals(BitBoard.EMPTY, move.getCapturedEnPassantPawn());
        assertEquals(BitBoard.EMPTY, move.getEnPassantTarget());
        assertEquals(BitBoard.EMPTY, move.getCastleRookMove());
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
        final Move move = Move.castle(BitBoard.POSITION[4], BitBoard.POSITION[6], Piece.WHITE | Piece.KING, BitBoard.POSITION[5] | BitBoard.POSITION[7]);
        assertEquals(false, move.equals(null));
        assertEquals(false, move.equals(Long.valueOf(2)));
        assertEquals(false, move.equals(Move.castle(BitBoard.POSITION[3], BitBoard.POSITION[6], Piece.WHITE | Piece.KING, BitBoard.POSITION[5] | BitBoard.POSITION[7])));
        assertEquals(true, move.equals(Move.castle(BitBoard.POSITION[4], BitBoard.POSITION[6], Piece.WHITE | Piece.KING, BitBoard.POSITION[5] | BitBoard.POSITION[7])));
    }

    @Test
    void toString_test() {
        assertEquals("a1h8", Move.basicMove(BitBoard.POSITION[0], BitBoard.POSITION[63], Piece.WHITE | Piece.PAWN).toString());
        assertEquals("a1h8Q", Move.pawnPromotion(BitBoard.POSITION[0], BitBoard.POSITION[63], Piece.WHITE | Piece.PAWN, Piece.WHITE | Piece.QUEEN).toString());
    }

}