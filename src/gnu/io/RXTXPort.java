/*-------------------------------------------------------------------------
|   rxtx is a native interface to serial ports in java.
|   Copyright 1997, 1998, 1999 by Trent Jarvi jarvi@ezlink.com.
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
import java.util.*;
import javax.comm.*;


/**
  * RXTXPort
  */
final class RXTXPort extends SerialPort {

	static {
		System.loadLibrary( "Serial" );
		Initialize();
	}


	/** Initialize the native library */
	private native static void Initialize();


	/** Actual SerialPort wrapper class */


	/** Open the named port */
	public RXTXPort( String name ) throws IOException {
		fd = open( name );
	}
	private native int open( String name ) throws IOException;


	/** File descriptor */
	private int fd;


	/** Output stream */
	private final SerialOutputStream out = new SerialOutputStream();
	public OutputStream getOutputStream() { return out; }


	/** Input stream */
	private final SerialInputStream in = new SerialInputStream();
	public InputStream getInputStream() { return in; }


	/** Set the SerialPort parameters */
	public void setSerialPortParams( int b, int d, int s, int p )
		throws UnsupportedCommOperationException
	{
		nativeSetSerialPortParams( b, d, s, p );
		speed = b;
		dataBits = d;
		stopBits = s;
		parity = p;
	}

	/** Set the native serial port parameters */
	private native void nativeSetSerialPortParams( int speed, int dataBits,
		int stopBits, int parity ) throws UnsupportedCommOperationException;

	/** Line speed in bits-per-second */
	private int speed;
	public int getBaudRate() { return speed; }

	/** Data bits port parameter */
	private int dataBits;
	public int getDataBits() { return dataBits; }

	/** Stop bits port parameter */
	private int stopBits;
	public int getStopBits() { return stopBits; }

	/** Parity port parameter */
	private int parity;
	public int getParity() { return parity; }


	/** Flow control */
	private int flowmode = SerialPort.FLOWCONTROL_NONE;
	public void setFlowControlMode( int flowcontrol ) {
		try {
			if( flowmode == SerialPort.FLOWCONTROL_RTSCTS_IN )
				setHWFC( true );
			else
				setHWFC( false );
		}
		catch( IOException e ) {
			e.printStackTrace();
			return;
		}
		flowmode = flowcontrol;
	}
	
	public int getFlowControlMode() { return flowmode; }
   native void setHWFC( boolean state ) throws IOException;


	/** Receive framing control */
	public void enableReceiveFraming( int f )
		throws UnsupportedCommOperationException
	{
		throw new UnsupportedCommOperationException( "Not supported" );
	}
	public void disableReceiveFraming() {}
	public boolean isReceiveFramingEnabled() { return false; }
	public int getReceiveFramingByte() { return 0; }


	/** Receive timeout control */
	private int timeout = 0;
	public void enableReceiveTimeout( int t ) {
		if( t > 0 ) timeout = t;
		else timeout = 0;
	}
	public void disableReceiveTimeout() { timeout = 0; }
	public boolean isReceiveTimeoutEnabled() { return timeout > 0; }
	public int getReceiveTimeout() { return timeout; }


	/** Receive threshold control */
	private int threshold = 1;
	public void enableReceiveThreshold( int t ) {
		if( t > 1 ) threshold = t;
		else threshold = 1;
	}
	public void disableReceiveThreshold() { threshold = 1; }
	public int getReceiveThreshold() { return threshold; }
	public boolean isReceiveThresholdEnabled() { return threshold > 1; };


	/** Input/output buffers */
	public void setInputBufferSize( int size ) {}
	public int getInputBufferSize() {
		return 1;
	}
	public void setOutputBufferSize( int size ) {}
	public int getOutputBufferSize() {
		return 1;
	}


	/** Line status methods */
   public native boolean isDTR();
	public native void setDTR( boolean state );
	public native void setRTS( boolean state );
	public native boolean isCTS();
	public native boolean isDSR();
	public native boolean isCD();
	public native boolean isRI();
	public native boolean isRTS();


	/** Write to the port */
	public native void sendBreak( int duration );
	private native void writeByte( int b ) throws IOException;
	private native void writeArray( byte b[], int off, int len )
		throws IOException;
	private native void drain() throws IOException;


	/** RXTXPort read methods */
	private native int nativeavailable() throws IOException;
	private native int readByte() throws IOException;
	private native int readArray( byte b[], int off, int len ) 
		throws IOException;


	/** Serial Port Event listener */
	private SerialPortEventListener SPEventListener;

	/** Thread to monitor data */
	private Thread monThread;

	/** Process SerialPortEvents */
	native void eventLoop();
	void sendEvent( int event, boolean state ) {
		switch( event ) {
			case SerialPortEvent.DATA_AVAILABLE:
				if( monData ) break;
				return;
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				if( monOutput ) break;
				return;
			case SerialPortEvent.CTS:
				if( monCTS ) break;
				return;
			case SerialPortEvent.DSR:
				if( monDSR ) break;
				return;
			case SerialPortEvent.RI:
				if( monRI ) break;
				return;
			case SerialPortEvent.CD:
				if( monCD ) break;
				return;
			case SerialPortEvent.OE:
				if( monOE ) break;
				return;
			case SerialPortEvent.PE:
				if( monPE ) break;
				return;
			case SerialPortEvent.FE:
				if( monFE ) break;
				return;
			case SerialPortEvent.BI:
				if( monBI ) break;
				return;
			default:
				return;
		}
		SerialPortEvent e = new SerialPortEvent(this, event, !state, state );
		if( SPEventListener != null ) SPEventListener.serialEvent( e );
	}

	/** Add an event listener */
	public void addEventListener( SerialPortEventListener lsnr )
		throws TooManyListenersException
	{
		if( SPEventListener != null ) throw new TooManyListenersException();
		SPEventListener = lsnr;
		monThread = new Thread() {
			public void run() {
				eventLoop();
			}
		};
		monThread.start(); 
	}

	/** Remove the serial port event listener */
	public void removeEventListener() {
		SPEventListener = null;
		if( monThread != null ) {
			monThread.interrupt();
			monThread = null;
		}
		/* FIXME: Should we reset all the notify flags here? */
	}

	/** Note: these have to be separate boolean flags because the
	   SerialPortEvent constants are NOT bit-flags, they are just
	   defined as integers from 1 to 10  -DPL */
	private boolean monData = false;
	public void notifyOnDataAvailable( boolean enable ) { monData = enable; }

	private boolean monOutput = false;
	public void notifyOnOutputEmpty( boolean enable ) { monOutput = enable; }

	private boolean monCTS = false;
	public void notifyOnCTS( boolean enable ) { monCTS = enable; }

	private boolean monDSR = false;
	public void notifyOnDSR( boolean enable ) { monDSR = enable; }

	private boolean monRI = false;
	public void notifyOnRingIndicator( boolean enable ) { monRI = enable; }

	private boolean monCD = false;
	public void notifyOnCarrierDetect( boolean enable ) { monCD = enable; }

	private boolean monOE = false;
	public void notifyOnOverrunError( boolean enable ) { monOE = enable; }

	private boolean monPE = false;
	public void notifyOnParityError( boolean enable ) { monPE = enable; }

	private boolean monFE = false;
	public void notifyOnFramingError( boolean enable ) { monFE = enable; }

	private boolean monBI = false;
	public void notifyOnBreakInterrupt( boolean enable ) { monBI = enable; }


	/** Close the port */
	private native void nativeClose();
	public void close() {
		nativeClose();
		super.close();
	}


	/** Finalize the port */
	protected void finalize() {
		close();
	}


        /** Inner class for SerialOutputStream */
        class SerialOutputStream extends OutputStream {
                public void write( int b ) throws IOException {
                        writeByte( b );
                }
                public void write( byte b[] ) throws IOException {
                        writeArray( b, 0, b.length );
                }
                public void write( byte b[], int off, int len ) throws IOException {
                        writeArray( b, off, len );
                }
                public void flush() throws IOException {
                        drain();
                }
        }

	/** Inner class for SerialInputStream */
	class SerialInputStream extends InputStream {
		public int read() throws IOException {
			return readByte();
		}
		public int read( byte b[] ) throws IOException {
			return readArray( b, 0, b.length );
		}
		public int read( byte b[], int off, int len ) throws IOException {
			return readArray( b, off, len );
		}
		public int available() throws IOException {
			return nativeavailable();
		}
	}
}
