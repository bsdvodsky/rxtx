#-------------------------------------------------------------------------
#   rxtx is a native interface to serial ports in java.
#   Copyright 1997, 1998, 1999 by Trent Jarvi jarvi@ezlink.com.
#
#   This library is free software; you can redistribute it and/or
#   modify it under the terms of the GNU Library General Public
#   License as published by the Free Software Foundation; either
#   version 2 of the License, or (at your option) any later version.
#
#   This library is distributed in the hope that it will be useful,
#   but WITHOUT ANY WARRANTY; without even the implied warranty of
#   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
#   Library General Public License for more details.
#
#   You should have received a copy of the GNU Library General Public
#   License along with this library; if not, write to the Free
#   Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
#-------------------------------------------------------------------------

AUTOMAKE_OPTIONS = foreign
TOP=@TOP@
JPATH=@JPATH@
INCLUDES= -I $(TOP) -I $(TOP)/$(target_alias) -I . -I $(JAVAINCLUDE) -I $(JAVANATINC)
NSJAVA=1
JAVAC=set NS_JAVA="$(NSJAVA)";export NS_JAVA;$(JPATH)/bin/javac $(JCLASSPATH):$(COMM_JAR_PATH):$(JCL_JAR_PATH) -d $(TOP)/ -O 
JAVAH=set NS_JAVA="$(NSJAVA)";export NS_JAVA;$(JPATH)/bin/javah $(JCLASSPATH) -d $(TOP)/$(target_alias) -jni
JAVA=set NS_JAVA="$(NSJAVA)";$(JPATH)/bin/java $(JCLASSPATH)
JAR=$(JPATH)/bin/jar cf
JAVADOC=$(JPATH)/bin/javadoc $(JCLASSPATH)
LIBTOOL = $(SHELL) $(TOP)/libtool
LINK = $(LIBTOOL) --mode=link $(CC) $(CFLAGS) $(LDFLAGS) -version-info 1:0:1
HEADER = $(TOP)/$(target_alias)/gnu_io_RXTXPort.h
CLEANFILES = .deps/* $(TOP)/*.class $(TOP)/gnu/io/*.class \
              $(TOP)/*/.libs/* $(TOP)/stamp-h
#LTLIBRARIES =     $(lib_LTLIBRARIES)
#BUILT_SOURCES = $(TOP)/$(target_alias)/libSerial.la
SUFFIXES = .java .class
SUFFIXES = .class .h

install: all jcl install-lib

install-lib:
	( cd $(TOP)/$(target_alias) ; \
	$(LIBTOOL) --mode=install $(INSTALL_PROGRAM) libSerial.la $(libdir));\
	$(INSTALL_PROGRAM) jcl.jar $(JPATH)lib/
run: $(TOP)/test.class all jcl
	(cd $(TOP)/;chmod 755 *.class;\
	set THREADS_FLAG="$(THREADS_FLAG)";export THREADS_FLAG;\
	LD_LIBRARY_PATH="$(target_alias)";\
	export LD_LIBRARY_PATH;\
	$(JAVA) test)

all: $(TOP)/$(target_alias)/@TARGETLIB@ 
	@echo ""
	@echo "If you have javax.comm.* and java.comm.* in your CLASSPATH, type make jcl to"
	@echo "build the javacomm20-x86.tar.Z supporting classes."
	@echo ""
	@echo "Note: CLASSPATH was grabbed during the configure stage.  Modification of the"
	@echo "environment variable will not change the behavior of make without rerunning" 
	@echo "configure or editing the Makefile."
	@echo ""

jcl:  $(TOP)/gnu/io/RXTXCommDriver.class $(TOP)/gnu/io/RXTXPort.class
	$(JAR) jcl.jar gnu/io/*

$(TOP)/gnu/io/RXTXCommDriver.class $(TOP)/gnu/io/RXTXPort.class \
		: $(srcdir)/src/RXTXCommDriver.java \
		$(srcdir)/src/RXTXPort.java 
	$(JAVAC) $(srcdir)/src/RXTXPort.java $(srcdir)/src/RXTXCommDriver.java

$(TOP)/$(target_alias)/libSerial.la: $(TOP)/$(target_alias)/SerialImp.lo
	( cd $(TOP)/$(target_alias); \
	$(LINK) -o libSerial.la -rpath $(libdir) $(<F); \
	for I in .libs/*.so*;do cp $$I . ;done;)

$(TOP)/$(target_alias)/SerialImp.lo: $(srcdir)/src/SerialImp.c \
                                  $(HEADER) \
				  $(TOP)/Makefile
	(cd $(TOP)/$(target_alias); \
	$(LIBTOOL) --mode=compile $(CC) $(INCLUDES) $(CPPFLAGS) $(CFLAGS) @VERBOSE_IOEXCEPTIONS@ -c ../$< )

$(TOP)/%.class $(TOP)/gnu/io/%.class: $(srcdir)/src/%.java 
	$(JAVAC) $< 

$(TOP)/$(target_alias)/%.h: $(TOP)/gnu/io/RXTXPort.class
	$(JAVAH) gnu.io.`echo $(@F) | sed s#\.h##|sed s#gnu_io_##`
	touch $(TOP)/$(target_alias)/$(@F)

docs:
	mkdir gnu;
	cd gnu;ln -s $(srcdir)/src io
	cd ..
	$(JAVADOC)  -d doc gnu.io
	rm -rf gnu

# END
