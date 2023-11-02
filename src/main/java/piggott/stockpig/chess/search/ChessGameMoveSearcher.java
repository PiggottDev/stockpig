package piggott.stockpig.chess.search;

import piggott.game.evaluation.GameEvaluator;
import piggott.game.search.AlphaBetaSearcher;
import piggott.game.search.CombinatorialGameMoveSearcher;
import piggott.game.search.MinMaxSearcher;
import piggott.game.search.QuiescenceSearcher;
import piggott.stockpig.chess.evaluation.ChessGameEvaluator;
import piggott.stockpig.chess.game.ChessGame;
import piggott.stockpig.chess.game.ChessMove;

public interface ChessGameMoveSearcher {

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> minmax(final int depth) {
        return new MinMaxSearcher<>(ChessGameEvaluator.position(), depth);
    }

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> minmax(final GameEvaluator<ChessGame> positionEvaluator, final int depth) {
        return new MinMaxSearcher<>(positionEvaluator, depth);
    }

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> alphaBeta(final int depth) {
        return new AlphaBetaSearcher<>(ChessGameEvaluator.position(), depth);
    }

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> alphaBeta(final GameEvaluator<ChessGame> positionEvaluator, final int depth) {
        return new AlphaBetaSearcher<>(positionEvaluator, depth);
    }

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> quiescence(final int depth, final int quiescenceDepth) {
        return new QuiescenceSearcher<>(ChessGameEvaluator.position(), depth, quiescenceDepth);
    }

    static CombinatorialGameMoveSearcher<ChessGame, ChessMove> quiescence(final GameEvaluator<ChessGame> positionEvaluator, final int depth, final int quiescenceDepth) {
        return new QuiescenceSearcher<>(positionEvaluator, depth, quiescenceDepth);
    }

}
