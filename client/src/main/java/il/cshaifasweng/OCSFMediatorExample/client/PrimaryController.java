package il.cshaifasweng.OCSFMediatorExample.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

public class PrimaryController {

	@FXML
	private Button playBtn;

	/**
	 * Initializes the controller by registering to the EventBus.
	 */
	@FXML
	void initialize(){
		EventBus.getDefault().register(this);
	}

	/**
	 * Handles the start game button click event.
	 *
	 * @param event The action event triggered by the button.
	 */
	@FXML
	void startGame(ActionEvent event) {
		playBtn.setDisable(true);
		playBtn.setText("Connecting..");

		try {
			// Notify the server that a player is being added
			SimpleClient.getClient().sendToServer("Add a player");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // Log the error for debugging
		}
	}

	/**
	 * Updates the UI when the client is successfully connected to a game.
	 *
	 * @param event The event signaling a successful connection.
	 */
	@Subscribe
	public void onConnected(ConnectedGame event) {
		Platform.runLater(() -> {
			playBtn.setText("Lookinng for Player");
		});
	}

}
