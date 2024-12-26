package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

/**
 * Represents a move message containing the row and column of a player's move.
 */
public class MoveMessage implements Serializable {

    private final int row; // The row index of the move
    private final int col; // The column index of the move

    /**
     * Constructs a MoveMessage with the specified row and column.
     *
     * @param row The row index of the move.
     * @param col The column index of the move.
     */
    public MoveMessage(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Retrieves the row index of the move.
     *
     * @return The row index.
     */
    public int getRow() {
        return row;
    }

    /**
     * Retrieves the column index of the move.
     *
     * @return The column index.
     */
    public int getCol() {
        return col;
    }
}
