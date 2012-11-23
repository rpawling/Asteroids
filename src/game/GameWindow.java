package game;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Main Program which contains game display and logic.
 * 
 * Will contain input command handlers to control the ship and menu
 * 
 * Run thread will have a loop that cycles through all the
 * entities in the game asking them to move and then drawing them
 * in the appropriate place onto the back buffer.
 * 
 * The input handlers allow the play to control the ship
 * 
 * It might be informed when entities within our game
 * detect events (e.g. alien killed, played died) and will take
 * appropriate game actions.
 * 
 * @author Ryan Pawling, Chris Ochynski
 */

public class GameWindow extends Applet implements Runnable, KeyListener {
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private static final int xScreen = 500; // screen dimensions 500x400
    private static final int yScreen = 400;   
	private Image screen;      // this is an object which stores an image for the back buffer
	private Graphics backbf;   // this is the graphics in the image back buffer
	private long tStart, tEnd; // This is used to tell the elapsed time in the main thread
	private int period = 25;   // This is the period that the main thread is executed and screen is refreshed
							   // Note: If this is faster, the game will speed up!

	// init() is kind of like main for an applet
	public void init(){
		resize(xScreen,yScreen); // Set applet size to 500x400 for now
		//setBackground(Color.BLACK);
		addKeyListener(this); // let this class handle key press events
		screen = createImage(xScreen, yScreen); // This is a graphics buffer that is drawn while old image is displayed
		backbf = screen.getGraphics();       
		Thread screen_thread = new Thread(this); // thread for screen
		screen_thread.start();
	}
	public void paint(Graphics gfx){
		// This update will implement double buffering. This is done by creating a new
		// image object which is painted on while the old image is being displayed.  Once
		// the object has been full painted, it is displayed over the old image. This
		// helps prevent some nasty screen tearing and stuff.

		backbf.setColor(Color.black); // jank way to clear the screen first
		backbf.fillRect(0,0,xScreen,yScreen);

		// ADD CODE here to run through the objects lists and repaint based on their new coordinates

		gfx.drawImage(screen,0,0,this); // copy the backbuffer onto the screen
	}

	public void update(Graphics gfx){
		paint(gfx);
	}

	/**
	 * This method contains the code to run the game.
	 * It is executed by a thread.
	 */
	@Override
	public void run() {
		// Later, while(true) could be replaced with end-game logic
		while(true){
			tStart = System.currentTimeMillis();  // find start time of period

			// CODE HERE to execute one frame of the game
			// e.g. move objects, detect collisions, trigger animations ...

			repaint();

			// wait for a period before executing next iteration of the game
			try{ 
				tEnd=System.currentTimeMillis();
				// check if the period is smaller than the elapsed time
				// if so, then have the thread sleep while other processes run
				if(period-(tEnd-tStart)>0)
					Thread.sleep(period-(tStart-tEnd));
			}catch(InterruptedException e){
				// I heard it was a good idea to catch exceptions on this
				// Well I guess this means the timing messed up
			}
		}
	}

	/**
	 * These functions can process what key the user has hit,
	 * and have the ship respond accordingly
	 */
	public void keyPressed(KeyEvent kEvent){
		// example
		if(kEvent.getKeyCode()==KeyEvent.VK_UP){
			// Have the ship accelerate
		}
		//}else if
	}
	
	public void keyReleased(KeyEvent kEvent){
		// example
		if(kEvent.getKeyCode()==KeyEvent.VK_UP){
			// have ship stop accelerating
		}
	}
	
	/**
	 * Method used to detect when escape key has been pressed
	 * also probably menu navigation
	 */
	public void keyTyped(KeyEvent kEvent){
		if(kEvent.getKeyCode()==KeyEvent.VK_ESCAPE) {
			// open the options menu
		}
	}

}
