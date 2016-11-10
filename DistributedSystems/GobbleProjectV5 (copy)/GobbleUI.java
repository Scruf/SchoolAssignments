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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

//******************************************************************************
//
// File:    GobbleUI.java
// Package: ---
// Unit:    Class GobbleUI
//
// This Java source file is copyright (C) 2015 Egor Kozitski. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ek5442@g.rit.edu
//
// This Java source file is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by the Free
// Software Foundation; either version 3 of the License, or (at your option) any
// later version.
//
// This Java source file is distributed in the hope that it will be useful, but
// WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// details.
//
// You may obtain a copy of the GNU General Public License on the World Wide Web
// at http://www.gnu.org/licenses/gpl.html.
//
//******************************************************************************
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
				if((r==3) && (c==0)){
					spot.setVisible(false);	
				}
				spot.setMinimumSize(new Dimension(rr, cc));
				spot.setEnabled (false);
				spot.setColor(Color.YELLOW);
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
		onSwingThreadDo(new Runnable(){
			public void run(){
				GobbleUI.this.viewListener = viewListener;
			}
		});
	}

		/**
	 * Marker added.
	 *
	 * @param r the row
	 * @param c the column
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
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
					checkWinner();
				} catch (Exception exc) {

				}
			}
		}
		// checkWinner();
		
		
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
		int enabled = 0;

		for (int i = 0; i < 4; i ++) { // To get round the spot button of player 1 to
			// define whether there is at least one enabled spot button.
			
			try {
				SpotButton sbp1 = spotButton[getRow(player1)+ROWS[i]][getColumn(player1) + COLS[i]];

				if (getSpotButtonColor(sbp1) == Color.YELLOW) {
					isFoodForPlayer1 = true;
					enabled++;
				}

			} catch (Exception exc) {

			}
		}
		System.out.printf("Number of enabled cells is-> %d\n",enabled);
		if(enabled==0){
			System.out.print("Player 1 has 0 enabled cells");
			disableButtons();
			return 2;
			// isFoodForPlayer1 = false;
		}
		else
			enabled = 0;
		for (int i = 0; i < 4; i ++) {// To get round the spot button of player 2 to
			// define whether there is at least one enabled spot button.
			
			try {
				SpotButton sbp2 = spotButton[getRow(player2) + 
				                             ROWS[i]][getColumn(player2) + COLS[i]];
				if (getSpotButtonColor(sbp2) == Color.YELLOW ) {
					isFoodForPlayer2 = true;
					enabled++;

				}
			} catch (Exception exc) {

			}
		}
		System.out.printf("Number of enabled cells is-> %d\n",enabled);
		if(enabled==0){
			isFoodForPlayer2 = false;
			System.out.print("Player 2 has 0 enabled cells");
			disableButtons();
			return 1;
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


	/**
	 * Reports that the board has been cleared.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
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

/**
	 * Report that the number of player has been sent.
	 *
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
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

	/**
	 * Report that the player number and name have been sent.
	 *
	 * @param player the player number
	 * @param name the name  of player
	 * @throws IOException Signals that an I/O exception has occurred.
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
			spotButton[3][0].setVisible(true);
			spotButton[3][0].setColor(Color.BLUE);
		}
	}
	

	/**
	 * Report that the turn has been sent.
	 *
	 * @param player the player's number
	 * @throws IOException Signals that an I/O exception has occurred.
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
				viewListener.sendWinner(playerName, 1);
			} else {
				messageField.setText(this.opponent + " won!");
			}

		} else if (win == 2) {
			if (this.player == 2) {
				messageField.setText("You won!");
				viewListener.sendWinner(playerName, 2);
			} else {
				messageField.setText(this.opponent + " won!");
			}
		} else if (win == 0) {
			messageField.setText("Draw!");
		}
		frame.repaint();
	}

	/**
	 * Closes game.
	 */
	@Override
	public void close(int sessionID) {
		// TODO Auto-generated method stub
		frame.dispose();
		System.exit(1);
	}

	/**
	 * Reports that the color is added to given button.
	 *
	 * @param r the row
	 * @param c the column
	 * @param color the color object to be added
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {
		spotButton[r][c].setColor(frame.getBackground());

		spotButton[r][c].repaint();
	}

		/**
	 * Reports that the winner's name has been sent.
	 *
	 * @param winner the winner
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void winnerSent(String winner, int player) throws IOException {
			if (player == this.player) {
				messageField.setText("You won!");
			} else {
				messageField.setText(winner + " won!");
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
	private static class GobbleUIRef{
		public GobbleUI ui;
	}
	public static GobbleUI create (final String name){
		final GobbleUIRef ref = new GobbleUIRef();
		onSwingThreadDo(new Runnable(){
			public void run(){
				ref.ui = new GobbleUI(name);
			}
		});
		return ref.ui;
	}
	private static void onSwingThreadDo(Runnable task){
		try{
			SwingUtilities.invokeAndWait(task);
		}catch(Throwable exc){
			exc.printStackTrace(System.err);
			System.exit(1);
		}

	}
	
}