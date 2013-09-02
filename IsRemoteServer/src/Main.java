/*
 * Main Application Class for IsRemote Server
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

public class Main {
	
	private static TCPServer server; // server for IsRemote
	
	public static TCPServer getServer() {
		if (server == null) initSingleton();
		return server;
	}
	
	public static void initSingleton() {
		server = new TCPServer(RemotePreferences.defaultPort);
		Thread serverThread = new Thread(server);
		serverThread.start();
	}
	
	public static void main(String args[]) {
		initSingleton();
	}

}
