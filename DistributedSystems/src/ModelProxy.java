import java.awt.Color;
import java.io.*;
import java.net.Socket;

public class ModelProxy implements ViewListener {

	/**
	 * The socket connected to the server.
	 */
	private Socket socket;

	/**
	 * Writer to the socket.
	 */
	private PrintWriter out;

	/**
	 * Reader from the socket.
	 */
	private BufferedReader in;

	/**
	 * Model connected to this proxy.
	 */
	private ModelListener modelListener;

	/**
	 * Construct a new model proxy.
	 * 
	 * @param socket
	 *            Socket.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public ModelProxy(Socket socket) throws IOException {
		this.socket = socket;
		out = new PrintWriter(socket.getOutputStream(), true);
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	/**
	 * Set the model listener object for this model proxy.
	 * 
	 * @param modelListener
	 *            Model listener.
	 */
	public void setModelListener(ModelListener modelListener) {
		this.modelListener = modelListener;
		new ReaderThread().start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#join(java.lang.String)
	 */
	public void join(ViewProxy proxy, String playername) throws IOException {
		out.println("join " + playername);
	}

	/**
	 * Place a marker on the Go board.
	 * 
	 * @param c
	 *            Column on which to place the marker.
	 * @param player
	 *            Marker player.
	 * 
	 * @exception IOException
	 *                Thrown if an I/O error occurred.
	 */
	public void addMarker(int r, int c, int player) throws IOException {
		out.println("add " + player + " " + r + " " + c);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#clearBoard()
	 */
	public void clearBoard() throws IOException {
		out.println("clear");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#playerNumber(int)
	 */
	public void playerNumber(int player) throws IOException { }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#playerName(int, java.lang.String)
	 */
	public void playerName(int player, String name) throws IOException { }

	/**
	 * Tells the view which player's turn it is.
	 * 
	 * @param player
	 *            The player ID.
	 */
	public void playerTurn(int player) throws IOException { }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#markerAdded(int, int, int)
	 */
	public void markerAdded(int r, int c, int player) throws IOException { }

	/**
	 * Tells the view that the board was cleared.
	 * 
	 * @throws IOException
	 */
	public void boardCleared() throws IOException { }

	/*
	 * (non-Javadoc)
	 * 
	 * @see ViewListener#close()
	 */
	public void close() {
		modelListener.close();
	}

	/**
	 * Class ReaderThread receives messages from the network, decodes
	 * them, and invokes the proper methods to process them.
	 * 
	 * @author Alan Kaminsky
	 * @author Vasilios Anton
	 * @version 19-Jan-2010
	 */
	private class ReaderThread extends Thread {
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

					// System.out.println("ModelProxy: " + b);

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

	@Override
	public void addColor(int r, int c, Color color) throws IOException {
		out.println("addcolor " + r + " " + c + " " + color.getRGB());
		
	}
}