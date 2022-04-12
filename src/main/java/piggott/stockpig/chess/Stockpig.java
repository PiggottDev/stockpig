package piggott.stockpig.chess;

import java.util.Scanner;

public class Stockpig {

    private static final String NEW = "new";
    private static final String FEN = "fen";
    private static final String MOVE = "move";
    private static final String UNDO = "undo";
    private static final String PERFT = "perft";
    private static final String DIVIDE = "divide";
    private static final String EXIT = "exit";

    public static void main(String[] args) {
        mainLoop();
    }

    private static void mainLoop() {

        final Scanner in = new Scanner(System.in);
        boolean exit = false;

        Game game = Game.standard();

        while(!exit) {

            System.out.println(game.toString());
            System.out.println("=>");

            final String input = in.nextLine();
            final String[] command = input.split(" ");

            switch (command[0]) {
                case NEW:
                    game = Game.standard();
                    break;
                case FEN:
                    try {
                        game = Game.fromFen(input.substring(4));
                    } catch (Exception ex) {
                        System.out.println("Invalid Fen");
                    }
                    break;
                case MOVE:
                    for(Move move : game.getPossibleMoves()) {
                        if (move.toString().equals(command[1])) game.applyMove(move);
                    }
                    break;
                case UNDO:
                    game.undoLastMove();
                    break;
                case PERFT:
                    try {
                        System.out.println(game.movePathEnumerationPerft(Integer.parseInt(command[1])) + " nodes analysed");
                    } catch (Exception ex) {
                        System.out.println("Invalid Depth");
                    }
                    break;
                case DIVIDE:
                    try {
                        System.out.println(game.divide(Integer.parseInt(command[1])) + " nodes analysed");
                    } catch (Exception ex) {
                        System.out.println("Invalid Depth");
                    }
                    break;
                case EXIT:
                    exit = true;
                    break;
                default:
                    System.out.println("Unknown Command");
            }
        }
    }

}
