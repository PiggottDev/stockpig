package piggott.stockpig.chess;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovePathEnumerationTest {

    @Test
    public void starting() {
        runMoveEnumerationPerfTest("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1", 7, 3195901860L);
    }

    @Test
    public void kiwipete() {
        runMoveEnumerationPerfTest("r3k2r/p1ppqpb1/bn2pnp1/3PN3/1p2P3/2N2Q1p/PPPBBPPP/R3K2R w KQkq - 0 1", 6, 8031647685L);
    }

    @Test
    public void pos3() {
        runMoveEnumerationPerfTest("8/2p5/3p4/KP5r/1R3p1k/8/4P1P1/8 w - - 0 1", 8, 3009794393L);
    }

    @Test
    public void pos4() {
        runMoveEnumerationPerfTest("r3k2r/Pppp1ppp/1b3nbN/nP6/BBP1P3/q4N2/Pp1P2PP/R2Q1RK1 w kq - 0 1", 6, 706045033);
    }

    @Test
    public void pos5() {
        runMoveEnumerationPerfTest("rnbq1k1r/pp1Pbppp/2p5/8/2B5/8/PPP1NnPP/RNBQK2R w KQ - 1 8  ", 5, 89941194);
    }

    @Test
    public void pos6() {
        runMoveEnumerationPerfTest("r4rk1/1pp1qppp/p1np1n2/2b1p1B1/2B1P1b1/P1NP1N2/1PP1QPPP/R4RK1 w - - 0 10", 6, 6923051137L);
    }

    private long runMoveEnumerationPerfTest(final String fen, final int depth, final long expectedPositions) {
        
        final long start = System.currentTimeMillis();
        
        assertEquals(expectedPositions, Fen.toGame(fen).movePathEnumerationPerft(depth));
        
        final long end = System.currentTimeMillis();
        final long elapsed = end-start;
        
        System.out.println("Analysed '" + fen + "' in " + elapsed + "ms, " + expectedPositions + " nodes");
        return elapsed;
    }

}
