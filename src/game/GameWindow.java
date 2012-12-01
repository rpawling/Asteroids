package game;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


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
	public static boolean boolPause = false;
	public static boolean boolGravity = false;
	public static boolean boolGVisible = false;
	public static boolean boolUnlimitedLives = false;
	public static boolean boolAsteroids = true;
	public static final int xScreen = 500; // screen dimensions 500x400 or 800x600
	public static final int yScreen = 400;
	public int level = 1;		// indicates level and # of asteroids
	public static boolean paused = false; // set to true while menu is open, so that game stops
	private Image screen;      // this is an object which stores an image for the back buffer
	private Graphics backbf;   // this is the graphics in the image back buffer
	private long tStart, tEnd; // This is used to tell the elapsed time in the main thread
	private int period = 25;   // This is the period that the main thread is executed and screen is refreshed
	// Note: If this is faster, the game will speed up!
	private Ship player1;
	private Asteroid asteroid1, asteroid2, asteroid3;
	private ArrayList<Asteroid> asteroidList = new ArrayList<Asteroid>();
	private Asteroid[] asteroidArray;
	private int numAsteroids;

	// init() is kind of like main for an applet
	public void init(){
		//SoundAsteroids.loadSound(); //load all sounds used in game
		resize(xScreen,yScreen); // Set applet size to 500x400 for now
		//setBackground(Color.BLACK);
		addKeyListener(this); // let this class handle key press events
		screen = createImage(xScreen, yScreen); // This is a graphics buffer that is drawn while old image is displayed
		backbf = screen.getGraphics();

		player1 = new Ship(xScreen/2,yScreen/2);
		this.generateAsteroids();

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

		if (!boolPause){
			player1.draw(backbf);
			for(int i=0; i<asteroidList.size();i++) {
				asteroidList.get(i).draw(backbf);
			}
			ArrayList<Shot> shotList = player1.getShots();
			for (int i=0; i<shotList.size(); i++) {
				shotList.get(i).draw(backbf);
			}
		}
		else {
			Menu.draw(backbf);
		}


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

			// If level complete, advance to next level
			if (asteroidList.isEmpty()) {
				level++;
				this.generateAsteroids();
			}

			player1.update();
			// update the shot arrays
			ArrayList<Shot> shotList = player1.getShots();
			for (int i=0; i<shotList.size(); i++) {
				shotList.get(i).update();
			}
			// *************************************************************************
			// update the asteroid arrays and check asteroids for contacts
			// if there is contact with player, delete player and remove one life
			// if there is contact with shot, delete asteroid and spawn smaller sizes
			// *************************************************************************
			outerLoop:
				for (int i=0; i<asteroidList.size(); i++) {
					asteroidList.get(i).update();
					if (asteroidList.get(i).checkForContact(player1.getX(), player1.getY(), player1.getRadius())) {
						//asteroidList.remove(i);
						//continue outerLoop;
						// change to remove player
					}
					
					// Check if the player's shot has hit an asteroid
					// If so, split the asteroid to the smaller size if available
					// Delete the asteroid
					for (int j=0; j<shotList.size(); j++) {
						if (asteroidList.get(i).checkForContact(shotList.get(j).getX(), shotList.get(j).getY(), shotList.get(j).getRadius())) {
							// Replace with smaller asteroids now
							// The secret is to set the spawn location on the previous + some random number within the radius
							if (asteroidList.get(i).getSize()==5) {
								for (int k = 0; k<2; k++) {
									double newX = asteroidList.get(i).getX() + Math.random()*asteroidList.get(i).getRadius();
									double newY = asteroidList.get(i).getY() + Math.random()*asteroidList.get(i).getRadius();
									Asteroid newAsteroid = new Asteroid(newX, newY, Math.random()*2 + 0.1, Math.random()*2 + 0.1, 3);
									asteroidList.add(newAsteroid);
								}
							}
							else if (asteroidList.get(i).getSize()==3) {
								for (int k = 0; k<3; k++) {
									double newX = asteroidList.get(i).getX() + Math.random()*asteroidList.get(i).getRadius();
									double newY = asteroidList.get(i).getY() + Math.random()*asteroidList.get(i).getRadius();
									Asteroid newAsteroid = new Asteroid(newX, newY, Math.random()*3 + 0.1, Math.random()*2 + 0.1, 1);
									asteroidList.add(newAsteroid);
								}
							}
							asteroidList.remove(i);
							shotList.remove(j);

							//System.out.println("Contact!");
							continue outerLoop;
							// spawn more asteroids!
						}
					}
				}

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
		if(kEvent.getKeyCode()==KeyEvent.VK_UP && !boolPause){
			// Have the ship accelerate
			player1.setAccelerate();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_LEFT && !boolPause){
			player1.setLeft();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_RIGHT && !boolPause){
			player1.setRight();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_ESCAPE) {
			// open the options menu
			//System.out.println("ESCAPE");
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_SPACE && !boolPause){
			player1.setSpace();
		}
	}

	public void keyReleased(KeyEvent kEvent){
		if(kEvent.getKeyCode()==KeyEvent.VK_UP && !boolPause){
			player1.unsetAccelerate();
			//System.out.println("Go");
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_LEFT && !boolPause){
			player1.unsetLeft();
			//System.out.println("Left");
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_RIGHT && !boolPause){
			player1.unsetRight();
			//stem.out.println("Right");
		}
		if(kEvent.getKeyCode()==KeyEvent.VK_SPACE && !boolPause){
			player1.unsetSpace();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
			player1.unsetRight();
			boolPause = boolPause ^ true;

		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_G && boolPause){
			boolGravity = boolGravity ^ true;
		}

		else if(kEvent.getKeyCode()==KeyEvent.VK_V && boolPause){
			boolGVisible = boolGVisible ^ true;
		}

		else if(kEvent.getKeyCode()==KeyEvent.VK_U && boolPause){
			boolUnlimitedLives = boolUnlimitedLives ^ true;
		}

		else if(kEvent.getKeyCode()==KeyEvent.VK_N && boolPause){
			boolAsteroids = boolAsteroids ^ true;
		}

	}

	/**
	 * Method used to detect when escape key has been pressed
	 * also probably menu navigation
	 */
	public void keyTyped(KeyEvent kEvent){

	}

	// Create an 3 asteroids + 1 additional for each new level
	// If the generated asteroid is too close to the player, then
	// Recalculate its position
	public void generateAsteroids() {
		for (int i=0; i<level+2; i++) {
			boolean isClose = true;
			double newX = 0;
			double newY = 0;
			while (isClose) {
				newX = Math.random()*xScreen;
				newY = Math.random()*yScreen;
				if (Math.sqrt((Math.pow((newX - player1.getX()),2) + Math.pow((newY - player1.getY()),2))) < 70) {
					isClose  = true;
					System.out.println("Asteroid readjusted");
				}
				else { isClose = false; }
			}
			Asteroid newAsteroid = new Asteroid(newX, newY, Math.random()*1 + 0.1, Math.random()*1 + 0.1, 5);
			asteroidList.add(newAsteroid);
		}
	}

}
