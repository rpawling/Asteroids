package game;


import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;


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
	public static boolean boolDeflect = false;
	public static boolean boolMusic = true;
	public static boolean boolResetScore = false;
	public static boolean boolPlayers = false;
	public static boolean endGame = false;
	public static final int xScreen = 800; // screen dimensions 500x400 or 800x600
	public static final int yScreen = 600;
	public static int level = 1;		// indicates level and # of asteroids
	public static boolean paused = false; // set to true while menu is open, so that game stops
	private Image screen;      // this is an object which stores an image for the back buffer
	private Graphics backbf;   // this is the graphics in the image back buffer
	private long tStart, tEnd; // This is used to tell the elapsed time in the main thread
	private int period = 25;   // This is the period that the main thread is executed and screen is refreshed
	// Note: If this is faster, the game will speed up!
	private Ship player1, player2;
	private int alienWait = 1000;
	private int rogueWait = 1377;
	private Alien alien = null;
	private Rogue rogue = null;
	private ArrayList<Asteroid> asteroidList = new ArrayList<Asteroid>();
	private Gravitational gravObject;
	
	// init() is kind of like main for an applet
	public void init(){
		SoundAsteroids.loadSound(); //load all sounds used in game
		resize(xScreen,yScreen); // Set applet size to 500x400 for now
		//setBackground(Color.BLACK);
		addKeyListener(this); // let this class handle key press events
		screen = createImage(xScreen, yScreen); // This is a graphics buffer that is drawn while old image is displayed
		backbf = screen.getGraphics();

		gravObject = new Gravitational(xScreen/2,yScreen/2);
		player1 = new Ship(xScreen/2,yScreen/2,Color.green,3,0);
		player2 = null;
		//player2 = new Ship(xScreen*0.75,yScreen*0.25,Color.yellow,3,0);
		Score.initialize(player1,player2);
		this.generateAsteroids();
		SoundAsteroids.start_music();
		//textField.addActionListener(this);

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

		if (!boolPause && !endGame){
			player1.draw(backbf);
			if (player2 != null) {
				player2.draw(backbf);
			}
			gravObject.draw(backbf);
			if (alien != null) {
				alien.draw(backbf);
			}
			for(int i=0; i<asteroidList.size();i++) {
				asteroidList.get(i).draw(backbf);
			}
			ArrayList<Shot> shotList = player1.getShots();
			for (int i=0; i<shotList.size(); i++) {
				shotList.get(i).draw(backbf);
			}
			if (player2 != null) {
				shotList = player2.getShots();
				for (int i=0; i<shotList.size(); i++) {
					shotList.get(i).draw(backbf);
				}
			}
			if (alien != null) { 
				shotList = alien.getShots();
				for (int i=0; i<shotList.size(); i++) {
					shotList.get(i).draw(backbf);
				}
			}
			if (rogue != null) {
				rogue.draw(backbf);
				shotList = rogue.getShots();
				for (int i=0; i<shotList.size(); i++) {
					shotList.get(i).draw(backbf);
				}
			}
			// Draw the score onto the screen, depending on number of players
			Score.draw(backbf);
		}
		else if(endGame) {
			Score.draw(backbf);
			Score.drawHiscores(backbf);
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

			if (!boolPause & !endGame) {
				// CODE HERE to execute one frame of the game
				// e.g. move objects, detect collisions, trigger animations ...

				// If level complete, advance to next level
				if (asteroidList.isEmpty()) {
					// award each player 100*l points
					Score.addScore(1, 100*level);
					Score.addScore(2, 100*level);
					level++;
					this.generateAsteroids();
					player1.resetLives();
					if (player2 != null) {
						player2.resetLives();
					}
				}

				// Update alien location if its spawned
				alienWait--;
				rogueWait--;
				if (alienWait < 0 && alien == null) {
					alien = new Alien();
				}
				if (alien != null) {
					alien.update();
				}
				if (rogueWait < 0 && rogue == null) {
					rogue = new Rogue(xScreen,yScreen,Color.RED,3);
				}
				if (rogue != null) {
					rogue.update();
				}


				// Update player location
				player1.update();
				gravObject.gravity(player1);
				if (player2 != null) {
					player2.update();
					gravObject.gravity(player2);
				}


				// update the shot arrays
				// Check if contact with other player
				ArrayList<Shot> shotList = player1.getShots();
				for (int i=0; i<shotList.size(); i++) {
					shotList.get(i).update();
					if (player2 != null && player2.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
						SoundAsteroids.explode();
						if (!boolUnlimitedLives) {
							player2.die();
						}
						//reward player 1 with points
						Score.addScore(1, 100);
						if (player2.getNumLives() > 0) {
							spawnPlayer(player2);
						}
						else {
							// GAME OVER player2
							gameOver();
						}
					}
					if (alien != null && alien.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
						if (alien.hit()) {
							alien = null;
							// reward player 1 with points
							Score.addScore(1, 100);
							//reset alien timer
							alienWait = 1000;
							SoundAsteroids.explode();
						}
						else {SoundAsteroids.bullet();}
					}
					if (rogue != null && rogue.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
						rogue.die();
						if (rogue.getNumLives() <= 0) {
							rogue = null;
							// reward player 1 with points
							Score.addScore(1, 100);
							//reset rogue
							rogueWait = 1377;
							SoundAsteroids.explode();
						}
						else {SoundAsteroids.bullet();}
					}
				}
				if (player2 != null) {
					shotList = player2.getShots();
					for (int i=0; i<shotList.size(); i++) {
						shotList.get(i).update();
						if (player1.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							SoundAsteroids.explode();
							if (!boolUnlimitedLives) {
								player1.die();
							}
							// reward player 2 with 100 points
							Score.addScore(2, 100);
							if (player1.getNumLives() > 0) {
								spawnPlayer(player1);
							}
							else {
								// GAME OVER player1
								gameOver();
							}
						}
						if (alien != null && alien.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							if (alien.hit()) {
								alien = null;
								// reward player 2 with points
								Score.addScore(2, 100);
								//reset alien timer
								alienWait = 1000;
								SoundAsteroids.explode();
							} else { SoundAsteroids.bullet(); }
						}
						if (rogue != null && rogue.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							rogue.die();
							if (rogue.getNumLives() <= 0) {
								rogue = null;
								// reward player 2 with points
								Score.addScore(2, 100);
								//reset rogue
								rogueWait = 1377;
								SoundAsteroids.explode();
							} else { SoundAsteroids.bullet(); }
						}
					}
				}
				if (alien != null) {
					shotList = alien.getShots();
					for (int i=0; i<shotList.size(); i++) {
						shotList.get(i).update();
						if (player2 != null && player2.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							SoundAsteroids.explode();
							if (!boolUnlimitedLives) {
								player2.die();
							}
							if (player2.getNumLives() > 0) {
								spawnPlayer(player2);
							}
							else {
								// GAME OVER player2
								gameOver();
							}
						}
						if (player1.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							SoundAsteroids.explode();
							if (!boolUnlimitedLives) {
								player1.die();
							}
							if (player1.getNumLives() > 0) {
								spawnPlayer(player1);
							}
							else {
								// GAME OVER player1
								gameOver();
							}
						}
					}
				}
				if (rogue != null) {
					shotList = rogue.getShots();
					for (int i=0; i<shotList.size(); i++) {
						shotList.get(i).update();
						if (player2 != null && player2.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							SoundAsteroids.explode();
							if (!boolUnlimitedLives) {
								player2.die();
							}
							if (player2.getNumLives() > 0) {
								spawnPlayer(player2);
							}
							else {
								// GAME OVER player2
								gameOver();
							}
						}
						if (player1.checkForContact(shotList.get(i).getX(), shotList.get(i).getY(), shotList.get(i).getRadius())) {
							SoundAsteroids.explode();
							if (!boolUnlimitedLives) {
								player1.die();
							}
							if (player1.getNumLives() > 0) {
								spawnPlayer(player1);
							}
							else {
								// GAME OVER player1
								gameOver();
							}
						}
					}
				}

				// update the asteroid arrays and check asteroids for contacts
				// if there is contact with player, delete player and remove one life
				// if there is contact with shot, delete asteroid and spawn smaller sizes
				outerLoop:
					for (int i=0; i<asteroidList.size(); i++) {
						asteroidList.get(i).update();
						if (asteroidList.get(i).checkForContact(player1.getX(), player1.getY(), player1.getRadius())) {
							// Subtract one life, move player to restart location
							if(!boolUnlimitedLives) {
								player1.die();
							}
							if (player1.getNumLives() > 0) {
								spawnPlayer(player1);
							}
							else {
								// GAME OVER player1
								gameOver();
							}
							SoundAsteroids.asteroid();
						}
						else if (player2 != null && asteroidList.get(i).checkForContact(player2.getX(), player2.getY(), player2.getRadius())) {
							// Subtract one life, move player to restart location
							if(!boolUnlimitedLives) {
								player2.die();
							}
							if (player2.getNumLives() > 0) {
								spawnPlayer(player2);
							}
							else {
								// GAME OVER player2
								gameOver();
							}
							SoundAsteroids.asteroid();
						}

						// Check if the player's shot has hit an asteroid
						// If so, split the asteroid to the smaller size if available
						// Delete the asteroid
						for (int l=0; l<2; l++) {
							if (l == 0) {
								shotList = player1.getShots();
							}
							else { shotList = player2.getShots(); }

							for (int j=0; j<shotList.size(); j++) {
								if (asteroidList.get(i).checkForContact(shotList.get(j).getX(), shotList.get(j).getY(), shotList.get(j).getRadius())) {

									// Award the player with the points corresponding to the asteroid size

									// Replace with smaller asteroids now
									// The secret is to set the spawn location on the previous + some random number within the radius
									if (asteroidList.get(i).getSize()==5) {
										for (int k = 0; k<2; k++) {
											double newX = asteroidList.get(i).getX() + Math.random()*asteroidList.get(i).getRadius();
											double newY = asteroidList.get(i).getY() + Math.random()*asteroidList.get(i).getRadius();
											Asteroid newAsteroid = new Asteroid(newX, newY, (Math.random()*4 - 2)*0.5*level, (Math.random()*4 - 2)*0.5*level, 3);
											asteroidList.add(newAsteroid);
										}
									}
									else if (asteroidList.get(i).getSize()==3) {
										for (int k = 0; k<3; k++) {
											double newX = asteroidList.get(i).getX() + Math.random()*asteroidList.get(i).getRadius();
											double newY = asteroidList.get(i).getY() + Math.random()*asteroidList.get(i).getRadius();
											Asteroid newAsteroid = new Asteroid(newX, newY, (Math.random()*6 - 3)*0.5*level, (Math.random()*6 - 3)*0.5*level, 1);
											asteroidList.add(newAsteroid);
										}
									}
									// reward player with points
									if (l==0) { Score.addScore(1, 5); } else { Score.addScore(2, 5); }
									asteroidList.remove(i);
									shotList.remove(j);
									SoundAsteroids.bullet();
									continue outerLoop;
								}

							}
							if (player2 == null) { l++; }
						}
					}
			}

			if (boolMusic) { SoundAsteroids.start_music(); } else { SoundAsteroids.stop_music(); }
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
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_A && !boolPause){
			player2.setLeft();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_W && !boolPause){
			player2.setAccelerate();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_D && !boolPause){
			player2.setRight();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_CONTROL && !boolPause){
			player2.setSpace();
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
		else if(kEvent.getKeyCode()==KeyEvent.VK_SPACE && !boolPause && !endGame){
			player1.unsetSpace();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_A && !boolPause){
			player2.unsetLeft();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_W && !boolPause){
			player2.unsetAccelerate();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_D && !boolPause){
			player2.unsetRight();
		}
		else if(player2 != null && kEvent.getKeyCode()==KeyEvent.VK_CONTROL && !boolPause){
			player2.unsetSpace();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_ESCAPE){
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
		else if(kEvent.getKeyCode()==KeyEvent.VK_D && boolPause){
			boolDeflect = boolDeflect ^ true;
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_M && boolPause){
			boolMusic = boolMusic ^ true;
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_R && boolPause){
			boolResetScore = true;
			Score.clearHiScores();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_S && boolPause){
			saveGame();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_C && boolPause){
			loadGame();
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_L && boolPause){
			asteroidList.clear();
			level++;
			this.generateAsteroids();
			player1.resetLives();
			if (player2 != null) {
				player2.resetLives();
			}
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_P && boolPause){
			boolPlayers = boolPlayers ^ true;
			if (boolPlayers) {
				player2 = new Ship(xScreen*0.75,yScreen*0.25,Color.yellow,3,0);
				Score.initialize(player1, player2);
				spawnPlayer(player2);
			}
			else {
				player2 = null;
				Score.initialize(player1, player2);
			}
		}
		else if(kEvent.getKeyCode()==KeyEvent.VK_SPACE && endGame){
			player1 = new Ship(xScreen/2,yScreen/2,Color.green,3,0);
			asteroidList.clear();
			level = 1;
			generateAsteroids();
			alien = null;
			alienWait = 1000;
			rogue = null;
			rogueWait = 1777;	
			player2 = null;
			Score.setScore1(0);
			Score.setScore2(0);
			Score.initialize(player1, player2);
			player1.resetLives();
			boolPause = false;
			boolGravity = false;
			boolGVisible = false;
			boolUnlimitedLives = false;
			boolAsteroids = true;
			boolDeflect = false;
			boolMusic = true;
			boolResetScore = false;
			boolPlayers = false;
			endGame = false;	
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
		int numAsteroids = level+2;
		if (!boolAsteroids) { numAsteroids /= 2; }
		for (int i=0; i<numAsteroids; i++) {
			boolean isClose = true;
			double newX = 0;
			double newY = 0;
			while (isClose) {
				newX = Math.random()*xScreen;
				newY = Math.random()*yScreen;
				if (Math.sqrt((Math.pow((newX - player1.getX()),2) + Math.pow((newY - player1.getY()),2))) < 80) {
					isClose  = true;
					//System.out.println("Asteroid readjusted");
				}
				if (player2 != null && Math.sqrt((Math.pow((newX - player2.getX()),2) + Math.pow((newY - player2.getY()),2))) < 80) {
					isClose  = true;
					//System.out.println("Asteroid readjusted");
				}
				else { isClose = false; }
			}
			Asteroid newAsteroid = new Asteroid(newX, newY, (Math.random()*2 - 1)*0.5*level, (Math.random()*2 - 1)*0.5*level, 5);
			asteroidList.add(newAsteroid);
		}
	}

	// This method spawns a new player ship at beginning of game or when they die
	// It checks to make sure there are no other object conflicts and then spawns
	// The player in the new location
	public void spawnPlayer(Ship player) {

		double newX = Math.random()*xScreen;
		double newY = Math.random()*yScreen;
		boolean isContact = true;
		while (isContact) {
			isContact = false;
			for (int i=0; i<asteroidList.size(); i++) {
				asteroidList.get(i).update();
				if (asteroidList.get(i).checkForContact(newX, newY, player.getRadius())) {
					isContact = true;
					newX = Math.random()*xScreen;
					newY = Math.random()*yScreen;
				}
			}
		}
		player.setX(newX);
		player.setY(newY);
		player.setXV(0);
		player.setYV(0);
	}

	public void gameOver() {
		endGame = true;
		repaint();
		Score.sortHiscores();
	}

	public void saveGame() {
		try{
			// Create file
            JFileChooser fc = new JFileChooser();
            File dir = new File(".");
            fc.setCurrentDirectory(dir);
            fc.showSaveDialog(null);
            FileWriter fstream = new FileWriter(fc.getSelectedFile());
			BufferedWriter out = new BufferedWriter(fstream);

			// player1 lives score x y xv yv angle
			out.write("player1 " + player1.getNumLives() + " " + Score.getScore1() + " " + player1.getX() + " " + player1.getY() + " " + player1.getXV() + " " + player1.getYV() + " " + player1.getAngle());
			out.newLine();

			// player2 lives score x y xv yv angle
			if (player2 != null) {
				out.write("player2 " + player2.getNumLives() + " " + Score.getScore2() + " " + player2.getX() + " " + player2.getY() + " " + player2.getXV() + " " + player2.getYV() + " " + player2.getAngle());
				out.newLine();
			}

			// alien hitPoints x y xv yv
			if (alien != null) {
				out.write("alien " + alien.getHits() + " " + alien.getX() + " " + alien.getY() + " " + alien.getXV() + " " + alien.getYV());
				out.newLine();
			}

			// rogue lives x y xv yv angle
			if (rogue != null) {
				out.write("rogue " + rogue.getNumLives() + " " + rogue.getX() + " " + rogue.getY() + " " + rogue.getXV() + " " + rogue.getYV() + " " + rogue.getAngle());
				out.newLine();
			}

			// asteroid size x y xv yv
			for (int i=0; i<asteroidList.size(); i++) {
				Asteroid ast = asteroidList.get(i);
				out.write("asteroid" + ast.getSize() + " " + ast.getX() + " " + ast.getY() + " " + ast.getXV() + " " + ast.getYV());
				out.newLine();
			}

			// level level#
			out.write("level " + level);
			out.newLine();
			// gravity boolGravity
			out.write("gravity " + boolGravity);
			out.newLine();
			// gvisible boolGVisible
			out.write("gvisible " + boolGVisible);
			out.newLine();
			// lives boolUnlimitedLives
			out.write("lives " + boolUnlimitedLives);
			out.newLine();
			// numberAsteroids boolAsteroids
			out.write("numberAsteroids " + boolAsteroids);
			out.newLine();
			// deflect boolDeflect
			out.write("deflect " + boolDeflect);
			out.newLine();
			// music boolMusic
			out.write("music " + boolMusic);
			out.newLine();
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}
	public void loadGame() {

		String line;
		String[] data;
		
		JFileChooser fc = new JFileChooser();
        File dir = new File(".");
        fc.setCurrentDirectory(dir);
        fc.showOpenDialog(null);
        File scoreFile = fc.getSelectedFile();
		if (scoreFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(scoreFile));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				asteroidList.clear();
				while ((line = reader.readLine()) != null) {
					// process the line.
					data = line.split(" ");
					if (data[0].equals("player1")) {
						// player1 lives score x y xv yv angle
						player1 = new Ship(Double.parseDouble(data[3]),Double.parseDouble(data[4]),Color.green,Integer.parseInt(data[1]),Double.parseDouble(data[7]));
						player1.setXV(Double.parseDouble(data[5]));
						player1.setYV(Double.parseDouble(data[6]));
						Score.setScore1(Integer.parseInt(data[2]));
					}
					else if (data[0].equals("player2")) {
						// player2 lives score x y xv yv angle
						player2 = new Ship(Double.parseDouble(data[3]),Double.parseDouble(data[4]),Color.yellow,Integer.parseInt(data[1]),Double.parseDouble(data[7]));
						player2.setXV(Double.parseDouble(data[5]));
						player2.setYV(Double.parseDouble(data[6]));
						Score.setScore2(Integer.parseInt(data[2]));
					}
					else if (data[0].equals("alien")) {
						// alien hitPoints x y xv yv
						alien = new Alien();
						alien.setX(Double.parseDouble(data[2]));
						alien.setY(Double.parseDouble(data[3]));
						alien.setXV(Double.parseDouble(data[4]));
						alien.setYV(Double.parseDouble(data[5]));
					}
					else if (data[0].equals("rogue")) {
						// rogue lives x y xv yv angle
						player2 = new Ship(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Color.red,Integer.parseInt(data[1]),Double.parseDouble(data[6]));
						player2.setXV(Double.parseDouble(data[4]));
						player2.setYV(Double.parseDouble(data[5]));
					}
					else if (data[0].equals("asteroid")) {
						// asteroid size x y xv yv
						Asteroid asteroid = new Asteroid(Double.parseDouble(data[2]),Double.parseDouble(data[3]),Double.parseDouble(data[4]),Double.parseDouble(data[5]),Integer.parseInt(data[1]));
						asteroidList.add(asteroid);
					}
					else if (data[0].equals("level")) {
						// level level#
						level = Integer.parseInt(data[1]);
					}
					else if (data[0].equals("gravity")) {
						// gravity boolGravity
						if (data[1] == "true") { boolGravity = true; } else { boolGravity = false; }
					}
					else if (data[0].equals("gvisible")) {
						// gravity boolGravity
						if (data[1] == "true") { boolGVisible = true; } else { boolGVisible = false; }
					}
					else if (data[0].equals("lives")) {
						// lives boolUnlimitedLives
						if (data[1] == "true") { boolUnlimitedLives = true; } else { boolUnlimitedLives = false; }
					}
					else if (data[0].equals("numberAsteroids")) {
						// numberAsteroids boolAsteroids
						if (data[1] == "true") { boolAsteroids = true; } else { boolAsteroids = false; }
					}
					else if (data[0].equals("deflect")) {
						// deflect boolDeflect
						if (data[1] == "true") { boolDeflect = true; } else { boolDeflect = false; }
					}
					Score.initialize(player1, player2);

				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
