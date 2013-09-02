/*
 * Remote Controller Class for IsRemote Server
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.AWTException;
import java.awt.Robot;

public class RemoteController {
	
	/** List of Attributes */
	private static Robot robot; // robot for automaton
	private static Object lock = new Object(); // thread-safe lock
	
	protected static void initSingleton() {
		synchronized (lock) {
			try {
				/** Initialize robot */
				robot = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static Robot getInstance() {
		synchronized (lock) {
			/** if robot is null, then initSingleton */
			if (robot == null) initSingleton();
			return robot;
		}
	}

}
