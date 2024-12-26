package il.cshaifasweng.OCSFMediatorExample.server;

import il.cshaifasweng.OCSFMediatorExample.entities.BoardMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.GameEndMessage;
import il.cshaifasweng.OCSFMediatorExample.entities.MoveMessage;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.AbstractServer;
import il.cshaifasweng.OCSFMediatorExample.server.ocsf.ConnectionToClient;

import java.io.IOException;

/**
 * A simple server implementation for managing a Tic-Tac-Toe game.
 */
public class SimpleServer extends AbstractServer {

	private ConnectionToClient playerX; // Connection for Player X
	private ConnectionToClient playerO; // Connection for Player O
	private boolean gameInProgress = false; // Indicates if a game is ongoing
	private char[][] board = new char[3][3]; // The game board
	private char currentPlayer = 'X'; // The current player's symbol

	/**
	 * Constructs a new SimpleServer.
	 *
	 * @param port The port on which the server will listen.
	 */
	public SimpleServer(int port) {
		super(port);
		resetBoard();
	}

	/**
	 * Handles messages received from a client.
	 *
	 * @param msg    The message sent by the client.
	 * @param client The connection of the client sending the message.
	 */
	@Override
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		if (msg instanceof MoveMessage && gameInProgress) {
			handleMove((MoveMessage) msg, client);
		} else if (msg instanceof String) {
			handleStringMessage(msg.toString(), client);
		}
	}

	/**
	 * Handles string messages from the client.
	 *
	 * @param message The string message.
	 * @param client  The client sending the message.
	 */
	private void handleStringMessage(String message, ConnectionToClient client) {
		System.out.println(message);
		if (message.startsWith("Add a player")) {
			addPlayer(client);
		} else if (message.startsWith("remove player")) {
			removePlayer(client);
		}
	}

	/**
	 * Adds a player to the game.
	 *
	 * @param client The client to be added.
	 */
	private void addPlayer(ConnectionToClient client) {
		try {
			System.out.println(playerX);
			System.out.println(playerO);
			if (playerX == null) {
				System.out.println("Client X connected");
				playerX = client;
				client.sendToClient("Client Added Successfully");
			} else if (playerO == null) {
				System.out.println("Client O connected");
				playerO = client;
				client.sendToClient("Client Added Successfully");
			}

			if (playerX != null && playerO != null && !gameInProgress) {
				startNewGame();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Removes a player from the game.
	 *
	 * @param client The client to be removed.
	 */
	private void removePlayer(ConnectionToClient client) {
		System.out.println("Remove Player");
		if (gameInProgress) {
			try {
				if (client == playerX) {
					notifyOpponent(playerO, "Opponent Left!!");
				} else if (client == playerO) {
					notifyOpponent(playerX, "Opponent Left!!");
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		gameInProgress = false;
		playerX = null;
		playerO = null;
	}

	/**
	 * Notifies a client with a message.
	 *
	 * @param client The client to notify.
	 * @param message The message to send.
	 */
	private void notifyOpponent(ConnectionToClient client, String message) throws IOException {
		if (client != null) {
			client.sendToClient(new GameEndMessage(message));
		}
	}

	/**
	 * Resets the game board and initializes the current player.
	 */
	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				board[i][j] = ' ';
			}
		}
		currentPlayer = 'X';
	}

	/**
	 * Starts a new game and notifies both players.
	 */
	private void startNewGame() {
		gameInProgress = true;
		resetBoard();

		try {
			playerO.sendToClient(new BoardMessage(board, currentPlayer, 'O'));
			playerX.sendToClient(new BoardMessage(board, currentPlayer, 'X'));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Handles a move made by a player.
	 *
	 * @param move   The move made by the player.
	 * @param client The client making the move.
	 */
	private void handleMove(MoveMessage move, ConnectionToClient client) {
		int row = move.getRow();
		int col = move.getCol();
		char clientSymbol = (client == playerX) ? 'X' : (client == playerO) ? 'O' : ' ';

		if (clientSymbol == currentPlayer && board[row][col] == ' ') {
			board[row][col] = currentPlayer;
			try {
				if (checkWin()) {
					endGame("Player " + currentPlayer + " Wins!");
				} else if (checkDraw()) {
					endGame("DRAW");
				} else {
					switchPlayer();
					updatePlayers();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Ends the game and notifies both players.
	 *
	 * @param message The game result message.
	 */
	private void endGame(String message) throws IOException {
		playerO.sendToClient(new GameEndMessage(message));
		playerX.sendToClient(new GameEndMessage(message));
		gameInProgress = false;
		playerX = null;
		playerO = null;
	}

	/**
	 * Switches the current player.
	 */
	private void switchPlayer() {
		currentPlayer = (currentPlayer == 'X') ? 'O' : 'X';
	}

	/**
	 * Sends the updated board state to both players.
	 */
	private void updatePlayers() throws IOException {
		playerO.sendToClient(new BoardMessage(board, currentPlayer, 'O'));
		playerX.sendToClient(new BoardMessage(board, currentPlayer, 'X'));
	}

	/**
	 * Checks if there is a winner.
	 *
	 * @return True if there is a winner, false otherwise.
	 */
	private boolean checkWin() {
		for (int i = 0; i < 3; i++) {
			if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
				return true;
			}
		}
		for (int j = 0; j < 3; j++) {
			if (board[0][j] != ' ' && board[0][j] == board[1][j] && board[1][j] == board[2][j]) {
				return true;
			}
		}
		if (board[1][1] != ' ' && ((board[0][0] == board[1][1] && board[1][1] == board[2][2]) ||
				(board[2][0] == board[1][1] && board[1][1] == board[0][2]))) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if the game is a draw.
	 *
	 * @return True if the game is a draw, false otherwise.
	 */
	private boolean checkDraw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (board[i][j] == ' ') {
					return false;
				}
			}
		}
		return true;
	}
}
