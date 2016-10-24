//******************************************************************************
//
// File:    ModelProxy.java
// Package: ---
// Unit:    Class ModelProxy
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
 * Class ModelProxy provides the network proxy for the model object in the
 * Network Go Game. The model proxy resides in the client program and
 * communicates with the server program.
 *
 * @author  Alan Kaminsky
 * @version 10-Aug-2016
 */
public class ModelProxy
	implements ViewListener
	{

// Hidden data members.

	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	private ModelListener modelListener;

// Exported constructors.

	/**
	 * Construct a new model proxy.
	 *
	 * @param  socket  Socket.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public ModelProxy
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
	 * Set the model listener object for this model proxy.
	 *
	 * @param  modelListener  Model listener.
	 */
	public void setModelListener
		(ModelListener modelListener)
		{
		this.modelListener = modelListener;
		new ReaderThread() .start();
		}

	/**
	 * Join the given session.
	 *
	 * @param  proxy    Reference to view proxy object.
	 * @param  session  Session name.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void join
		(ViewProxy proxy,
		 String session)
		throws IOException
		{
		out.writeByte ('J');
		out.writeUTF (session);
		out.flush();
		}

	/**
	 * Place a marker on the Go board.
	 *
	 * @param  r      Row on which to place the marker.
	 * @param  c      Column on which to place the marker.
	 * @param  color  Marker color.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void addMarker
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
	 * Remove a marker from the Go board.
	 *
	 * @param  r  Row from which to remove the marker.
	 * @param  c  Column from which to remove the marker.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void removeMarker
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
	 * Clear the Go board.
	 *
	 * @exception  IOException
	 *     Thrown if an I/O error occurred.
	 */
	public void clearBoard()
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
					int r, c;
					Color color;
					byte b = in.readByte();
					switch (b)
						{
						case 'A':
							r = in.readByte();
							c = in.readByte();
							color = new Color (in.readInt(), true);
							modelListener.markerAdded (r, c, color);
							break;
						case 'R':
							r = in.readByte();
							c = in.readByte();
							modelListener.markerRemoved (r, c);
							break;
						case 'C':
							modelListener.boardCleared();
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