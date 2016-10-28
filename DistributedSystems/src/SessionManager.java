import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

public class SessionManager implements ViewListener {
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private int players = 0;
	private ModelListener modelListener;

	/**
	 * Construct a new session manager.
	 */
	public SessionManager() { }

	/**
	 * Join an open session
	 *
	 * @param  proxy    Reference to view proxy object.
	 * @param  session  Player name.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException {
		boolean joined = false;

		for(int i=0; i<sessions.size(); i++) {
			Session session = sessions.get(i);

			if (session.getNumPlayers() < 2) {
				session.join(proxy, name);
				joined = true;
			}
		}

		// If no open games are found, create a new one
		if (joined != true) {
			Session session = new Session();
			session.join(proxy, name);
			sessions.add(session);
		}
	}

	public void addMarker(int r, int c, int player) {

	}

	public void clearBoard() throws IOException { }
	public void playerNumber(int player) throws IOException { }
	public void playerTurn(int player) throws IOException { }
	public void playerName(int player, String name) throws IOException { }
	public void close() { }
	public void addColor(int r, int c, Color color) throws IOException {}
}