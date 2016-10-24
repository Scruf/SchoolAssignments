//******************************************************************************
//
// File:    SessionManager.java
// Package: ---
// Unit:    Class SessionManager
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
import java.util.HashMap;

/**
 * Class SessionManager maintains the sessions' model objects in the Network Go
 * Game server.
 *
 * @author  Alan Kaminsky
 * @version 21-Jan-2010
 */
public class SessionManager
	implements ViewListener
	{

// Hidden data members.

	private HashMap<String,GoModel> sessions =
		new HashMap<String,GoModel>();

// Exported constructors.

	/**
	 * Construct a new session manager.
	 */
	public SessionManager()
		{
		}

// Exported operations.

	/**
	 * Join the given session.
	 *
	 * @param  proxy    Reference to view proxy object.
	 * @param  session  Session name.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public synchronized void join
		(ViewProxy proxy,
		 String session)
		throws IOException
		{
		GoModel model = sessions.get (session);
		if (model == null)
			{
			model = new GoModel();
			sessions.put (session, model);
			}
		model.addModelListener (proxy);
		proxy.setViewListener (model);
		}

	/**
	 * Place a marker on the Go board.
	 *
	 * @param  r      Row on which to place the marker.
	 * @param  c      Column on which to place the marker.
	 * @param  color  Marker color.
	 */
	public void addMarker
		(int r,
		 int c,
		 Color color)
		{
		}

	/**
	 * Remove a marker from the Go board.
	 *
	 * @param  r  Row from which to remove the marker.
	 * @param  c  Column from which to remove the marker.
	 */
	public void removeMarker
		(int r,
		 int c)
		{
		}

	/**
	 * Clear the Go board.
	 */
	public void clearBoard()
		{
		}

	}