package game;

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
	private float angle = 0;
	private float rVelocity = 0;

	// Constants that determine how fast the ship will accelerate while
	// the forward key is held and how fast the ship will decelerate
	// per frame (80% speed on each frame)
	private final float acceleration = 1;
	private final float velocityDecay = (float) 0.8;

	// These booleans will pass whether left,right,or accelerate has been
	// pressed and pause will be true while player has menu open
	boolean left = false;
	boolean right = false;
	boolean accelerate = false;
	boolean paused = false;

	// create a new ship at location x,y
	Ship(float x, float y) {
		// initialize the Entity Class variables
		// location, velocity, and contact radius
		this.setX(x);
		this.setY(y);
		this.setXV(0);
		this.setYV(0);
		this.setRadius(6);
	}
}
