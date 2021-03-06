import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

public class GobbleModel implements ViewListener {
//	private GobbleBoard board = new GobbleBoard();
	private LinkedList<ModelListener> listeners = new LinkedList<ModelListener>();

	private int turn = -1;

	/**
	 * Construct a new Go model.
	 */
	public GobbleModel() { }

	/**
	 * Add the given model listener to this Go model.
	 *
	 * @param  modelListener  Model listener.
	 */
	public synchronized void addModelListener(ModelListener modelListener) {

			// Record listener.
			listeners.add(modelListener);

	}

	/**
	 * Join the given session.
	 *
	 * @param  proxy    Reference to view proxy object.
	 * @param  session  Session name.
	 */
	public void join(ViewProxy proxy, String session) { }

	public synchronized void addMarker(int r, int c, int player) throws IOException {

			// Report update to all clients.
			Iterator<ModelListener> iter = listeners.iterator();
			while (iter.hasNext()) {
				ModelListener listener = iter.next();
				try {
					listener.markerAdded(r, c, player);
				} catch (IOException exc) {
					// Client failed, stop reporting to it.
					iter.remove();
				}
			}

			playerTurn(turn);
//		}
	}

	public synchronized void clearBoard() throws IOException {
		// Update board state.


		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.boardCleared();
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}

		playerTurn(1);
	}

	public synchronized void playerNumber(int player) {
		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerNumber(player);
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}
	}

	public synchronized void playerName(int player, String name) {
		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerName(player, name);
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}
	}

	public synchronized void playerTurn(int player) {
		if (turn == -1) {
			turn = player;
		}

		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerTurn(player);
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}

		turn = (player == 1) ? 2 : 1;
	}

	public void close() { }

	@Override
	public void addColor(int r, int c, Color color) throws IOException {
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.colorAdded(r, c, color);
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}
		
	}
}