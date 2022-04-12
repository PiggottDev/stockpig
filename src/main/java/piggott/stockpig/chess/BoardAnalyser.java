package piggott.stockpig.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to analyse a given chess position and calculate legal moves
 * Also keeps track of:
 * - Check and the cause of check
 * - Attacked/Checked/Threatened squares
 * - Pinned pieces
 */
public class BoardAnalyser {

    private final boolean isWhiteTurn;
    private final Board board;
    private final int castlesAllowed;
    private final long enPassantTarget;

    private long threatened = 0L; // The squares that the enemy team threatens

    private boolean isCheck = false;
    private boolean isDoubleCheck = false; // Whether there is check from more than one piece

    private long movableSquares;

    // Pins include enemy piece but not the king
    private long allPin = 0L;
    private final long[] pins = new long[4];
    private static final int HORIZONTAL = 0;
    private static final int VERTICAL = 1;
    private static final int DIAGONAL_POSITIVE = 2;
    private static final int DIAGONAL_NEGATIVE = 3;

    final long unoccupied;

    final int team;
    final long pieces;
    final long king;

    final int enemyTeam;
    final long enemyPieces;

    final List<Move> moves = new ArrayList<>();

    public BoardAnalyser(final Board board, final boolean isWhiteTurn, final int castlesAllowed, final long enPassantTarget) {
        this.board = board;
        this.isWhiteTurn = isWhiteTurn;
        this.castlesAllowed = castlesAllowed;
        this.enPassantTarget = enPassantTarget;

        unoccupied = board.getPieces(Piece.UNOCCUPIED);

        team = Piece.getTeam(isWhiteTurn);
        pieces = board.getPieces(team);
        king = board.getPieces(team | Piece.KING);

        enemyTeam = Piece.getTeam(!isWhiteTurn);
        enemyPieces = board.getPieces(enemyTeam);

        movableSquares = (enemyPieces | unoccupied);

        calculateThreatsAndPins();
    }

    /**
     * Whether the position is in check
     *
     * @return check or not
     */
    public boolean isCheck() {
        return isCheck;
    }

    /**
     * Bit board showing all threatened squares by the none moving team
     * Rays go through the king
     *
     * @return bit board showing checked/threatened squares
     */
    public long getThreatened() {
        return threatened;
    }

    /**
     * Get the squares which none king pieces can move into
     * If not check, this is any square that is not an ally
     * If check, this shows squares that will block/remove check
     *
     * @return bit board showing squares none king pieces can move to
     */
    public long getMovableSquares() {
        return movableSquares;
    }

    /**
     * Get bit board showing all squares that form pins
     * The pins include the enemy piece causing the pin but not the king
     *
     * @return bit board of pins
     */
    public long getAllPin() {
        return allPin;
    }

    /**
     * Get all legal moves for the position
     *
     * @return all legal moves
     */
    public List<Move> generateLegalMoves() {

        if (isDoubleCheck) {
            kingMoves();
        } else if (isCheck) {
            pawnMoves();
            knightMoves();
            bishopMoves();
            rookMoves();
            kingMoves();
            queenMoves();
        } else {

            final Move castleKsMove = Castling.getKingSideCastleIfPossible(castlesAllowed, isWhiteTurn, unoccupied, threatened);
            final Move castleQsMove = Castling.getQueenSideCastleIfPossible(castlesAllowed, isWhiteTurn, unoccupied, threatened);
            if (castleKsMove != null) moves.add(castleKsMove);
            if (castleQsMove != null) moves.add(castleQsMove);

            pawnMoves();
            knightMoves();
            bishopMoves();
            queenMoves();
            rookMoves();

            kingMoves();
        }

        return moves;
    }

    //  ---------------------------------------------- Calculating Threats And Pins ----------------------------------------------

    private void pawnMoves() {

        final int pawnPiece = team | Piece.PAWN;
        final long pawns = board.getPieces(pawnPiece);

        // Pushing (NORTH/SOUTH)
        final long pushablePawns = pawns & ~(allPin ^ pins[VERTICAL]);

        // -- Single Push
        final long oneBack = BitBoard.directionalShift(unoccupied & movableSquares, Piece.getBackwardDirection(isWhiteTurn));

        long onePushablePawns = oneBack & pushablePawns;
        while (onePushablePawns != BitBoard.EMPTY) {
            final long onePushablePawn = Long.lowestOneBit(onePushablePawns);

            final long to = BitBoard.directionalShift(onePushablePawn, Piece.getForwardDirection(isWhiteTurn));
            if (BitBoard.intersects(to, Piece.getPawnPromotionRank(isWhiteTurn))) {
                moves.add(Move.pawnPromotion(onePushablePawn, to, pawnPiece, team | Piece.QUEEN));
                moves.add(Move.pawnPromotion(onePushablePawn, to, pawnPiece, team | Piece.KNIGHT));
                moves.add(Move.pawnPromotion(onePushablePawn, to, pawnPiece, team | Piece.ROOK));
                moves.add(Move.pawnPromotion(onePushablePawn, to, pawnPiece, team | Piece.BISHOP));
            } else {
                moves.add(Move.basicMove(onePushablePawn, to, pawnPiece));
            }
            onePushablePawns ^= onePushablePawn;
        }

        // -- Double Push
        long twoPushablePawns = pushablePawns & Piece.getPawnStartingRank(isWhiteTurn) & BitBoard.directionalShift(oneBack & unoccupied, Piece.getBackwardDirection((isWhiteTurn)));
        while (twoPushablePawns != BitBoard.EMPTY) {
            final long twoPushablePawn = Long.lowestOneBit(twoPushablePawns);

            final long emptySquare = BitBoard.directionalShift(twoPushablePawn, Piece.getForwardDirection(isWhiteTurn));
            final long to = BitBoard.directionalShift(emptySquare, Piece.getForwardDirection(isWhiteTurn));
            moves.add(Move.doublePush(twoPushablePawn, to, pawnPiece, emptySquare));

            twoPushablePawns ^= twoPushablePawn;
        }

        // Captures
        // -- Direction one (Diagonal Positive)
        long pawnsWithPositiveAttack = BitBoard.directionalShiftBoundedWithinArea(enPassantTarget | (movableSquares & enemyPieces), Piece.getPawnAttackingDirections(!isWhiteTurn)[0], pawns & ~(allPin ^ pins[DIAGONAL_POSITIVE]));
        while (pawnsWithPositiveAttack != BitBoard.EMPTY) {
            final long pawnWithPositiveAttack = Long.lowestOneBit(pawnsWithPositiveAttack);

            final long to = BitBoard.directionalShift(pawnWithPositiveAttack, Piece.getPawnAttackingDirections(isWhiteTurn)[0]);
            if (BitBoard.intersects(to, enPassantTarget)) {
                final long capturedPawn = BitBoard.oppositeDirectionalShift(to, Piece.getForwardDirection(isWhiteTurn));
                if (!isIllegalEnPassantState(pawnWithPositiveAttack, capturedPawn)) {
                    moves.add(Move.enPassantCapture(pawnWithPositiveAttack, to, pawnPiece, enemyTeam | Piece.PAWN, capturedPawn));
                }
            } else if (BitBoard.intersects(to, Piece.getPawnPromotionRank(isWhiteTurn))) {
                moves.add(Move.pawnPromotionWithCapture(pawnWithPositiveAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.QUEEN));
                moves.add(Move.pawnPromotionWithCapture(pawnWithPositiveAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.KNIGHT));
                moves.add(Move.pawnPromotionWithCapture(pawnWithPositiveAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.ROOK));
                moves.add(Move.pawnPromotionWithCapture(pawnWithPositiveAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.BISHOP));
            } else {
                moves.add(Move.basicCapture(pawnWithPositiveAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam)));
            }
            pawnsWithPositiveAttack ^= pawnWithPositiveAttack;
        }

        // -- Direction two (Diagonal Negative)
        long pawnsWithNegativeAttack = BitBoard.directionalShiftBoundedWithinArea(enPassantTarget | (movableSquares & enemyPieces), Piece.getPawnAttackingDirections(!isWhiteTurn)[1], pawns & ~(allPin ^ pins[DIAGONAL_NEGATIVE]));
        while (pawnsWithNegativeAttack != BitBoard.EMPTY) {
            final long pawnWithNegativeAttack = Long.lowestOneBit(pawnsWithNegativeAttack);

            final long to = BitBoard.directionalShift(pawnWithNegativeAttack, Piece.getPawnAttackingDirections(isWhiteTurn)[1]);
            if (BitBoard.intersects(to, enPassantTarget)) {
                final long capturedPawn = BitBoard.oppositeDirectionalShift(to, Piece.getForwardDirection(isWhiteTurn));
                if (!isIllegalEnPassantState(pawnWithNegativeAttack, capturedPawn)) {
                    moves.add(Move.enPassantCapture(pawnWithNegativeAttack, to, pawnPiece, enemyTeam | Piece.PAWN, capturedPawn));
                }
            } else if (BitBoard.intersects(to, Piece.getPawnPromotionRank(isWhiteTurn))) {
                moves.add(Move.pawnPromotionWithCapture(pawnWithNegativeAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.QUEEN));
                moves.add(Move.pawnPromotionWithCapture(pawnWithNegativeAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.KNIGHT));
                moves.add(Move.pawnPromotionWithCapture(pawnWithNegativeAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.ROOK));
                moves.add(Move.pawnPromotionWithCapture(pawnWithNegativeAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam), team | Piece.BISHOP));
            } else {
                moves.add(Move.basicCapture(pawnWithNegativeAttack, to, pawnPiece, board.getPieceAtBitFromTeam(to, enemyTeam)));
            }
            pawnsWithNegativeAttack ^= pawnWithNegativeAttack;
        }
    }

    private void queenMoves() {

        final int queenPiece = team | Piece.QUEEN;
        long queens = board.getPieces(queenPiece);

        while (queens != BitBoard.EMPTY) {
            final long queen = Long.lowestOneBit(queens);

            if (BitBoard.intersects(allPin, queen)) {
                // Queen is pinned somehow
                // For each pin, the queen can only move in the direction of the pin
                if (BitBoard.intersects(queen, pins[HORIZONTAL] | pins[VERTICAL])) {
                    if (BitBoard.intersects(queen, pins[HORIZONTAL])) {
                        slidingPieceMoves(queen, queenPiece, BitBoard.HORIZONTAL);
                    } else {
                        slidingPieceMoves(queen, queenPiece, BitBoard.VERTICAL);
                    }
                } else {
                    if (BitBoard.intersects(queen, pins[DIAGONAL_POSITIVE])) {
                        slidingPieceMoves(queen, queenPiece, BitBoard.DIAGONAL_POSITIVE);
                    } else {
                        slidingPieceMoves(queen, queenPiece, BitBoard.DIAGONAL_NEGATIVE);
                    }
                }
            } else {
                slidingPieceMoves(queen, queenPiece, BitBoard.EVERY_DIRECTION);
            }
            queens ^= queen;
        }
    }

    private void bishopMoves() {

        final int bishopPiece = team | Piece.BISHOP;
        long bishops = board.getPieces(bishopPiece);

        while (bishops != BitBoard.EMPTY) {
            final long bishop = Long.lowestOneBit(bishops);

            if (BitBoard.intersects(allPin, bishop)) {
                // Bishop is pinned somehow
                if (!BitBoard.intersects(bishop, pins[HORIZONTAL] | pins[VERTICAL])) {
                    // Bishop can only move if the pin is NOT cardinal
                    if (BitBoard.intersects(bishop, pins[DIAGONAL_POSITIVE])) {
                        // If the pin is diagonal positive, the bishop can only move diagonal positive
                        slidingPieceMoves(bishop, bishopPiece, BitBoard.DIAGONAL_POSITIVE);
                    } else {
                        // If the pin is diagonal negative, the bishop can only move diagonal negative
                        slidingPieceMoves(bishop, bishopPiece, BitBoard.DIAGONAL_NEGATIVE);
                    }
                }
            } else {
                slidingPieceMoves(bishop, bishopPiece, BitBoard.DIAGONAL);
            }
            bishops ^= bishop;
        }
    }

    private void rookMoves() {

        final int rookPiece = team | Piece.ROOK;
        long rooks = board.getPieces(rookPiece);

        while (rooks != BitBoard.EMPTY) {
            final long rook = Long.lowestOneBit(rooks);

            if (BitBoard.intersects(allPin, rook)) {
                // Rook is pinned somehow
                if (!BitBoard.intersects(rook, pins[DIAGONAL_NEGATIVE] | pins[DIAGONAL_POSITIVE])) {
                    // Rook can only move if the pin is NOT diagonal
                    if (BitBoard.intersects(rook, pins[HORIZONTAL])) {
                        // If the pin is horizontal, rook can only move horizontally
                        slidingPieceMoves(rook, rookPiece, BitBoard.HORIZONTAL);
                    } else {
                        // If the pin is vertical, rook can only move vertically
                        slidingPieceMoves(rook, rookPiece, BitBoard.VERTICAL);
                    }
                }
            } else {
                slidingPieceMoves(rook, rookPiece, BitBoard.CARDINAL);
            }
            rooks ^= rook;
        }
    }

    private void knightMoves() {

        final int knightPiece = team | Piece.KNIGHT;
        long knights = board.getPieces(knightPiece) &~ allPin;

        while (knights != BitBoard.EMPTY) {
            final long knight = Long.lowestOneBit(knights);

            for (int direction : BitBoard.L_SHAPES) {

                final long to = BitBoard.directionalShiftBoundedWithinArea(knight, direction, movableSquares);

                if (to != BitBoard.EMPTY) {
                    if (BitBoard.intersects(unoccupied, to)) {
                        moves.add(Move.basicMove(knight, to, knightPiece));
                    } else {
                        moves.add(Move.basicCapture(knight, to, knightPiece, board.getPieceAtBitFromTeam(to, enemyTeam)));
                    }
                }
            }
            knights ^= knight;
        }
    }

    private void kingMoves() {
        final long targetSquares = (unoccupied | enemyPieces) & ~threatened;

        for (int direction : BitBoard.EVERY_DIRECTION) {

            final long to = BitBoard.directionalShiftBoundedWithinArea(king, direction, targetSquares);
            if (BitBoard.EMPTY != to) { // Move is possible
                if (BitBoard.intersects(to, unoccupied)) {
                    moves.add(Move.basicMove(king, to, team | Piece.KING));
                } else {
                    moves.add(Move.basicCapture(king, to, team | Piece.KING, board.getPieceAtBitFromTeam(to, enemyTeam)));
                }
            }
        }
    }

    private void slidingPieceMoveInDirection(final long piece, final int pieceType, final int direction) {

        long previousTo = piece;
        long to = BitBoard.directionalShiftBoundedWithinArea(piece, direction, unoccupied);

        while (to != BitBoard.EMPTY) {

            if (BitBoard.intersects(to, movableSquares)) {
                moves.add(Move.basicMove(piece, to, pieceType));
            }

            previousTo = to;
            to = BitBoard.directionalShiftBoundedWithinArea(to, direction, unoccupied);
        }

        to = BitBoard.directionalShiftBoundedWithinArea(previousTo, direction, enemyPieces);
        if (BitBoard.intersects(to, movableSquares)) {
            moves.add(Move.basicCapture(piece, to, pieceType, board.getPieceAtBitFromTeam(to, enemyTeam)));
        }
    }

    private void slidingPieceMoves(final long piece, final int pieceType, final int[] directions) {
        for (int direction : directions) {
            slidingPieceMoveInDirection(piece, pieceType, direction);
        }
    }

    // This is needed to catch a very sneaky illegal en passant move (en passant reveals horizontal check)
    private boolean isIllegalEnPassantState(final long movingPawn, final long capturedPawn) {

        final long cardinalEnemies = board.getPieces(enemyTeam | Piece.ROOK) | board.getPieces(enemyTeam | Piece.QUEEN);

        long line = BitBoard.fill(king, BitBoard.EAST, unoccupied | movingPawn | capturedPawn);
        line = BitBoard.fill(line, BitBoard.WEST, unoccupied | movingPawn | capturedPawn);
        line |= BitBoard.directionalShiftBoundedWithinArea(line, BitBoard.EAST, cardinalEnemies);
        line |= BitBoard.directionalShiftBoundedWithinArea(line, BitBoard.WEST, cardinalEnemies);

        return BitBoard.intersects(line, cardinalEnemies);
    }

    //  ---------------------------------------------- Calculating Threats And Pins ----------------------------------------------

    private void calculateThreatsAndPins() {

        // Pawns
        singleMoveThreatsInMultipleDirections(board.getPieces(enemyTeam | Piece.PAWN), Piece.getPawnAttackingDirections(!isWhiteTurn));

        // Knights
        singleMoveThreatsInMultipleDirections(board.getPieces(enemyTeam | Piece.KNIGHT), BitBoard.L_SHAPES);

        // King
        singleMoveThreatsInMultipleDirections(board.getPieces(enemyTeam | Piece.KING), BitBoard.EVERY_DIRECTION);

        final long enemyQueens = board.getPieces(enemyTeam | Piece.QUEEN);
        final long enemyDiagonals =  board.getPieces(enemyTeam | Piece.BISHOP) | enemyQueens;
        final long enemyCardinals = board.getPieces(enemyTeam | Piece.ROOK) | enemyQueens;

        slidingMoveThreatsInMultipleDirections(enemyDiagonals, BitBoard.DIAGONAL);
        slidingMoveThreatsInMultipleDirections(enemyCardinals, BitBoard.CARDINAL);

        calculatePins(enemyCardinals, enemyDiagonals);
    }

    private void calculatePins(final long cardinals, final long diagonals) {
        calculatePinInAxis(cardinals, BitBoard.HORIZONTAL, HORIZONTAL);
        calculatePinInAxis(cardinals, BitBoard.VERTICAL, VERTICAL);

        calculatePinInAxis(diagonals, BitBoard.DIAGONAL_POSITIVE, DIAGONAL_POSITIVE);
        calculatePinInAxis(diagonals, BitBoard.DIAGONAL_NEGATIVE, DIAGONAL_NEGATIVE);
    }

    private void calculatePinInAxis(final long enemies, final int[] directions, final int pinIndex) {
        for (int direction : directions) {
            calculatePin(enemies, direction, pinIndex);
        }
    }

    private void calculatePin(final long enemies, final int direction, final int pinIndex) {

        long line = BitBoard.fill(king, direction, unoccupied);  // Fill from the king in a given direction in the unoccupied spaces
        line = BitBoard.directionalShiftBounded(line, direction); // Move one again (regardless of occupation) in the same direction - removes the king space from the line

        if (BitBoard.intersects(line, enemies)) { // We have found a check ray
            addCheckRay(line);
            return;
        }

        if (BitBoard.intersects(line, pieces)) { // We now have a line originating from the king, and ending at a defending piece (possible pin)

            line = BitBoard.fill(line, direction, unoccupied);  // Fill again unoccupied squares
            line |= BitBoard.directionalShiftBounded(line, direction); // Move one again (regardless of occupation) in the same direction

            if (BitBoard.intersects(enemies, line)) { // Found a pin
                pins[pinIndex] |= line;
                allPin |= line;
            }
        }
    }

    private void addCheckRay(final long checkRay) {
        if (isCheck) {
            isDoubleCheck = true;
        } else {
            this.movableSquares = checkRay;
            isCheck = true;
        }
    }

    private void slidingMoveThreatsInMultipleDirections(final long pieces, final int[] directions) {
        for (int direction : directions) {
            slidingMoveThreat(pieces, direction);
        }
    }

    private void slidingMoveThreat(final long pieces, final int direction) {
        threatened |= BitBoard.directionalShiftBounded(BitBoard.fill(pieces, direction, unoccupied | king), direction);
    }

    private void singleMoveThreatsInMultipleDirections(final long pieces, final int[] directions) {
        for (int direction : directions) {
            singleMoveThreat(pieces, direction);
        }
    }

    private void singleMoveThreat(final long pieces, final int direction) {
        final long threats = BitBoard.directionalShiftBounded(pieces, direction);
        if (BitBoard.intersects(king, threats)) {
            // When calculating threats, we found the king...
            isCheck = true;
            movableSquares = BitBoard.oppositeDirectionalShift(king, direction);
        }
        threatened |= threats;
    }

}
