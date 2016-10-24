//******************************************************************************
//
// File:    GoModel.java
// Package: ---
// Unit:    Class GoModel
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
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class GoModel provides the server-side model object in the Network Go Game.
 *
 * @author  Alan Kaminsky
 * @version 20-Jan-2010
 */
public class GoModel
	implements ViewListener
	{

// Hidden data members.

	private GoBoard board = new GoBoard();
	private LinkedList<ModelListener> listeners =
		new LinkedList<ModelListener>();

// Exported constructors.

	/**
	 * Construct a new Go model.
	 */
	public GoModel()
		{
		}

// Exported operations.

	/**
	 * Add the given model listener to this Go model.
	 *
	 * @param  modelListener  Model listener.
	 */
	public synchronized void addModelListener
		(ModelListener modelListener)
		{
		try
			{
			// Pump up the new client with the current state of the Go board.
			for (int r = 0; r < GoBoard.ROWS; ++ r)
				{
				for (int c = 0; c < GoBoard.COLS; ++ c)
					{
					Color color = board.getSpot (r, c);
					if (color != null)
						{
						modelListener.markerAdded (r, c, color);
						}
					}
				}

			// Record listener.
			listeners.add (modelListener);
			}

		catch (IOException exc)
			{
			// Don't record listener.
			}
		}

	/**
	 * Join the given session.
	 *
	 * @param  proxy    Reference to view proxy object.
	 * @param  session  Session name.
	 */
	public void join
		(ViewProxy proxy,
		 String session)
		{
		}

	/**
	 * Place a marker on the Go board.
	 *
	 * @param  r      Row on which to place the marker.
	 * @param  c      Column on which to place the marker.
	 * @param  color  Marker color.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void addMarker
		(int r,
		 int c,
		 Color color)
		throws IOException
		{
		// Update board state.
		board.setSpot (r, c, color);

		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext())
			{
			ModelListener listener = iter.next();
			try
				{
				listener.markerAdded (r, c, color);
				}
			catch (IOException exc)
				{
				// Client failed, stop reporting to it.
				iter.remove();
				}
			}
		}

	/**
	 * Remove a marker from the Go board.
	 *
	 * @param  r  Row from which to remove the marker.
	 * @param  c  Column from which to remove the marker.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void removeMarker
		(int r,
		 int c)
		throws IOException
		{
		// Update board state.
		board.setSpot (r, c, null);

		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext())
			{
			ModelListener listener = iter.next();
			try
				{
				listener.markerRemoved (r, c);
				}
			catch (IOException exc)
				{
				// Client failed, stop reporting to it.
				iter.remove();
				}
			}
		}

	/**
	 * Clear the Go board.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void clearBoard()
		throws IOException
		{
		// Update board state.
		board.clear();

		// Report update to all clients.
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext())
			{
			ModelListener listener = iter.next();
			try
				{
				listener.boardCleared();
				}
			catch (IOException exc)
				{
				// Client failed, stop reporting to it.
				iter.remove();
				}
			}
		}

	}