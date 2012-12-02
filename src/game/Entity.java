package game;

import java.awt.Graphics;

// Base Class for objects on screen

public abstract class Entity {
	private double xLocation, yLocation; // position of the entity
	private double xVelocity, yVelocity; // velocity of the entity
	private double contactRadius;		// radius of the contact circle
	
	public double getX() { return xLocation; }
	public double getY() { return yLocation; }
	public double getXV() { return xVelocity; }
	public double getYV() { return yVelocity; }
	
	public void setX(double x) { xLocation = x; }
	public void setY(double y) { yLocation = y; }
	public void setXV(double xv) { xVelocity = xv; }
	public void setYV(double yv) { yVelocity = yv; }
	
	public void setRadius(double radius) { contactRadius = radius; }
	public double getRadius() { return contactRadius; }
	
	abstract public void draw(Graphics backbf);
	
	public boolean checkForContact(double x, double y, double radius) {
		// First find distance between the two centers
		double distance = Math.sqrt(Math.pow((this.getX() - x),2)+Math.pow((this.getY() - y),2));
		// Check if distance is less than the radii
		if (radius == 0) {
			//System.out.println("Distance: " + distance);
		}
		if (distance < (this.getRadius() + radius)) {
			return true;
		}
		else { return false; }
	}
	// collision detection can be implemented on the asteroids and the enemy ships
}
