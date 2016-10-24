//******************************************************************************
//
// File:    GoBoard.java
// Package: ---
// Unit:    Class GoBoard
//
// This Java source file is copyright (C) 2010 by Alan Kaminsky. All rights
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

/**
 * Class GoBoard encapsulates the state of the Go board.
 *
 * @author  Alan Kaminsky
 * @version 14-Jan-2010
 */
public class GoBoard
	{

// Exported constants.

	/**
	 * Number of rows.
	 */
	public static final int ROWS = 19;

	/**
	 * Number of columns.
	 */
	public static final int COLS = 19;

// Hidden data members.

	private Color[][] spot = new Color [ROWS] [COLS];

// Exported constructors.

	/**
	 * Construct a new Go board. All spots are initially empty.
	 */
	public GoBoard()
		{
		}

// Exported operations.

	/**
	 * Get the color of the given spot.
	 *
	 * @param  r  Row index, 0 .. ROWS-1.
	 * @param  c  Column index, 0 .. COLS-1.
	 *
	 * @return  Color, or null if the spot is empty.
	 */
	public synchronized Color getSpot
		(int r,
		 int c)
		{
		return spot[r][c];
		}

	/**
	 * Set the color of the given spot.
	 *
	 * @param  r      Row index, 0 .. ROWS-1.
	 * @param  c      Column index, 0 .. COLS-1.
	 * @param  color  Color, or null to make the spot empty.
	 */
	public synchronized void setSpot
		(int r,
		 int c,
		 Color color)
		{
		spot[r][c] = color;
		}

	/**
	 * Clear this Go board. All spots become empty.
	 */
	public synchronized void clear()
		{
		for (int r = 0; r < ROWS; ++ r)
			{
			for (int c = 0; c < COLS; ++ c)
				{
				spot[r][c] = null;
				}
			}
		}

	}