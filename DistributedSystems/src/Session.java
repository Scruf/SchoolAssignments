import java.io.IOException;

public class Session {
	private GobbleModel model = new GobbleModel();
	private int numPlayers = 0;

	private ViewProxy player1Proxy;
	private ViewProxy player2Proxy;

	private String player1Name;
	private String player2Name;

	private int turn = 1;

	public void join(ViewProxy proxy, String name) throws IOException {
		if (numPlayers == 0) {
			player1Name = name;
			player1Proxy = proxy;
		} else {
			player2Name = name;
			player2Proxy = proxy;
		}

		model.addModelListener(proxy);
		proxy.setViewListener(model);

		numPlayers++;

		proxy.playerNumber(numPlayers);

		if (numPlayers == 2) {
			model.playerName(1, player1Name);
			model.playerName(2, player2Name);
			model.playerTurn(1);
		}
	}

	public int getNumPlayers() {
		return this.numPlayers;
	}
}