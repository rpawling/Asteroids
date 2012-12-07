package game;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Alien extends Entity {

	// Define the shape of the alien ship as three trapezoids
	private final int[] botTrapX = {-14,-6,6,14};
	private final int[] botTrapY = {0, 5, 5, 0};
	private final int[] midTrapX = {-12,12,5,-5};
	private final int[] midTrapY = {0,0,-4,-4};
	private final int[] topTrapX = {-4,4,2,-2};
	private final int[] topTrapY = {-4,-4,-6,-6};

	private int[] botTrapXOffset = new int[4];
	private int[] botTrapYOffset = new int[4];
	private int[] midTrapXOffset = new int[4];
	private int[] midTrapYOffset = new int[4];
	private int[] topTrapXOffset = new int[4];
	private int[] topTrapYOffset = new int[4];

	// once direction timer expires, change direction and velocity randomly
	private int directionTimer = 100;
	private int shotTimer = 0;
	private final int shotDelay = 6;
	private int hitPoints = 3;

	private ArrayList<Shot> shotList = new ArrayList<Shot>();

	Alien () {
		this.setX(GameWindow.xScreen);
		this.setY(0);
		this.setXV(Math.random()*4 - 2);
		this.setYV(Math.random()*4 - 2);
		this.setRadius(6);
	}

	public void update() {

		// Check if time to change direction of alien ship randomly
		// Velocity increases with the level
		if (directionTimer < 0 ){
			this.setXV((Math.random()*4 - 2)*GameWindow.level);
			this.setYV((Math.random()*4 - 2)*GameWindow.level);
			directionTimer = 100;

		}
		else { directionTimer--; }
		if (shotTimer <= 0) {
			Shot newShot = new Shot(this.getXV(), this.getYV(), this.getX(), this.getY(), Math.random()*(2*Math.PI), Color.cyan);
			shotList.add(newShot);
			shotTimer = shotDelay;
		}
		else { shotTimer--; }
		// Iterate through list and remove shots if old
		for (int i=0; i<shotList.size(); i++) {
			if (shotList.get(i).isOld()) {
				shotList.remove(i);
			}
		}

		//update location of alien
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
		for (int i=0; i<4; i++) {
			topTrapXOffset[i] = topTrapX[i] + (int) (this.getX() + 0.5);
			topTrapYOffset[i] = topTrapY[i] + (int) (this.getY() + 0.5);
			midTrapXOffset[i] = midTrapX[i] + (int) (this.getX() + 0.5);
			midTrapYOffset[i] = midTrapY[i] + (int) (this.getY() + 0.5);
			botTrapXOffset[i] = botTrapX[i] + (int) (this.getX() + 0.5);
			botTrapYOffset[i] = botTrapY[i] + (int) (this.getY() + 0.5);
		}
		backbf.setColor(Color.cyan);
		backbf.drawPolygon(topTrapXOffset,topTrapYOffset,4);
		backbf.drawPolygon(midTrapXOffset,midTrapYOffset,4);
		backbf.drawPolygon(botTrapXOffset,botTrapYOffset,4);
		//backbf.setColor(Color.green);
		// upper left corner, radius * object size
		//backbf.drawOval((int) (this.getX() + 0.5 - this.getRadius()), (int) (this.getY() + 0.5 - this.getRadius()), (int)this.getRadius()*2, (int)this.getRadius()*2);
		//backbf.setColor(Color.red);
		//backbf.drawOval((int)(this.getX()-1 + 0.5), (int)(this.getY()-1 + 0.5), 2, 2);
	}

	public ArrayList<Shot> getShots() { return shotList; }
	public boolean hit() {
		hitPoints--;
		if (hitPoints <= 0) {
			return true;
		}
		else { return false; }
	}
	
	public int getHits() {return hitPoints;}

}
