import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server
	{
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 1) usage();
		String host = args[0];
		ServerSocket serversocket = new ServerSocket();
		serversocket.bind (new InetSocketAddress (host, 13));
		for (;;)
			{
			final Socket socket = serversocket.accept();
			new Thread()
				{
				public void run()
					{
					PrintStream out = null;
					try
						{
						out = new PrintStream (socket.getOutputStream());
						Thread.sleep (5000L);
						out.println (new Date());
						}
					catch (Exception exc)
						{
						exc.printStackTrace (System.err);
						}
					finally
						{
						out.close();
						try
							{
							socket.close();
							}
						catch (IOException exc)
							{
							exc.printStackTrace (System.err);
							}
						}
					}
				}.start();
			}
		}

	private static void usage()
		{
		System.err.println ("Usage: java DayTimeServer <host>");
		System.exit (1);
		}
	}