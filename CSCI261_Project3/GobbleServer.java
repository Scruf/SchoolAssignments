/**
 * Server program.
 */
public class GobbleServer {

    /**
     * Main program.
     */
    public static void main (String[] args) throws Exception {

        if (args.length != 2) usage();
        String host = args[0];
        int port = Integer.parseInt(args[1]);

    }

    /**
     * Print a usage message and exit.
     */
    private static void usage() {
        System.err.println ("Usage: java GobbleServer <host> <port>");
        System.exit (1);
    }

}
