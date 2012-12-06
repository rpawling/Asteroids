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
	
	Asteroid(double x, double y, double xV, double yV, int sizeMultiplier) {
		this.setX(x);
		this.setY(y);
		this.setXV(xV);
		this.setYV(yV);
		this.setRadius(7*sizeMultiplier);
		size = sizeMultiplier;		
	}
	
	public int getSize() {
		return (size);
	}
	
	public void update() {
		// First, update location
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
	}

	public void draw(Graphics backbf) {
		for (int i=0; i<12; i++) {
			asteroidXOffset[i] = asteroidX[i]*size + (int) (this.getX() + 0.5);
			asteroidYOffset[i] = asteroidY[i]*size + (int) (this.getY() + 0.5);
		}
		backbf.setColor(Color.white);
		backbf.drawPolygon(asteroidXOffset,asteroidYOffset,12); // 11 is the number of points
		//backbf.setColor(Color.green);
		// upper left corner, radius * object size
		//backbf.drawOval((int) (this.getX() + 0.5 - this.getRadius()), (int) (this.getY() + 0.5 - this.getRadius()), (int)this.getRadius()*2, (int)this.getRadius()*2);
		//backbf.setColor(Color.red);
		//backbf.drawOval((int)(this.getX()-1 + 0.5), (int)(this.getY()-1 + 0.5), 2, 2);
	}
}
