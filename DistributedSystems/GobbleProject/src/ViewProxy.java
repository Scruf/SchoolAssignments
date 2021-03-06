import java.awt.Color;
import java.io.*;
import java.net.Socket;

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
	
	private GobbleBoard gb;

	public GobbleBoard getGb() {
		return gb;
	}

	public void setGb(GobbleBoard gb) {
		this.gb = gb;
	}

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

	/* (non-Javadoc)
	 * @see ModelListener#markerAdded(int, int, int)
	 */
	public void markerAdded(int r, int c, int player) throws IOException {
		out.println("add " + player + " " + r + " " + c);
	}

	/* (non-Javadoc)
	 * @see ModelListener#boardCleared()
	 */
	public void boardCleared() throws IOException {
		out.println("clear");
	}

	/* (non-Javadoc)
	 * @see ModelListener#playerNumber(int)
	 */
	public void playerNumber(int player, int sessionID) throws IOException {
		out.println("number " + player + " " + sessionID);
	}

	/* (non-Javadoc)
	 * @see ModelListener#playerTurn(int)
	 */
	public void playerTurn(int player) throws IOException {
		out.println("turn " + player);
	}

	/* (non-Javadoc)
	 * @see ModelListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException {
		out.println("name " + player + " " + name);
	}

	/* (non-Javadoc)
	 * @see ModelListener#close()
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
								gb.doMove(Integer.valueOf(parts[2]),
										Integer.valueOf(parts[3]),
										Integer.valueOf(parts[1]) == 1 ? Color.RED : Color.BLUE);
								int w = gb.getWinner();
								viewListener.sendWinner("winnger", w);
								
								break;
							case "clear":
								viewListener.clearBoard();
								gb.clearBoard();
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

	/* (non-Javadoc)
	 * @see ModelListener#colorAdded(int, int, java.awt.Color)
	 */
	@Override
	public void colorAdded(int r, int c, Color color) throws IOException {
		out.println("addcolor " + r + " " + c + " " + color.getRGB());
		
	}

	/* (non-Javadoc)
	 * @see ModelListener#winnerSent(java.lang.String)
	 */
	@Override
	public void winnerSent(String winner, int player) throws IOException {
		out.println("sendwinner " + winner + " " + player);
		
	}
}