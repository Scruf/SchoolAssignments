import java.awt.Color;

/**
 * 
 * This class represents a board
 * for two players in the one session.
 *
 */
public class GobbleBoard {

	private Color [][] colors = new Color[4][4]; // Array of all the colors in the board.
	private int curRred = 0, curCred = 3, curRblue = 3, curCblue = 0; // Stores
	// current rows and columns for read (player 1) and blue (player 2).
	
	/** This array stores row-coordinates which should be
	 * added to current row in a loop to get round the {@link SpotButton} to
	 * detect all enabled other spot buttons. */
	private final int ROWS[] = {-1, 0, 1, 0};
	
	/** This array stores column-coordinates which should be
	 * added to current column in a loop to get round the {@link SpotButton} to
	 * detect all enabled other spot buttons. */
	private final int COLS[] = {0, 1, 0, -1};

	/**
	 * Initialize a new board with 1 read, 1 blue and yellow 
	 * color for the rest.
	 */
	public GobbleBoard() {
		clearBoard();
	}

	/**
	 * Method to change the current state of the board.
	 * @param r
	 *        row to add color
	 * @param c
	 *        column to add color
	 * @param color
	 *        color to be added
	 */
	public void doMove(int r, int c, Color color) {
		if (color == Color.RED) {
			colors[curRred][curCred] = Color.GRAY;
			curRred = r;
			curCred = c;
		} else {
			colors[curRblue][curCblue] = Color.GRAY;
			curRblue = r;
			curCblue = c;
		}
		colors[r][c] = color;
		
		for (int a = 0; a < 4; a ++) {
			for (int b = 0; b < 4; b ++) {
				if (colors[a][b] == Color.YELLOW) {
					System.out.print("Y ");
				} else if (colors[a][b] == Color.GRAY) {
					System.out.print("g ");
				} else if (colors[a][b] == Color.RED) {
					System.out.print("R ");
				} else if (colors[a][b] == Color.BLUE) {
					System.out.print("B ");
				}
			}System.out.println();
		}
		System.out.println();

	}

	/**
	 * This method calculates whether there is 
	 * a winner in the current state of this board.
	 * @return
	 *        1 if the winner is player1,
	 *        2 if the winner is player2,
	 *        0 if draw,
	 *        -1 if there are no winners at this time
	 */
	public int getWinner() {
		boolean isFoodForRed = false;
		boolean isFoodForBlue = false;
		for (int i = 0; i < 4; i ++) {
			try {
				if (colors[curRred + ROWS[i]][curCred + COLS[i]] == Color.YELLOW) {
					isFoodForRed = true;
				}
			} catch (Exception exc){}
			try {
				if (colors[curRblue + ROWS[i]][curCblue + COLS[i]] == Color.YELLOW) {
					isFoodForBlue = true;
				}		
			} catch (Exception exc){}
		}

		if (isFoodForRed && !isFoodForBlue) {
			return 1;
		} else if (!isFoodForRed && isFoodForBlue) {
			return 2;
		} else if (!isFoodForBlue && !isFoodForRed) {
			return 0;
		} else {
			return -1;
		}

	}
	
	/**
	 * Gets back the initial state to this board.
	 */
	public void clearBoard() {
		for (int r = 0; r < colors.length; r ++) {
			for (int c = 0; c < colors.length; c ++) {
				colors[r][c] = Color.YELLOW;
			}
		}

		colors [0][3] = Color.RED;
		colors [3][0] = Color.BLUE;
		curRred = 0; 
		curCred = 3;
		curRblue = 3;
		curCblue = 0;
	}

}
