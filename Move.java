/**
 * Move: a class that represents a move for a piece on a reversi board
 * @author Grace Bocek
 */
public class Move {
    private int row;
    private int col;

    /**
     * move constructor
     * @param i row where reversi piece is placed (int)
     * @param j column where reversi piece is placed (int)
     */
    public Move(int i, int j) {
        row = i;
        col = j;
    }

    /**
     * gets row of move
     * @return row (int)
     */

    public int getRow() {
        return row;
    }

    /**
     * gets column of move
     * @return column (int)
     */
    public int getCol() {
        return col;
    }

    /**
     * represents move as a string
     * @return string representation of move
     */
    public String toString() {
        return (row + 1) + "-" + (char) (col + 97);
    }
}