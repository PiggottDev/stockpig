package piggott.stockpig.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a game of chess implemented using bitboards.
 * Also stores history so moves can be undone.
 *
 * @see Bitboard
 */
public class Game {

    private final Board board;
    private boolean isWhiteTurn;
    private int castlesPossible;
    private long enPassantTarget;
    private int turnsSincePushOrCapture;
    private int turnNumber;

    private final List<Move> previousMoves = new ArrayList<>();
    private final List<Integer> previousCastlesPossible = new ArrayList<>();
    private final List<Long> previousEnPassantTargets = new ArrayList<>();
    private final List<Integer> previousTurnSincePushOrCapture = new ArrayList<>();

    private List<Move> possibleMoves;
    private MoveGenerator moveGenerator;

    /**
     * Game with standard chess set up.
     *
     * @return standard chess game
     */
    public static Game standard() {
        return new Game(Board.standard(), true, Castling.ALL_ALLOWED, Bitboard.EMPTY, 0, 1);
    }

    Game(final Board board, final boolean isWhiteTurn, final int castlesPossible, final long enPassantTarget, final int turnsSincePushOrCapture, final int turnNumber) {
        this.board = board;
        this.isWhiteTurn = isWhiteTurn;
        this.castlesPossible = castlesPossible;
        this.enPassantTarget = enPassantTarget;
        this.turnsSincePushOrCapture = turnsSincePushOrCapture;
        this.turnNumber = turnNumber;
        analyseBoard();
    }

    // -- Getters --

    /**
     * Is the game over for any reason:
     * <ul>
     *     <li>Checkmate</li>
     *     <li>Stalemate</li>
     *     <li>Dead position</li>
     *     <li>50 move rule</li>
     * </ul>
     *
     * @return whether the game is over
     */
    public boolean isGameOver() {
        return possibleMoves.size() == 0;
    }

    /**
     * If the current moving player is in checkmate.
     *
     * @return is moving player in checkmate
     */
    public boolean isCheckMate() {
        return (possibleMoves.size() == 0 && isCheck());
    }

    /**
     * Return value representing who has won or if it's a draw.
     * Should be used in conjunction with {@link #isGameOver()} to avoid
     * active games being marked as a stalemate.
     * <ol>
     *     <li>-1 = Black has won</li>
     *     <li>0 = Draw or game is not over</li>
     *     <li>1 = White has won</li></>
     * </ol>
     *
     * @return winner value
     */
    public int getWinner() {
        if (!isGameOver()) return 0; // Game isn't over
        if (isCheck()) return isWhiteTurn ? -1 : 1; // If check, loser is move maker
        return 0; //Stalemate
    }

    /**
     * Get whether the current moving player is in check.
     *
     * @return is moving player in check
     */
    public boolean isCheck() {
        return moveGenerator.isCheck();
    }

    /**
     * Whether it is currently white's turn to move.
     *
     * @return is white's turn to move
     */
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * Get list of legal moves.
     *
     * @return legal moves
     */
    public List<Move> getPossibleMoves() {
        return possibleMoves;
    }

    /**
     * Get the board object.
     *
     * @return board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Current turn number.
     *
     * @return turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Turns since pawn push or a capture.
     *
     * @return turns since push/capture
     */
    public int getTurnsSincePushOrCapture() {
        return turnsSincePushOrCapture;
    }

    /**
     * Get the bitmap of the current castles allowed.
     *
     * @return bitmap of castles allowed
     * @see Castling
     */
    public int getCastlesPossible() {
        return castlesPossible;
    }

    /**
     * Get the current target square of an en passant.
     * Only present directly after a double pawn push move.
     *
     * @return en passant target bitboard
     */
    public long getEnPassantTarget() {
        return enPassantTarget;
    }

    /**
     * Get bitboard of squares currently threatened by the non-moving player.
     *
     * @return threat bitboard
     */
    public long getThreatenedSquares() {
        return moveGenerator.getThreatened();
    }

    /**
     * Get the squares in which the moving team is allowed to move.
     * When NOT in check:
     * - All squares not occupied by an allied piece
     * When in check:
     * - Squares that can be moved/captured to block check
     *
     * @return movable squares for the current moving player
     */
    public long getMovableSquares() {
        return moveGenerator.getMovableSquares();
    }

    /**
     * Get a bitboard of 'pin rays'.
     * A 'pin ray' starts at the enemy piece's square and ends at the king (excluding the king square).
     * Contains exactly one allied piece (the pinned piece), per ray.
     *
     * @return bitboard of pin rays
     */
    public long getPinSquares() {
        return moveGenerator.getAllPin();
    }

    // -- Move --

    /**
     * Apply a move to the game.
     *
     * @param move move
     */
    public void applyMove(final Move move) {
        // Save previous state
        previousMoves.add(move);
        previousCastlesPossible.add(castlesPossible);
        previousEnPassantTargets.add(enPassantTarget);
        previousTurnSincePushOrCapture.add(turnsSincePushOrCapture);

        // Apply move
        board.applyMove(move);
        castlesPossible = Castling.getCastlesAllowedAfterMove(castlesPossible, move, isWhiteTurn);
        isWhiteTurn = !isWhiteTurn;
        enPassantTarget = move.getEnPassantTarget();
        turnsSincePushOrCapture = (move.isPawnMove() || move.isCapture()) ? 0 : turnsSincePushOrCapture + 1;
        if (isWhiteTurn) turnNumber++;

        // Generate possible moves
        analyseBoard();
    }

    /**
     * Undo last move.
     */
    public void undoLastMove() {
        undoLastMove(true);
    }

    /**
     * Undo last move.
     * Can opt out of board analysis/move generation to save time if the list of moves is
     * stored elsewhere - useful in a depth/perft test.
     *
     * @param regenMoves whether to generate moves after undo
     */
    void undoLastMove(final boolean regenMoves) {
        final int moveNumber = previousMoves.size() - 1;
        if (moveNumber < 0) return;

        // Undo last move
        board.undoMove(previousMoves.get(moveNumber));
        castlesPossible = previousCastlesPossible.get(moveNumber);
        enPassantTarget = previousEnPassantTargets.get(moveNumber);
        turnsSincePushOrCapture = previousTurnSincePushOrCapture.get(moveNumber);
        if (isWhiteTurn) turnNumber--;
        isWhiteTurn = !isWhiteTurn;

        // Remove the un-done move from the history
        previousMoves.remove(moveNumber);
        previousCastlesPossible.remove(moveNumber);
        previousEnPassantTargets.remove(moveNumber);
        previousTurnSincePushOrCapture.remove(moveNumber);

        // Generate possible moves
         if (regenMoves) analyseBoard();
    }

    private void analyseBoard() {
        moveGenerator = new MoveGenerator(board, isWhiteTurn, castlesPossible, enPassantTarget);
        if (turnsSincePushOrCapture > 49 || board.isDeadPosition()) {
            possibleMoves = new ArrayList<>();
        } else {
            possibleMoves = moveGenerator.generateLegalMoves();
        }
    }

    // -- Debug String --

    public String debugString() {
        String str = board.debugString();

        if (isGameOver()) str = str + "\n\tWinner: " + getWinner();
        if (isCheck()) str = str + "\n\tCheck";
        str = str + (isWhiteTurn ? "\n\tWhite Turn" : "\n\tBlack Turn");
        str = str + "\n\t\tCastles: " + Castling.toString(castlesPossible);
        str = str + "\n\t\tEnPassant Target: " + AlgebraNotation.fromBitboard(enPassantTarget);
        str = str + "\n\t\tTurns Since Push/Cap: " + turnsSincePushOrCapture;
        str = str + "\n\t\tTurn Number: "+ turnNumber;

        return str;
    }

    // -- Depth Test -- TODO: Move to separate class

    public long movePathEnumerationPerft(final int depth) {
        if (depth < 1) return 1;
        if (depth == 1) return possibleMoves.size();

        long numberOfPositions = 0;

        for (Move move : getPossibleMoves()) {

            applyMove(move);
            numberOfPositions += movePathEnumerationPerft(depth - 1);
            undoLastMove(false);
        }

        return numberOfPositions;
    }

    public long divide(final int depth) {

        long totalNumberOfPositions = 0L;

        for (Move move : getPossibleMoves()) {

            applyMove(move);
            final long nodesAfterMove = movePathEnumerationPerft(depth - 1);
            undoLastMove(false);

            System.out.println(move.toString() + ": " + nodesAfterMove);

            totalNumberOfPositions += nodesAfterMove;
        }

        return totalNumberOfPositions;
    }

}
