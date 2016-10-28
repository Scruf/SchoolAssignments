import java.awt.Color;
import java.io.IOException;
public interface ModelListener {

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
	public void markerAdded(int r, int c, int player) throws IOException;

	/**
	 * Report that the Connect Four board was cleared.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void boardCleared() throws IOException;

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
	 * Report that the windows should be closed.
	 */
	public void close();
	
	public void colorAdded(int r, int c, Color color) throws IOException;

}