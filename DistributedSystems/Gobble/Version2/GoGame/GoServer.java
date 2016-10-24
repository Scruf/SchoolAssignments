//******************************************************************************
//
// File:    GoServer.java
// Package: ---
// Unit:    Class GoServer
//
// This Java source file is copyright (C) 2010 by Alan Kaminsky. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ark@cs.rit.edu.
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

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Class GoServer is the server main program for the Network Go Game. The
 * command line arguments specify the host and port to which the server should
 * listen for connections.
 * <P>
 * Usage: java GoServer <I>host</I> <I>port</I>
 *
 * @author  Alan Kaminsky
 * @version 21-Jan-2010
 */
public class GoServer
	{

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 2) usage();
		String host = args[0];
		int port = Integer.parseInt (args[1]);

		ServerSocket serversocket = new ServerSocket();
		serversocket.bind (new InetSocketAddress (host, port));

		SessionManager manager = new SessionManager();

		for (;;)
			{
			Socket socket = serversocket.accept();
			ViewProxy proxy = new ViewProxy (socket);
			proxy.setViewListener (manager);
			}
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java GoServer <host> <port>");
		System.exit (1);
		}

	}