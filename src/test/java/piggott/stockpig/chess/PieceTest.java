package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PieceTest {

    @Test
    void pieces() {
        assertEquals(0, Piece.BLACK);
        assertEquals(1, Piece.BLACK | Piece.KING);
        assertEquals(2, Piece.BLACK | Piece.PAWN);
        assertEquals(3, Piece.BLACK | Piece.KNIGHT);
        assertEquals(4, Piece.BLACK | Piece.BISHOP);
        assertEquals(5, Piece.BLACK | Piece.ROOK);
        assertEquals(6, Piece.BLACK | Piece.QUEEN);

        assertEquals(7, Piece.UNOCCUPIED);

        assertEquals(8, Piece.WHITE);
        assertEquals(9, Piece.WHITE | Piece.KING);
        assertEquals(10, Piece.WHITE | Piece.PAWN);
        assertEquals(11, Piece.WHITE | Piece.KNIGHT);
        assertEquals(12, Piece.WHITE | Piece.BISHOP);
        assertEquals(13, Piece.WHITE | Piece.ROOK);
        assertEquals(14, Piece.WHITE | Piece.QUEEN);
    }

    @Test
    void getTeamOnly() {
        assertEquals(0, Piece.getTeamOnly(Piece.BLACK));
        assertEquals(0, Piece.getTeamOnly(Piece.BLACK | Piece.KING));
        assertEquals(0, Piece.getTeamOnly(Piece.BLACK | Piece.PAWN));
        assertEquals(0, Piece.getTeamOnly( Piece.BLACK | Piece.KNIGHT));
        assertEquals(0, Piece.getTeamOnly( Piece.BLACK | Piece.BISHOP));
        assertEquals(0, Piece.getTeamOnly( Piece.BLACK | Piece.ROOK));
        assertEquals(0, Piece.getTeamOnly( Piece.BLACK | Piece.QUEEN));

        assertEquals(0, Piece.getTeamOnly( Piece.UNOCCUPIED));

        assertEquals(8, Piece.getTeamOnly(Piece.WHITE));
        assertEquals(8, Piece.getTeamOnly(Piece.WHITE | Piece.KING));
        assertEquals(8, Piece.getTeamOnly(Piece.WHITE | Piece.PAWN));
        assertEquals(8, Piece.getTeamOnly( Piece.WHITE | Piece.KNIGHT));
        assertEquals(8, Piece.getTeamOnly( Piece.WHITE | Piece.BISHOP));
        assertEquals(8, Piece.getTeamOnly( Piece.WHITE | Piece.ROOK));
        assertEquals(8, Piece.getTeamOnly( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void getTeam() {
        assertEquals(0, Piece.getTeam(false));
        assertEquals(8, Piece.getTeam(true));
    }

    @Test
    void flipTeam() {
        assertEquals(8, Piece.flipTeam(Piece.BLACK));
        assertEquals(9, Piece.flipTeam(Piece.BLACK | Piece.KING));
        assertEquals(10, Piece.flipTeam(Piece.BLACK | Piece.PAWN));
        assertEquals(11, Piece.flipTeam( Piece.BLACK | Piece.KNIGHT));
        assertEquals(12, Piece.flipTeam( Piece.BLACK | Piece.BISHOP));
        assertEquals(13, Piece.flipTeam( Piece.BLACK | Piece.ROOK));
        assertEquals(14, Piece.flipTeam( Piece.BLACK | Piece.QUEEN));

        assertEquals(0, Piece.flipTeam(Piece.WHITE));
        assertEquals(1, Piece.flipTeam(Piece.WHITE | Piece.KING));
        assertEquals(2, Piece.flipTeam(Piece.WHITE | Piece.PAWN));
        assertEquals(3, Piece.flipTeam( Piece.WHITE | Piece.KNIGHT));
        assertEquals(4, Piece.flipTeam( Piece.WHITE | Piece.BISHOP));
        assertEquals(5, Piece.flipTeam( Piece.WHITE | Piece.ROOK));
        assertEquals(6, Piece.flipTeam( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isWhite() {
        assertEquals(false, Piece.isWhite(Piece.BLACK));
        assertEquals(false, Piece.isWhite(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isWhite(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isWhite( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isWhite( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isWhite( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isWhite( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isWhite( Piece.UNOCCUPIED));

        assertEquals(true, Piece.isWhite(Piece.WHITE));
        assertEquals(true, Piece.isWhite(Piece.WHITE | Piece.KING));
        assertEquals(true, Piece.isWhite(Piece.WHITE | Piece.PAWN));
        assertEquals(true, Piece.isWhite( Piece.WHITE | Piece.KNIGHT));
        assertEquals(true, Piece.isWhite( Piece.WHITE | Piece.BISHOP));
        assertEquals(true, Piece.isWhite( Piece.WHITE | Piece.ROOK));
        assertEquals(true, Piece.isWhite( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void getTypeOnly() {
        assertEquals(0, Piece.getTypeOnly(Piece.BLACK));
        assertEquals(1, Piece.getTypeOnly(Piece.BLACK | Piece.KING));
        assertEquals(2, Piece.getTypeOnly(Piece.BLACK | Piece.PAWN));
        assertEquals(3, Piece.getTypeOnly( Piece.BLACK | Piece.KNIGHT));
        assertEquals(4, Piece.getTypeOnly( Piece.BLACK | Piece.BISHOP));
        assertEquals(5, Piece.getTypeOnly( Piece.BLACK | Piece.ROOK));
        assertEquals(6, Piece.getTypeOnly( Piece.BLACK | Piece.QUEEN));

        assertEquals(7, Piece.getTypeOnly( Piece.UNOCCUPIED));

        assertEquals(0, Piece.getTypeOnly(Piece.WHITE));
        assertEquals(1, Piece.getTypeOnly(Piece.WHITE | Piece.KING));
        assertEquals(2, Piece.getTypeOnly(Piece.WHITE | Piece.PAWN));
        assertEquals(3, Piece.getTypeOnly( Piece.WHITE | Piece.KNIGHT));
        assertEquals(4, Piece.getTypeOnly( Piece.WHITE | Piece.BISHOP));
        assertEquals(5, Piece.getTypeOnly( Piece.WHITE | Piece.ROOK));
        assertEquals(6, Piece.getTypeOnly( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isSlider() {
        assertEquals(false, Piece.isSlider(Piece.BLACK));
        assertEquals(false, Piece.isSlider(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isSlider(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isSlider( Piece.BLACK | Piece.KNIGHT));
        assertEquals(true, Piece.isSlider( Piece.BLACK | Piece.BISHOP));
        assertEquals(true, Piece.isSlider( Piece.BLACK | Piece.ROOK));
        assertEquals(true, Piece.isSlider( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isSlider(Piece.WHITE));
        assertEquals(false, Piece.isSlider(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isSlider(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isSlider( Piece.WHITE | Piece.KNIGHT));
        assertEquals(true, Piece.isSlider( Piece.WHITE | Piece.BISHOP));
        assertEquals(true, Piece.isSlider( Piece.WHITE | Piece.ROOK));
        assertEquals(true, Piece.isSlider( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isKing() {
        assertEquals(false, Piece.isKing(Piece.BLACK));
        assertEquals(true, Piece.isKing(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isKing(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isKing( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isKing( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isKing( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isKing( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isKing( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isKing(Piece.WHITE));
        assertEquals(true, Piece.isKing(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isKing(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isKing( Piece.WHITE | Piece.KNIGHT));
        assertEquals(false, Piece.isKing( Piece.WHITE | Piece.BISHOP));
        assertEquals(false, Piece.isKing( Piece.WHITE | Piece.ROOK));
        assertEquals(false, Piece.isKing( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isPawn() {
        assertEquals(false, Piece.isPawn(Piece.BLACK));
        assertEquals(false, Piece.isPawn(Piece.BLACK | Piece.KING));
        assertEquals(true, Piece.isPawn(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isPawn( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isPawn( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isPawn( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isPawn( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isPawn( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isPawn(Piece.WHITE));
        assertEquals(false, Piece.isPawn(Piece.WHITE | Piece.KING));
        assertEquals(true, Piece.isPawn(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isPawn( Piece.WHITE | Piece.KNIGHT));
        assertEquals(false, Piece.isPawn( Piece.WHITE | Piece.BISHOP));
        assertEquals(false, Piece.isPawn( Piece.WHITE | Piece.ROOK));
        assertEquals(false, Piece.isPawn( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isKnight() {
        assertEquals(false, Piece.isKnight(Piece.BLACK));
        assertEquals(false, Piece.isKnight(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isKnight(Piece.BLACK | Piece.PAWN));
        assertEquals(true, Piece.isKnight( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isKnight( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isKnight( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isKnight( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isKnight( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isKnight(Piece.WHITE));
        assertEquals(false, Piece.isKnight(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isKnight(Piece.WHITE | Piece.PAWN));
        assertEquals(true, Piece.isKnight( Piece.WHITE | Piece.KNIGHT));
        assertEquals(false, Piece.isKnight( Piece.WHITE | Piece.BISHOP));
        assertEquals(false, Piece.isKnight( Piece.WHITE | Piece.ROOK));
        assertEquals(false, Piece.isKnight( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isBishop() {
        assertEquals(false, Piece.isBishop(Piece.BLACK));
        assertEquals(false, Piece.isBishop(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isBishop(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isBishop( Piece.BLACK | Piece.KNIGHT));
        assertEquals(true, Piece.isBishop( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isBishop( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isBishop( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isBishop( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isBishop(Piece.WHITE));
        assertEquals(false, Piece.isBishop(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isBishop(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isBishop( Piece.WHITE | Piece.KNIGHT));
        assertEquals(true, Piece.isBishop( Piece.WHITE | Piece.BISHOP));
        assertEquals(false, Piece.isBishop( Piece.WHITE | Piece.ROOK));
        assertEquals(false, Piece.isBishop( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isRook() {
        assertEquals(false, Piece.isRook(Piece.BLACK));
        assertEquals(false, Piece.isRook(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isRook(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isRook( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isRook( Piece.BLACK | Piece.BISHOP));
        assertEquals(true, Piece.isRook( Piece.BLACK | Piece.ROOK));
        assertEquals(false, Piece.isRook( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isRook( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isRook(Piece.WHITE));
        assertEquals(false, Piece.isRook(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isRook(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isRook( Piece.WHITE | Piece.KNIGHT));
        assertEquals(false, Piece.isRook( Piece.WHITE | Piece.BISHOP));
        assertEquals(true, Piece.isRook( Piece.WHITE | Piece.ROOK));
        assertEquals(false, Piece.isRook( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void isQueen() {
        assertEquals(false, Piece.isQueen(Piece.BLACK));
        assertEquals(false, Piece.isQueen(Piece.BLACK | Piece.KING));
        assertEquals(false, Piece.isQueen(Piece.BLACK | Piece.PAWN));
        assertEquals(false, Piece.isQueen( Piece.BLACK | Piece.KNIGHT));
        assertEquals(false, Piece.isQueen( Piece.BLACK | Piece.BISHOP));
        assertEquals(false, Piece.isQueen( Piece.BLACK | Piece.ROOK));
        assertEquals(true, Piece.isQueen( Piece.BLACK | Piece.QUEEN));

        assertEquals(false, Piece.isQueen( Piece.UNOCCUPIED));

        assertEquals(false, Piece.isQueen(Piece.WHITE));
        assertEquals(false, Piece.isQueen(Piece.WHITE | Piece.KING));
        assertEquals(false, Piece.isQueen(Piece.WHITE | Piece.PAWN));
        assertEquals(false, Piece.isQueen( Piece.WHITE | Piece.KNIGHT));
        assertEquals(false, Piece.isQueen( Piece.WHITE | Piece.BISHOP));
        assertEquals(false, Piece.isQueen( Piece.WHITE | Piece.ROOK));
        assertEquals(true, Piece.isQueen( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void getSameTeamRook() {
        assertEquals(5, Piece.getSameTeamRook(Piece.BLACK));
        assertEquals(5, Piece.getSameTeamRook(Piece.BLACK | Piece.KING));
        assertEquals(5, Piece.getSameTeamRook(Piece.BLACK | Piece.PAWN));
        assertEquals(5, Piece.getSameTeamRook( Piece.BLACK | Piece.KNIGHT));
        assertEquals(5, Piece.getSameTeamRook( Piece.BLACK | Piece.BISHOP));
        assertEquals(5, Piece.getSameTeamRook( Piece.BLACK | Piece.ROOK));
        assertEquals(5, Piece.getSameTeamRook( Piece.BLACK | Piece.QUEEN));

        assertEquals(5, Piece.getSameTeamRook( Piece.UNOCCUPIED));

        assertEquals(13, Piece.getSameTeamRook(Piece.WHITE));
        assertEquals(13, Piece.getSameTeamRook(Piece.WHITE | Piece.KING));
        assertEquals(13, Piece.getSameTeamRook(Piece.WHITE | Piece.PAWN));
        assertEquals(13, Piece.getSameTeamRook( Piece.WHITE | Piece.KNIGHT));
        assertEquals(13, Piece.getSameTeamRook( Piece.WHITE | Piece.BISHOP));
        assertEquals(13, Piece.getSameTeamRook( Piece.WHITE | Piece.ROOK));
        assertEquals(13, Piece.getSameTeamRook( Piece.WHITE | Piece.QUEEN));
    }

    @Test
    void getForwardDirection() {
        assertEquals(Bitboard.NORTH, Piece.getForwardDirection(true));
        assertEquals(Bitboard.SOUTH, Piece.getForwardDirection(false));
    }

    @Test
    void getBackwardDirection() {
        assertEquals(Bitboard.SOUTH, Piece.getBackwardDirection(true));
        assertEquals(Bitboard.NORTH, Piece.getBackwardDirection(false));
    }

    @Test
    void getPawnStartingRank() {
        assertEquals(Bitboard.RANKS[1], Piece.getPawnStartingRank(true));
        assertEquals(Bitboard.RANKS[6], Piece.getPawnStartingRank(false));
    }

    @Test
    void getPawnPromotionRank() {
        assertEquals(Bitboard.RANKS[7], Piece.getPawnPromotionRank(true));
        assertEquals(Bitboard.RANKS[0], Piece.getPawnPromotionRank(false));
    }

    @Test
    void getPawnAttackingDirections() {
        assertEquals(Bitboard.WHITE_PAWN_ATTACK_DIRECTIONS, Piece.getPawnAttackingDirections(true));
        assertEquals(Bitboard.BLACK_PAWN_ATTACK_DIRECTIONS, Piece.getPawnAttackingDirections(false));
    }

    @Test
    void fromChar() {
        assertEquals(Piece.BLACK | Piece.KING, Piece.fromChar('k'));
        assertEquals(Piece.BLACK | Piece.PAWN, Piece.fromChar('p'));
        assertEquals(Piece.BLACK | Piece.KNIGHT, Piece.fromChar('n'));
        assertEquals(Piece.BLACK | Piece.BISHOP, Piece.fromChar('b'));
        assertEquals(Piece.BLACK | Piece.ROOK, Piece.fromChar('r'));
        assertEquals(Piece.BLACK | Piece.QUEEN, Piece.fromChar('q'));

        assertEquals(Piece.UNOCCUPIED, Piece.fromChar(' '));

        assertEquals(Piece.WHITE | Piece.KING, Piece.fromChar('K'));
        assertEquals(Piece.WHITE | Piece.PAWN, Piece.fromChar('P'));
        assertEquals(Piece.WHITE | Piece.KNIGHT, Piece.fromChar('N'));
        assertEquals(Piece.WHITE | Piece.BISHOP, Piece.fromChar('B'));
        assertEquals(Piece.WHITE | Piece.ROOK, Piece.fromChar('R'));
        assertEquals(Piece.WHITE | Piece.QUEEN, Piece.fromChar('Q'));
    }

    @Test
    void toChar() {
        assertEquals(' ', Piece.toChar(Piece.BLACK));
        assertEquals('k', Piece.toChar(Piece.BLACK | Piece.KING));
        assertEquals('p', Piece.toChar(Piece.BLACK | Piece.PAWN));
        assertEquals('n', Piece.toChar( Piece.BLACK | Piece.KNIGHT));
        assertEquals('b', Piece.toChar( Piece.BLACK | Piece.BISHOP));
        assertEquals('r', Piece.toChar( Piece.BLACK | Piece.ROOK));
        assertEquals('q', Piece.toChar( Piece.BLACK | Piece.QUEEN));

        assertEquals(' ', Piece.toChar( Piece.UNOCCUPIED));

        assertEquals(' ', Piece.toChar(Piece.WHITE));
        assertEquals('K', Piece.toChar(Piece.WHITE | Piece.KING));
        assertEquals('P', Piece.toChar(Piece.WHITE | Piece.PAWN));
        assertEquals('N', Piece.toChar( Piece.WHITE | Piece.KNIGHT));
        assertEquals('B', Piece.toChar( Piece.WHITE | Piece.BISHOP));
        assertEquals('R', Piece.toChar( Piece.WHITE | Piece.ROOK));
        assertEquals('Q', Piece.toChar( Piece.WHITE | Piece.QUEEN));
    }
}