package game;

public class Entity {
	private float xLocation, yLocation; // position of the entity
	private float xVelocity, yVelocity; // velocity of the entity
	private float contactRadius;		  // radius of the contact circle
	
	public float getX() { return xLocation; }
	public float getY() { return yLocation; }
	public float getXV() { return xVelocity; }
	public float getYV() { return yVelocity; }
	
	public void setX(float x) { xLocation = x; }
	public void setY(float y) { yLocation = y; }
	public void setXV(float xv) { xVelocity = xv; }
	public void setYV(float yv) { yVelocity = yv; }
	
	public void setRadius(float radius) { contactRadius = radius; }
	public float getRadius() { return contactRadius; }
	// collision detection can be implemented on the asteroids and the enemy ships
}
