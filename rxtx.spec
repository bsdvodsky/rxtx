# this is not ready for prime time.
# It's in the process of being removed from the build process and being
# used external to the .tgz
# Trent Jarvi taj@www.linux.org.uk
Summary: RXTX -- W/Escient Mods
Name: rxtx
%define version 1.3
%define release 1
Version: %{version}
Release: %{release}
Copyright: GPL (programs), relaxed LGPL (libraries), and public domain (docs)
Group: Applications/compiler
Source: lpr-%{PACKAGE_VERSION}.tar.gz
Provides: libserial.so libserial.so.0 libserial.so.0.1.0 jcl.jar
BuildRoot: /var/tmp/rxtx-root

%description
rxtx is a native interface to serial ports in java.

%prep
%setup -q

%build
cd rxtx
./configure --prefix=$BuildRoot/usr
make clean
make

%install
cd rxtx
mkdir -p $BuildRoot/usr/{lib,local}
make ROOT="$BuildRoot" install

%post
/sbin/ldconfig

%files
%attr(755, root, root) /usr/lib/libSerial.so.0.1.0
%attr(777, root, root) /usr/lib/libSerial.so.0
%attr(777, root, root) /usr/lib/libSerial.so
%attr(644, root, root) /usr/local/java/lib/jcl.jar
