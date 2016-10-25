//******************************************************************************
//
// File:    ViewProxy.java
// Package: ---
// Unit:    Class ViewProxy
//
// This Java source file is copyright (C) 2016 by Alan Kaminsky. All rights
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

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class ViewProxy provides the network proxy for the view object in the Network
 * Go Game. The view proxy resides in the server program and communicates with
 * the client program.
 *
 * @author  Alan Kaminsky
 * @version 10-Aug-2016
 */
public class ViewProxy
	implements ModelListener
	{

// Hidden data members.

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ViewListener viewListener;

// Exported constructors.

	/**
	 * Construct a new view proxy.
	 *
	 * @param  socket  Socket.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public ViewProxy
		(Socket socket)
		throws IOException
		{
		this.socket = socket;
		socket.setTcpNoDelay (true);
		out = new DataOutputStream (socket.getOutputStream());
		in = new DataInputStream (socket.getInputStream());
		}

// Exported operations.

	/**
	 * Set the view listener object for this view proxy.
	 *
	 * @param  viewListener  View listener.
	 */
	public void setViewListener
		(ViewListener viewListener)
		{
		if (this.viewListener == null)
			{
			this.viewListener = viewListener;
			new ReaderThread() .start();
			}
		else
			{
			this.viewListener = viewListener;
			}
		}


	/**
	 * Report that a marker was placed on the Go board.
	 *
	 * @param  r      Row on which marker was placed.
	 * @param  c      Column on which marker was placed.
	 * @param  color  Marker color.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void markerAdded
		(int r,
		 int c,
		 Color color)
		throws IOException
		{
		out.writeByte ('A');
		out.writeByte (r);
		out.writeByte (c);
		out.writeInt (color.getRGB());
		out.flush();
		}

	/**
	 * Report that a marker was removed from the Go board.
	 *
	 * @param  r  Row from which marker was removed.
	 * @param  c  Column from which marker was removed.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void markerRemoved
		(int r,
		 int c)
		throws IOException
		{
		out.writeByte ('R');
		out.writeByte (r);
		out.writeByte (c);
		out.flush();
		}

	/**
	 * Report that the Go board was cleared.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void boardCleared()
		throws IOException
		{
		out.writeByte ('C');
		out.flush();
		}

// Hidden helper classes.

	/**
	 * Class ReaderThread receives messages from the network, decodes them, and
	 * invokes the proper methods to process them.
	 *
	 * @author  Alan Kaminsky
	 * @version 19-Jan-2010
	 */
	private class ReaderThread
		extends Thread
		{
		public void run()
			{
			try
				{
				for (;;)
					{
					String session;
					int r, c;
					Color color;
					byte b = in.readByte();
					switch (b)
						{
						case 'J':
							session = in.readUTF();
							viewListener.join (ViewProxy.this, session);
							break;
						case 'A':
							r = in.readByte();
							c = in.readByte();
							color = new Color (in.readInt(), true);
							viewListener.addMarker (r, c, color);
							break;
						case 'R':
							r = in.readByte();
							c = in.readByte();
							viewListener.removeMarker (r, c);
							break;
						case 'C':
							viewListener.clearBoard();
							break;
						default:
							System.err.println ("Bad message");
							break;
						}
					}
				}
			catch (IOException exc)
				{
				}
			finally
				{
				try
					{
					socket.close();
					}
				catch (IOException exc)
					{
					}
				}
			}
		}

	}