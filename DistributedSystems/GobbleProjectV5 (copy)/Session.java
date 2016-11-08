import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//******************************************************************************
//
// File:    Session.java
// Package: ---
// Unit:    Class Session
//
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
 * The Class Session.
 */
public class Session {
	
	/** The model. */
	private GobbleModel model;
	
	/** The number of players. */
	private int numPlayers = 0;


	/** The player1 name. */
	private String player1Name;
	
	/** The player2 name. */
	private String player2Name;
	
	private int sessionID;
	
	private List<Session> sessions;
	
	public Session(int sessionID, List<Session> sessions) {
		this.sessionID = sessionID;
		this.sessions = sessions;
		model = new GobbleModel(sessions);
	}


	/**
	 * Joins the new player (client).
	 *
	 * @param proxy the proxy
	 * @param name the name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void join(ViewProxy proxy, String name) throws IOException {
		if (numPlayers == 0) {
			player1Name = name;
		} else {
			player2Name = name;
		}

		model.addModelListener(proxy);
		proxy.setViewListener(model);

		numPlayers++;

		proxy.playerNumber(numPlayers, sessionID);

		if (numPlayers == 2) {
			model.playerName(1, player1Name);
			model.playerName(2, player2Name);
			model.playerTurn(1);
		}
	}

	/**
	 * Gets the number players.
	 *
	 * @return the number players
	 */
	public int getNumPlayers() {
		return this.numPlayers;
	}

	public int getSessionID() {
		return sessionID;
	}

	public void setSessionID(int sessionID) {
		this.sessionID = sessionID;
	}
}