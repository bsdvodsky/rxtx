#include <windows.h>
#include <stdlib.h>
#include "win32s.h"

int fd;

void loop() {
	char *in = malloc(sizeof(char)*256);
	char *line = in, ch, prevch = 0;
	while (1) {
		while ((*in = getchar()) != '\n')
			in++;
		*in = '\r';	/* convert lf to cr */
		*++in = '\0';
		in = line;
		write(fd, line, strlen(line));
		while(read(fd, &ch, 1) > 0) {
			if (ch == '\r' && prevch != '\r') {
//				if (prevch == '\r' && ch == '\r')
					printf("\n");
			} else printf("%c", ch);
			prevch = ch;
		}
	}
}

int main() {
	struct termios ttyset;
	char ir[3];
	fd = open("COM2", 0);
	if (fd < 0)
		return -1;

	tcgetattr(fd, &ttyset);

	cfsetospeed(&ttyset, B19200);
	cfsetispeed(&ttyset, B19200);
	ttyset.c_cc[VMIN] = 0;
	ttyset.c_cc[VTIME] = 2;
	tcsetattr(fd, TCSANOW, &ttyset);

	while(read(fd, ir, 2) < 1) {	/* init micro488 */
		printf(".");
		write(fd, "\r", 1);
	}
	if (ir[0] != '\r' && ir[1] != '>') {
		printf("cannot init micro488\n");
		return -1;
	}
	ir[3] = '\0';
	printf("\n%s", ir);

	loop();

	return 0;
}
