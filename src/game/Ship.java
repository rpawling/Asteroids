package game;

import java.awt.Color;
import java.awt.Graphics;

public class Ship extends Entity {
	// this is the outline of the ship the flame
	private final int[] shipX = {14,-10,-6,-10};
	private final int[] shipY = {0,-8,0,8};
	//private final int[] flameX = {-6,-23,-6};
	//private final int[] flameY = {-3,0,3};

	// These are the outline points of the ship offset by x and y location
	private int[] shipXOffset = new int[4];
	private int[] shipYOffset = new int[4];

	// Store the angle of the ship and its velocity of rotation
	private double angle = 0;
	private final double rVelocity = 0.1;

	// Constants that determine how fast the ship will accelerate while
	// the forward key is held and how fast the ship will decelerate
	// per frame (80% speed on each frame)
	private final double acceleration = 1;
	private final double decay = (double) 0.8;

	// These booleans will pass whether left,right,or accelerate has been
	// pressed and pause will be true while player has menu open
	boolean left = false;
	boolean right = false;
	boolean accelerate = false;
	boolean paused = false;

	// create a new ship at location x,y which is not moving
	Ship(double x, double y) {
		// initialize the Entity Class variables
		// location, velocity, and contact radius
		this.setX(x);
		this.setY(y);
		this.setXV(0);
		this.setYV(0);
		this.setRadius(6);
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

		if(!paused) // active means game is running (not paused)
			backbf.setColor(Color.white);
		else // draw the ship gray if the game is paused
			backbf.setColor(Color.gray);

		// draw the polygon for the ship
		backbf.drawPolygon(shipXOffset,shipYOffset,4); // 4 is the number of points
	}

	public void update() {
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
		this.setY(this.getY() + this.getYV());
		// decay by deceleration amount
		this.setXV(this.getXV() * decay);
		this.setYV(this.getYV() * decay);
	}
	
	public void setAccelerate() { accelerate = true; }
	public void unsetAccelerate() { accelerate = false; }
	public void setLeft() { left = true; }
	public void unsetLeft() { left = false; }
	public void setRight() { right = true; }
	public void unsetRight() { right = false; }
}

