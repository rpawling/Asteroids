package game;

public class Ship extends Entity {
	// this is the outline of the ship the flame
	//private final float[] shipX = {14,-10,-6,-10};
	//private final float[] shipY = {0,-8,0,8};
	//private final float[] flameX = {-6,-23,-6};
	//private final float[] flameY = {-3,0,3};
	private float angle;
	private final float acceleration = 1;
	private final float velocityDecay = (float) 0.8;
	private float rotationalSpeed;
	boolean turningLeft, turningRight, accelerating, active;
	int[] xPts, yPts, flameXPts, flameYPts; //store the current locations
	//of the points used to draw the ship and its flame
	int shotDelay, shotDelayLeft; //used to determine the rate of firing
	
	// create a new ship at location x,y
	Ship(float x, float y) {
		this.setX(x);
		this.setY(y);
		this.angle = 0;
		this.acceleration = 1;
		this.setRadius(6);
		xVelocity=0; // not moving
		yVelocity=0;
		turningLeft=false; // not turning
		turningRight=false;
		accelerating=false; // not accelerating
		xPts=new int[4]; // allocate space for the arrays
		yPts=new int[4];
		flameXPts=new int[3];
		flameYPts=new int[3];
		this.shotDelay=shotDelay; // # of frames between shots
		shotDelayLeft=0; // ready to shoot
	}
