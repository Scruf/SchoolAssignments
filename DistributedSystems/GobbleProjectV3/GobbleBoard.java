public class GobbleBoard implements GobbleBoardBehavior {
	
	private final int ROWSA[] = {-1, 0, 1, 0};
	private final int COLSA[] = {0, 1, 0, -1};

	/**
	 * Constructs a new board and fills the cells with a player ID of
	 * -1 (empty)
	 */
	public GobbleBoard() {

	}

	/**
	 * Sets the cell's value to a player ID.
	 * 
	 * @param r
	 *            Row of the cell.
	 * @param c
	 *            Column of the cell.
	 * @param player
	 *            Player ID.
	 */
	public void setMarker(int r, int c, int player) {
		board[r][c] = player;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see GobbleBoardBehavior#getPlayer(int, int)
	 */
	public int getPlayer(int r, int c) {
		return board[r][c];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GobbleBoardBehavior#hasPlayer1Marker(int, int)
	 */
	public boolean hasPlayer1Marker(int r, int c) {
		// TODO Auto-generated method stub
		return board[r][c] == 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GobbleBoardBehavior#hasPlayer2Marker(int, int)
	 */
	public boolean hasPlayer2Marker(int r, int c) {
		// TODO Auto-generated method stub
		return board[r][c] == 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GobbleBoardBehavior#hasWon()
	 */
	@Override
	public boolean hasWon() {
		int count, r1, c1, r2, c2;
		boolean win = false;

		count = r1 = c1 = r2 = c2 = 0;

		winCheck: for (int player = 1; player < 3; player++) {
			// Check for horizontal
			for (int r = 0; r < ROWS; r++) {
				count = 0;
				for (int c = 0; c < COLS; c++) {
					if (getPlayer(r, c) == player) {
						count++;

						if (count == 1) {
							r1 = r;
							c1 = c;
						} else if (count == 4) {
							r2 = r;
							c2 = c;
							win = true;
							break winCheck;
						}
					} else {
						count = 0;
						c1 = r1 = 0;
					}
				}
			}

			
		

			
		}

		if (win) {
			int[] result = { r1, c1, r2, c2 };
			return true;
		} else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see GobbleBoardBehavior#clearBoard()
	 */
	public void clearBoard() {

	}

}
