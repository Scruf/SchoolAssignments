//******************************************************************************
//
// File:    ModelListener.java
// Package: ---
// Unit:    Interface ModelListener
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
 * Interface ModelListener specifies the interface for an object that is
 * triggered by events from the model object in the Network Go Game.
 *
 * @author  Alan Kaminsky
 * @version 07-Jan-2010
 */
public interface ModelListener
	{

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
		throws IOException;

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
		throws IOException;

	/**
	 * Report that the Go board was cleared.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void boardCleared()
		throws IOException;

	}