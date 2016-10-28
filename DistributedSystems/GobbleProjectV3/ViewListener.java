import java.awt.Color;
import java.io.IOException;

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
	 * Place a marker on the Go board.
	 * 
	 * @param r
	 *            Row on which to place the marker.
	 * @param c
	 *            Column on which to place the marker.
	 * @param color
	 *            Marker color.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void addMarker(int r, int c, int player) throws IOException;

	/**
	 * Report that the Connect Four board was cleared.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void clearBoard() throws IOException;

	/**
	 * Report that the current player is 1 or 2.
	 * 
	 * @param player
	 *            The player number.
	 */
	public void playerNumber(int player) throws IOException;

	/**
	 * Report a certain player is player 1 or 2.
	 * 
	 * @param player
	 *            The player ID.
	 * @param name
	 *            The name of the player.
	 */
	public void playerName(int player, String name) throws IOException;

	/**
	 * Report which player's turn it is.
	 * 
	 * @param player
	 *            The player ID.
	 */
	public void playerTurn(int player) throws IOException;

	/**
	 * Report that a marker was placed on the Connect Four board.
	 * 
	 * @param r
	 *            Row for the cell.
	 * @param c
	 *            Column for the cell.
	 * @param player
	 *            Player ID to assign to that cell.
	 */
	public void addColor(int r, int c, Color color) throws IOException;

	/**
	 * Report that the windows should be closed.
	 */
	public void close();

}