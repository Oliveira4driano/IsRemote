/*
 * Main Application Class for IsRemote Client
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */
import java.util.Scanner;

import sun.net.util.IPAddressUtil;

public class Main {
	
	public static void main(String args[]) {
		/** Get Server IP address from User */
		System.out.println("--Welcome to Sister Network--");
		System.out.print("Enter Server IPv4 Address: ");
		Scanner sc=new Scanner(System.in);
		String userInput = sc.nextLine();
		/** Check the validity of IPv4 Address */
		if (IPAddressUtil.isIPv4LiteralAddress(userInput)) {
			RemotePreferences.defaultAddress = userInput;	
		}
		System.out.print("Enter Screen Width: ");
		RemotePreferences.defaultX = Integer.parseInt(sc.nextLine());
		System.out.print("Enter Screen Height: ");
		RemotePreferences.defaultY = Integer.parseInt(sc.nextLine());
	    new UserInterface(); //Create an User Interface
	}

}
