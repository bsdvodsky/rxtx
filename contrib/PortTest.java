/*  A simple test to examine how Sun's CommAPI behaves with respect to
    read(...)

    Contributed By Bill Smith <bsmith@tridium.com>
*/
import java.util.*;
import javax.comm.*;
import java.io.*;

public class PortTest
{
  public static void main(String[] args)
  {
    String portName;
    CommPortIdentifier portId;
    Enumeration enum = CommPortIdentifier.getPortIdentifiers();

    System.out.println("Ports found:");

    while (enum.hasMoreElements()){
       portId = (CommPortIdentifier) enum.nextElement();
       System.out.println("  - " + portId.getName());
    }    

    if (args.length <= 0){
      System.out.println("usage: PortTest <portname>");
      return;
    }
      
    portName = args[0]; 
    
    try{
      portId = CommPortIdentifier.getPortIdentifier(portName);
    }
    catch(NoSuchPortException n){
      n.printStackTrace();
      return;
    }
    
    int val;
    byte[] valArray = new byte[128];
    
    try{
      SerialPort port = (SerialPort) portId.open("PortTest", 5000);           
      InputStream in = port.getInputStream();

      System.out.println("[" + portName + "] 'read()' test (0, 0):");
      port.disableReceiveTimeout();
      port.disableReceiveThreshold();
      val = in.read(); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read()' test (5000, 0):");      
      port.enableReceiveTimeout(5000);      
      val = in.read(); 
      System.out.println("[" + val + "]");    
  
      System.out.println("[" + portName + "] 'read()' test (5000, 5):");      
      port.enableReceiveThreshold(5);
      val = in.read(); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read()' test (0, 5):");      
      port.disableReceiveTimeout();
      val = in.read(); 
      System.out.println("[" + val + "]");    

      System.out.println("[" + portName + "] 'read(byte[])' test (0, 0):");
      port.disableReceiveTimeout();
      port.disableReceiveThreshold();
      val = in.read(valArray); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read(byte[])' test (5000, 0):");      
      port.enableReceiveTimeout(5000);      
      val = in.read(valArray); 
      System.out.println("[" + val + "]");    
  
      System.out.println("[" + portName + "] 'read(byte[])' test (5000, 5):");      
      port.enableReceiveThreshold(5);
      val = in.read(valArray); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read(byte[])' test (0, 5):");      
      port.disableReceiveTimeout();
      val = in.read(valArray); 
      System.out.println("[" + val + "]");    

      System.out.println("[" + portName + "] 'read(byte[], int, int)' test (0, 0):");
      port.disableReceiveTimeout();
      port.disableReceiveThreshold();
      val = in.read(valArray, 0, 3); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read(byte[], int, int)' test (5000, 0):");      
      port.enableReceiveTimeout(5000);      
      val = in.read(valArray, 0, 3); 
      System.out.println("[" + val + "]");    
  
      System.out.println("[" + portName + "] 'read(byte[], int, int)' test (5000, 5):");      
      port.enableReceiveThreshold(5);
      val = in.read(valArray, 0, 3); 
      System.out.println("[" + val + "]");    
      
      System.out.println("[" + portName + "] 'read(byte[], int, int)' test (0, 5) (len = 3):");      
      port.disableReceiveTimeout();
      val = in.read(valArray, 0, 3); 
      System.out.println("[" + val + "]");    

      System.out.println("[" + portName + "] 'read(byte[], int, int)' test (0, 5) (len = 10):");      
      port.disableReceiveTimeout();
      val = in.read(valArray, 0, 10); 
      System.out.println("[" + val + "]");    
      
      port.close();    
    }
    catch(Exception e){
      e.printStackTrace(); 
      return;
    }    
  }  
}

-----Original Message-----
From: taj@parcelfarce.linux.theplanet.co.uk
[mailto:taj@parcelfarce.linux.theplanet.co.uk]
Sent: Wednesday, September 04, 2002 9:55 PM
To: Bill Smith
Cc: Dan Giorgis
Subject: RE: thresholds


On Wed, 4 Sep 2002, Bill Smith wrote:

> Trent,
>  
> I played with the sun/win32 comm package and here are the results.
>  
> for read():
>     timeout = threshold = 0, blocks until 1 byte is available
>     timeout > 0, threshold = 0, blocks until timeout occurs, returns -1 on timeout
>     timeout & threshold > 0, blocks until timeout, returns - 1 on timeout, magnitude of threshold doesn't play a role.
>     timeout = 0, threshold > 0, blocks until 1 byte, magnitude of  threshold doesn't play a role
>  
> for read(byte[]):
>     timeout = threshold = 0, blocks until 1 byte is available
>     timeout > 0, threshold = 0, blocks until timeout occurs, returns 0 on timeout
>     timeout & threshold > 0, blocks until timeout or reads threshold bytes, returns 0 on timeout
>     timeout = 0, threshold > 0, blocks until reads threshold bytes
>  
> I didn't test read(byte[], int, int) but I speculate it's the same as read(byte[]). I think what I prepose below can do all this.
> A little tweaking will be required for the read() vs read(byte[]), but shouldn't be bad.
>  


Hi Bill:

This is actually very close to the InputStream Documentation mentioned 
earlier.   I only point this out because if its true, 
tread(byte[],int,int) will not block all the time for the first byte.

I'm all for getting something together and getting some tests going.  The 
test suite on win32 is proving problematic but I've got new tests comming.  
I'd rather release after this and get the win32 tests going when the 
material comes.

Here, again, is the InputStream Documentation for comparison:
(I underlined the important notes)

read

public abstract int read()
                  throws IOException

      Reads the next byte of data from the input stream. The value byte is 
returned as an int in the range
      0 to 255. If no byte is available because the end of the stream has 
been reached, the value -1 is
      returned. This method blocks until input data is available, the end
                -------------------------------------------------------- 
of the stream is detected, or an exception is thrown.
----------------------------------------------------- 

      A subclass must provide an implementation of this method.
      Returns:
            the next byte of data, or -1 if the end of the stream is 
reached.
      Throws:
            IOException - if an I/O error occurs.

public int read(byte[] b)
         throws IOException

      Reads some number of bytes from the input stream and stores them 
into the buffer array b. The
      number of bytes actually read is returned as an integer. This method 
                                                               -----------
blocks until input data is available, end of file is detected, or an 
--------------------------------------------------------------------
exception is thrown. 
--------------------

      If b is null, a NullPointerException is thrown. If the length of b 
is zero, then no bytes are
      read and 0 is returned; otherwise, there is an attempt to read at 
least one byte. If no byte is available
      because the stream is at end of file, the value -1 is returned; 
otherwise, at least one byte is read and
      stored into b. 

      The first byte read is stored into element b[0], the next one into 
b[1], and so on. The number of
      bytes read is, at most, equal to the length of b. Let k be the 
number of bytes actually read; these
      bytes will be stored in elements b[0] through b[k-1], leaving 
elements b[k] through
      b[b.length-1] unaffected. 

      If the first byte cannot be read for any reason other than end of 
file, then an IOException is
      thrown. In particular, an IOException is thrown if the input stream 
has been closed. 

      The read(b) method for class InputStream has the same effect as: 

       read(b, 0, b.length) 

      Parameters:
            b - the buffer into which the data is read.
      Returns:
            the total number of bytes read into the buffer, or -1 is there 
is no more data because the end
            of the stream has been reached.
      Throws:
            IOException - if an I/O error occurs.
      See Also: 
            read(byte[], int, int)

read

public int read(byte[] b,
                int off,
                int len)
         throws IOException

      Reads up to len bytes of data from the input stream into an array of 
bytes. An attempt is made to
       ---------------------
      read as many as len bytes, but a smaller number may be read, 
      ------------------------------------------------------------
possibly zero. The number of bytes
--------------
      actually read is returned as an integer. 

      This method blocks until input data is available, end of file is 
detected, or an exception is thrown. 

      If b is null, a NullPointerException is thrown. 

      If off is negative, or len is negative, or off+len is greater than 
the length of the array b, then an
      IndexOutOfBoundsException is thrown. 

      If len is zero, then no bytes are read and 0 is returned; otherwise, 
there is an attempt to read at
      least one byte. If no byte is available because the stream is at end 
of file, the value -1 is returned;
      otherwise, at least one byte is read and stored into b. 

      The first byte read is stored into element b[off], the next one into 
b[off+1], and so on. The
      number of bytes read is, at most, equal to len. Let k be the number 
of bytes actually read; these
      bytes will be stored in elements b[off] through b[off+k-1], leaving 
elements b[off+k]
      through b[off+len-1] unaffected. 

      In every case, elements b[0] through b[off] and elements b[off+len] 
through
      b[b.length-1] are unaffected. 

      If the first byte cannot be read for any reason other than end of 
file, then an IOException is
      thrown. In particular, an IOException is thrown if the input stream 
has been closed. 

      The read(b, off, len) method for class InputStream simply calls the 
method read()
      repeatedly. If the first such call results in an IOException, that 
exception is returned from the call
      to the read(b, off, len) method. If any subsequent call to read() 
results in a IOException,
      the exception is caught and treated as if it were end of file; the 
bytes read up to that point are stored
      into b and the number of bytes read before the exception occurred is 
returned. Subclasses are
      encouraged to provide a more efficient implementation of this 
method.
      Parameters:
            b - the buffer into which the data is read.
            off - the start offset in array b at which the data is 
written.
            len - the maximum number of bytes to read.
      Returns:
            the total number of bytes read into the buffer, or -1 if there 
is no more data because the end
            of the stream has been reached.
      Throws:
            IOException - if an I/O error occurs.
      See Also: 
            read()

-- 
Trent Jarvi
taj@www.linux.org.uk

