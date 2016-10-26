//******************************************************************************
//
// File:    GoBoardPanel.java
// Package: ---
// Unit:    Class GoBoardPanel
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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Class GoBoardPanel provides a widget for displaying a Go Board in a UI.
 *
 * @author  Alan Kaminsky
 * @version 05-Jan-2016
 */
public class GoBoardPanel
	extends JPanel
	{

// Hidden constants.

	private static final int W = 24;
	private static final int D = 20;
	private static final int OFFSET = (W - D)/2;

	private static final int X1 = W/2;
	private static final int X2 = X1 + (GoBoard.COLS - 1)*W;

	private static final int Y1 = W/2;
	private static final int Y2 = Y1 + (GoBoard.ROWS - 1)*W;

// Hidden data members.

	private GoBoard board;

// Exported constructors.

	/**
	 * Construct a new Go board panel.
	 *
	 * @param  board  Go board.
	 */
	public GoBoardPanel
		(GoBoard board)
		{
		super();
		this.board = board;

		Dimension dim = new Dimension (W*GoBoard.COLS, W*GoBoard.ROWS);
		setMinimumSize (dim);
		setPreferredSize (dim);
		setMaximumSize (dim);
		setBackground (new Color (0.8f, 1.0f, 0.8f));
		}

// Exported operations.

	/**
	 * Determine the row on the Go board that was clicked.
	 *
	 * @param  e  Mouse event.
	 *
	 * @return  Row index.
	 */
	public int clickToRow
		(MouseEvent e)
		{
		return e.getY()/W;
		}

	/**
	 * Determine the column on the Go board that was clicked.
	 *
	 * @param  e  Mouse event.
	 *
	 * @return  Column index.
	 */
	public int clickToColumn
		(MouseEvent e)
		{
		return e.getX()/W;
		}

// Hidden operations.

	/**
	 * Paint this Go board panel in the given graphics context.
	 *
	 * @param  g  Graphics context.
	 */
	protected void paintComponent
		(Graphics g)
		{
		super.paintComponent (g);

		// Clone graphics context.
		Graphics2D g2d = (Graphics2D) g.create();

		// Turn on antialiasing.
		g2d.setRenderingHint
			(RenderingHints.KEY_ANTIALIASING,
			 RenderingHints.VALUE_ANTIALIAS_ON);

		// Draw lines.
		g2d.setStroke (new BasicStroke (1.0f));
		g2d.setColor (Color.BLACK);
		Line2D.Double line = new Line2D.Double();
		for (int r = 0; r < GoBoard.ROWS; ++ r)
			{
			line.x1 = X1;
			line.y1 = Y1 + r*W;
			line.x2 = X2;
			line.y2 = line.y1;
			g2d.draw (line);
			}
		for (int c = 0; c < GoBoard.COLS; ++ c)
			{
			line.x1 = X1 + c*W;
			line.y1 = Y1;
			line.x2 = line.x1;
			line.y2 = Y2;
			g2d.draw (line);
			}

		// // Draw spots.
		// Ellipse2D.Double ellipse = new Ellipse2D.Double();
		// ellipse.width = D;
		// ellipse.height = D;
		// synchronized (board)
		// 	{
		// 	for (int r = 0; r < GoBoard.ROWS; ++ r)
		// 		{
		// 		for (int c = 0; c < GoBoard.COLS; ++ c)
		// 			{
		// 			Color color = board.getSpot (r, c);
		// 			if (color != null)
		// 				{
		// 				ellipse.x = c*W + OFFSET;
		// 				ellipse.y = r*W + OFFSET;
		// 				g2d.setColor (color);
		// 				g2d.fill (ellipse);
		// 				g2d.setColor (Color.BLACK);
		// 				g2d.draw (ellipse);
		// 				}
		// 			}
		// 		}
		// 	}
		}

// Unit test main program.

	/**
	 * Unit test main program.
	 */
	public static void main
		(String[] args)
		{
		GoBoard board = new GoBoard();
		board.setSpot (0, 0, Color.RED);
		board.setSpot (0, 1, Color.RED);
		board.setSpot (1, 0, Color.RED);
		board.setSpot (1, 1, Color.RED);
		board.setSpot (0, 18, Color.WHITE);
		board.setSpot (18, 0, Color.BLACK);
		board.setSpot (18, 18, Color.BLUE);

		JFrame frame = new JFrame ("GoBoardPanel Unit Test");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		frame.add (new GoBoardPanel (board));
		frame.pack();
		frame.setVisible (true);
		}

	}