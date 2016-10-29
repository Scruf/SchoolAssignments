import java.awt.Color;
import java.io.IOException;

/**
 * The listener interface for receiving view events.
 * The class that is interested in processing a view
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addViewListener<code> method. When
 * the view event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ViewEvent
 */
public interface ViewListener {

	/**
	 * Join the given session.
	 * 
	 * @param proxy
	 *            Reference to view proxy object.
	 * @param session
	 *            Session name.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void join(ViewProxy proxy, String session) throws IOException;

	/**
	 * Place a marker on the board.
	 *
	 * @param r            Row on which to place the marker.
	 * @param c            Column on which to place the marker.
	 * @param player the player
	 * @exception IOException                Thrown if an I/O error occurred.
	 */
	public void addMarker(int r, int c, int player) throws IOException;

	/**
	 * Report that the Connect board was cleared.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void clearBoard() throws IOException;

	/**
	 * Report that the current player is 1 or 2.
	 *
	 * @param player            The player number.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerNumber(int player) throws IOException;

	/**
	 * Report a certain player is player 1 or 2.
	 *
	 * @param player            The player ID.
	 * @param name            The name of the player.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerName(int player, String name) throws IOException;

	/**
	 * Report which player's turn it is.
	 *
	 * @param player            The player ID.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerTurn(int player) throws IOException;

	/**
	 * Report that a marker was placed on the board.
	 *
	 * @param r            Row for the cell.
	 * @param c            Column for the cell.
	 * @param color the color
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addColor(int r, int c, Color color) throws IOException;

	/**
	 * Report that the windows should be closed.
	 */
	public void close()  throws IOException;
	
	/**
	 * Sends winner's name.
	 *
	 * @param winner the winner's name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void sendWinner(String winner) throws IOException;


}