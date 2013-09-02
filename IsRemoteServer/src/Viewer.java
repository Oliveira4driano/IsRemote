/*
 * Viewer Class for IsRemote Server
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.Rectangle;
import java.awt.image.BufferedImage;


public class Viewer {
	
	/** List of Attributes */
	
	public static BufferedImage getScreenCapture() {
		
		BufferedImage image; //Return value
		
		Rectangle screenRectangle = new Rectangle(RemotePreferences.screenSize);
		image = RemoteController.getInstance().createScreenCapture(screenRectangle);
		
		return image;
	}
}
