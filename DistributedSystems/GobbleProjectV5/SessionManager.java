import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//******************************************************************************
//
// File:    SessionManager.java
// Package: ---
// Unit:    Class SessionManager
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
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
/**
 * The Class SessionManager.
 */
public class SessionManager implements ViewListener {
	
	/** The sessions. */
	private ArrayList<Session> sessions = new ArrayList<Session>();
	
	private Random rand = new Random();

	/**
	 * Construct a new session manager.
	 */
	public SessionManager() { }

	/**
	 * Join an open session.
	 *
	 * @param proxy the proxy
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public synchronized void join(ViewProxy proxy, String name) throws IOException {
		boolean joined = false;

		for(int i=0; i<sessions.size(); i++) {
			Session session = sessions.get(i);

			if (session.getNumPlayers() < 2) {
				session.join(proxy, name);
				joined = true;
			}
		}

		if (joined != true) {
			Session session = new Session(rand.nextInt(1000000), sessions);
			session.join(proxy, name);
			sessions.add(session);
		}
	}

	/* (non-Javadoc)
	 * @see ViewListener#addMarker(int, int, int)
	 */
	public void addMarker(int r, int c, int player) {

	}

	/* (non-Javadoc)
	 * @see ViewListener#clearBoard()
	 */
	public void clearBoard() throws IOException { }
	
	/* (non-Javadoc)
	 * @see ViewListener#playerNumber(int)
	 */
	public void playerNumber(int player, int sessionID) throws IOException { }
	
	/* (non-Javadoc)
	 * @see ViewListener#playerTurn(int)
	 */
	public void playerTurn(int player) throws IOException { }
	
	/* (non-Javadoc)
	 * @see ViewListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException { }
	
	
	/* (non-Javadoc)
	 * @see ViewListener#addColor(int, int, java.awt.Color)
	 */
	public void addColor(int r, int c, Color color) throws IOException {}
	
	/* (non-Javadoc)
	 * @see ViewListener#sendWinner(java.lang.String)
	 */
	public void sendWinner(String winner, int player) throws IOException {}

	@Override
	public void close(int sessionID) throws IOException {
		
		
		
	}
}