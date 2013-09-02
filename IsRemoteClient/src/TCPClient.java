/*
 * TCPClient Class for IsRemote Client
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.Image;
import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;


public class TCPClient implements Runnable {
	
	/** List of attributes */
	private Socket clientSocket; //Socket for TCP Client
	private String ip; //Connect to this IP
	private int port; //Connect to this port
	
	private Image screen; //Screen
	
	public TCPClient(String _ip, int _port) {
		this.ip = _ip;
		this.port = _port;
	}

	@Override
	public void run() {
		/** Send MouseEvents and KeyEvents while receiving Screen Capture */
		try {
			clientSocket = new Socket(ip, port); //Binding ip at port
			System.out.println("Client is now ready.");
			
			BufferedImage image;
			ImageIcon imageIcon;
			
			while (clientSocket.isConnected()) {
				/** Receive all data */
				image = ImageIO.read(ImageIO.createImageInputStream(clientSocket.getInputStream()));
				if (image != null) {
					imageIcon = new ImageIcon(image);
					if ((image.getWidth() == RemotePreferences.defaultX) && (image.getHeight() == RemotePreferences.defaultY)) {
						screen = imageIcon.getImage();		
						System.out.println(screen.getWidth(null) + " " + screen.getHeight(null));
					}
					
					/** Send data */
					DataOutputStream info = new DataOutputStream(clientSocket.getOutputStream());
					/** Cursor coordinates */
					if (UserInterface.getCursorScreen() != null) {
						Point position = UserInterface.getCursorScreen();
						info.writeInt(position.x - 3); // vertical bar = 3 pixel
						info.writeInt(position.y - 25);	// menu bar = 25 pixel
						System.out.println("Cursor sent : " + (position.x - 3) + " " + (position.y - 25));
					} else {
						info.writeInt(-1);
						info.writeInt(-1);
					}
					if (UserInterface.isMouseEventExist()) {
						ArrayList<MouseEvent> mouseEvents = new ArrayList<MouseEvent>(UserInterface.getMouseEvent());
						for (int i = 0; i < mouseEvents.size(); i++) {
							int buttonMask = 0;
							if (mouseEvents.get(i).getButton() == MouseEvent.BUTTON1) buttonMask = InputEvent.BUTTON1_MASK;
							if (mouseEvents.get(i).getButton() == MouseEvent.BUTTON2) buttonMask = InputEvent.BUTTON2_MASK;
							if (mouseEvents.get(i).getButton() == MouseEvent.BUTTON3) buttonMask = InputEvent.BUTTON3_MASK;
							info.writeInt(buttonMask); // write buttonMask
							switch (mouseEvents.get(i).getID()) {
							case MouseEvent.MOUSE_PRESSED : info.writeInt(RemotePreferences.mousePressCode); // Code for mouse pressed
							                                info.writeInt(mouseEvents.get(i).getX());
							                                info.writeInt(mouseEvents.get(i).getY());
								                            System.out.println("Mouse pressed at Coordinate : " + mouseEvents.get(i).getX() + " " + mouseEvents.get(i).getY());
															break;
							case MouseEvent.MOUSE_RELEASED : info.writeInt(RemotePreferences.mouseReleaseCode); // Code for mouse released
                                                             info.writeInt(mouseEvents.get(i).getX());
                                                             info.writeInt(mouseEvents.get(i).getY());
                                                             System.out.println("Mouse released at Coordinate : " + mouseEvents.get(i).getX() + " " + mouseEvents.get(i).getY());
							                                 break;
							case MouseEvent.MOUSE_WHEEL    : info.writeInt(((MouseWheelEvent) mouseEvents.get(i)).getUnitsToScroll()); // Number of wheel
                                                             info.writeInt(mouseEvents.get(i).getX());
                                                             info.writeInt(mouseEvents.get(i).getY());
                                                             System.out.println("Mouse wheel at Coordinate : " + mouseEvents.get(i).getX() + " " + mouseEvents.get(i).getY());
                                                             break;
							}
						}
					}
					// EOF code
					info.writeInt(RemotePreferences.mouseEOFCode);
					
					if (UserInterface.isKeyEventExist()) {
						ArrayList<KeyEvent> keyEvents = new ArrayList<KeyEvent>(UserInterface.getKeyEvent());
						for (int i = 0; i < keyEvents.size(); i++) {
							info.writeInt(keyEvents.get(i).getID());
							switch (keyEvents.get(i).getID()) {
							case KeyEvent.KEY_PRESSED : info.writeInt(keyEvents.get(i).getKeyCode());
								                        System.out.println("Key with KeyCode " + keyEvents.get(i).getKeyCode() + " is pressed");
								                        break;
							case KeyEvent.KEY_RELEASED : info.writeInt(keyEvents.get(i).getKeyCode());
	                                                     System.out.println("Key with KeyCode " + keyEvents.get(i).getKeyCode() + " is released");
	                                                     break;								                        
							}
						}
					}
					//EOF code
					info.writeInt(RemotePreferences.keyEOFCode);
				}

			}
	    	if (!clientSocket.isConnected()) {
	    		System.out.println("Server is disconnected");
	    		Thread.currentThread().interrupt();
	    	}
		} catch (UnknownHostException e) {
		//	e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Server is not available");
			Thread.currentThread().interrupt();
		}
		
	}

	public Image getScreen() {
		return screen;
	}

	public void setScreen(Image _screen) {
		this.screen = _screen;
	}

}
