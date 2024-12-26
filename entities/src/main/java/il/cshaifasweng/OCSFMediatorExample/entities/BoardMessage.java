package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

/**
 * Represents the state of the game board, the current player, and the client player's identity.
 */
public class BoardMessage implements Serializable {

    private final char[][] board; // The game board
    private final char currentPlayer; // The player currently making a move
    private final char selfPlayer; // The client player's identifier

    /**
     * Constructs a new BoardMessage.
     *
     * @param board         The game board state.
     * @param currentPlayer The current player's symbol.
     * @param selfPlayer    The symbol representing the client player.
     */
    public BoardMessage(char[][] board, char currentPlayer, char selfPlayer) {
        this.board = deepCopyBoard(board);
        this.currentPlayer = currentPlayer;
        this.selfPlayer = selfPlayer;
    }

    /**
     * Retrieves the current state of the board.
     *
     * @return A 2D character array representing the board.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Retrieves the symbol of the player currently taking their turn.
     *
     * @return The current player's symbol.
     */
    public char getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Retrieves the symbol representing the client player.
     *
     * @return The client player's symbol.
     */
    public char getSelfPlayer() {
        return selfPlayer;
    }

    /**
     * Creates a deep copy of the provided 2D board array to ensure immutability.
     *
     * @param source The original board to copy.
     * @return A deep copy of the provided board.
     */
    private char[][] deepCopyBoard(char[][] source) {
        char[][] copy = new char[3][3];
        for (int i = 0; i < 3; i++) {
            System.arraycopy(source[i], 0, copy[i], 0, 3);
        }
        return copy;
    }
}
