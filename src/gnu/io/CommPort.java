/*-------------------------------------------------------------------------
|   rxtx is a native interface to serial ports in java.
|   Copyright 1997-2000 by Trent Jarvi trentjarvi@yahoo.com.
|
|   This library is free software; you can redistribute it and/or
|   modify it under the terms of the GNU Library General Public
|   License as published by the Free Software Foundation; either
|   version 2 of the License, or (at your option) any later version.
|
|   This library is distributed in the hope that it will be useful,
|   but WITHOUT ANY WARRANTY; without even the implied warranty of
|   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
|   Library General Public License for more details.
|
|   You should have received a copy of the GNU Library General Public
|   License along with this library; if not, write to the Free
|   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
--------------------------------------------------------------------------*/
package javax.comm;

import java.io.*;
import java.util.*;

/**
  * SerialPort
  */
public abstract class CommPort extends Object {
	protected String name;

	public abstract void enableReceiveFraming( int f ) throws UnsupportedCommOperationException;
	public abstract void disableReceiveFraming();
	public abstract boolean isReceiveFramingEnabled();
	public abstract int getReceiveFramingByte();
	public abstract void disableReceiveTimeout();
	public abstract void enableReceiveTimeout( int time );
	public abstract boolean isReceiveTimeoutEnabled();
	public abstract int getReceiveTimeout();
	public abstract void enableReceiveThreshold( int thresh );
	public abstract void disableReceiveThreshold();
	public abstract int getReceiveThreshold();
	public abstract boolean isReceiveThresholdEnabled();
	public abstract void setInputBufferSize( int size );
	public abstract int getInputBufferSize();
	public abstract void setOutputBufferSize( int size );
	public abstract int getOutputBufferSize();
	public void close() {};

	public abstract InputStream getInputStream() throws IOException;
	public abstract OutputStream getOutputStream() throws IOException;

	public String getName()
	{
		return( name );
	}
	public String toString()
	{
		return( name );
	}
}
