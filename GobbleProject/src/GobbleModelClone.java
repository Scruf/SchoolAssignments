import java.awt.Color;
import java.io.IOException;

/**
 * The Class GobbleModelClone.
 */
public class GobbleModelClone implements ModelListener {


	/** The model listener. */
	private ModelListener modelListener;

	/**
	 * Instantiates a new gobble model clone.
	 */
	public GobbleModelClone() {
	}


	/**
	 * Sets the model listener.
	 *
	 * @param modelListener the new model listener
	 */
	public void setModelListener(ModelListener modelListener) {
		this.modelListener = modelListener;
	}


	/* (non-Javadoc)
	 * @see ModelListener#markerAdded(int, int, int)
	 */
	public void markerAdded(int r, int c, int player) throws IOException {
		modelListener.markerAdded(r, c, player);
	}


	/* (non-Javadoc)
	 * @see ModelListener#boardCleared()
	 */
	public void boardCleared() throws IOException {
		modelListener.boardCleared();
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerNumber(int)
	 */
	public void playerNumber(int player) throws IOException {
		modelListener.playerNumber(player);
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException {
		modelListener.playerName(player, name);
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerTurn(int)
	 */
	public void playerTurn(int player) throws IOException {
		modelListener.playerTurn(player);
	}


	/* (non-Javadoc)
	 * @see ModelListener#close()
	 */
	public void close() throws IOException {
		modelListener.close();
	}

	/* (non-Javadoc)
	 * @see ModelListener#colorAdded(int, int, java.awt.Color)
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {

		modelListener.colorAdded(r, c, color);
	}


	/* (non-Javadoc)
	 * @see ModelListener#winnerSent(java.lang.String)
	 */
	@Override
	public void winnerSent(String winner, int player) throws IOException {
		modelListener.winnerSent(winner, player);
		
	}

}