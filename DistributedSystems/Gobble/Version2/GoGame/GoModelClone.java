//******************************************************************************
//
// File:    GoModelClone.java
// Package: ---
// Unit:    Class GoModelClone
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
import java.io.IOException;

/**
 * Class GoModelClone provides a client-side clone of the server-side model
 * object in the Network Go Game.
 *
 * @author  Alan Kaminsky
 * @version 07-Jan-2010
 */
public class GoModelClone
	implements ModelListener
	{

// Hidden data members.

	private GoBoard board = new GoBoard();
	private ModelListener modelListener;

// Exported constructors.

	/**
	 * Construct a new Go model clone.
	 */
	public GoModelClone()
		{
		}

	/**
	 * Returns a reference to the Go board object in this Go model clone.
	 *
	 * @return  Go board.
	 */
	public GoBoard getBoard()
		{
		return board;
		}

	/**
	 * Set the model listener for this Go model clone.
	 *
	 * @param  modelListener  Model listener.
	 */
	public void setModelListener
		(ModelListener modelListener)
		{
		this.modelListener = modelListener;
		}

	/**
	 * Report that a marker was placed on the Go board.
	 *
	 * @param  r      Row on which marker was placed.
	 * @param  c      Column on which marker was placed.
	 * @param  color  Marker color.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void markerAdded
		(int r,
		 int c,
		 Color color)
		throws IOException
		{
		board.setSpot (r, c, color);
		modelListener.markerAdded (r, c, color);
		}

	/**
	 * Report that a marker was removed from the Go board.
	 *
	 * @param  r  Row from which marker was removed.
	 * @param  c  Column from which marker was removed.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void markerRemoved
		(int r,
		 int c)
		throws IOException
		{
		board.setSpot (r, c, null);
		modelListener.markerRemoved (r, c);
		}

	/**
	 * Report that the Go board was cleared.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void boardCleared()
		throws IOException
		{
		board.clear();
		modelListener.boardCleared();
		}

	}