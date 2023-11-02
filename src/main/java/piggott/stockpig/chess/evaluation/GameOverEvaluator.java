package piggott.stockpig.chess.evaluation;

import piggott.game.evaluation.GameEvaluator;
import piggott.stockpig.chess.game.ChessGame;

/**
 * Evaluate whether a game of chess is over and if so return the max score
 * for the winning player.
 * Max per team (for checkmate): 12000
 */
public class GameOverEvaluator implements GameEvaluator<ChessGame> {

    private final GameEvaluator<ChessGame> positionEvaluator;

    public GameOverEvaluator(final GameEvaluator<ChessGame> positionEvaluator) {
        this.positionEvaluator = positionEvaluator;
    }

    @Override
    public int evaluate(final ChessGame game) {
        return game.isGameOver() ? game.getWinner() * 12000 : this.positionEvaluator.evaluate(game);
    }

}
