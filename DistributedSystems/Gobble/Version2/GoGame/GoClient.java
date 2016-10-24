//******************************************************************************
//
// File:    GoClient.java
// Package: ---
// Unit:    Class GoClient
//
// This Java source file is copyright (C) 2015 by Alan Kaminsky. All rights
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
import java.net.Socket;

/**
 * Class GoClient is the client main program for the Network Go Game. The
 * command line arguments specify the host and port to which the server is
 * listening for connections.
 * <P>
 * Usage: java GoClient <I>host</I> <I>port</I> <I>session</I>
 *
 * @author  Alan Kaminsky
 * @version 27-Apr-2015
 */
public class GoClient
	{

	/**
	 * Main program.
	 */
	public static void main
		(String[] args)
		throws Exception
		{
		if (args.length != 3) usage();
		String host = args[0];
		int port = Integer.parseInt (args[1]);
		String session = args[2];

		Socket socket = new Socket();
		socket.connect (new InetSocketAddress (host, port));

		GoModelClone model = new GoModelClone();
		GoUI view = GoUI.create (session, model.getBoard());
		ModelProxy proxy = new ModelProxy (socket);
		model.setModelListener (view);
		view.setViewListener (proxy);
		proxy.setModelListener (model);
		proxy.join (null, session);
		}

	/**
	 * Print a usage message and exit.
	 */
	private static void usage()
		{
		System.err.println ("Usage: java GoClient <host> <port> <session>");
		System.exit (1);
		}

	}