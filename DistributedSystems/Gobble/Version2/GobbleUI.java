import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.io.IOException;
import javax.swing.SwingUtilities;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/*
 * Class GobbleUI provides the view object for the network Gobble game.
 *
 * @author  Alan Kaminsky
 * @version 10-Oct-2016
 */
public class GobbleUI implements ModelListener
	{
	public static final int R = 4;
	public static final int C = 4;

	private JFrame frame;
	private SpotButton[][] spotButton;
	private JButton newGameButton;
	private JTextField messageField;

	private static final int GAP = 10;
	private static final int W = 50;
	private static final int H = 50;
	private static final Dimension PD = new Dimension (C*W, R*H);

	/**
	 * Construct a new Gobble UI object.
	 *
	 * @param  name  Player's name.
	 */public static final int ROWS = 4;
	private GobbleBoard board;
	private GobbleModel boardPanel;
	private GobbleModel model;
public static final int COLS = 4;

	private GobbleUI
		(String name, GobbleBoard board)
		{
		frame = new JFrame ("Gobble -- " + name);
		JPanel p1 = new JPanel();
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (GAP, GAP, GAP, GAP));
		frame.add (p1);
		
		this.board = board;
		boardPanel = new GobbleModel(board);
		boardPanel.setAlignmentX(0.5f);
	
		p1.add(boardPanel);
		spotButton = new SpotButton [R] [C];
		JPanel p2 = new JPanel();
		p2.setMaximumSize (PD);
		p2.setMinimumSize (PD);
		p2.setPreferredSize (PD);
		p2.setLayout (new GridLayout (R, C));
		for (int r = 0; r < R; ++ r)
			for (int c = 0; c < C; ++ c)
				{
				SpotButton spot = spotButton[r][c] = new SpotButton();
					spot.setEnabled (false);

					boardPanel.add(spot);
					p2.add (spot);
				}
		p1.add (p2);

		newGameButton = new JButton ("New Game");
		newGameButton.setAlignmentX (0.5f);
		newGameButton.setEnabled (false);
		p1.add (Box.createVerticalStrut (GAP));
		p1.add (newGameButton);

		messageField = new JTextField();
		messageField.setEditable (false);
		p1.add (Box.createVerticalStrut (GAP));
		p1.add (messageField);
	
		frame.pack();
		frame.setVisible (true);
		boardPanel.addMouseListener(new MouseAdapter(){
			public void mouseClicked(MouseEvent e){
				doMouseClick(e);
			}
		});
		}

		private static class GobbleUIRef{
			public GobbleUI ui;
		}


		public static GobbleUI create(String name, GobbleBoard board){
			final GobbleUIRef ref =new GobbleUIRef();
			onSwingThreadDo(new Runnable(){
				public void run(){
					ref.ui = new GobbleUI(name, board);

				}
			});
			return ref.ui;
		}
		public void doMouseClick(MouseEvent e){
			switch (e.getButton()){
				case MouseEvent.BUTTON1:
					boardPanel.clickToRow(e);
					boardPanel.clickToColum(e);


				break;
				default:
				break;
			}
		}
		private static void onSwingThreadDo(Runnable task){
			try{
				SwingUtilities.invokeAndWait(task);
			}catch(Exception ex){
				ex.printStackTrace(System.err);
					
			}
		}
		public void markAdded(int x,int y,Color color){
			boardPanel.repaint();
		}
		

	}