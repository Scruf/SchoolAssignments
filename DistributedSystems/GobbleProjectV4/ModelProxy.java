import java.awt.Color;
import java.io.*;
import java.net.Socket;

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


	/* (non-Javadoc)
	 * @see ViewListener#join(ViewProxy, java.lang.String)
	 */
	public void join(ViewProxy proxy, String playername) throws IOException {
		out.println("join " + playername);
	}


	/* (non-Javadoc)
	 * @see ViewListener#addMarker(int, int, int)
	 */
	public void addMarker(int r, int c, int player) throws IOException {
		out.println("add " + player + " " + r + " " + c);
	}


	/* (non-Javadoc)
	 * @see ViewListener#clearBoard()
	 */
	public void clearBoard() throws IOException {
		out.println("clear");
	}


	/* (non-Javadoc)
	 * @see ViewListener#playerNumber(int)
	 */
	public void playerNumber(int player) throws IOException { }


	/* (non-Javadoc)
	 * @see ViewListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException { }


	/* (non-Javadoc)
	 * @see ViewListener#playerTurn(int)
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


	/* (non-Javadoc)
	 * @see ViewListener#close()
	 */
	public void close() throws IOException {
//		modelListener.close();
		out.println("close ");
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
						close();
						break;
					}


					if (b.length() == 0) {
						System.err.println("Bad message");
					} else {
						String[] parts = b.split(" ");

						switch (parts[0]) {
							case "number":
								modelListener.playerNumber(Integer.valueOf(parts[1]));
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
								modelListener.winnerSent(parts[1]);
								break;
							case "close":
								modelListener.close();
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
	 * @see ViewListener#addColor(int, int, java.awt.Color)
	 */
	@Override
	public void addColor(int r, int c, Color color) throws IOException {
		out.println("addcolor " + r + " " + c + " " + color.getRGB());
		
	}


	/* (non-Javadoc)
	 * @see ViewListener#sendWinner(java.lang.String)
	 */
	@Override
	public void sendWinner(String winner) throws IOException {
		out.println("sendwinner " + winner);
		
	}
}