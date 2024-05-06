import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Reversi: a class that simulates a game of Reversi in the terminal. The
 * user plays against the computer
 * @author Grace Bocek
 */
public class Reversi {
    private Board board;
    private boolean userSkip;
    private boolean computerSkip;
    private ArrayList<Move> validMoves;

    /**
     * Constructor for a game of reversi
     */
    public Reversi() {
        board = new Board();
        userSkip = false;
        computerSkip = false;
    }

    /**
     * allows user to place the next piece on the board
     */
    private void userMove() {
        board.findValidMoves();
        validMoves = board.getValidMoves();
        userSkip = validMoves.size() == 0;
        if (board.gameOver()) {
            return;
        }
        if (computerSkip && userSkip) {
            //both players are out of moves
            board.endGame();
            int[] count = board.count();
            if (count[0] + count[1] != 64) {
                //empty cells left
                System.out.println("No valid moves.");
                System.out.println();
            }
            return;
        }
        if (userSkip) {
            System.out.println("No valid moves. You skip a turn.");
            System.out.println();
            board.switchTurn();
            return;
        }
        System.out.print("Valid moves: ");
        for (int i = 0; i < validMoves.size(); i++) {
            Move option = validMoves.get(i);
            System.out.print((i + 1) + ". " + option + "   ");
        }
        System.out.println();
        printEnterMoves();
        Scanner scan = new Scanner(System.in);
        String input = scan.next();
        scan.nextLine();
        int choice = 0;
        boolean isInt = true;
        try {
            choice = Integer.parseInt(input);
        }
        catch (NumberFormatException e) {
            isInt = false;
        }
        while (!isInt || choice < 1 || choice > validMoves.size()) {
            printEnterMoves();
            input = scan.next();
            scan.nextLine();
            try {
                choice = Integer.parseInt(input);
                isInt = true;

            }
            catch (NumberFormatException e) {
                isInt = false;
            }
        }
        board.updateBoard(validMoves.get(choice - 1));
        System.out.println();
        System.out.println(board);
    }

    /**
     * prints a message to the user to input a move
     */
    private void printEnterMoves() {
        System.out.print("Enter 1");
        validMoves = board.getValidMoves();
        if (validMoves.size() > 1) {
            System.out.print("-" + validMoves.size());
        }
        System.out.print(": ");
    }

    /**
     * plays a piece for the computer
     */
    private void computerMove() {
        board.findValidMoves();
        validMoves = board.getValidMoves();
        computerSkip = validMoves.size() == 0;
        if (board.gameOver()) {
            return;
        }
        if (computerSkip && userSkip) {
            //both players out of moves
            board.endGame();
            int[] count = board.count();
            if (count[0] + count[1] != 64) {
                //empty cells left
                System.out.println("No valid computer moves.");
                System.out.println();
            }
            return;
        }
        if (computerSkip) {
            System.out.println("No valid moves. Computer skips a turn.");
            System.out.println();
            board.switchTurn();
            return;
        }
        Collections.shuffle(validMoves);
        Move move = findBestMove();
        System.out.println("Computer move: " + move);
        board.updateBoard(move);
        System.out.println();
        System.out.println(board);
    }

    /**
     * determines which valid move is best for the computer
     * @return the best valid move (int)
     */
    private Move findBestMove() {
        ArrayList<Integer> nextMoveScores = new ArrayList<>();
        for (Move move : validMoves) {
            Board nextBoard = new Board(board);
            nextBoard.updateBoard(move);
            nextBoard.findValidMoves();
            nextMoveScores.add(minimax(3, nextBoard, -100, 100));
        }
        //find idx of move with highest score
        int moveIdx = 0;
        for (int i = 0; i < nextMoveScores.size(); i++) {
            if (nextMoveScores.get(i) > nextMoveScores.get(moveIdx)) {
                moveIdx = i;
            }
        }
        return validMoves.get(moveIdx);
    }

    /**
     * predicts what future moves will be best for the user and computer and determines
     * the best score the computer can achieve
     * @param depth depth of search (int)
     * @param board reversi board to search (Board)
     * @param alpha optimal score for the computer (int)
     * @param beta optimal score for the user (int)
     * @return best score the computer can achieve on the next move (int)
     */
    private int minimax(int depth, Board board, int alpha, int beta) {
        //[1, Minimax Algorithm with Alpha-Beta Pruning]
        if (depth == 0) {
            return board.getComputerScore();
        }
        board.findValidMoves();
        ArrayList<Move> moves = board.getValidMoves();
        Collections.shuffle(moves);
        //minimize computer score
        int bestScore;
        if (board.getUsersTurn()) {
            bestScore = 100;
            for (Move move : moves) {
                Board nextBoard = new Board(board);
                nextBoard.updateBoard(move);
                int score = minimax(depth - 1, nextBoard, alpha, beta);
                bestScore = Math.min(bestScore, score);
                alpha = Math.min(bestScore, alpha);
                if (beta <= alpha) {
                    //don't go down branch that favors user
                    break;
                }
            }
            //skipped turn or game over
            if (moves.size() == 0) {
                board.switchTurn();
                int score = minimax(depth - 1, board, alpha, beta);
                bestScore = Math.min(bestScore, score);
            }
        }
        //maximize computer score
        else {
            bestScore = -100;
            for (Move move : moves) {
                Board nextBoard = new Board(board);
                nextBoard.updateBoard(move);
                int score = minimax(depth - 1, nextBoard, alpha, beta);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(bestScore, alpha);
                if (beta <= alpha) {
                    //don't go down branch that favors user
                    break;
                }
            }
            //skipped turn or game over
            if (moves.size() == 0) {
                board.switchTurn();
                int score = minimax(depth - 1, board, alpha, beta);
                bestScore = Math.max(bestScore, score);
            }
        }
        return bestScore;
    }

    /**
     * plays a match of reversi
     */
    public void play(){
        System.out.println(board);
        while (!board.gameOver()) {
            if (board.getUsersTurn()) {
                userMove();
            }
            else {
                computerMove();
            }
        }
        System.out.println("Game Over");
        int[] count = board.count();
        int whiteCount = count[0];
        int blackCount = count[1];
        if (whiteCount > blackCount) {
            System.out.println("You win!");
        }
        else if (whiteCount < blackCount) {
            System.out.println("You lose!");
        }
        else {
            System.out.println("Tie!");
        }
    }
}