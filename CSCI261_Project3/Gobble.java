import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Client Program.
 */
public class Gobble {

    /**
     * Main program.
     */
    public static void main (String[] args) throws Exception {

        if (args.length != 3) usage();
        String host = args[0];
        int port = Integer.parseInt (args[1]);
        String playername = args[2];

        Socket socket = new Socket();
        socket.connect (new InetSocketAddress(host, port));

    }

    /**
     * Print a usage message and exit.
     */
    private static void usage() {
        System.err.println ("Usage: java Gobble <host> <port> <playername>");
        System.exit (1);
    }
}
