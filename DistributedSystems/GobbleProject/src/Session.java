import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Session.
 */
public class Session {
	
	/** The model. */
	private GobbleModel model;
	
	/** The number of players. */
	private int numPlayers = 0;


	/** The player1 name. */
	private String player1Name;
	
	/** The player2 name. */
	private String player2Name;
	
	private int sessionID;
	
	private GobbleBoard gb;
	
	private List<Session> sessions;
	
	public Session(int sessionID, List<Session> sessions) {
		this.sessionID = sessionID;
		this.sessions = sessions;
		model = new GobbleModel(sessions);
		gb = new GobbleBoard();
	}


	/**
	 * Joins the new player (client).
	 *
	 * @param proxy the proxy
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void join(ViewProxy proxy, String name) throws IOException {
		if (numPlayers == 0) {
			player1Name = name;
		} else {
			player2Name = name;
		}

		model.addModelListener(proxy);
		proxy.setViewListener(model);

		numPlayers++;

		proxy.playerNumber(numPlayers, sessionID);
		proxy.setGb(gb);

		if (numPlayers == 2) {
			model.playerName(1, player1Name);
			model.playerName(2, player2Name);
			model.playerTurn(1);
		}
	}

	/**
	 * Gets the number players.
	 *
	 * @return the number players
	 */
	public int getNumPlayers() {
		return this.numPlayers;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
}