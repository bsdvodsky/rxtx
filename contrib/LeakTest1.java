/*
	
	test   :  LeakTest1
	Author :  MathWorks
	added  :  Thu Jul 26 16:43:08 MDT 2001
	Problem:  open() can leak memory in some CommAPI implementations
		  when called multiple times.
*/

import javax.comm.*;

public class LeakTest1
{
	public static void main(String args[]){
		CommPortIdentifier portId;
		SerialPort serialPort;

		int i=0;
		while (true){
			try{
				portId = CommPortIdentifier.getPortIdentifier(
								"/dev/cua0"
							);
				serialPort = (SerialPort)portId.open(
								"/dev/cua0", 2000
							);
				serialPort.close();
				System.gc();
				if(!(++i%1000 > 0))
					System.out.println(i);
			}catch (Exception ie){}
		}
	}
}

