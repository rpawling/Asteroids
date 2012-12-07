package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Menu {
	private static final int x = 300;
	private static final int y = 40;

	public static void draw(Graphics backbf){
		
		//set font
		Font f = new Font("Dialog", Font.BOLD, 12);
		backbf.setFont(f);
		
		//menu title
		backbf.setColor(Color.GREEN);
		backbf.drawString("Asteroids - Menu", x + 30, y + 0);
		
		//menu
		backbf.setColor(Color.CYAN);
		backbf.drawString("[G] Graviational Object -- ", x, y + 30);
		toggle(backbf, "<ON>", "<OFF>", GameWindow.boolGravity, x + 160, y + 30);
		
		backbf.drawString("[V] Object Visibility -- ", x, y + 50);
		toggle(backbf, "<VISIBLE>", "<INVISIBLE>", GameWindow.boolGVisible, x + 160, y + 50);
		
		backbf.drawString("[U] Unlimited Lives -- ", x, y + 70);
		toggle(backbf, "<ON>", "<OFF>", GameWindow.boolUnlimitedLives, x + 160, y + 70);
		
		backbf.drawString("[N] Number of Asteroids -- ", x, y + 90);
		toggle(backbf, "<HIGH>", "<LOW>", GameWindow.boolAsteroids, x + 160, y + 90);
		
		backbf.drawString("[R] Reset High Score -- ", x, y + 110);
		toggle(backbf, "<RESET>", "<OFF>", GameWindow.boolResetScore, x + 160, y + 110);
		
		backbf.drawString("[L] Select Starting Level -- ", x, y + 130);
		backbf.setColor(Color.PINK);
		backbf.drawString("<" + GameWindow.level + ">", x + 160, y + 130);
		backbf.setColor(Color.CYAN);
		
		backbf.drawString("[D] Deflect On Edges -- ", x, y + 150);
		toggle(backbf, "<ON>", "<OFF>", GameWindow.boolDeflect, x + 160, y + 150);
		
		backbf.drawString("[M] Music -- ", x, y + 170);
		toggle(backbf, "<ON>", "<OFF>", GameWindow.boolMusic, x + 160, y + 170);
		
		backbf.drawString("[P] Number of Players -- ", x, y + 190);
		toggle(backbf, "<TWO>", "<ONE>", GameWindow.boolPlayers, x + 160, y + 190);
		
		backbf.drawString("[S] Save Game", x, y + 210);
		backbf.drawString("[C] Continue Saved Game ", x, y + 230);
		
	}
	
	
	private static void toggle(Graphics backbf, String on, String off, boolean mode, int intX, int intY){
		if (mode){
			backbf.setColor(Color.PINK);
			backbf.drawString(on, intX, intY);
		}
		else {
			backbf.setColor(Color.GRAY);
			backbf.drawString(off, intX, intY);
		}
		backbf.setColor(Color.CYAN);
	}

}
