/*
 * TCPServer Class for IsRemote Server
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;


public class TCPServer implements Runnable {
	
	/** List of attributes */
	private ServerSocket serverSocket; //Socket for server
	
	public TCPServer(int port){
		/** Activate Server */
		try {
			serverSocket = new ServerSocket(port);
			System.out.println("Server is now active at port " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		Socket connectionSocket;
		try {
			connectionSocket = serverSocket.accept();
			if (connectionSocket != null) {
				Client client = new Client(connectionSocket);
				System.out.println("A new client has been connected.");
				client.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

/*
 * Client class, each Client (piece) will run in different Thread
 */
class Client extends Thread {
	
    private Socket connectionSocket; //connection Socket
    
    /** Each Client is binded by one socket */
    public Client(Socket c) throws IOException {
        connectionSocket = c;
    }
    
    /** Run client */
    @SuppressWarnings("deprecation")
	public void run() {
    	BufferedImage screen;
    	while (connectionSocket.isConnected()) {
    		/** Process data */
    	    screen = Viewer.getScreenCapture();
    		/** Send data */
    		try {
				ImageIO.write(screen, "PNG", new MemoryCacheImageOutputStream(connectionSocket.getOutputStream()));
				System.out.println("Screen capture size :" + screen.getWidth(null) + " " + screen.getHeight(null));
			} catch (IOException e) {
	    		System.out.println("Client is disconnected");
	    		this.stop();
	    	   // exit Client
			}
    		
    		/** Receive all data */
    		try {
				DataInputStream info = new DataInputStream(connectionSocket.getInputStream());
				int cursorX = info.readInt();
				int cursorY = info.readInt();

				System.out.println("Cursor coordinate: " + cursorX + " " + cursorY);
				
				while (true) {
					int code = info.readInt();	
					if (code == RemotePreferences.mouseEOFCode) break;
					else {
						int buttonMask = code;
						int buttonAct = info.readInt();
						int xPosition = info.readInt();
						int yPosition = info.readInt();
						if (xPosition > 0 && xPosition < RemotePreferences.screenSize.getWidth()
								&& yPosition > 0 && yPosition < RemotePreferences.screenSize.getHeight()) {
							/** Move cursor to xPosition and yPosition */
							RemoteController.getInstance().mouseMove(xPosition, yPosition);
							/** Click Act */
							if (buttonAct == RemotePreferences.mousePressCode) RemoteController.getInstance().mousePress(buttonMask);
							/** Release Act */
							else if (buttonAct == RemotePreferences.mouseReleaseCode) RemoteController.getInstance().mouseRelease(buttonMask);
							/** Wheel Act */
							else RemoteController.getInstance().mouseWheel(buttonAct);
							
						}
						System.out.println("ButtonMask : " + buttonMask + " - buttonAct : " + buttonAct + " - Coordinate : " + xPosition + " " + yPosition);
					}
				}
				
				/** Move back cursor to (cursorX, cursorY) position */
				if (cursorX > 0 && cursorX < RemotePreferences.screenSize.getWidth()
						&& cursorY > 0 && cursorY < RemotePreferences.screenSize.getHeight()) {
					RemoteController.getInstance().mouseMove(cursorX, cursorY);
				}
				
				while (true) {
					int code = info.readInt();
					if (code == RemotePreferences.keyEOFCode) break;
					else {
						int keyMask = code;
						int keyCode = info.readInt();
						if (cursorX > 0 && cursorX < RemotePreferences.screenSize.getWidth()
								&& cursorY > 0 && cursorY < RemotePreferences.screenSize.getHeight()) {
							/** Key Press Act */
							if (keyMask == KeyEvent.KEY_PRESSED) RemoteController.getInstance().keyPress(keyCode);
							/** Key Release Act */
							else if (keyMask == KeyEvent.KEY_RELEASED) RemoteController.getInstance().keyRelease(keyCode);	
						}
						System.out.println("KeyMask : " + keyMask + " - keyCode : " + keyCode);
					}
				}
				
			} catch (IOException e) {
	    		System.out.println("Client is disconnected");
	    		this.stop();
	    	   // exit Client
			}
    		
    		Sleep();	
    	}
    	
    }
	
	@SuppressWarnings("deprecation")
	public void Sleep() {
		try {
			Thread.sleep(RemotePreferences.sleepTime);
		} catch (InterruptedException e) {
			this.stop();
		}
	}
    
    
}

