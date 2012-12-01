package game;


import java.awt.Color;
import java.awt.Graphics;

public class Shot extends Entity {
	private int decayTime = 30; // Number of frames before the shot disappears
	private final static int shotSpeed = 10; // number of pixels shot moves per frame, will be added to the ship velocity
	
	
	// Constructor sets the velocity and initial location of the shot
	Shot(double ShipVX, double ShipVY, double ShipX, double ShipY, double ShipAngle) {
		this.setX(ShipX);
		this.setY(ShipY);
		this.setXV(shotSpeed*Math.cos(ShipAngle)+ShipVX);
		this.setYV(shotSpeed*Math.sin(ShipAngle)+ShipVY);
		this.setRadius(1);
	}
	
	// Update moves the shot based on the velocity, and decrements the decayTime
	public void update() {
		this.setX(this.getX() + this.getXV());
		if (this.getX() > GameWindow.xScreen) {
			this.setX(this.getX() - GameWindow.xScreen);
		}
		else if (this.getX() < 0) {
			this.setX(this.getX() + GameWindow.xScreen);
		}
		this.setY(this.getY() + this.getYV());
		if (this.getY() > GameWindow.yScreen) {
			this.setY(this.getY() - GameWindow.yScreen);
		}
		else if (this.getY() < 0) {
			this.setY(this.getY() + GameWindow.yScreen);
		}
		decayTime--;
	}
	
	// Tell the ship if the shot has expired and should be deleted
	public boolean isOld() {
		if (decayTime > 0) {
			return false;
		}
		else {
			return true;
		}
	}
	
	// This method just draws the shot as a circle at coordinates x,y
	public void draw(Graphics backbf) {
		/*if(!paused) // active means game is running (not paused)
			backbf.setColor(Color.white);
		else // draw the ship gray if the game is paused
			backbf.setColor(Color.gray);*/
		backbf.setColor(Color.white);
		backbf.drawOval((int) (this.getX() - this.getRadius() + 0.5), (int) (this.getY() - this.getRadius() + 0.5), (int)(this.getRadius()*2 + 0.5), (int)(this.getRadius()*2 + 0.5));
	}
}
