# this is not ready for prime time.
# It's in the process of being removed from the build process and being
# used external to the .tgz
# Trent Jarvi jarvi@ezlink.com
Summary: RXTX -- W/Escient Mods
Name: rxtx
%define version 1.2-beta
%define release 1
Version: %{version}
Release: %{release}
Copyright: GPL (programs), relaxed LGPL (libraries), and public domain (docs)
Group: Applications/compiler
Source: rxtx-1.2-beta-1.tar.gz
Provides: libserial.so libserial.la libserial.so.0 libserial.so.0.1.0 jcl.jar
BuildRoot: $HOME/redhat/root

%description
rxtx is a native interface to serial ports in java.

%prep
echo "In Prep."

%build
cd rxtx
./configure --prefix=$HOME/redhat/INSTALLROOT/usr/local
make clean
make

%install
cd rxtx
rm -rf $RPM_BUILD_ROOT/usr/local/lib/libtya.so
mkdir -p $RPM_BUILD_ROOT/usr/local/lib
mkdir -p $RPM_BUILD_ROOT/usr/share/jre/bin
make ROOT="$RPM_BUILD_ROOT" install

%post
/sbin/ldconfig

%files
%attr(755, root, root) /usr/local/lib/libSerial.so.0.1.0
%attr(777, root, root) /usr/local/lib/libSerial.so.0
%attr(777, root, root) /usr/local/lib/libSerial.so
%attr(755, root, root) /usr/local/lib/libSerial.la
%attr(644, root, root) /usr/local/lib/libSerial.a
%attr(644, root, root) /usr/share/jre/bin/jcl.jar
