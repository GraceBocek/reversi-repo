import java.util.ArrayList;

/**
 * Board: a class that represents the board in a reversi game
 * @author Grace Bocek x500
 */
public class Board {
    private char[][] board;
    private static final char white = 'O';
    private static final char black = 'X';
    private static final char empty = '-';
    private boolean usersTurn;
    private ArrayList<Move> validMoves;
    private boolean gameOver;
    private int whiteCount;
    private int blackCount;
    private static int gameCount = 0;

    /**
     * constructor for a new board
     */
    public Board() {
        gameCount++;
        board = new char[8][8];
        initializeBoard();
        whiteCount = 2;
        blackCount = 2;
        usersTurn = gameCount % 2 == 1;
        gameOver = false;
        validMoves = new ArrayList<>();
        findValidMoves();
    }

    /**
     * constructor for a copy of an old board
     * @param old board to be copied (Board)
     */
    public Board(Board old) {
        board = copyBoard(old.board);
        whiteCount = old.whiteCount;
        blackCount = old.blackCount;
        usersTurn = old.usersTurn;
        gameOver = old.gameOver;
        validMoves = new ArrayList<>();
        findValidMoves();
    }

    /**
     * resets the count of games played to 0
     */
    public static void resetGameCount() {
        gameCount = 0;
    }

    /**
     * represents the reversi board as a string
     * @return reversi string representation
     */
    public String toString() {
        String header = "  ";
        for (int i = 0; i < board.length; i++) {
            header += (char)(i + 97) + " ";
        }
        header += "\n";
        String out = header;
        for (int i = 0; i < board.length; i++) {
            out += (i + 1) + " ";
            for (int j = 0; j < board.length; j++) {
                out += board[i][j] + " ";
            }
            out += (i + 1) + "\n";
        }
        out += header;
        out += "\n" + white + " count: " + whiteCount + "   " + black + " count: " + blackCount + "\n";
        return out;
    }

    /**
     * sets up a reversi board with two black and two white pieces on the middle diagonals
     */
    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = empty;
            }
        }
        int mid = (board.length - 1)/2;
        board[mid][mid] = white;
        board[mid + 1][mid + 1] = white;
        board[mid][mid + 1] = black;
        board[mid + 1][mid] = black;
    }

    /**
     * creates a copy of a board array
     * @param oldBoard board to be copied (char[][])
     * @return board copy (char[][])
     */
    private char[][] copyBoard(char[][] oldBoard) {
        char[][] out = new char[oldBoard.length][oldBoard[0].length];
        for (int i = 0; i < oldBoard.length; i++) {
            for (int j = 0; j < oldBoard[i].length; j++) {
                out[i][j] = oldBoard[i][j];
            }
        }
        return out;
    }

    /**
     * updates validMoves with a list of possible moves for the current player
     */
    public void findValidMoves() {
        validMoves.clear();
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                                            //move sandwiches at least one opponent piece
                if (board[i][j] == empty && getNextPlayerChars(board, i, j).size() != 0) {
                    Move move = new Move(i, j);
                    validMoves.add(move);
                }
            }
        }
    }

    /**
     * determines what piece sequences should be flipped after a piece is placed
     * @param board reversi board (char[][])
     * @param row row where piece will be placed (int)
     * @param col column where piece will be placed (int)
     * @return coordinates for final cells of sequences that need to be flipped (ArrayList)
     */
    private ArrayList<Move> getNextPlayerChars(char[][] board, int row, int col) {
        ArrayList<Move> out = new ArrayList<>();
        char opponentChar, playerChar;
        if (usersTurn) {
            opponentChar = black;
            playerChar = white;
        }
        else {
            opponentChar = white;
            playerChar = black;
        }
        boolean left, right, up, down;
        int playerCharI, playerCharJ, searchI, searchJ;
        for (int i = -1 + row; i <= 1 + row; i++) {
            for (int j = -1 + col; j <= 1 + col; j++) {
                left = false;
                right = false;
                up = false;
                down = false;
                                        //adjacent to opponent piece
                if (inBounds(i, j) && board[i][j] == opponentChar && (i != row || j != col)) {
                    //determines direction of search
                    if (i < row) {
                        up = true;
                    }
                    else if (i > row) {
                        down = true;
                    }
                    if (j < col) {
                        left = true;
                    }
                    else if (j > col) {
                        right = true;
                    }
                    playerCharJ = -1;
                    playerCharI = -1;
                    searchJ = j;
                    searchI = i;
                    if (down && left) {
                        searchJ--;
                        searchI++;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[searchI][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ--;
                            searchI++;
                        }
                    }
                    else if (down && right) {
                        searchJ++;
                        searchI++;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[searchI][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ++;
                            searchI++;
                        }
                    }
                    else if (down) {
                        searchI++;
                        while (keepSearching(searchI, searchJ, playerCharI)) {
                            if (board[searchI][j] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchI++;
                        }
                    }
                    else if (up && left) {
                        searchJ--;
                        searchI--;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[searchI][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ--;
                            searchI--;
                        }
                    }
                    else if (up && right) {
                        searchJ++;
                        searchI--;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[searchI][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ++;
                            searchI--;
                        }
                    }
                    else if (up) {
                        searchI--;
                        while (keepSearching(searchI, searchJ, playerCharI)) {
                            if (board[searchI][j] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchI--;
                        }
                    }
                    else if (left) {
                        searchJ--;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[i][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ--;
                        }
                    }
                    else {
                        searchJ++;
                        while (keepSearching(searchI, searchJ, playerCharJ)) {
                            if (board[i][searchJ] == playerChar) {
                                playerCharJ = searchJ;
                                playerCharI = searchI;
                            }
                            searchJ++;
                        }
                    }
                    if (playerCharJ != -1) {
                        //sandwiching player's piece has been found
                        Move move = new Move(playerCharI, playerCharJ);
                        out.add(move);
                    }
                }
            }
        }
        return out;
    }

    /**
     * determines if a cell is on the board
     * @param i row (int)
     * @param j column
     * @return whether the indices are in bounds (bool)
     */
    private boolean inBounds(int i, int j) {
        return i >= 0 && j >= 0 && i < board.length && j < board[0].length;
    }

    /**
     * determines whether a given sequence should still be searched for one of the
     * current player's pieces
     * @param i row to search (int)
     * @param j column to search (int)
     * @param playerIdx index of the next piece of the current player's color
     * @return whether to keep searching the sequence (bool)
     */
    private boolean keepSearching(int i, int j, int playerIdx) {
        return inBounds(i, j) && playerIdx == -1 && board[i][j] != empty;
    }

    /**
     * places the next piece on the board and flips sandwiched piece sequences
     * @param move coordinates of placed piece (int[])
     */
    public void updateBoard(Move move) {
        int flipped = 0;
        char[][] oldBoard = copyBoard(board);
        char playerChar;
        if (usersTurn) {
            playerChar = white;
        }
        else {
            playerChar = black;
        }
        int row = move.getRow();
        int col = move.getCol();
        board[row][col] = playerChar;
        ArrayList<Move> playerCharLocations = getNextPlayerChars(oldBoard, row, col);
        int i, j, searchI, searchJ;
        boolean up, down, left, right;
        for (Move location : playerCharLocations) {
            up = false; down = false; left = false; right = false;
            i = location.getRow();
            j = location.getCol();
            searchI = row;
            searchJ = col;
            //determines direction to search
            if (i < row) {
                up = true;
            }
            else if (i > row) {
                down = true;
            }
            if (j < col) {
                left = true;
            }
            else if (j > col) {
                right = true;
            }
            if (down && left) {
                searchJ--; searchI++;
                while (searchJ > j) {
                    board[searchI][searchJ] = playerChar;
                    searchJ--; searchI++;
                    flipped++;
                }
            }
            else if (down && right) {
                searchJ++; searchI++;
                while (searchJ < j) {
                    board[searchI][searchJ] = playerChar;
                    searchJ++; searchI++;
                    flipped++;
                }
            }
            else if (down) {
                for (int k = row + 1; k < i; k++) {
                    board[k][col] = playerChar;
                    flipped++;
                }
            }
            else if (up && left) {
                searchJ--; searchI--;
                while (searchJ > j) {
                    board[searchI][searchJ] = playerChar;
                    searchJ--; searchI--;
                    flipped++;
                }
            }
            else if (up && right) {
                searchJ++; searchI--;
                while (searchJ < j) {
                    board[searchI][searchJ] = playerChar;
                    searchJ++; searchI--;
                    flipped++;
                }
            }
            else if (up) {
                for (int k = row - 1; k > i; k--) {
                    board[k][col] = playerChar;
                    flipped++;
                }
            }
            else if (left) {
                for (int k = col - 1; k > j; k--) {
                    board[row][k] = playerChar;
                    flipped++;
                }
            }
            else {
                for (int k = col + 1; k < j; k++) {
                    board[row][k] = playerChar;
                    flipped++;
                }
            }
        }
        if (usersTurn) {
            whiteCount += flipped + 1;
            blackCount -= flipped;
        }
        else {
            blackCount += flipped + 1;
            whiteCount -= flipped;
        }
        if (blackCount + whiteCount == 64) {
            gameOver = true;
        }
        switchTurn();
    }

    /**
     * gets valid moves
     * @return list of valid moves (ArrayList)
     */
    public ArrayList<Move> getValidMoves() {
        return validMoves;
    }

    /**
     * switches turns between the user and the computer
     */
    public void switchTurn() {
        usersTurn = !usersTurn;
    }

    /**
     * determines whether it's the user's turn
     * @return whether it's the user's turn (bool)
     */
    public boolean getUsersTurn() {
        return usersTurn;
    }

    /**
     * counts the number of black and white pieces on the board
     * @return the number of black and white pieces on the board (int[])
     */
    public int[] count() {
        return new int[]{whiteCount, blackCount};
    }

    /**
     * ends the reversi game
     */
    public void endGame() {
        gameOver = true;
    }

    /**
     * determines whether the reversi game is over
     * @return whether the game is over (bool)
     */
    public boolean gameOver() {
        return gameOver;
    }
    /**
     * determines the score of a piece based on its location on the board
     * @param row row where piece is located (int)
     * @param col column where piece is located (int)
     * @return score of the piece
     */
    public int pieceScore(int row, int col) {
        int n = board.length - 1;
        //prefers corner pieces
        if (( row == 0 || row == n) && (col == 0 || col == n)) {
            return 10;
        }
        //avoids moves next to empty corner
        if (((row == 0 && col == 1) || (row == 1 && (col == 0 || col == 1))) && board[0][0] == empty) {
            return -10;
        }
        if (((row == n && col == 1) || (row == n - 1 && (col == 0 || col == 1))) && board[n][0] == empty) {
            return -10;
        }
        if (((row == 0 && col == n - 1) || (row == 1 && (col == n || col == n - 1))) && board[0][n] == empty) {
            return -10;
        }
        if (((row == n && col == n - 1) || (row == n - 1 && (col == n - 1 || col == n)))
                && board[n][n] == empty) {
            return -10;
        }
        //other pieces
        return 1;
    }

    /**
     * determines the computer's score on the current board
     * @return the computer's score (int)
     */
    public int getComputerScore() {
        int score = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == white) {
                    score -= pieceScore(i, j);
                }
                else if (board[i][j] == black) {
                    score += pieceScore(i, j);
                }
            }
        }
        return score;
    }
}