
public interface GobbleBoardBehavior {

	/**
	 * Number of rows.
	 */
	public static final int ROWS = 4;

	/**
	 * Number of columns.
	 */
	public static final int COLS = 4;

	/**
	 * 2D integer array of integers to represent the board.
	 */
	public static Integer[][] board = new Integer[ROWS][COLS];

	/**
	 * Removes all player IDs (set to -1) from the board.
	 */
	public void clearBoard();

	/**
	 * Get the player of the cell. -1 means empty.
	 * 
	 * @param r
	 *            Row of the cell
	 * @param c
	 *            Column of the cell.
	 * @return Returns integer of player ID.
	 */
	public int getPlayer(int r, int c);

	/**
	 * Determine if the given row and column contains player 1's
	 * marker.
	 * 
	 * @param r
	 *            Row.
	 * @param c
	 *            Column.
	 * 
	 * @return True if (r, c) contains player 1's marker, false
	 *         otherwise.
	 */
	public boolean hasPlayer1Marker(int r, int c);

	/**
	 * Determine if the given row and column contains player 2's
	 * marker.
	 * 
	 * @param r
	 *            Row.
	 * @param c
	 *            Column.
	 * 
	 * @return True if (r, c) contains player 2's marker, false
	 *         otherwise.
	 */
	public boolean hasPlayer2Marker(int r, int c);

	public void setMarker(int r, int c, int player);


	public boolean hasWon();

}
