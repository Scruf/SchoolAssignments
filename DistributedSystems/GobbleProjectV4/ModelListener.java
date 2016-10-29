import java.awt.Color;
import java.io.IOException;

/**
 * The listener interface for receiving model events.
 * The class that is interested in processing a model
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addModelListener<code> method. When
 * the model event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ModelEvent
 */
public interface ModelListener {


	/**
	 * Marker added.
	 *
	 * @param r the row
	 * @param c the column
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void markerAdded(int r, int c, int player) throws IOException;


	/**
	 * Reports that the board has been cleared.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void boardCleared() throws IOException;


	/**
	 * Report that the number of player has been sent.
	 *
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerNumber(int player) throws IOException;


	/**
	 * Report that the player number and name have been sent.
	 *
	 * @param player the player number
	 * @param name the name  of player
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerName(int player, String name) throws IOException;


	/**
	 * Report that the turn has been sent.
	 *
	 * @param player the player's number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerTurn(int player) throws IOException;


	/**
	 * Closes game.
	 */
	public void close() throws IOException;
	
	/**
	 * Reports that the color is added to given button.
	 *
	 * @param r the row
	 * @param c the column
	 * @param color the color object to be added
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void colorAdded(int r, int c, Color color) throws IOException;
	
	/**
	 * Reports that the winner's name has been sent.
	 *
	 * @param winner the winner
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void winnerSent(String winner) throws IOException;


}