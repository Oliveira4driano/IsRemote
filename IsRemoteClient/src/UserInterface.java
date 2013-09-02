/*
 * User Interface Class for Client
 * @freedomofkeima - Iskandar Setiadi ( iskandarsetiadi@students.itb.ac.id )
 * August 2013
 *
 */

import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;



@SuppressWarnings("serial")
public class UserInterface extends JFrame {

	/** List of Attributes */
	private JPanel container; /* Main application panel */
	
	private TCPClient client; // TCP for client
	
	private static Point cursorScreen; // Cursor coordinates
	
	private static ArrayList<MouseEvent> mouseEvents; // ArrayList of MouseEvent
	
	private static ArrayList<KeyEvent> keyEvents; // ArrayList of KeyEvent
	
	private static final Object mouseEventsLock = new Object(); // thread-safe lock
	
	private static final Object keyEventsLock = new Object(); // thread-safe lock
	
	public UserInterface() {
		initComponents(); //initialize Screen
	}
	
	private synchronized void initComponents() {
		
		/** Initialize */
		mouseEvents = new ArrayList<MouseEvent>();
		keyEvents = new ArrayList<KeyEvent>();
		
		client = new TCPClient(RemotePreferences.defaultAddress, RemotePreferences.defaultPort);
		Thread clientThread = new Thread(client);
		clientThread.start();
		
		container = new JPanel() {
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				if (client.getScreen() != null) g.drawImage(client.getScreen(),0,0,this);
			}
		};
		
		/** Listener for Mouse Activities */
		container.addMouseListener(new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent e) {
				// Nothing to do
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// Nothing to do
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// Nothing to do
			}

			@Override
			public void mousePressed(MouseEvent e) {
				synchronized (mouseEventsLock) {
					mouseEvents.add(e);	
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				synchronized (mouseEventsLock) {
					mouseEvents.add(e);	
				}
			}
			
			
		});
		
		container.addMouseWheelListener(new MouseWheelListener() {

			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				synchronized (mouseEventsLock) {
					mouseEvents.add(e);	
				}
			}
			
		});
		
		
		/** Listener for Keyboard Activities */
		this.addKeyListener(new KeyListener() {

			@Override
			public void keyPressed(KeyEvent e) {
				synchronized (keyEventsLock) {
					keyEvents.add(e);
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				synchronized (keyEventsLock) {
					keyEvents.add(e);
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// Nothing to do
			}
			
		});
			
		add(container);
	    pack();
		setSize(RemotePreferences.defaultX + 3, RemotePreferences.defaultY + 25); //Set UI resolution
		// Note : 25 pixel = menu bar, 3 pixel = vertical bar
		setLocationRelativeTo(null); //Set UI Relative Location
	    setVisible(true); //Set UI Visibility
	    setResizable(false); //Set UI Resize-ability
	    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	    /** Thread for refreshing screen */
	    Thread actionViewer = new Thread() {
	    	@Override
	    	public void run() {
	    		while (true) {
	    			container.revalidate();
	    			container.repaint();
	    			
	    			/** Update cursor position */
	    			cursorScreen = getScreenPosition();
	    			try {
						Thread.sleep(RemotePreferences.sleepTime);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
	    		}	
	    	}
	    };
	    actionViewer.start();
	}
	
	/** Retrieve cursor coordinate on applet */
	public Point getScreenPosition() {
		Point ret = MouseInfo.getPointerInfo().getLocation();
		ret.setLocation(ret.x - this.getLocationOnScreen().x, ret.y - this.getLocationOnScreen().y);
		return ret;
	}
	
	public static Point getCursorScreen() {
		return cursorScreen;
	}
	
	/** Retrieve arraylist of MouseEvent */
	public static ArrayList<MouseEvent> getMouseEvent() {
		synchronized (mouseEventsLock) {
			ArrayList<MouseEvent> ret = new ArrayList<MouseEvent>(mouseEvents);
			mouseEvents.clear(); // reset ArrayList
			return ret;	
		}
	}
	
	public static boolean isMouseEventExist() {
		if (mouseEvents == null) return false;
		else return true;
	}
	
	/** Retrieve arraylist of KeyEvent */
	public static ArrayList<KeyEvent> getKeyEvent() {
		synchronized (keyEventsLock) {
			ArrayList<KeyEvent> ret = new ArrayList<KeyEvent>(keyEvents);
			keyEvents.clear(); // reset ArrayList
			return ret;
		}
	}
	
	public static boolean isKeyEventExist() {
		if (keyEvents == null) return false;
		else return true;
	}
	
}
