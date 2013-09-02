/*
 * Remote Preferences Class for Server
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.Dimension;
import java.awt.Toolkit;


public class RemotePreferences {
	
	/** List of preferences */
	
	public static String saveDirectory = System.getProperty("user.dir"); // default save directory
	
	public static String defaultAddress = "127.0.0.1"; // default IP address
	
	public static int defaultPort = 6999; // default port
	
	public static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); // default screen size

	public static int sleepTime = 300; // default sleep time
	
	public static int mousePressCode = 9997; // if mouse is pressed

	public static int mouseReleaseCode = 9998; // if mouse is released
	
	public static int mouseEOFCode = -9999; // eof code
	
	public static int keyEOFCode = -9996; // eof code
}
