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

#include <termios.h>
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <stdlib.h>

int main(int argc, char *argv[])
{
	int i, fd ;
	struct termios ttyset;

	if (argc < 1 )
	{
		printf("usage ./testSerial /dev/ttyS0\n");
		exit(1);
	}
	fd = open (argv[1], O_RDWR | O_NOCTTY | O_NONBLOCK );

		// wait for minicom/rxtx to start and user to press a key
		// You can set up the port in minicom/rxtx now.  Dont
		// exit minicom until you have hit a key to continue.

	fgetc(stdin);

	// get the attributes.
	
	tcgetattr( fd, &ttyset );

	/* print the attributes */
       
	fprintf(stderr, "c_iflag=%#x\n", ttyset.c_iflag);

	fprintf(stderr, "c_lflag=%#x\n", ttyset.c_lflag);
	fprintf(stderr, "c_oflag=%#x\n", ttyset.c_oflag);
	fprintf(stderr, "c_cflag=%#x\n", ttyset.c_cflag);
	fprintf(stderr, "c_cc[]: ");
	for(i=0; i<NCCS; i++)
	{
		fprintf(stderr,"%d=%x ", i, ttyset.c_cc[i]);
	}
	fprintf(stderr,"\n" );
	exit(0);
}
