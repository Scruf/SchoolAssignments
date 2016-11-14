import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.lang.reflect.Field;
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

	private int sessionID;



	/**
	 * Construct a new Gobble UI object.
	 *
	 * @param name the name
	 */
	private GobbleUI
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
					viewListener.close(sessionID);
				} catch (IOException e) {

				}
			}
		});

		frame.pack();
		frame.setVisible (true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public GobbleUI(String playerName, boolean b){
		this(playerName);
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


		} else if (player == 2) {
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
	public void playerNumber(int player, int sessionID) throws IOException {
		// TODO Auto-generated method stub
		this.player = player;
		this.sessionID = sessionID;
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


	/* (non-Javadoc)
	 * @see ModelListener#playerTurn(int)
	 */
	@Override
	public void playerTurn(int player) throws IOException {

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

		frame.repaint();
	}


	/* (non-Javadoc)
	 * @see ModelListener#close()
	 */
	@Override
	public void close(int sessionID) {
		// TODO Auto-generated method stub
		frame.dispose();
		System.exit(0);
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
	public void winnerSent(String winner, int player) throws IOException {
		
		if (player == 1) {
			if (this.player == player) {
				messageField.setText("You won!");
			} else {
				messageField.setText(opponent + " won!");
			}
			disableButtons();
		} else if (player == 2) {
			if (this.player == player) {
				messageField.setText("You won!");
			} else {
				messageField.setText(opponent + " won!");
			}
			disableButtons();
		} else if (player == 0) {
			messageField.setText("Draw!");
			disableButtons();
		}

	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

}
