import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * This class runs the server using
 * host and port as the parameters
 * of {@link GobbleServer#main(String[])} method.
 */
public class GobbleServer {


	/**
	 * The main method.
	 *
	 * @param args must be:
	 * <ul>
	 * <li>host (localhost, for example)
	 * <li>port number
	 * </ul>
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 2) usage();

		String host = args[0];
		int port = Integer.parseInt(args[1]);

		ServerSocket serversocket = new ServerSocket();
		serversocket.bind(new InetSocketAddress(host, port));

		SessionManager manager = new SessionManager();

		for (;;) { // Listen for clients.
			Socket socket = serversocket.accept(); // Client accepted.
			ViewProxy proxy = new ViewProxy(socket); // Run listener for this client in a new thread.
			proxy.setViewListener(manager);
		}
	}


	/**
	 * Usage.
	 */
	private static void usage() {
		System.err.println("Usage: java GobbleServer <serverhost> <serverport>");
		System.exit(1);
	}

}