import java.awt.Color;
import java.io.IOException;
//******************************************************************************
//
// File:    GobbleModelClient.java
// Package: ---
// Unit:    Class GobbleModelClient
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
 * The Class GobbleModelClone.
 */
public class GobbleModelClone implements ModelListener {


	/** The model listener. */
	private ModelListener modelListener;

	/**
	 * Instantiates a new gobble model clone.
	 */
	public GobbleModelClone() {
	}


	/**
	 * Sets the model listener.
	 *
	 * @param modelListener the new model listener
	 */
	public void setModelListener(ModelListener modelListener) {
		this.modelListener = modelListener;
	}


	/* (non-Javadoc)
	 * @see ModelListener#markerAdded(int, int, int)
	 */
	public void markerAdded(int r, int c, int player) throws IOException {
		modelListener.markerAdded(r, c, player);
	}


	/* (non-Javadoc)
	 * @see ModelListener#boardCleared()
	 */
	public void boardCleared() throws IOException {
		modelListener.boardCleared();
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerNumber(int)
	 */
	public void playerNumber(int player, int sessionID) throws IOException {
		modelListener.playerNumber(player, sessionID);
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException {
		modelListener.playerName(player, name);
	}


	/* (non-Javadoc)
	 * @see ModelListener#playerTurn(int)
	 */
	public void playerTurn(int player) throws IOException {
		modelListener.playerTurn(player);
	}


	/* (non-Javadoc)
	 * @see ModelListener#close()
	 */
	public void close(int sessionID) throws IOException {
		modelListener.close(sessionID);
	}

	/* (non-Javadoc)
	 * @see ModelListener#colorAdded(int, int, java.awt.Color)
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {

		modelListener.colorAdded(r, c, color);
	}


	/* (non-Javadoc)
	 * @see ModelListener#winnerSent(java.lang.String)
	 */
	@Override
	public void winnerSent(String winner, int player) throws IOException {
		modelListener.winnerSent(winner, player);
		
	}

}