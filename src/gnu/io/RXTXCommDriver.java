/*-------------------------------------------------------------------------
|   A wrapper to convert RXTX into Linux Java Comm
|   Copyright 1998 Kevin Hester, kevinh@acm.org
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

import java.io.*;
import javax.comm.*;

/**
   This is the JavaComm for Linux driver.  
*/
public class RXTXCommDriver implements CommDriver {


        static { System.loadLibrary( "Serial" ); }

	/** Get the Serial port prefixes for the running OS */
	private native boolean IsDeviceGood(String dev);
	private final String[] getPortPrefixes(String AllKnownPorts[]) {
		int i=0;
		String PortString[]=new String [256];
		for(int j=0;j<AllKnownPorts.length;j++){
			if(IsDeviceGood(AllKnownPorts[j])) {
				PortString[i++]=AllKnownPorts[j];
			}
		}
		String PortString2[] =new String[i];
		for(int j=0;j<i;j++){
			PortString2[j]=PortString[j];
		}
		return PortString2;
	}


   /*
    * initialize() will be called by the CommPortIdentifier's static
    * initializer. The responsibility of this method is:
    * 1) Ensure that that the hardware is present.
    * 2) Load any required native libraries.
    * 3) Register the port names with the CommPortIdentifier.
	 * 
	 * <p>From the NullDriver.java CommAPI sample.
    */
	public void initialize() {
		File dev = new File( "/dev" );
		String[] devs = dev.list();
		String[] AllKnownSerialPorts={
			"modem",// linux symbolic link to modem.
			"ttyS", // linux Serial Ports
			"ttyI", // linux virtual modems
			"ttyW", // linux specialix cards
			"ttyC", // linux cyclades cards
			"ttyf", // irix serial ports with hardware flow
			"ttym", // irix modems
			"ttyq", // irix pseudo ttys
			"ttyd"  // irix serial ports
		};
		String[] AllKnownParallelPorts={
			"lp"    // linux printer port
		};
		String[] SerialportPrefix = getPortPrefixes(AllKnownSerialPorts);
		String[] ParallelportPrefix = getPortPrefixes(AllKnownParallelPorts);
		for( int i = 0; i < devs.length; i++ ) {
			for( int p = 0; p < SerialportPrefix.length; p++ ) {
				if( devs[ i ].startsWith( SerialportPrefix[ p ] ) ) {
					String portName = "/dev/" + devs[ i ];
					File port = new File( portName );
					if( port.canRead() && port.canWrite() ) 
						CommPortIdentifier.addPortName( portName,
							CommPortIdentifier.PORT_SERIAL, this );
				}
			}
		}
		for( int i = 0; i < devs.length; i++ ) {
			for( int p = 0; p < ParallelportPrefix.length; p++ ) {
				if( devs[ i ].startsWith( ParallelportPrefix[ p ] ) ) {
					String portName = "/dev/" + devs[ i ];
					File port = new File( portName );
					if( port.canRead() && port.canWrite() ) {
						CommPortIdentifier.addPortName( portName,
							CommPortIdentifier.PORT_PARALLEL, this );
					}
				}
			}
		}
	}


	/*
	 * getCommPort() will be called by CommPortIdentifier from its openPort()
	 * method. portName is a string that was registered earlier using the
	 * CommPortIdentifier.addPortName() method. getCommPort() returns an
	 * object that extends either SerialPort or ParallelPort.
	 *
	 * <p>From the NullDriver.java CommAPI sample.
	 */
	public CommPort getCommPort( String portName, int portType ) {
		try {
			if (portType==CommPortIdentifier.PORT_SERIAL)
				return new RXTXPort( portName ); 
			else if (portType==CommPortIdentifier.PORT_PARALLEL)
				return new LPRPort( portName ); 
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
}
