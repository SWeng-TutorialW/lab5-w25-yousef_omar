package il.cshaifasweng.OCSFMediatorExample.client;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.GameEndMessage;
import org.greenrobot.eventbus.EventBus;

import il.cshaifasweng.OCSFMediatorExample.client.ocsf.AbstractClient;

/**
 * Handles communication between the client and server.
 */
public class SimpleClient extends AbstractClient {

	// Singleton instance of the client
	private static SimpleClient clientInstance = null;

	/**
	 * Private constructor for creating the client instance.
	 *
	 * @param host The server host.
	 * @param port The server port.
	 */
	private SimpleClient(String host, int port) {
		super(host, port);
	}

	/**
	 * Processes messages received from the server.
	 *
	 * @param message The message sent from the server.
	 */
	@Override
	protected void handleMessageFromServer(Object message) {
		if (message instanceof BoardMessage) {
			EventBus.getDefault().post(message);
		} else if (message instanceof GameEndMessage) {
			EventBus.getDefault().post(message);
		} else {
			String response = message.toString();
			if ("Client Added Successfully".equals(response)) {
				System.out.println("Client Added Successfully");
				EventBus.getDefault().post(new ConnectedGame());
			}
			System.out.println(response);
		}
	}

	/**
	 * Provides the singleton instance of the client.
	 * If the instance doesn't exist, it creates one.
	 *
	 * @return The singleton client instance.
	 */
	public static SimpleClient getClient() {
		if (clientInstance == null) {
			clientInstance = new SimpleClient("localhost", 3000);
		}
		return clientInstance;
	}
}

