import java.awt.Color;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
//******************************************************************************
//
// File:    GobbleModel.java
// Package: ---
// Unit:    Class GobbleModel
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
 * The Class GobbleModel.
 */
public class GobbleModel implements ViewListener {

	/** The listeners. */
	private LinkedList<ModelListener> listeners = new LinkedList<ModelListener>();

	/** Indicates turn. */
	private int turn = -1;
	
	private List<Session> sessions;


	/**
	 * Instantiates a new gobble model.
	 */
	public GobbleModel(List<Session> sessions) { 
		this.sessions = sessions;
	}



	/**
	 * Adds the model listener.
	 *
	 * @param modelListener the model listener
	 */
	public synchronized void addModelListener(ModelListener modelListener) {

			listeners.add(modelListener);

	}


	/* (non-Javadoc)
	 * @see ViewListener#join(ViewProxy, java.lang.String)
	 */
	public void join(ViewProxy proxy, String session) { }

	/* (non-Javadoc)
	 * @see ViewListener#addMarker(int, int, int)
	 */
	public synchronized void addMarker(int r, int c, int player) throws IOException {

			Iterator<ModelListener> iter = listeners.iterator();
			while (iter.hasNext()) {
				ModelListener listener = iter.next();
				try {
					listener.markerAdded(r, c, player);
				} catch (IOException exc) {

					iter.remove();
				}
			}

			playerTurn(turn);

	}

	/* (non-Javadoc)
	 * @see ViewListener#clearBoard()
	 */
	public synchronized void clearBoard() throws IOException {

		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.boardCleared();
			} catch (IOException exc) {

				iter.remove();
			}
		}

		playerTurn(1);
	}

	/* (non-Javadoc)
	 * @see ViewListener#playerNumber(int)
	 */
	public synchronized void playerNumber(int player, int sessionID) {

		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerNumber(player, sessionID);
			} catch (IOException exc) {
				iter.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ViewListener#playerName(int, java.lang.String)
	 */
	public synchronized void playerName(int player, String name) {

		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerName(player, name);
			} catch (IOException exc) {
				// Client failed, stop reporting to it.
				iter.remove();
			}
		}
	}

	/* (non-Javadoc)
	 * @see ViewListener#playerTurn(int)
	 */
	public synchronized void playerTurn(int player) {
		if (turn == -1) {
			turn = player;
		}

		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.playerTurn(player);
			} catch (IOException exc) {

				iter.remove();
			}
		}

		turn = (player == 1) ? 2 : 1;
	}

	/* (non-Javadoc)
	 * @see ViewListener#close()
	 */
	@Override
	public synchronized void close(int sessionID) throws IOException { 
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
				listener.close(sessionID);

		}
		for (int i = 0; i < sessions.size(); i ++) {
			if (sessions.get(i).getSessionID() == sessionID) {
				sessions.remove(i);
				break;
			}
		}
	}

	/* (non-Javadoc)
	 * @see ViewListener#addColor(int, int, java.awt.Color)
	 */
	@Override
	public  synchronized void addColor(int r, int c, Color color) throws IOException {
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.colorAdded(r, c, color);
			} catch (IOException exc) {
	
				iter.remove();
			}
		}
		
	}



	/* (non-Javadoc)
	 * @see ViewListener#sendWinner(java.lang.String)
	 */
	@Override
	public  synchronized void sendWinner(String winner, int player) throws IOException {
		Iterator<ModelListener> iter = listeners.iterator();
		while (iter.hasNext()) {
			ModelListener listener = iter.next();
			try {
				listener.winnerSent(winner, player);
			} catch (IOException exc) {
	
				iter.remove();
			}
		}
		
	}

}