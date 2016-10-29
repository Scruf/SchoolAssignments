import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GobbleUI implements ModelListener
{
	public static final int R = 4;
	public static final int C = 4;

	private final int ROWS[] = {-1, 0, 1, 0};
	private final int COLS[] = {0, 1, 0, -1};

	private JFrame frame;
	private SpotButton[][] spotButton;
	private JButton newGameButton;
	private JTextField messageField;

	private boolean waiting = true; // Added.
	private int player; // Added.
	private String opponent; // Added.
	private ViewListener viewListener; // Added.
	private GobbleBoardBehavior c4board; // Added.
	private SpotButton currentSpotButton;
	


	private static final int GAP = 10;
	private static final int W = 50;
	private static final int H = 50;
	private static final Dimension PD = new Dimension (C*W, R*H);

	/**
	 * Construct a new Gobble UI object.
	 *
	 * @param  name  Player's name.
	 */
	public GobbleUI
	(
			GobbleBoardBehavior board, 
			String name)
	{
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
				spot.setEnabled (false);
				spot.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {

						try {

							viewListener.addMarker(rr, cc, player);
							spotButton[currentSpotButton.getRow()][currentSpotButton.getColumn()].setColor(frame.getBackground());
							viewListener.addColor(currentSpotButton.getRow(), currentSpotButton.getColumn(), frame.getBackground());

							currentSpotButton.setRow(rr);
							currentSpotButton.setColumn(cc);
							//								spot.setColor(frame.getBackground());
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

		frame.pack();
		frame.setVisible (true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setViewListener(ViewListener viewListener) {
		this.viewListener = viewListener;
	}

	@Override
	public void markerAdded(int r, int c, int player) throws IOException {
		
		for (int a = 0; a < R; a ++) {
			for (int b = 0; b < C; b ++) {
				spotButton[a][b].setEnabled(false);

				if (spotButton[a][b].getColor() != Color.BLUE && 
						spotButton[a][b].getColor() != Color.RED && 
						spotButton[a][b].getColor() != frame.getBackground()) {
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
						SpotButton sb = spotButton[currentSpotButton.getRow() + ROWS[i]][currentSpotButton.getColumn() + COLS[i]];
						if (sb.getColor() == Color.YELLOW) {
							sb.setEnabled(true);
						}
						
					} catch (Exception exc) {

					}
				}
				
			}

	}

	@Override
	public void boardCleared() throws IOException {
		
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
			currentSpotButton.setRow(0);
			currentSpotButton.setColumn(3);
		} else {
			currentSpotButton = spotButton[3][0];
			currentSpotButton.setColor(Color.BLUE);
			currentSpotButton.setRow(3);
			currentSpotButton.setColumn(0);
		}

	}

	@Override
	public void playerNumber(int player) throws IOException {
		
		this.player = player;
		if (player == 1) {
			currentSpotButton = spotButton[0][3];
			currentSpotButton.setColor(Color.RED);
			currentSpotButton.setRow(0);
			currentSpotButton.setColumn(3);
		} else {
			currentSpotButton = spotButton[3][0];
			currentSpotButton.setColor(Color.BLUE);
			currentSpotButton.setRow(3);
			currentSpotButton.setColumn(0);
		}
	}

	@Override
	public void playerName(int player, String name) throws IOException {
		
		if (player != this.player) {
			this.opponent = name;
			newGameButton.setEnabled(true);
			boardCleared();
		}
		if (player == 1) {
			waiting = false;
		}
	}

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

	@Override
	public void close() {
		
		frame.dispose();
		System.exit(1);
	}

	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {
		spotButton[r][c].setColor(frame.getBackground());
		
		spotButton[r][c].repaint();
	}

}
