package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MoveMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Controller for the secondary scene.
 * Manages the game board and player interactions.
 */
public class SecondaryController {

    @FXML
    private Button btn00, btn01, btn02;
    @FXML
    private Button btn10, btn11, btn12;
    @FXML
    private Button btn20, btn21, btn22;
    @FXML
    private Label statusLbl;

    private BoardMessage currentBoard;

    /**
     * Handles button clicks on the game board grid.
     *
     * @param event The action event triggered by button clicks.
     */
    @FXML
    void onGridButtonClick(ActionEvent event) {
        Button clickedButton = (Button) event.getSource();
        if (clickedButton.getText().trim().isEmpty()) {
            int[] position = determineButtonPosition(clickedButton);
            try {
                SimpleClient.getClient().sendToServer(new MoveMessage(position[0], position[1]));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Determines the row and column of a button on the grid.
     *
     * @param button The button clicked.
     * @return An array with the row and column indices.
     */
    private int[] determineButtonPosition(Button button) {
        if (button == btn00) return new int[]{0, 0};
        if (button == btn01) return new int[]{1, 0};
        if (button == btn02) return new int[]{2, 0};
        if (button == btn10) return new int[]{0, 1};
        if (button == btn11) return new int[]{1, 1};
        if (button == btn12) return new int[]{2, 1};
        if (button == btn20) return new int[]{0, 2};
        if (button == btn21) return new int[]{1, 2};
        if (button == btn22) return new int[]{2, 2};
        return new int[]{-1, -1}; // Default case (shouldn't occur)
    }

    /**
     * Initializes the controller by registering to the EventBus.
     */
    @FXML
    void initialize() {
        EventBus.getDefault().register(this);
    }

    /**
     * Sets up the initial state of the game board.
     *
     * @param boardMessage The initial board message from the server.
     */
    public void initializeBoard(BoardMessage boardMessage) {
        this.currentBoard = boardMessage;
        updateUI();
    }

    /**
     * Updates the UI elements based on the current board state.
     */
    public void updateUI() {
        if (currentBoard != null) {
            boolean isPlayerTurn = currentBoard.getCurrentPlayer() == currentBoard.getSelfPlayer();
            toggleAllButtons(isPlayerTurn);
            statusLbl.setText(String.format("%s is playing (%s)",
                    currentBoard.getCurrentPlayer(), isPlayerTurn ? "You" : "Opponent"));

            char[][] board = currentBoard.getBoard();
            btn00.setText(String.valueOf(board[0][0]));
            btn01.setText(String.valueOf(board[1][0]));
            btn02.setText(String.valueOf(board[2][0]));
            btn10.setText(String.valueOf(board[0][1]));
            btn11.setText(String.valueOf(board[1][1]));
            btn12.setText(String.valueOf(board[2][1]));
            btn20.setText(String.valueOf(board[0][2]));
            btn21.setText(String.valueOf(board[1][2]));
            btn22.setText(String.valueOf(board[2][2]));
        }
    }

    /**
     * Enables or disables all grid buttons.
     *
     * @param enable True to enable buttons, false to disable.
     */
    private void toggleAllButtons(boolean enable) {
        btn00.setDisable(!enable);
        btn01.setDisable(!enable);
        btn02.setDisable(!enable);
        btn10.setDisable(!enable);
        btn11.setDisable(!enable);
        btn12.setDisable(!enable);
        btn20.setDisable(!enable);
        btn21.setDisable(!enable);
        btn22.setDisable(!enable);
    }

    /**
     * Handles updates to the board state from the server.
     *
     * @param boardMessage The updated board message.
     */
    @Subscribe
    public void onBoardMessage(BoardMessage boardMessage) {
        this.currentBoard = boardMessage;
        Platform.runLater(this::updateUI);
    }
}
