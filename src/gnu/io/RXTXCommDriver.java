/*-------------------------------------------------------------------------
|   A wrapper to convert RXTX into Linux Java Comm
|   Copyright 1998 Kevin Hester, kevinh@acm.org
|   Copyright 2000 Trent Jarvi, trentjarvi@yahoo.com
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

/* Martin Pool <mbp@linuxcare.com> added support for explicitly-specified
 * lists of ports, October 2000. */

package gnu.io;

import java.io.*;
import java.util.*;
import javax.comm.*;
import java.util.StringTokenizer;

/**
   This is the JavaComm for Linux driver.
*/
public class RXTXCommDriver implements CommDriver
{
	static
	{
		System.loadLibrary( "Serial" );
	}

	/** Get the Serial port prefixes for the running OS */
	private native boolean isDeviceGood(String dev);


	private final String[] getPortPrefixes(String AllKnownPorts[]) {
		int i=0;
		String PortString[]=new String [256];
		for(int j=0;j<AllKnownPorts.length;j++){
			if(isDeviceGood(AllKnownPorts[j])) {
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
    * Check whether we can do r/w io on the specified filename.
    */
	private boolean isUsable(String portName)
	{
		final File port = new File(portName);
		return port.canRead() && port.canWrite();
	}


	private void RegisterValidPorts(
		String devs[],
		String Prefix[],
		int PortType
	) {
		for( int i = 0; i < devs.length; i++ ) {
			for( int p = 0; p < Prefix.length; p++ ) {
				if( devs[ i ].startsWith( Prefix[ p ] ) ) {
					String portName = "/dev/" + devs[ i ];
					if (isUsable(portName))
						CommPortIdentifier.addPortName(
							portName,
							PortType,
							this
						);
				}
			}
		}
	}

   /*
    * initialize() will be called by the CommPortIdentifier's static
    * initializer. The responsibility of this method is:
    * 1) Ensure that that the hardware is present.
    * 2) Load any required native libraries.
    * 3) Register the port names with the CommPortIdentifier.
	*
	* <p>From the NullDriver.java CommAPI sample.
	*
	* added printerport stuff
	* Holger Lehmann
	* July 12, 1999
	* IBM

	* Added ttyM for Moxa boards
	* Removed obsolete device cuaa
	* Peter Bennett
	* January 02, 2000
	* Bencom

    */
    /*
	See SerialImp.c's *KnownPorts[] when adding ports
    */
	public void initialize()
	{
	/*
	 First try to register ports specified in the properties
	 file.  If that doesn't exist, then scan for ports.
	*/
		if (!registerSpecifiedPorts())
			registerScannedPorts();
   	}


	private void addSpecifiedPorts(String names, int type)
	{
		final String pathSep = System.getProperty("path.separator", ":");
		final StringTokenizer tok = new StringTokenizer(names, pathSep);

		while (tok.hasMoreElements())
		{
			String portName = tok.nextToken();

			if (isUsable(portName))
				CommPortIdentifier.addPortName(portName,
					type, this);
		}
	}


   /*
    * Register ports specified by the gnu.io.rxtx.SerialPorts and
    * gnu.io.rxtx.ParallelPorts system properties.
    */
	private boolean registerSpecifiedPorts()
	{
		boolean found = false;
		String val;

		val = System.getProperty("gnu.io.rxtx.SerialPorts");
		if (val != null)
		{
			addSpecifiedPorts(val, CommPortIdentifier.PORT_SERIAL);
			found = true;
		}

		val = System.getProperty("gnu.io.rxtx.ParallelPorts");
		if (val != null)
		{
			addSpecifiedPorts(val, CommPortIdentifier.PORT_PARALLEL);
			found = true;
		}

		return found;
	}


   /*
    * Look for all entries in /dev, and if they look like they should
    * be serial ports on this OS and they can be opened then register
    * them.
    *
    * See SerialImp.c's *KnownPorts[] when adding ports
    */
	private void registerScannedPorts()
	{
		File dev = new File( "/dev" );
		String[] devs = dev.list();
		String[] AllKnownSerialPorts={
			"comx",      // linux COMMX synchronous serial card
			"holter",    // custom card for heart monitoring
			"modem",     // linux symbolic link to modem.
			"ttyircomm", // linux IrCommdevices (IrDA serial emu)
			"ttycosa0c", // linux COSA/SRP synchronous serial card
			"ttycosa1c", // linux COSA/SRP synchronous serial card
			"ttyC", // linux cyclades cards
			"ttyCH",// linux Chase Research AT/PCI-Fast serial card
			"ttyD", // linux Digiboard serial card
			"ttyE", // linux Stallion serial card
			"ttyF", // linux Computone IntelliPort serial card
			"ttyH", // linux Chase serial card
			"ttyI", // linux virtual modems
			"ttyL", // linux SDL RISCom serial card
			"ttyM", // linux PAM Software's multimodem boards
				// linux ISI serial card
			"ttyMX",// linux Moxa Smart IO cards
			"ttyP", // linux Hayes ESP serial card
			"ttyR", // linux comtrol cards
				// linux Specialix RIO serial card
			"ttyS", // linux Serial Ports
			"ttySI",// linux SmartIO serial card
			"ttySR",// linux Specialix RIO serial card 257+
			"ttyT", // linux Technology Concepts serial card
			"ttyUSB",//linux USB serial converters
			"ttyV", // linux Comtrol VS-1000 serial controller
			"ttyW", // linux specialix cards
			"ttyX", // linux SpecialX serial card

			"ttyc", // irix raw character devices
			"ttyd", // irix basic serial ports
			"ttyf", // irix serial ports with hardware flow
			"ttym", // irix modems
			"ttyq", // irix pseudo ttys
			"tty4d",// irix RS422
			"tty4f",// irix RS422 with HSKo/HSki
			"midi", // irix serial midi
			"us",   // irix mapped interface

			"cuaa", // FreeBSD Serial Ports

			"tty0", // netbsd serial ports

			"tty0p",// HP-UX serial ports
			"tty1p",// HP-UX serial ports

			"serial",// BeOS serial ports

			"COM"    // win32 serial ports
		};
	/** Get the Parallel port prefixes for the running os
	* Holger Lehmann
	* July 12, 1999
	* IBM
	*/
		String[] AllKnownParallelPorts={
			"lp"    // linux printer port
		};
		RegisterValidPorts(
			devs,
			getPortPrefixes(AllKnownSerialPorts),
			CommPortIdentifier.PORT_SERIAL
		);
		RegisterValidPorts(
			devs,
			getPortPrefixes(AllKnownParallelPorts),
			CommPortIdentifier.PORT_PARALLEL
		);
	}


	/*
	 * getCommPort() will be called by CommPortIdentifier from its
	 * openPort() method. portName is a string that was registered earlier
	 * using the CommPortIdentifier.addPortName() method. getCommPort()
	 * returns an object that extends either SerialPort or ParallelPort.
	 *
	 * <p>From the NullDriver.java CommAPI sample.
	 */
	public CommPort getCommPort( String portName, int portType )
	{
		try {
			if (portType==CommPortIdentifier.PORT_SERIAL)
			{
				return new RXTXPort( portName );
			}
			else if (portType==CommPortIdentifier.PORT_PARALLEL)
			{
				return new LPRPort( portName );
			}
		} catch( IOException e ) {
			e.printStackTrace();
		}
		return null;
	}
}
