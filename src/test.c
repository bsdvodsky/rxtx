#include <windows.h>
#include "win32s.h"
#include "stdlib.h"

int fd;
char str[] = {0x55, 0xaa};

void test_timeout() {
	int len, i;
	for (i=0; i<10; i++) {
		len = read(fd, str, 2);
		printf("%d:read: %d\n", i, len);
	}
}

void print_c_iflag(struct termios *ttyset) {
	/* input flags c_iflag */
	printf("input flags: ");
	if (ttyset->c_iflag & INPCK)
		printf("INPCK ");
	if (ttyset->c_iflag & IGNPAR)
		printf("IGNPAR ");
	if (ttyset->c_iflag & PARMRK)
		printf("PARMRK ");
	if (ttyset->c_iflag & ISTRIP)
		printf("ISTRIP ");
	if (ttyset->c_iflag & IGNBRK)
		printf("IGNBRK ");
	if (ttyset->c_iflag & BRKINT)
		printf("BRKINT ");
	if (ttyset->c_iflag & IGNCR)
		printf("IGNCR ");
	if (ttyset->c_iflag & ICRNL)
		printf("ICRNL ");
	if (ttyset->c_iflag & INLCR)
		printf("ICRNL ");
	if (ttyset->c_iflag & IXOFF)
		printf("IXOFF ");
	if (ttyset->c_iflag & IXON)
		printf("IXON ");
	if (ttyset->c_iflag & IXON)
		printf("IXANY ");
	if (ttyset->c_iflag & IMAXBEL)
		printf("IMAXBEL ");
	printf("\n");
}


void print_c_oflag(struct termios *ttyset) {
	/* output flags c_oflag */
	printf("output flags: ");
	if (ttyset->c_oflag & OPOST)
		printf("OPOST ");
	if (ttyset->c_oflag & ONLCR)
		printf("ONLCR ");
/*	if (ttyset->c_oflag & OXTABS)
		printf("OXTABS ");	*/
/*	if (ttyset->c_oflag & ONOEOT)
		printf("ONOEOT ");	*/
	printf("\n");
}

void print_c_lflag(struct termios *ttyset) {
	if (ttyset->c_lflag & ICANON)
		printf("ICANON");
	if (ttyset->c_lflag & ECHO)
		printf("ECHO");
	if (ttyset->c_lflag & ECHOE)
		printf("ECHOE");
	if (ttyset->c_lflag & ECHOPRT)
		printf("ECHOPRT");
	if (ttyset->c_lflag & ECHOK)
		printf("ECHOK");
	if (ttyset->c_lflag & ECHOKE)
		printf("ECHOKE");
	if (ttyset->c_lflag & ECHONL)
		printf("ECHONL");
	if (ttyset->c_lflag & ECHOCTL)
		printf("ECHOCTL");
	if (ttyset->c_lflag & ISIG)
		printf("ISIG");
	if (ttyset->c_lflag & IEXTEN)
		printf("IEXTEN");
	if (ttyset->c_lflag & NOFLSH)
		printf("NOFLSH");
	if (ttyset->c_lflag & TOSTOP)
		printf("TOSTOP");
/*	if (ttyset->c_lflag & ALTWERASE)
		printf("ALTWERASE");	*/
	if (ttyset->c_lflag & FLUSHO)
		printf("FLUSHO");
/*	if (ttyset->c_lflag & NOKERNINFO)
		printf("NOKERNINFO");	*/
	if (ttyset->c_lflag & PENDIN)
		printf("PENDIN");
}

void print_c_cflag(struct termios *ttyset) {
}

void print_speed(speed_t speed) {
	printf(" baudrate: ");
	switch (speed) {
		case B0:		printf("B0"); break;
		case B50:		printf("B50"); break;
		case B75:		printf("B75"); break;
		case B110:		printf("B110"); break;
		case B134:		printf("B134"); break;
		case B150:		printf("B150"); break;
		case B200:		printf("B200"); break;
		case B300:		printf("B300"); break;
		case B600:		printf("B600"); break;
		case B1200:		printf("B1200"); break;
		case B1800:		printf("B1800"); break;
		case B2400:		printf("B2400"); break;
		case B4800:		printf("B4800"); break;
		case B9600:		printf("B9600"); break;
		case B19200:	printf("B19200"); break;
		case B38400:	printf("B38400"); break;
		case B57600:	printf("B57600"); break;
		case B115200:	printf("B115200"); break;
		case B230400:	printf("B230400"); break;
		case B460800:	printf("B460800"); break;
		default:		printf("UNKNOWN: %#x", speed); break;
	}
	printf("\n");
}

void write_test() {
	char ch[256];
	int i;
	for(i=0; i<256; i++)
		ch[i] = i;
	for(i=0; i<20; i++) {
		printf(".");
		write(fd, ch, 256);
	}
}

void read_test() {
	char ch;
	while(1) {
		read(fd, &ch, 1);
		printf("%c", ch);
	}
}

void dtr_rts_test() {
	int arg;
	ioctl(fd, TIOCMGET, &arg);
	while(1) {
		arg |= TIOCM_DTR;
		ioctl(fd, TIOCMSET, &arg);
		sleep(250);
		arg &= ~TIOCM_RTS;
		ioctl(fd, TIOCMSET, &arg);
		sleep(250);
		arg &= ~TIOCM_DTR;
		ioctl(fd, TIOCMSET, &arg);
		sleep(250);
		arg |= TIOCM_RTS;
		ioctl(fd, TIOCMSET, &arg);
		sleep(250);
	}
}

int main() {
	struct termios ttyset;
	fd = open("COM2", 0);
	if (fd < 0)
		return -1;
	printf("fd: %d\n", fd);

	tcgetattr(fd, &ttyset);

	printf("input");
	print_speed(ttyset.c_ispeed);
	printf("output");
	print_speed(ttyset.c_ospeed);

	print_c_iflag(&ttyset);

	/* output flags: c_oflag */
	/* control flags: c_cflag */
	/* local flags: c_lflag */
	/* c_cc */

/*	cfsetospeed(&ttyset, B57600);
	cfsetispeed(&ttyset, B57600);	*/

/*	ttyset.c_cc[VMIN] = 0;
	ttyset.c_cc[VTIME] = 10;	*/
	tcsetattr(fd, TCSANOW, &ttyset);

//	dtr_rts_test();
	read_test();
//	write_test();
	close(fd);
	return 0;
}
