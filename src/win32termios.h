/*-------------------------------------------------------------------------
|   Win32 support By Wayne Roberts wroberts1@home.com
|   Copyright 1999 by the Free Software Foundation. 
|
|   rxtx is a native interface to serial ports in java.
|   Copyright 1997-1999 by Trent Jarvi trentjarvi@yahoo.com.
|
|   This library is free software; you can redistribute it and/or
|   modify it under the terms of the GNU Library General Public
|   License as published by the Free Software Foundation; either
|   version 2 of the License, or (at your option) any later version.
|
|   If you compile this program with cygwin32 tools this package falls
|   under the GPL.  See COPYING.CYGNUS for details.
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

#ifndef _TERMIOS_H_
#define _TERMIOS_H_
#include <windows.h>
#include <sys/types.h>
//#include "jni.h"
#include <io.h>
typedef unsigned char   cc_t;
typedef unsigned int    speed_t;
typedef unsigned int    tcflag_t;

#define NCCS 32
struct termios
  {
    tcflag_t c_iflag;           /* input mode flags */
    tcflag_t c_oflag;           /* output mode flags */
    tcflag_t c_cflag;           /* control mode flags */
    tcflag_t c_lflag;           /* local mode flags */
    cc_t c_cc[NCCS];            /* control characters */
    cc_t c_line;                /* line discipline (== c_cc[33]) */
    speed_t c_ispeed;           /* input speed */
    speed_t c_ospeed;           /* output speed */
  };
const char *get_dos_port(const char *);
void set_errno(int);
char *sterror(int);
int B_to_CBR(int);
int CBR_to_B(int);
int termios_to_bytesize(int);
int bytesize_to_termios(int);
int win32open(char *File, int flags);
int win32tcgetattr(int Fd, struct termios *s_termios);
int win32tcsetattr(int Fd, int when, struct termios *);
int win32write(int Fd, char *Str, int length);
int win32close(int );
speed_t win32cfgetospeed(struct termios *s_termios);
int win32cfsetospeed(struct termios *, speed_t speed);
speed_t win32cfgetispeed(struct termios *s_termios);
int win32cfsetispeed ( struct termios *, speed_t speed);
int win32tcflush ( int , int );
int win32tcgetpgrp ( int );
int win32tcsetpgrp ( int , int );
int win32tcdrain ( int );
int win32tcflow ( int , int );
int win32tcsendbreak ( int , int );
int win32read(int fd, char *b, int size);
int win32ioctl(int fd, int request, unsigned int *arg);
void cfmakeraw(struct termios *s_termios);
#define  ioctl       win32ioctl
#define  open        win32open
#define  close       win32close
#define  write       win32write
#define  tcgetattr   win32tcgetattr
#define  tcsetattr   win32tcsetattr
#define  cfgetospeed win32cfgetospeed
#define  cfsetospeed win32cfsetospeed
#define  cfgetispeed win32cfgetispeed
#define  cfsetispeed win32cfsetispeed
#define  tcflush     win32tcflush
#define  tcgetpgrp   win32tcgetpgrp
#define  tcsetpgrp   win32tcsetpgrp
#define  tcdrain     win32tcdrain
#define  tcflush     win32tcflush
#define  tcflow      win32tcflow
#define  tcsendbreak win32tcsendbreak
#define  read        win32read

/* The following is taken from glibc/linux includes */
#define	EPERM		 1	/* Operation not permitted */
#define	ENOENT		 2	/* No such file or directory */
#define	ESRCH		 3	/* No such process */
#define	EINTR		 4	/* Interrupted system call */
#define	EIO		 5	/* I/O error */
#define	ENXIO		 6	/* No such device or address */
#define	E2BIG		 7	/* Arg list too long */
#define	ENOEXEC		 8	/* Exec format error */
#define	EBADF		 9	/* Bad file number */
#define	ECHILD		10	/* No child processes */
#define	EDEADLK		11	/* Resource deadlock would occur */
#define	ENOMEM		12	/* Out of memory */
#define	EACCES		13	/* Permission denied */
#define	EFAULT		14	/* Bad address */
#define	ENOTBLK		15	/* Block device required */
#define	EBUSY		16	/* Device or resource busy */
#define	EEXIST		17	/* File exists */
#define	EXDEV		18	/* Cross-device link */
#define	ENODEV		19	/* No such device */
#define	ENOTDIR		20	/* Not a directory */
#define	EISDIR		21	/* Is a directory */
#define	EINVAL		22	/* Invalid argument */
#define	ENFILE		23	/* File table overflow */
#define	EMFILE		24	/* Too many open files */
#define	ENOTTY		25	/* Not a typewriter */
#define	ETXTBSY		26	/* Text file busy */
#define	EFBIG		27	/* File too large */
#define	ENOSPC		28	/* No space left on device */
#define	ESPIPE		29	/* Illegal seek */
#define	EROFS		30	/* Read-only file system */
#define	EMLINK		31	/* Too many links */
#define	EPIPE		32	/* Broken pipe */
#define	EDOM		33	/* Math argument out of domain of func */
#define	ERANGE		34	/* Math result not representable */
#define	EAGAIN		35	/* Try again */
#define	EWOULDBLOCK	EAGAIN	/* Operation would block */
#define	EINPROGRESS	36	/* Operation now in progress */
#define	EALREADY	37	/* Operation already in progress */
#define	ENOTSOCK	38	/* Socket operation on non-socket */
#define	EDESTADDRREQ	39	/* Destination address required */
#define	EMSGSIZE	40	/* Message too long */
#define	EPROTOTYPE	41	/* Protocol wrong type for socket */
#define	ENOPROTOOPT	42	/* Protocol not available */
#define	EPROTONOSUPPORT	43	/* Protocol not supported */
#define	ESOCKTNOSUPPORT	44	/* Socket type not supported */
#define	EOPNOTSUPP	45	/* Operation not supported on transport endpoint */
#define	EPFNOSUPPORT	46	/* Protocol family not supported */
#define	EAFNOSUPPORT	47	/* Address family not supported by protocol */
#define	EADDRINUSE	48	/* Address already in use */
#define	EADDRNOTAVAIL	49	/* Cannot assign requested address */
#define	ENETDOWN	50	/* Network is down */
#define	ENETUNREACH	51	/* Network is unreachable */
#define	ENETRESET	52	/* Network dropped connection because of reset */
#define	ECONNABORTED	53	/* Software caused connection abort */
#define	ECONNRESET	54	/* Connection reset by peer */
#define	ENOBUFS		55	/* No buffer space available */
#define	EISCONN		56	/* Transport endpoint is already connected */
#define	ENOTCONN	57	/* Transport endpoint is not connected */
#define	ESHUTDOWN	58	/* Cannot send after transport endpoint shutdown */
#define	ETOOMANYREFS	59	/* Too many references: cannot splice */
#define	ETIMEDOUT	60	/* Connection timed out */
#define	ECONNREFUSED	61	/* Connection refused */
#define	ELOOP		62	/* Too many symbolic links encountered */
#define	ENAMETOOLONG	63	/* File name too long */
#define	EHOSTDOWN	64	/* Host is down */
#define	EHOSTUNREACH	65	/* No route to host */
#define	ENOTEMPTY	66	/* Directory not empty */

#define	EUSERS		68	/* Too many users */
#define	EDQUOT		69	/* Quota exceeded */
#define	ESTALE		70	/* Stale NFS file handle */
#define	EREMOTE		71	/* Object is remote */

#define	ENOLCK		77	/* No record locks available */
#define	ENOSYS		78	/* Function not implemented */

#define	ENOMSG		80	/* No message of desired type */
#define	EIDRM		81	/* Identifier removed */
#define	ENOSR		82	/* Out of streams resources */
#define	ETIME		83	/* Timer expired */
#define	EBADMSG		84	/* Not a data message */
#define	EPROTO		85	/* Protocol error */
#define	ENODATA		86	/* No data available */
#define	ENOSTR		87	/* Device not a stream */

#define	ENOPKG		92	/* Package not installed */

#define	EILSEQ		116	/* Illegal byte sequence */

/* The following are just random noise.. */
#define	ECHRNG		88	/* Channel number out of range */
#define	EL2NSYNC	89	/* Level 2 not synchronized */
#define	EL3HLT		90	/* Level 3 halted */
#define	EL3RST		91	/* Level 3 reset */

#define	ELNRNG		93	/* Link number out of range */
#define	EUNATCH		94	/* Protocol driver not attached */
#define	ENOCSI		95	/* No CSI structure available */
#define	EL2HLT		96	/* Level 2 halted */
#define	EBADE		97	/* Invalid exchange */
#define	EBADR		98	/* Invalid request descriptor */
#define	EXFULL		99	/* Exchange full */
#define	ENOANO		100	/* No anode */
#define	EBADRQC		101	/* Invalid request code */
#define	EBADSLT		102	/* Invalid slot */

#define	EDEADLOCK	EDEADLK

#define	EBFONT		104	/* Bad font file format */
#define	ENONET		105	/* Machine is not on the network */
#define	ENOLINK		106	/* Link has been severed */
#define	EADV		107	/* Advertise error */
#define	ESRMNT		108	/* Srmount error */
#define	ECOMM		109	/* Communication error on send */
#define	EMULTIHOP	110	/* Multihop attempted */
#define	EDOTDOT		111	/* RFS specific error */
#define	EOVERFLOW	112	/* Value too large for defined data type */
#define	ENOTUNIQ	113	/* Name not unique on network */
#define	EBADFD		114	/* File descriptor in bad state */
#define	EREMCHG		115	/* Remote address changed */

#define	EUCLEAN		117	/* Structure needs cleaning */
#define	ENOTNAM		118	/* Not a XENIX named type file */
#define	ENAVAIL		119	/* No XENIX semaphores available */
#define	EISNAM		120	/* Is a named type file */
#define	EREMOTEIO	121	/* Remote I/O error */

#define	ELIBACC		122	/* Can not access a needed shared library */
#define	ELIBBAD		123	/* Accessing a corrupted shared library */
#define	ELIBSCN		124	/* .lib section in a.out corrupted */
#define	ELIBMAX		125	/* Attempting to link in too many shared libraries */
#define	ELIBEXEC	126	/* Cannot exec a shared library directly */
#define	ERESTART	127	/* Interrupted system call should be restarted */
#define	ESTRPIPE	128	/* Streams pipe error */



/* In GNU, read and write are bits (unlike BSD).  */
/*
#define	O_READ		O_RDONLY 
#define O_WRITE		O_WRONLY
#define O_ACCMODE	  0003
#define O_RDONLY	    00
#define O_WRONLY	    01
#define O_RDWR		    02
#define O_CREAT		 01000
#define O_TRUNC		 02000
#define O_EXCL		 04000
#define O_NOCTTY	010000
#define O_APPEND	 00010
*/
#define O_NOCTTY	0400
#define O_NONBLOCK	 00004
#define O_NDELAY	O_NONBLOCK
#define O_SYNC		040000
#define O_FSYNC		O_SYNC
#define O_ASYNC		020000	/* fcntl, for BSD compatibility */

#define F_DUPFD		0	/* dup */
#define F_GETFD		1	/* get f_flags */
#define F_SETFD		2	/* set f_flags */
#define F_GETFL		3	/* more flags (cloexec) */
#define F_SETFL		4
#define F_GETLK		7
#define F_SETLK		8
#define F_SETLKW	9

#define F_SETOWN	5	/*  for sockets. */
#define F_GETOWN	6	/*  for sockets. */

/* for F_[GET|SET]FL */
#define FD_CLOEXEC	1	/* actually anything with low bit set goes */

/* for posix fcntl() and lockf() */
#define F_RDLCK		1
#define F_WRLCK		2
#define F_UNLCK		8

/* for old implementation of bsd flock () */
#define F_EXLCK		16	/* or 3 */
#define F_SHLCK		32	/* or 4 */

/* operations for bsd flock(), also used by the kernel implementation */
#define LOCK_SH		1	/* shared lock */
#define LOCK_EX		2	/* exclusive lock */
#define LOCK_NB		4	/* or'd with one of the above to prevent
				   blocking */
#define LOCK_UN		8	/* remove lock */

/* c_cc characters */
#define VINTR 0
#define VQUIT 1
#define VERASE 2
#define VKILL 3
#define VEOF 4
#define VTIME 5
#define VMIN 6
#define VSWTC 7
#define VSTART 8
#define VSTOP 9
#define VSUSP 10
#define VEOL 11
#define VREPRINT 12
#define VDISCARD 13
#define VWERASE 14
#define VLNEXT 15
#define VEOL2 16

/* c_iflag bits */
#define IGNBRK	0000001
#define BRKINT	0000002
#define IGNPAR	0000004
#define PARMRK	0000010
#define INPCK	0000020
#define ISTRIP	0000040
#define INLCR	0000100
#define IGNCR	0000200
#define ICRNL	0000400
#define IXON	0001000
#define IXANY	0004000
#define IXOFF	0010000
#define IMAXBEL	   0020000
#define CRTS_IFLOW 0040000
#define CCTS_OFLOW 0100000
#define CIGNORE    0200000
/* c_oflag bits */
#define OPOST	0000001
#define ONLCR	0000002
#define OLCUC	0000004

#define OCRNL	0000010
#define ONOCR	0000020
#define ONLRET	0000040

#define OFILL	00000100
#define OFDEL	00000200
#define NLDLY	00001400
#define   NL0	00000000
#define   NL1	00000400
#define   NL2	00001000
#define   NL3	00001400
#define TABDLY	00006000
#define   TAB0	00000000
#define   TAB1	00002000
#define   TAB2	00004000
#define   TAB3	00006000
#define CRDLY	00030000
#define   CR0	00000000
#define   CR1	00010000
#define   CR2	00020000
#define   CR3	00030000
#define FFDLY	00040000
#define   FF0	00000000
#define   FF1	00040000
#define BSDLY	00100000
#define   BS0	00000000
#define   BS1	00100000
#define VTDLY	00200000
#define   VT0	00000000
#define   VT1	00200000
#define XTABS	01000000 /* Hmm.. Linux/i386 considers this part of TABDLY.. */

/* c_cflag bit meaning */
#define CBAUD	0000037
#define  B0	0000000		/* hang up */
#define  B50	0000001
#define  B75	0000002
#define  B110	0000003
#define  B134	0000004
#define  B150	0000005
#define  B200	0000006
#define  B300	0000007
#define  B600	0000010
#define  B1200	0000011
#define  B1800	0000012
#define  B2400	0000013
#define  B4800	0000014
#define  B9600	0000015
#define  B19200	0000016
#define  B38400	0000017
#define EXTA B19200
#define EXTB B38400
#define CBAUDEX 0000000
#define  B57600   00020
#define  B115200  00021
#define  B230400  00022
#define  B460800  00023

#define CSIZE	00001400
#define   CS5	00000000
#define   CS6	00000400
#define   CS7	00001000
#define   CS8	00001400

#define CSTOPB	00002000
#define CREAD	00004000
#define PARENB	00010000
#define PARODD	00020000
#define HUPCL	00040000

#define CLOCAL	00100000
#define CRTSCTS	  020000000000		/* flow control */
#define CRTSXOFF        010000000000


/* c_lflag bits */
#define ISIG	0x00000080
#define ICANON	0x00000100
#define XCASE	0x00004000
#define ECHO	0x00000008
#define ECHOE	0x00000002
#define ECHOK	0x00000004
#define ECHONL	0x00000010
#define NOFLSH	0x80000000
#define TOSTOP	0x00400000
#define ECHOCTL	0x00000040
#define ECHOPRT	0x00000020
#define ECHOKE	0x00000001
#define FLUSHO	0x00800000
#define PENDIN	0x20000000
#define IEXTEN	0x00000400

/* Values for the ACTION argument to `tcflow'.  */
#define	TCOOFF		0
#define	TCOON		1
#define	TCIOFF		2
#define	TCION		3

/* Values for the QUEUE_SELECTOR argument to `tcflush'.  */
#define	TCIFLUSH	0
#define	TCOFLUSH	1
#define	TCIOFLUSH	2

/* Values for the OPTIONAL_ACTIONS argument to `tcsetattr'.  */
#define	TCSANOW		0
#define	TCSADRAIN	1
#define	TCSAFLUSH	2
#endif /*_TERMIOS_H_*/

/* ioctls */
#define TIOCMGET	0x5415
#define TIOCMSET	0x5418
/* modem lines */
#define TIOCM_LE    0x001
#define TIOCM_DTR   0x002
#define TIOCM_RTS   0x004
#define TIOCM_ST    0x008
#define TIOCM_SR    0x010
#define TIOCM_CTS   0x020
#define TIOCM_CAR   0x040
#define TIOCM_RNG   0x080
#define TIOCM_DSR   0x100
#define TIOCM_CD    TIOCM_CAR
#define TIOCM_RI    TIOCM_RNG


