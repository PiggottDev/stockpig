package piggott.stockpig.chess;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a game of chess
 * Also stores history so moves can be undone
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
    private BoardAnalyser boardAnalyser;

    /**
     * Game with standard chess set up
     *
     * @return Standard chess game
     */
    public static Game standard() {
        return new Game(Board.standard(), true, Castling.ALL_ALLOWED, BitBoard.EMPTY, 0, 1);
    }

    private Game(final Board board, final boolean isWhiteTurn, final int castlesPossible, final long enPassantTarget, final int turnsSincePushOrCapture, final int turnNumber) {
        this.board = board;
        this.isWhiteTurn = isWhiteTurn;
        this.castlesPossible = castlesPossible;
        this.enPassantTarget = enPassantTarget;
        this.turnsSincePushOrCapture = turnsSincePushOrCapture;
        this.turnNumber = turnNumber;
        analyseBoard();
    }

    //  ---------------------------------------------- Getters And Debug ----------------------------------------------

    /**
     * Is the game over for any reason:
     * - Check mate
     * - Stale mate
     * - Dead position
     * - 50 move rule
     *
     * @return Whether the game is over
     */
    public boolean isGameOver() {
        return possibleMoves.size() == 0;
    }

    /**
     * If the current moving player is in check mate
     *
     * @return Is moving player in check mate
     */
    public boolean isCheckMate() {
        return (possibleMoves.size() == 0 && isCheck());
    }

    /**
     * Return -1/0/1 showing who has won/draw
     * -1: Black has won
     * 0: Draw
     * 1: White has won
     *
     * @return Winner
     */
    public int getWinner() {
        if (!isGameOver()) return 0; // Game isn't over
        if (isCheck()) return isWhiteTurn ? -1 : 1; // If check, loser is move maker
        return 0; //Stalemate
    }

    /**
     * Get whether the current moving player is in check
     *
     * @return Is moving player in check
     */
    public boolean isCheck() {
        return boardAnalyser.isCheck();
    }

    /**
     * Whether it is currently white's turn to move
     *
     * @return Is white's turn to move
     */
    public boolean isWhiteTurn() {
        return isWhiteTurn;
    }

    /**
     * Get list of legal moves
     *
     * @return List of legal moves
     */
    public List<Move> getPossibleMoves() {
        return possibleMoves;
    }

    /**
     * Get the board object
     *
     * @return Chess board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Current turn number
     *
     * @return Turn number
     */
    public int getTurnNumber() {
        return turnNumber;
    }

    /**
     * Turns since pawn push or a capture
     *
     * @return Turns since push/capture
     */
    public int getTurnsSincePushOrCapture() {
        return turnsSincePushOrCapture;
    }

    /**
     * Get the bitmap of the current castles allowed
     *
     * @return Bitmap of castles allowed
     */
    public int getCastlesPossible() {
        return castlesPossible;
    }

    /**
     * Get the current target square of an en passant
     * Only present directly after a double pawn push move
     *
     * @return En passant target bitboard
     */
    public long getEnPassantTarget() {
        return enPassantTarget;
    }

    /**
     * Get bitboard of squares currently threatened by the non-moving player
     *
     * @return Threat bitboard
     */
    public long getThreatenedSquares() {
        return boardAnalyser.getThreatened();
    }

    /**
     * Get the squares in which the moving team is allowed to move
     * When NOT in check:
     * - All squares not occupied by an allied piece
     * When in check:
     * - Squares that can be moved/captured to block check
     *
     * @return Movable squares for the current moving player
     */
    public long getMovableSquares() {
        return boardAnalyser.getMovableSquares();
    }

    /**
     * Get a bitboard of 'pin rays'
     * A 'pin ray' starts at the enemy piece's square and ends at the king (excluding the king square)
     * Contains exactly one allied piece (the pinned piece)
     *
     * @return Bitboard of pin rays
     */
    public long getPinSquares() {
        return boardAnalyser.getAllPin();
    }

    //  ---------------------------------------------- Moves ----------------------------------------------

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

    public void undoLastMove() {
        undoLastMove(true);
    }

    public void undoLastMove(final boolean regenMoves) {
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
        boardAnalyser = new BoardAnalyser(board, isWhiteTurn, castlesPossible, enPassantTarget);
        if (turnsSincePushOrCapture > 49 || Board.isDeadPosition(board)) {
            possibleMoves = new ArrayList<>();
        } else {
            possibleMoves = boardAnalyser.generateLegalMoves();
        }
    }

    //  ---------------------------------------------- Fen and String ----------------------------------------------

    public String toFen() {
        String fen = board.toFen();
        fen = fen + (isWhiteTurn ? " w " : " b ");
        fen = fen + Castling.toString(castlesPossible);
        fen = fen + " " + AlgebraNotation.fromBit(enPassantTarget);
        fen = fen + " " + turnsSincePushOrCapture + " " + turnNumber;
        return fen;
    }

    public static Game fromFen(final String fen) {
        final String[] fenParts = fen.split(" ");
        return new Game(Board.fromFen(fenParts[0]), "w".equals(fenParts[1]), Castling.fromString(fenParts[2]), AlgebraNotation.toBit(fenParts[3]), Integer.parseInt(fenParts[4]), Integer.parseInt(fenParts[5]));
    }

    @Override
    public String toString() {
        String str = board.toString();

        if (isGameOver()) str = str + "\n\tWinner: " + getWinner();
        if (isCheck()) str = str + "\n\tCheck";
        str = str + (isWhiteTurn ? "\n\tWhite Turn" : "\n\tBlack Turn");
        str = str + "\n\t\tCastles: " + Castling.toString(castlesPossible);
        str = str + "\n\t\tEnPassant Target: " + AlgebraNotation.fromBit(enPassantTarget);
        str = str + "\n\t\tTurns Since Push/Cap: " + turnsSincePushOrCapture;
        str = str + "\n\t\tTurn Number: "+ turnNumber;

        return str;
    }

    //  ---------------------------------------------- Depth Test ----------------------------------------------

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
