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

/* javax.comm.ParallelPort constants */
/*  this appears to be handled in /usr/src/linux/misc/parport_pc.c */
#define LPT_MODE_ANY
#define LPT_MODE_ECP
#define LPT_MODE_EPP
#define LPT_MODE_NIBBLE
#define LPT_MODE_PS2
#define LPT_MODE_SPP

/* javax.comm.ParallelPortEvent constants */
#define PAR_EV_BUFFER
#define PAR_EV_ERROR

/*
Flow Control defines inspired by reading how mgetty by Gert Doering does it
*/

/* PROTOTYPES */
int read_byte_array( int fd, unsigned char *buffer, int length, int threshold,
   int timeout );
int get_java_fd( JNIEnv *env, jobject jobj );
void send_modem_events( JNIEnv *env, jobject jobj, jmethodID method,
   int event, int change, int state );
void IOException( JNIEnv *Env, char *msg );
void UnsupportedCommOperationException( JNIEnv *env, char *msg );
