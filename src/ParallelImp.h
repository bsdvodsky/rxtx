/*-------------------------------------------------------------------------
|   rxtx is a native interface to serial ports in java.
|   Copyright 1997-2002 by Trent Jarvi taj@www.linux.org.uk
|
|   This library is free software; you can redistribute it and/or
|   modify it under the terms of the GNU Lesser General Public
|   License as published by the Free Software Foundation; either
|   version 2.1 of the License, or (at your option) any later version.
|
|   This library is distributed in the hope that it will be useful,
|   but WITHOUT ANY WARRANTY; without even the implied warranty of
|   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
|   Lesser General Public License for more details.
|
|   The following has been added to allow RXTX to be distributed with Sun
|   Microsystem's CommAPI library as suggested by the FSF.
|
|   http://www.fsf.org/licenses/gpl-faq.html#LinkingOverControlledInterface
|
|   A program that contains no derivative of any portion of RXTX, but
|   is designed to work with RXTX by being compiled or linked with it,
|   is considered a "work that uses the Library" subject to the terms and
|   conditions of the GNU Lesser General Public License.
|
|   As a special exception, the copyright holders of RXTX give you
|   permission to link RXTX with independent modules that communicate with
|   RXTX solely through the Sun Microsytems CommAPI interface, regardless of
|   the license terms of these independent modules, and to copy and distribute
|   the resulting combined work under terms of your choice, provided that
|   every copy of the combined work is accompanied by a complete copy of
|   the source code of RXTX (the version of RXTX used to produce the
|   combined work), being distributed under the terms of the GNU Lesser General
|   Public License plus this exception.  An independent module is a
|   module which is not derived from or based on RXTX.
|
|   Note that people who make modified versions of RXTX are not obligated
|   to grant this special exception for their modified versions; it is
|   their choice whether to do so.  The GNU Lesser General Public License
|   gives permission to release a modified version without this exception; this
|   exception also makes it possible to release a modified version which
|   carries forward this exception.
|
|   You should have received a copy of the GNU Lesser General Public
|   License along with this library; if not, write to the Free
|   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
--------------------------------------------------------------------------*/

/* javax.comm.ParallelPort constants */
/*  this appears to be handled in /usr/src/linux/misc/parport_pc.c */
#define LPT_MODE_ANY	0
#define LPT_MODE_SPP	1
#define LPT_MODE_PS2	2
#define LPT_MODE_EPP	3
#define LPT_MODE_ECP	4
#define LPT_MODE_NIBBLE	5

/* some popular releases of Slackware do not have SSIZE_MAX */

#ifndef SSIZE_MAX
#	if defined(INT_MAX)
#		define SSIZE_MAX  INT_MAX
#	elif defined(MAXINT)
#		define SSIZE_MAX MAXINT
#	else
#		define SSIZE_MAX 2147483647 /* ugh */
#	endif
#endif

/* javax.comm.ParallelPortEvent constants */
#define PAR_EV_ERROR	1
#define PAR_EV_BUFFER	2

/* java exception class names */
#define UNSUPPORTED_COMM_OPERATION "javax/comm/UnsupportedCommOperationException"
#define ARRAY_INDEX_OUT_OF_BOUNDS "java/lang/ArrayIndexOutOfBoundsException"
#define OUT_OF_MEMORY "java/lang/OutOfMemoryError"
#define IO_EXCEPTION "java/io/IOException"
#define PORT_IN_USE_EXCEPTION "javax/comm/PortInUseException"

/*
Flow Control defines inspired by reading how mgetty by Gert Doering does it
*/

/* PROTOTYPES */
jboolean is_interrupted(JNIEnv *, jobject );
int send_event(JNIEnv *, jobject, jint, int );
int read_byte_array( int fd, unsigned char *buffer, int length, int threshold,
   int timeout );
int get_java_var( JNIEnv *, jobject, char *, char * );
void report(char *);
void throw_java_exception( JNIEnv *, char *, char *, char * );

