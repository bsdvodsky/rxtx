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
	private int speed=9600;
	public int getBaudRate() { return speed; }

	/** Data bits port parameter */
	private int dataBits=DATABITS_8;
	public int getDataBits() { return dataBits; }

	/** Stop bits port parameter */
	private int stopBits=SerialPort.STOPBITS_1;
	public int getStopBits() { return stopBits; }

	/** Parity port parameter */
	private int parity= SerialPort.PARITY_NONE;
	public int getParity() { return parity; }


	/** Flow control */
	private int flowmode = SerialPort.FLOWCONTROL_NONE;
	public void setFlowControlMode( int flowcontrol ) {
		try { setflowcontrol( flowcontrol ); }
		catch( IOException e ) {
			e.printStackTrace();
			return;
		}
		flowmode=flowcontrol;
	}
	public int getFlowControlMode() { return flowmode; }
	native void setflowcontrol( int flowcontrol ) throws IOException;


	/*
	linux/drivers/char/n_hdlc.c? FIXME
		jarvi@ezlink.com
	*/
	/** Receive framing control 
	*/
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

	public native int NativegetReceiveTimeout();
	public native boolean NativeisReceiveTimeoutEnabled();
	public native void NativeenableReceiveTimeout(int t);
	public void disableReceiveTimeout(){
		NativeenableReceiveTimeout(0);
		timeout = 0;
	}
	public void enableReceiveTimeout( int t ){
		if( t > 0 )  {
			timeout = t;
			NativeenableReceiveTimeout( t );
		}
		else {
			timeout = 0;
			NativeenableReceiveTimeout( 0 );
		}
	}
	public boolean isReceiveTimeoutEnabled(){
		return(NativeisReceiveTimeoutEnabled());
	}
	public int getReceiveTimeout(){
		return(NativegetReceiveTimeout( ));
	}

	/** Receive threshold control */
	public native void enableReceiveThreshold( int t );
	public void disableReceiveThreshold() { 
		enableReceiveThreshold(0);
	}
	public native int getReceiveThreshold();
	public native boolean isReceiveThresholdEnabled();

	/** Input/output buffers */
	/** FIXME I think this refers to 
		FOPEN(3)/SETBUF(3)/FREAD(3)/FCLOSE(3) 
		jarvi@ezlink.com
	*/
	public void setInputBufferSize( int size ) {
		System.out.println("setInputBufferSize is not implemented");
	}
	public int getInputBufferSize() {
		System.out.println("getInputBufferSize is not implemented");
		return 1;
	}
	public void setOutputBufferSize( int size ) {
		System.out.println("setOutputBufferSize is not implemented");
	}
	public int getOutputBufferSize() {
		System.out.println("getOutputBufferSize is not implemented");
		return 1;
	}


	/** Line status methods */
	public native boolean isDTR();
	public native void setDTR( boolean state );
	public native void setRTS( boolean state );
	private native void setDSR( boolean state );
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
	private MonitorThread monThread;

	/** Process SerialPortEvents */
	native void eventLoop();
	void sendEvent( int event, boolean state ) {
		switch( event ) {
			case SerialPortEvent.DATA_AVAILABLE:
				if( monThread.Data ) break;
				return;
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
				if( monThread.Output ) break;
				return;
			case SerialPortEvent.CTS:
				if( monThread.CTS ) break;
				return;
			case SerialPortEvent.DSR:
				if( monThread.DSR ) break;
				return;
			case SerialPortEvent.RI:
				if( monThread.RI ) break;
				return;
			case SerialPortEvent.CD:
				if( monThread.CD ) break;
				return;
			case SerialPortEvent.OE:
				if( monThread.OE ) break;
				return;
			case SerialPortEvent.PE:
				if( monThread.PE ) break;
				return;
			case SerialPortEvent.FE:
				if( monThread.FE ) break;
				return;
			case SerialPortEvent.BI:
				if( monThread.BI ) break;
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
		monThread = new MonitorThread();
		monThread.start(); 
	}
	/** Remove the serial port event listener */
	public void removeEventListener() {
		SPEventListener = null;
		if( monThread != null ) {
			monThread.interrupt();
			monThread = null;
		}
	}

	public void notifyOnDataAvailable( boolean enable ) { monThread.Data = enable; }

	public void notifyOnOutputEmpty( boolean enable ) { monThread.Output = enable; }

	public void notifyOnCTS( boolean enable ) { monThread.CTS = enable; }
	public void notifyOnDSR( boolean enable ) { monThread.DSR = enable; }
	public void notifyOnRingIndicator( boolean enable ) { monThread.RI = enable; }
	public void notifyOnCarrierDetect( boolean enable ) { monThread.CD = enable; }
	public void notifyOnOverrunError( boolean enable ) { monThread.OE = enable; }
	public void notifyOnParityError( boolean enable ) { monThread.PE = enable; }
	public void notifyOnFramingError( boolean enable ) { monThread.FE = enable; }
	public void notifyOnBreakInterrupt( boolean enable ) { monThread.BI = enable; }


	/** Close the port */
	private native void nativeClose();
	public void close() {
		setDTR(false);
		setDSR(false);
		nativeClose();
		super.close();
		fd = 0;
	}


	/** Finalize the port */
	protected void finalize() {
		if( fd > 0 ) close();
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
	class MonitorThread extends Thread {
	/** Note: these have to be separate boolean flags because the
	   SerialPortEvent constants are NOT bit-flags, they are just
	   defined as integers from 1 to 10  -DPL */
		private boolean CTS=false;
		private boolean DSR=false;
		private boolean RI=false;
		private boolean CD=false;
		private boolean OE=false;
		private boolean PE=false;
		private boolean FE=false;
		private boolean BI=false;
		private boolean Data=false;
		private boolean Output=false;
		MonitorThread() { }
		public void run() {
			eventLoop();
		}
	}
}
