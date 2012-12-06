package game;

import java.awt.Color;
import java.awt.Graphics;

public class Gravitational extends Entity {

	private double g = 0.05;

	public Gravitational(double x, double y) {
		this.setX(x);
		this.setY(y);
	}

	public void draw(Graphics backbf) {

		if (GameWindow.boolGVisible) {
			backbf.setColor(Color.orange);
			backbf.fillOval((int) this.getX(), (int) this.getY(), 10, 10);
		}

	}

	public void gravity(Entity entity) {

		g = 0.05 + 0.05*(GameWindow.level-1);
		if (GameWindow.boolGravity) {

//			if (entity.getX() > GameWindow.xScreen / 2) {
//				entity.setXV(entity.getXV() - g);
//			} else {
//				entity.setXV(entity.getXV() + g);
//			}
//			
//			if (entity.getY() > GameWindow.yScreen / 2) {
//				entity.setYV(entity.getYV() - g);
//			} else {
//				entity.setYV(entity.getYV() + g);
//			}
			
			double angle = Math.atan2(GameWindow.yScreen/2 - entity.getY(),entity.getX()-GameWindow.xScreen/2);
			
			if (entity.getX() > GameWindow.xScreen / 2) {
				entity.setXV(entity.getXV() - g*Math.abs((Math.cos(angle))));
			} else {
				entity.setXV(entity.getXV() + g*Math.abs((Math.cos(angle))));
			}
			
			if (entity.getY() > GameWindow.yScreen / 2) {
				entity.setYV(entity.getYV() - g*Math.abs((Math.sin(angle))));
			} else {
				entity.setYV(entity.getYV() + g*Math.abs((Math.sin(angle))));
			}
		}
	}

}
