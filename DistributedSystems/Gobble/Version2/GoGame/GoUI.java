//******************************************************************************
//
// File:    GoUI.java
// Package: ---
// Unit:    Class GoUI
//
// This Java source file is copyright (C) 2016 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
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

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

/**
 * Class GoUI provides the user interface for the network game of Go.
 *
 * @author  Alan Kaminsky
 * @version 05-Jan-2016
 */
public class GoUI
	implements ModelListener
	{

// Hidden data members.

	private JFrame frame;
	private GoBoardPanel boardPanel;
	private JPanel swatch;
	private JButton clearButton;

	private Color color = Color.BLACK;

	private ViewListener viewListener;

// Hidden constructors.

	/**
	 * Construct a new Go UI.
	 *
	 * @param  session  Session name.
	 * @param  board    Go board.
	 */
	private GoUI
		(String session,
		 GoBoard board)
		{
		// Set up window.
		frame = new JFrame ("Go -- "+session);
		Container pane = frame.getContentPane();
		JPanel p1 = new JPanel();
		pane.add (p1);
		p1.setLayout (new BoxLayout (p1, BoxLayout.Y_AXIS));
		p1.setBorder (BorderFactory.createEmptyBorder (10, 10, 10, 10));

		// Create and add widgets.
		boardPanel = new GoBoardPanel (board);
		boardPanel.setAlignmentX (0.5f);
		boardPanel.setBorder
			(BorderFactory.createBevelBorder (BevelBorder.RAISED));
		p1.add (boardPanel);
		p1.add (Box.createVerticalStrut (10));
		JPanel p2 = new JPanel();
		p2.setLayout (new BoxLayout (p2, BoxLayout.X_AXIS));
		p2.setAlignmentX (0.5f);
		p1.add (p2);
		swatch = new JPanel();
		Dimension dim = new Dimension (125, 25);
		swatch.setMinimumSize (dim);
		swatch.setPreferredSize (dim);
		swatch.setMaximumSize (dim);
		swatch.setBackground (color);
		swatch.setBorder (BorderFactory.createLineBorder (Color.BLACK));
		swatch.setAlignmentY (0.5f);
		p2.add (swatch);
		p2.add (Box.createHorizontalStrut (10));
		clearButton = new JButton ("Clear Board");
		clearButton.setAlignmentY (0.5f);
		p2.add (clearButton);

		// Clicking the color swatch pops up a color chooser dialog.
		swatch.addMouseListener (new MouseAdapter()
			{
			public void mouseClicked (MouseEvent e)
				{
				doChooseColor();
				}
			});

		// Clicking the Go board panel informs the view listener.
		boardPanel.addMouseListener (new MouseAdapter()
			{
			public void mouseClicked (MouseEvent e)
				{
				doMouseClick (e);
				}
			});

		// Clicking the Clear Board button informs the view listener.
		clearButton.addActionListener (new ActionListener()
			{
			public void actionPerformed (ActionEvent e)
				{
				doClearBoard();
				}
			});

		// Closing the window exits the client.
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);

		// Display window.
		frame.pack();
		frame.setVisible (true);
		}

// Exported operations.

	/**
	 * An object holding a reference to a Go UI.
	 */
	private static class GoUIRef
		{
		public GoUI ui;
		}

	/**
	 * Construct a new Go UI.
	 *
	 * @param  session  Session name.
	 * @param  board    Go board.
	 */
	public static GoUI create
		(final String session,
		 final GoBoard board)
		{
		final GoUIRef ref = new GoUIRef();
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				ref.ui = new GoUI (session, board);
				}
			});
		return ref.ui;
		}

	/**
	 * Set the view listener for this Go UI.
	 *
	 * @param  viewListener  View listener.
	 */
	public void setViewListener
		(final ViewListener viewListener)
		{
		onSwingThreadDo (new Runnable()
			{
			public void run()
				{
				GoUI.this.viewListener = viewListener;
				}
			});
		}

	/**
	 * Report that a marker was placed on the Go board.
	 *
	 * @param  r      Row on which marker was placed.
	 * @param  c      Column on which marker was placed.
	 * @param  color  Marker color.
	 */
	public void markerAdded
		(int r,
		 int c,
		 Color color)
		{
		boardPanel.repaint();
		}

	/**
	 * Report that a marker was removed from the Go board.
	 *
	 * @param  r  Row from which marker was removed.
	 * @param  c  Column from which marker was removed.
	 */
	public void markerRemoved
		(int r,
		 int c)
		{
		boardPanel.repaint();
		}

	/**
	 * Report that the Go board was cleared.
	 */
	public void boardCleared()
		{
		boardPanel.repaint();
		}

// Hidden operations.

	/**
	 * Handle the color swatch button.
	 */
	private void doChooseColor()
		{
		Color newColor =
			JColorChooser.showDialog (frame, "Choose Color", color);
		if (newColor != null)
			{
			color = newColor;
			swatch.setBackground (color);
			}
		}

	/**
	 * Handle a mouse click.
	 */
	private void doMouseClick
		(MouseEvent e)
		{
		try
			{
			switch (e.getButton())
				{
				case MouseEvent.BUTTON1:
					viewListener.addMarker
						(boardPanel.clickToRow (e),
						 boardPanel.clickToColumn (e),
						 color);
					break;
				case MouseEvent.BUTTON3:
					viewListener.removeMarker
						(boardPanel.clickToRow (e),
						 boardPanel.clickToColumn (e));
					break;
				}
			}
		catch (IOException exc)
			{
			}
		}

	/**
	 * Handle the Clear Board button.
	 */
	private void doClearBoard()
		{
		try
			{
			viewListener.clearBoard();
			}
		catch (IOException exc)
			{
			}
		}

	/**
	 * Execute the given runnable object on the Swing thread.
	 */
	private static void onSwingThreadDo
		(Runnable task)
		{
		try
			{
			SwingUtilities.invokeAndWait (task);
			}
		catch (Throwable exc)
			{
			exc.printStackTrace (System.err);
			System.exit (1);
			}
		}

// Unit test main program.

	/**
	 * Unit test main program.
	 */
	public static void main
		(String[] args)
		{
		GoBoard board = new GoBoard();
		board.setSpot (7, 8, Color.BLUE);
		board.setSpot (7, 10, Color.BLUE);
		board.setSpot (9, 9, Color.RED);
		board.setSpot (10, 7, Color.WHITE);
		board.setSpot (11, 8, Color.WHITE);
		board.setSpot (11, 9, Color.WHITE);
		board.setSpot (11, 10, Color.WHITE);
		board.setSpot (10, 11, Color.WHITE);
		GoUI ui = new GoUI ("Unit Test", board);
		}

	}