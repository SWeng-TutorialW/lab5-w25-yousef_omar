package il.cshaifasweng.OCSFMediatorExample.entities;

import java.io.Serializable;

/**
 * Represents the message sent when the game ends.
 */
public class GameEndMessage implements Serializable {

    private final String result; // The result of the game (e.g., "Player X Wins")

    /**
     * Constructs a new GameEndMessage with the specified result.
     *
     * @param result The result of the game.
     */
    public GameEndMessage(String result) {
        this.result = result;
    }

    /**
     * Retrieves the result of the game.
     *
     * @return A string describing the game result.
     */
    public String getResult() {
        return result;
    }
}
