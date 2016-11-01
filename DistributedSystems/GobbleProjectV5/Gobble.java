import java.net.InetSocketAddress;
import java.net.Socket;

//******************************************************************************
//
// File:    Gobble.java
// Package: ---
// Unit:    Gobble 
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
public class Gobble {


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
		GobbleUI view = new GobbleUI(playername, true);
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
