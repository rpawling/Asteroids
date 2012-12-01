package game;

import java.awt.Color;
import java.awt.Graphics;

public class Asteroid extends Entity {

	private final int[] asteroidX = {2-10, 4-10, 3-10, 8-10, 12-10, 16-10, 18-10, 17-10, 18-10, 16-10, 12-10, 8-10};
	private final int[] asteroidY = {6-9, 10-9, 14-9, 16-9, 16-9, 14-9, 12-9, 8-9, 6-9, 2-9, 4-9, 1-9};
	//private final int centerX = 10;
	//private final int centerY = 9;
	
	private int size = 1;
	
	// Multipliers for asteroid type
	// Large = 5
	// Medium = 3
	// Small = 1

	private int[] asteroidXOffset = new int[12];
	private int[] asteroidYOffset = new int[12];
	
	Asteroid(int x, int y, int xV, int yV, int sizeMultiplier) {
		this.setX(x);
		this.setY(y);
		this.setXV(xV);
		this.setYV(yV);
		this.setRadius(7);
		size = sizeMultiplier;		
	}
	
	public void update() {
		// First, update location
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
	}
	
	public boolean checkForContact(double x, double y, double radius) {
		// First find distance between the two centers
		double distance = Math.sqrt(Math.pow((this.getX() - x),2)-Math.pow((this.getY() - y),2));
		// Check if distance is less than the radii
		if (distance < (this.getRadius() + radius)) {
			return true;
		}
		else { return false; }
	}


	public void draw(Graphics backbf) {
		for (int i=0; i<12; i++) {
			asteroidXOffset[i] = asteroidX[i]*size + (int) (this.getX() + 0.5);
			asteroidYOffset[i] = asteroidY[i]*size + (int) (this.getY() + 0.5);
		}
		backbf.setColor(Color.white);
		backbf.drawPolygon(asteroidXOffset,asteroidYOffset,12); // 11 is the number of points
		backbf.setColor(Color.green);
		// upper left corner, radius * object size
		backbf.drawOval((int) (this.getX() + 0.5 - this.getRadius()*size), (int) (this.getY() + 0.5 - this.getRadius()*size), (int)this.getRadius()*size*2, (int)this.getRadius()*size*2);
	}
}
