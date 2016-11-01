import java.awt.Color;
import java.io.*;
import java.net.Socket;
//******************************************************************************
//
// File:    ModelProxy.java
// Package: ---
// Unit:    Class ModelProxy
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
 * The Class ModelProxy.
 */
public class ModelProxy implements ViewListener {


	/** The socket object. */
	private Socket socket;


	/** The out stream. */
	private PrintWriter out;


	/** The input stream. */
	private BufferedReader in;
	


	/** The model listener. */
	private ModelListener modelListener;


	/**
	 * Instantiates a new model proxy.
	 *
	 * @param socket the socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ModelProxy(Socket socket) throws IOException {
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}


	/**
	 * Sets the model listener.
	 *
	 * @param modelListener the new model listener
	 */
	public void setModelListener(ModelListener modelListener) {
		this.modelListener = modelListener;
		new ReaderThread().start();
	}


		/**
	 * Join the given session.
	 * 
	 * @param proxy
	 *            Reference to view proxy object.
	 * @param session
	 *            Session name.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void join(ViewProxy proxy, String playername) throws IOException {
		out.println("join " + playername);
	}


		/**
	 * Place a marker on the board.
	 *
	 * @param r            Row on which to place the marker.
	 * @param c            Column on which to place the marker.
	 * @param player the player
	 * @exception IOException                Thrown if an I/O error occurred.
	 */
	public void addMarker(int r, int c, int player) throws IOException {
		out.println("add " + player + " " + r + " " + c);
	}


	/**
	 * Report that the Connect board was cleared.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void clearBoard() throws IOException {
		out.println("clear");
	}


	
	/**
	 * Report that the current player is 1 or 2.
	 *
	 * @param player            The player number.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerNumber(int player, int sessionID) throws IOException { 
		out.println("number " + player + " " + sessionID);
	}


		/**
	 * Report a certain player is player 1 or 2.
	 *
	 * @param player            The player ID.
	 * @param name            The name of the player.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerName(int player, String name) throws IOException { }


		/**
	 * Report which player's turn it is.
	 *
	 * @param player            The player ID.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerTurn(int player) throws IOException { }


	/**
	 * Marker added.
	 *
	 * @param r the r
	 * @param c the c
	 * @param player the player
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void markerAdded(int r, int c, int player) throws IOException { }


	/**
	 * Board cleared.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void boardCleared() throws IOException { }


	/**
	 * Report that the windows should be closed.
	 */
	@Override
	public void close(int sessionID) throws IOException {
//		modelListener.close();
		
		out.println("close " + sessionID);
	}


	/**
	 * The Class ReaderThread.
	 */
	private class ReaderThread extends Thread {
		
		
		public void run() {
			try {
				for (;;) {
					String b = in.readLine();

					if (b != null) {
						b = b.trim();
					} else {
//						close(sessionID);
						break;
					}


					if (b.length() == 0) {
						System.err.println("Bad message");
					} else {
						String[] parts = b.split(" ");

						switch (parts[0]) {
							case "number":
								modelListener.playerNumber(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
								break;
							case "name":
								modelListener.playerName(Integer.valueOf(parts[1]), parts[2]);
								break;
							case "turn":
								modelListener.playerTurn(Integer.valueOf(parts[1]));
								break;
							case "add":
								modelListener.markerAdded(Integer.valueOf(parts[2]),
										Integer.valueOf(parts[3]),
										Integer.valueOf(parts[1]));
								break;
							case "clear":
								modelListener.boardCleared();
								break;
							case "addcolor":
								modelListener.colorAdded(
										Integer.valueOf(parts[1]), 
										Integer.valueOf(parts[2]), 
										new Color(Integer.valueOf(parts[3]), true));
								break;
							case "sendwinner":
								modelListener.winnerSent(parts[1], Integer.parseInt(parts[2]));
								break;
							case "close":
								modelListener.close(Integer.parseInt(parts[1]));
								break;
							default:
								System.err.println("Bad message");
								break;
						}
					}
				}
			} catch (IOException exc) {
			} finally {
				try {
					socket.close();
				} catch (IOException exc) {
				}
			}
		}
	}

		/**
	 * Report that a marker was placed on the board.
	 *
	 * @param r            Row for the cell.
	 * @param c            Column for the cell.
	 * @param color the color
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void addColor(int r, int c, Color color) throws IOException {
		out.println("addcolor " + r + " " + c + " " + color.getRGB());
		
	}


		/**
	 * Sends winner's name.
	 *
	 * @param winner the winner's name
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void sendWinner(String winner, int player) throws IOException {
		out.println("sendwinner " + winner + " " + player);
		
	}
}