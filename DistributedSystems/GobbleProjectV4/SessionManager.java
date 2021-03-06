import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * The Class SessionManager.
 */
public class SessionManager implements ViewListener {
	
	/** The sessions. */
	private ArrayList<Session> sessions = new ArrayList<Session>();
	private Integer sessionId = 0;
	/**
	 * Construct a new session manager.
	 */
	public SessionManager() { }


	/**
	 * Join an open session.
	 *
	 * @param proxy the proxy
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException {
		boolean joined = false;
		System.out.print(sessionId);
		for(int i=0; i<sessions.size(); i++) {
			Session session = sessions.get(i);

			if (session.getNumPlayers() < 2) {
				session.join(proxy, name,sessionId);
				joined = true;
			}

		}

		if (joined != true) {
			Session session = new Session();
			session.join(proxy, name,sessionId);
			sessions.add(session);
		}else{
			sessionId = sessionId + 1;
		}
	}

	/* (non-Javadoc)
	 * @see ViewListener#addMarker(int, int, int)
	 */
	public void addMarker(int r, int c, int player) {

	}

	/* (non-Javadoc)
	 * @see ViewListener#clearBoard()
	 */
	public void clearBoard() throws IOException { }
	
	/* (non-Javadoc)
	 * @see ViewListener#playerNumber(int)
	 */
	public void playerNumber(int player) throws IOException { 

	}
	
	/* (non-Javadoc)
	 * @see ViewListener#playerTurn(int)
	 */
	public void playerTurn(int player) throws IOException { 
		}
	
	/* (non-Javadoc)
	 * @see ViewListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException { }
	
	/* (non-Javadoc)
	 * @see ViewListener#close()
	 */

	public synchronized void close() throws IOException{
		
	}
	
	/* (non-Javadoc)
	 * @see ViewListener#addColor(int, int, java.awt.Color)
	 */
	public void addColor(int r, int c, Color color) throws IOException {}
	
	/* (non-Javadoc)
	 * @see ViewListener#sendWinner(java.lang.String)
	 */
	public void sendWinner(String winner, int player) throws IOException {}
}