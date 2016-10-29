import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The Class GobbleUI.
 */
public class GobbleUI implements ModelListener
{
	
	/** The Constant rows. */
	public static final int R = 4;
	
	/** The Constant columns. */
	public static final int C = 4;

	/** This array stores row-coordinates which should be
	 * added to current row in a loop to get round the {@link SpotButton} to
	 * detect all enabled other spot buttons. */
	private final int ROWS[] = {-1, 0, 1, 0};
	
	/** This array stores column-coordinates which should be
	 * added to current column in a loop to get round the {@link SpotButton} to
	 * detect all enabled other spot buttons. */
	private final int COLS[] = {0, 1, 0, -1};

	/** The reference to {@link JFrame} */
	private JFrame frame;
	
	/** The array of {@link SpotButton} objects which are used
	 * in the game*/
	private SpotButton[][] spotButton;
	
	/** The new game button. */
	private JButton newGameButton;
	
	/** The message field. */
	private JTextField messageField;

	/** Stores true if this client is waiting for the opponent's move */
	private boolean waiting = true; // Indicates whether this client is waiting for the opponent's move.
	
	/** The player number. */
	private int player; // 1 or 2 player.
	
	/** The opponent's name. */
	private String opponent; // Opponent's name.
	
	/** The view listener. */
	private ViewListener viewListener; // Added.
	
	/** The current player's name. */
	private String playerName;

	/** The current spot button. */
	private SpotButton currentSpotButton;

	/** The Constant GAP. */
	private static final int GAP = 10;
	
	/** The Constant W. */
	private static final int W = 50;
	
	/** The Constant H. */
	private static final int H = 50;
	
	/** The Constant PD. */
	private static final Dimension PD = new Dimension (C*W, R*H);
	
	

	/**
	 * Construct a new Gobble UI object.
	 *
	 * @param name the name
	 */
	public GobbleUI
	(
			String name)
	{

		this.playerName = name;
		frame = new JFrame ("Gobble -- " + name);
		JPanel p1 = new JPanel();
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		frame.add (p1);

		spotButton = new SpotButton [R] [C];
		JPanel p2 = new JPanel();
		p2.setMaximumSize (PD);
		p2.setMinimumSize (PD);
		p2.setPreferredSize (PD);
		p2.setLayout (new GridLayout (R, C));
		for (int r = 0; r < R; ++ r)
			for (int c = 0; c < C; ++ c)
			{
				final int rr = r;
				final int cc = c;
				SpotButton spot = spotButton[r][c] = new SpotButton();
				spot.setMinimumSize(new Dimension(rr, cc));
				spot.setEnabled (false);
				spot.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						try {

							viewListener.addMarker(rr, cc, player);
							spotButton[getRow(currentSpotButton)][getColumn(currentSpotButton)].setColor(frame.getBackground());
							viewListener.addColor(getRow(currentSpotButton), getColumn(currentSpotButton), frame.getBackground());

							currentSpotButton.setMinimumSize(new Dimension(rr, cc));
							checkWinner();
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
				p2.add (spot);
			}
		p1.add (p2);

		newGameButton = new JButton ("New Game");
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setEnabled (false);
		newGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					viewListener.clearBoard();
				} catch (IOException exc) {
				}
			}
		});
		p1.add (Box.createVerticalStrut (GAP));
		p1.add (newGameButton);

		messageField = new JTextField("Waiting for partner");

		messageField.setEditable (false);
		p1.add (Box.createVerticalStrut (GAP));
		p1.add (messageField);
		
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				try {
					viewListener.close();
				} catch (IOException e) {
					
				}
			}
		});

		frame.pack();
		frame.setVisible (true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Sets the view listener.
	 *
	 * @param viewListener the new view listener
	 */
	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	/* (non-Javadoc)
	 * @see ModelListener#markerAdded(int, int, int)
	 */
	@Override
	public void markerAdded(int r, int c, int player) throws IOException {

		for (int a = 0; a < R; a ++) {
			for (int b = 0; b < C; b ++) {
				spotButton[a][b].setEnabled(false);

				Color color = getSpotButtonColor(spotButton[a][b]);
				if (color != Color.BLUE && 
						color != Color.RED && 
								color != frame.getBackground()) {
					spotButton[a][b].setColor(Color.YELLOW);
				}
				
				spotButton[a][b].repaint();

			}
		}

		if (player == 1) {
			spotButton[r][c].setColor(Color.RED);

		} else {
			spotButton[r][c].setColor(Color.BLUE);

		}

		if (waiting) {
			for (int i = 0; i < 4; i ++) {
				try {
					SpotButton sb = spotButton[getRow(currentSpotButton) + 
					                           ROWS[i]][getColumn(currentSpotButton) + 
					                                    COLS[i]];
					Field f = sb.getClass().getDeclaredField("color");
					f.setAccessible(true);
					Color color = (Color) f.get(sb);
					if (color == Color.YELLOW) {
						sb.setEnabled(true);
					}

				} catch (Exception exc) {

				}
			}
		}

		checkWinner();
		
	}
	
	private Color getSpotButtonColor(SpotButton sb) {
		try {
		Field f = sb.getClass().getDeclaredField("color");
		f.setAccessible(true);
		return (Color) f.get(sb);
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException exc) {}
		return null;
	}
	
	private int getRow(SpotButton sb) {
		
		return sb.getMinimumSize().width;
	}
	
	private int getColumn(SpotButton sb) {
		return sb.getMinimumSize().height;
	}

	/**
	 * Checks winner.
	 *
	 * @return 1 if the player 1 is winner,
	 *         <br>2 if player 2 is winner,
	 *         <br>0 if draw
	 *         <br>-1 if no winners now 
	 */
	private int checkWinner() {

		/*
		 * Get current spot buttons for 2 players.
		 */
		SpotButton player1 = new SpotButton();
		SpotButton player2 = new SpotButton();
		for (int a = 0; a < R; a ++) {
			for (int b = 0; b < C; b ++) {
				Color color = getSpotButtonColor(spotButton[a][b]);
				if (color == Color.RED) {

					player1.setMinimumSize(new Dimension(a, b));

				}
				if (color == Color.BLUE) {

					player2.setMinimumSize(new Dimension(a, b));

				}
			}
		}		

		boolean isFoodForPlayer1 = false;
		boolean isFoodForPlayer2 = false;
		for (int i = 0; i < 4; i ++) { // To get round the spot button of player 1 to
			// define whether there is at least one enabled spot button.
			
			try {
				SpotButton sbp1 = spotButton[getRow(player1) + 
				                             ROWS[i]][getColumn(player1) + COLS[i]];

				if (getSpotButtonColor(sbp1) == Color.YELLOW) {
					isFoodForPlayer1 = true;
					break;
				}

			} catch (Exception exc) {

			}
		}

		for (int i = 0; i < 4; i ++) {// To get round the spot button of player 2 to
			// define whether there is at least one enabled spot button.
			
			try {
				SpotButton sbp2 = spotButton[getRow(player2) + 
				                             ROWS[i]][getColumn(player2) + COLS[i]];
				if (getSpotButtonColor(sbp2) == Color.YELLOW) {
					isFoodForPlayer2 = true;
					break;
				}
			} catch (Exception exc) {

			}
		}


		if (isFoodForPlayer1 && !isFoodForPlayer2 && player == 1 && !waiting) { // If no enabled buttons around the player 2.
			disableButtons(); // Disable all buttons.
			return 1;
		} else if (!isFoodForPlayer1 && isFoodForPlayer2 && player == 2 && !waiting) { // If no enabled buttons around the player 1.
			disableButtons();
			return 2;
		} else if (!isFoodForPlayer1 && !isFoodForPlayer2) { // If no enabled buttons around the both of players.
		return 0;
		} else {  // If there are enabled buttons for both of players.
			return -1;
		}

	}
	
	/**
	 * Disables buttons.
	 */
	private void disableButtons() {
		for (int a = 0; a < R; a ++) {
			for (int b = 0; b < C; b ++) {
				spotButton[a][b].setEnabled(false);
			}
		}
	}

	/* (non-Javadoc)
	 * @see ModelListener#boardCleared()
	 */
	@Override
	public void boardCleared() throws IOException {
		// TODO Auto-generated method stub
		for (int a = 0; a < R; a ++) {
			for (int b = 0; b < C; b ++) {
				spotButton[a][b].setEnabled(false);
				spotButton[a][b].setColor(Color.YELLOW);
			}
		}

		spotButton[3][0].setColor(Color.BLUE);
		spotButton[0][3].setColor(Color.RED);


		if (player == 1) {
			spotButton[0][2].setEnabled(true);
			spotButton[1][3].setEnabled(true);
			currentSpotButton = spotButton[0][3];
			currentSpotButton.setColor(Color.RED);

			currentSpotButton.setMinimumSize(new Dimension(0, 3));
		} else {
			currentSpotButton = spotButton[3][0];
			currentSpotButton.setColor(Color.BLUE);

			currentSpotButton.setMinimumSize(new Dimension(3, 0));
		}

	}

	/* (non-Javadoc)
	 * @see ModelListener#playerNumber(int)
	 */
	@Override
	public void playerNumber(int player) throws IOException {
		// TODO Auto-generated method stub
		this.player = player;
		if (player == 1) {
			currentSpotButton = spotButton[0][3];
			currentSpotButton.setColor(Color.RED);

			currentSpotButton.setMinimumSize(new Dimension(0, 3));
		} else {
			currentSpotButton = spotButton[3][0];
			currentSpotButton.setColor(Color.BLUE);

			currentSpotButton.setMinimumSize(new Dimension(3, 0));
		}
	}

	/* (non-Javadoc)
	 * @see ModelListener#playerName(int, java.lang.String)
	 */
	@Override
	public void playerName(int player, String name) throws IOException {
		// TODO Auto-generated method stub
		if (player != this.player) {
			this.opponent = name;
			newGameButton.setEnabled(true);
			boardCleared();
		}
		if (player == 1) {
			waiting = false;
		}
	}
	
	/**
	 * Are all buttons disabled.
	 *
	 * @return true, if successful
	 */
	private boolean areAllButtonsDisabled() {
		for (int r = 0; r < R; r ++) {
			for (int c = 0; c < C; c ++) {
				if (spotButton[r][c].isEnabled()) {
					return true;
				}
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see ModelListener#playerTurn(int)
	 */
	@Override
	public void playerTurn(int player) throws IOException {

		int win = checkWinner();
		if (player == this.player) {
			this.waiting = false;
			messageField.setText("Your turn");			

		} else if (player == 0) {
			this.waiting = true;
			messageField.setText("Game over");
		} else {
			this.waiting = true;
			messageField.setText(this.opponent + "'s turn");			
		}

		if (win == 1) {
			if (this.player == 1) {
				messageField.setText("You won!");
				viewListener.sendWinner(playerName);
			} else {
				messageField.setText(this.opponent + " won!");
			}

		} else if (win == 2) {
			if (this.player == 2) {
				messageField.setText("You won!");
				viewListener.sendWinner(playerName);
			} else {
				messageField.setText(this.opponent + " won!");
			}
		} else if (win == 0) {
			messageField.setText("Draw!");
		}
		frame.repaint();
	}

	/* (non-Javadoc)
	 * @see ModelListener#close()
	 */
	@Override
	public void close() {
		// TODO Auto-generated method stub
		frame.dispose();
		System.exit(1);
	}

	/* (non-Javadoc)
	 * @see ModelListener#colorAdded(int, int, java.awt.Color)
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {
		spotButton[r][c].setColor(frame.getBackground());

		spotButton[r][c].repaint();
	}

	/* (non-Javadoc)
	 * @see ModelListener#winnerSent(java.lang.String)
	 */
	@Override
	public void winnerSent(String winner) throws IOException {
		if (winner.equals(playerName)) {
			messageField.setText("You won!");
		} else {
			messageField.setText(winner + " won!");
		}
		
	}

}
