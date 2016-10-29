import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * The Class GobbleClient.
 */
public class GobbleClient {


	/**
	 * The main method.
	 *
	 * @param args the arguments
	 * @throws Exception the exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 3) {
			usage();
		}

		String host = args[0];
		int port = 0;
		try {
			port = Integer.parseInt(args[1]);
		} catch (NumberFormatException e) {
			usage();
		}
		String playername = args[2];

		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(host, port));

		GobbleModelClone model = new GobbleModelClone();
		GobbleUI view = new GobbleUI(
				args[2]);
		ModelProxy proxy = new ModelProxy(socket);

		model.setModelListener(view);
		view.setViewListener(proxy);
		proxy.setModelListener(model);
		proxy.join(null, playername);
	}

	
	/**
	 * Usage.
	 */
	private static void usage() {
		System.err.println("Usage: java GobbleClient <host> <port> <playername>");
		System.exit(0);
	}

}
