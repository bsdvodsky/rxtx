package gnu.io;
import javax.comm.*;

/**
A class to keep the current version in
*/

public class RXTXVersion
{
/*------------------------------------------------------------------------------
	RXTXVersion  
	accept:       -
	perform:      Set Version.
	return:       -
	exceptions:   Throwable
	comments:     
		      See INSTALL for details.
------------------------------------------------------------------------------*/
	private static String Version;

	static {
		Version = "RXTX-1.4-9";
	}
	/**
	*  static method to return the current version of RXTX
	*  unique to RXTX.
	*  @returns a string representing the version  "RXTX-1.4-9"
	*/
	public static String getVersion()
	{
		return(Version);
	}
}
