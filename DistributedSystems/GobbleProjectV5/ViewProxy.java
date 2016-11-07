import java.awt.Color;
import java.io.*;
import java.net.Socket;
//******************************************************************************
//
// File:    ViewProxy.java
// Package: ---
// Unit:    Class ViewProxy
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
 * The Class ViewProxy.
 */
public class ViewProxy implements ModelListener {
	
	/** The socket. */
	private Socket socket;
	
	/** The output stream writer. */
	private PrintWriter out;
	
	/** The input stream reader. */
	private BufferedReader in;
	
	/** The view listener. */
	private ViewListener viewListener;

	/**
	 * Construct a new view proxy.
	 *
	 * @param socket the socket
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ViewProxy(Socket socket) throws IOException {
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Set the view listener object for this view proxy.
	 *
	 * @param viewListener the new view listener
	 */
	public void setViewListener(ViewListener viewListener) {
		if (this.viewListener == null) {
			this.viewListener = viewListener;
			new ReaderThread().start();
		} else {
			this.viewListener = viewListener;
		}
	}

/**
	 * Marker added.
	 *
	 * @param r the row
	 * @param c the column
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void markerAdded(int r, int c, int player) throws IOException {
		out.println("add " + player + " " + r + " " + c);
	}


	/**
	 * Reports that the board has been cleared.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void boardCleared() throws IOException {
		out.println("clear");
	}

	/**
	 * Report that the number of player has been sent.
	 *
	 * @param player the player number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerNumber(int player, int sessionID) throws IOException {
		out.println("number " + player + " " + sessionID);
	}

	/**
	 * Report that the turn has been sent.
	 *
	 * @param player the player's number
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerTurn(int player) throws IOException {
		out.println("turn " + player);
	}
	/**
	 * Report that the player number and name have been sent.
	 *
	 * @param player the player number
	 * @param name the name  of player
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void playerName(int player, String name) throws IOException {
		out.println("name " + player + " " + name);
	}

	/**
	 * Closes game.
	 */
	@Override
	public void close(int sessionID) throws IOException { 
		out.println("close " + sessionID);
	}


	/**
	 * The Class ReaderThread.
	 */
	private class ReaderThread extends Thread {
		
		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			try {
				for (;;) {
					String b = in.readLine();

					if (b != null) {
						b = b.trim();
					} else {
//						close();
						break;
					}


					if (b.length() == 0) {
						System.err.println("Bad message");
					} else {
						String[] parts = b.split(" ");

						switch (parts[0]) {
							case "join":
								String name = parts[1];
								viewListener.join(ViewProxy.this, name);
								break;
							case "add":
								viewListener.addMarker(Integer.valueOf(parts[2]),
										Integer.valueOf(parts[3]),
										Integer.valueOf(parts[1]));
								break;
							case "clear":
								viewListener.clearBoard();
								break;
							case "addcolor":
								viewListener.addColor(
										Integer.valueOf(parts[1]), 
										Integer.valueOf(parts[2]), 
										new Color(Integer.valueOf(parts[3]), true));
								break;
							case "sendwinner":
								viewListener.sendWinner(parts[1], Integer.parseInt(parts[2]));
								break;
							case "close":
								viewListener.close(Integer.valueOf(parts[1]));
								break;
							case "number":
								viewListener.playerNumber(Integer.valueOf(parts[1]), Integer.valueOf(parts[2]));
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
	 * Reports that the color is added to given button.
	 *
	 * @param r the row
	 * @param c the column
	 * @param color the color object to be added
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {
		out.println("addcolor " + r + " " + c + " " + color.getRGB());
		
	}

	/**
	 * Reports that the winner's name has been sent.
	 *
	 * @param winner the winner
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@Override
	public void winnerSent(String winner, int player) throws IOException {
		out.println("sendwinner " + winner + " " + player);
		
	}
}