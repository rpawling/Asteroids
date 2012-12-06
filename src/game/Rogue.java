
package game;


import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Rogue extends Entity {
	// this is the outline of the ship the flame
	private final int[] shipX = {14,-10,-6,-10};
	private final int[] shipY = {0,-8,0,8};
	private final int[] thrustX = {-8,-25,-8};
	private final int[] thrustY = {-4,0,4};

	ArrayList<Shot> shotList = new ArrayList<Shot>();

	// These are the outline points of the ship offset by x and y location
	private int[] shipXOffset = new int[4];
	private int[] shipYOffset = new int[4];
	private int[] thrustXOffset = new int[3];
	private int[] thrustYOffset = new int[3];

	// Store the angle of the ship and its velocity of rotation
	private double angle = 0;
	private final double rVelocity = 0.1;

	// Constants that determine how fast the ship will accelerate while
	// the forward key is held and how fast the ship will decelerate
	// per frame (80% speed on each frame)
	private final double acceleration = 0.5;
	private final double decay = (double) 0.95;
	private final static int shotDelay = 5;
	private int shotsLeft = 0;

	// These booleans will pass whether left,right,or accelerate has been
	// pressed and pause will be true while player has menu open
	private boolean left = false;
	private boolean right = false;
	private boolean accelerate = false;
	private boolean shooting = false;
	private int lives = 3;
	private int changeDirection = 50;
	private Color color;

	// create a new ship at location x,y which is not moving
	Rogue(double x, double y, Color shipColor, int numLives) {
		// initialize the Entity Class variables
		// location, velocity, and contact radius
		this.setX(x);
		this.setY(y);
		this.setXV(0);
		this.setYV(0);
		this.setRadius(6);
		color = shipColor;
		lives = numLives;
		shooting = true;
		accelerate = true;
	}

	public void draw(Graphics backbf) {
		// 1. rotate points by the ship's angle
		// 2. offset points by x and y location
		// 3. draw the polygon
		double offset;
		for(int i=0; i<4; i++){
			offset =  (double) (shipX[i]*Math.cos(angle) - shipY[i]*Math.sin(angle)); // rotate each point
			offset += this.getX(); 													 // translate each point by x
			offset += 0.5;															 // round off the result
			shipXOffset[i] = (int) offset;

			offset = (double) (shipX[i]*Math.sin(angle) + shipY[i]*Math.cos(angle));	 // rotate each point
			offset += this.getY();												     // translate each point by y
			offset += 0.5;															 // round of result to an integer
			shipYOffset[i] = (int) offset;
		}
		for (int i=0; i<3; i++) {
			offset =  (double) (thrustX[i]*Math.cos(angle) - thrustY[i]*Math.sin(angle)); // rotate each point
			offset += this.getX(); 													 // translate each point by x
			offset += 0.5;															 // round off the result
			thrustXOffset[i] = (int) offset;

			offset = (double) (thrustX[i]*Math.sin(angle) + thrustY[i]*Math.cos(angle));	 // rotate each point
			offset += this.getY();												     // translate each point by y
			offset += 0.5;															 // round of result to an integer
			thrustYOffset[i] = (int) offset;
		}

		backbf.setColor(color);

		// draw the polygon for the ship
		backbf.drawPolygon(shipXOffset,shipYOffset,4); // 4 is the number of points
		backbf.setColor(Color.orange);
		if (accelerate) {
			backbf.drawPolygon(thrustXOffset,thrustYOffset,3);
		}

		//backbf.setColor(Color.orange);
		//backbf.drawOval((int)this.getX()-6, (int)this.getY()-6, 12, 12);
		//backbf.setColor(Color.red);
		//backbf.drawOval((int)(this.getX()-1 + 0.5), (int)(this.getY()-1 + 0.5), 2, 2);
	}

	public void update() {
		// Create a shot if space is held
		changeDirection--;
		if (changeDirection <= 0) {
			changeDirection = (int) (50 + Math.random()*10);
			int direction = (int) (Math.random()*5);
			if (direction == 0) {
				left = true;
				right = false;
			}
			else if (direction == 1) {
				left = false;
				right = true;
			}
			else {
				left = false;
				right = false;
			}
			direction = (int) (Math.random()*3);
			if (direction == 0) {
				accelerate = false;
			}
			else { accelerate = true;
			}
		}
		if (shooting) {
			if (shotsLeft <= 0) {
				// add shot to shot list
				Shot newShot = new Shot(this.getXV(),this.getYV(), this.getX(), this.getY(), angle, color);
				shotList.add(newShot);
				shotsLeft = shotDelay;
			}
			else {
				shotsLeft--;
			}
		}
		// Iterate through list and remove shots if old
		for (int i=0; i<shotList.size(); i++) {
			if (shotList.get(i).isOld()) {
				shotList.remove(i);
			}
		}
		// Add the acceleration to the velocity if accelerating
		if (accelerate) {
			// update the velocity
			this.setXV((double) (this.getXV()+acceleration*Math.cos(angle)));
			this.setYV((double) (this.getYV()+acceleration*Math.sin(angle)));
		}
		// Increase angle when right key pressed
		if (right) {
			this.angle += rVelocity;
		}
		// Decrease angle when left key pressed
		if (left) {
			this.angle -= rVelocity;
		}	
		// update location
		this.setX(this.getX() + this.getXV());
		if (!GameWindow.boolDeflect) {
			if (this.getX() > GameWindow.xScreen) {
				this.setX(this.getX() - GameWindow.xScreen);
			}
			else if (this.getX() < 0) {
				this.setX(this.getX() + GameWindow.xScreen);
			}
		} else {
			if ((this.getX() > GameWindow.xScreen) || (this.getX() < 0)) {
				this.setXV(-this.getXV());
			}
		}

		this.setY(this.getY() + this.getYV());
		if (!GameWindow.boolDeflect) {
			if (this.getY() > GameWindow.yScreen) {
				this.setY(this.getY() - GameWindow.yScreen);
			}
			else if (this.getY() < 0) {
				this.setY(this.getY() + GameWindow.yScreen);
			}
		} else {
			if ((this.getY() > GameWindow.yScreen) || (this.getY() < 0)) {
				this.setYV(-this.getYV());
			}
		}

		// decay by deceleration amount
		this.setXV(this.getXV() * decay);
		this.setYV(this.getYV() * decay);
	}

	public ArrayList<Shot> getShots() { return shotList; }
	public int getNumLives() { return lives; }
	public void die() { lives--; }

}

