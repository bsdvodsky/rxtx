/*-------------------------------------------------------------------------
|   rxtx is a native interface to serial ports in java.
|   Copyright 1997, 1998, 1999 by Trent Jarvi trentjarvi@yahoo.com
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
package gnu.io;

import  java.io.*;
import  java.util.*;
import javax.comm.*;

/*------------------------------------------------------------------------------
Lots of stubs here.  Taken from the javadoc material produced from Sun's
commapi porting file.  Not used yet.
------------------------------------------------------------------------------*/

public class CommPortIdentifier {
	public static final int PORT_SERIAL = 1;  // Serial Port
	public static final int PORT_PARALLEL = 2; // Parallel Port

/*------------------------------------------------------------------------------
	addPortName()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public static void addPortName(String s, int i, RXTXCommDriver c) { 
	}
/*------------------------------------------------------------------------------
	addPortOwnershipListener()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public void addPortOwnershipListener(CommPortOwnershipListener c) { 
	}
/*------------------------------------------------------------------------------
	getCurrentOwner()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public String getCurrentOwner() { 
	String s=new String();
	return(s);
	}
/*------------------------------------------------------------------------------
	getName()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public String getName() { 
	String s = new String();
	return(s);
	}
/*------------------------------------------------------------------------------
	getPortIdentifier()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	static public CommPortIdentifier getPortIdentifier(String s) throws NoSuchPortException { 
		CommPortIdentifier ci=new CommPortIdentifier();
		return(ci);
	}
/*------------------------------------------------------------------------------
	getPortIdentifier()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	static public CommPortIdentifier getPortIdentifier(CommPort c) throws NoSuchPortException { 
		CommPortIdentifier ci=new CommPortIdentifier();
		return(ci);

	}
/*------------------------------------------------------------------------------
	getPortIdentifiers()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
/*
	static public Enumeration getPortIdentifiers() { 
	}
*/
/*------------------------------------------------------------------------------
	getPortType()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public int getPortType() { 
		return(1);
	}
/*------------------------------------------------------------------------------
	isCurrentlyOwned()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public boolean isCurrentlyOwned() { 
		return(false);
	}
/*------------------------------------------------------------------------------
	open()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
/*
	public CommPort open(FileDescriptor f) throws UnsupportedCommOperationException { 

	}
*/
/*------------------------------------------------------------------------------
	open()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
/*
	public synchronized CommPort open(String s, int i) throws PortInUseException { 
	}
*/
/*------------------------------------------------------------------------------
	removePortOwnership()
	accept:
	perform:
	return:
	exceptions:
	comments:
------------------------------------------------------------------------------*/
	public void removePortOwnershipListener(CommPortOwnershipListener c) { 
	}
}

