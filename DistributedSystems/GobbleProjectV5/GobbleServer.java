import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
//******************************************************************************
//
// File:    GobbleServer.java
// Package: ---
// Unit:    Class GObbleServer
//
//
// This Java source file is copyright (C) 2015 Egor Kozitski. All rights
// reserved. For further information, contact the author, Alan Kaminsky, at
// ek5442@g.rit.edu
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