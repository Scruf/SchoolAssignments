import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
//******************************************************************************
//
// File:    SessionManager.java
// Package: ---
// Unit:    Class SessionManager
///
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

	/**
	 * Place a marker on the board.
	 *
	 * @param r            Row on which to place the marker.
	 * @param c            Column on which to place the marker.
	 * @param player the player
	 * @exception IOException                Thrown if an I/O error occurred.
	 */
	public void addMarker(int r, int c, int player) {

	}

		/**
	 * Report that the Connect board was cleared.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void clearBoard() throws IOException { }
	
		/**
	 * Report that the current player is 1 or 2.
	 *
	 * @param player            The player number.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerNumber(int player, int sessionID) throws IOException { }
	
/**
	 * Report which player's turn it is.
	 *
	 * @param player            The player ID.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerTurn(int player) throws IOException { }
	
		/**
	 * Report a certain player is player 1 or 2.
	 *
	 * @param player            The player ID.
	 * @param name            The name of the player.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerName(int player, String name) throws IOException { }
	
	
	
	/**
	 * Report that a marker was placed on the board.
	 *
	 * @param r            Row for the cell.
	 * @param c            Column for the cell.
	 * @param color the color
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void addColor(int r, int c, Color color) throws IOException {}
	
	/**
	 * Sends winner's name.
	 *
	 * @param winner the winner's name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void sendWinner(String winner, int player) throws IOException {}

	@Override
	public void close(int sessionID) throws IOException {
		
		
		
	}
}