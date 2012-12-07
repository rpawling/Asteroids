package game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.*;

import javax.swing.JOptionPane;

public class Score {

	private static final int x = 0;
	private static final int y = 0;
	private static int score1 = 0;
	private static int score2 = 0;
	private static Ship player1;
	private static Ship player2;
	private static String[] hiWinners = new String[10];
	private static int[] hiScores = new int[10];
	// Initialize the highscore table by loading .asteroids file and parsing into list of player scores
	public static void initialize(Ship ship1, Ship ship2) {
		player1 = ship1;
		player2 = ship2;

		File scoreFile = new File(".scores.txt");
		if (scoreFile.exists()) {
			BufferedReader reader = null;
			try {
				reader = new BufferedReader(new FileReader(scoreFile));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			String line;
			try {
				int i = 0;
				while ((line = reader.readLine()) != null) {
					// process the line.
					if (i<10) {
						hiWinners[i] = line.split(" ")[0];
						hiScores[i] = Integer.parseInt(line.split(" ")[1]);
						i++;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else {
			System.out.println("not exist");
			// Make a default list to be saved later
			hiWinners[0] = "Alfred";
			hiScores[0] = 1000;
			hiWinners[1] = "Bruce";
			hiScores[1] = 900;
			hiWinners[2] = "Crane";
			hiScores[2] = 800;
			hiWinners[3] = "Dent";
			hiScores[3] = 700;
			hiWinners[4] = "Nigma";
			hiScores[4] = 600;
			hiWinners[5] = "Grayson";
			hiScores[5] = 500;
			hiWinners[6] = "Fries";
			hiScores[6] = 400;
			hiWinners[7] = "Strange";
			hiScores[7] = 300;
			hiWinners[8] = "Gordon";
			hiScores[8] = 200;
			hiWinners[9] = "Kyle";
			hiScores[9] = 100;
		}


	}

	public static void draw(Graphics backbf){

		//set font
		Font f = new Font("Dialog", Font.BOLD, 12);
		backbf.setFont(f);
		if (player2 != null) {
			backbf.setColor(Color.YELLOW);
			backbf.drawString("Score:  " + Integer.toString(score2), x + GameWindow.xScreen - 90, y + 30);
			if (!GameWindow.boolUnlimitedLives) {
				backbf.drawString("Lives:  " + Integer.toString(player2.getNumLives()), x + GameWindow.xScreen - 90, y + 50);
			} else { backbf.drawString("Lives:  Inf", x + GameWindow.xScreen - 90, y + 50); }
		}

		backbf.setColor(Color.GREEN);
		backbf.drawString("Score:  " + Integer.toString(score1), x + 30, y + 30);
		if (!GameWindow.boolUnlimitedLives) {
			backbf.drawString("Lives:  " + Integer.toString(player1.getNumLives()), x + 30, y + 50);
		} else { backbf.drawString("Lives:  Inf", x + 30, y + 50); }

		backbf.setColor(Color.BLUE);
		backbf.drawString("Level " + Integer.toString(GameWindow.level), x + GameWindow.xScreen/2 - 20, y + 30);

	}

	public static void sortHiscores() {
		if (score1 > hiScores[9]) {
			// Query for player 1's name
			String name = JOptionPane.showInputDialog("Player1 Enter Name for Hiscore:");
			if (name == null) {
				name = "Unknown";
			}
			// Sort in score with other players
			for (int i = 0; i<10; i++) {
				if (score1 > hiScores[i]) {
					// copy down one
					for (int j=9; j>i; j--) {
						hiScores[j] = hiScores[j-1];
						hiWinners[j] = hiWinners[j-1];
					}
					// replace with the player
					hiScores[i] = score1;
					hiWinners[i] = name;
					break;
				}
			}
		}
		if (player2 != null) {
			if (score2 > hiScores[9]) {
				// Query for player 2's name
				String name = JOptionPane.showInputDialog("Player2 Enter Name for Hiscore:");
				if (name == null) {
					name = "Unknown";
				}
				// Sort in score with other players
				for (int i = 0; i<10; i++) {
					if (score2 > hiScores[i]) {
						// copy down one
						for (int j=9; j>i; j--) {
							hiScores[j] = hiScores[j-1];
							hiWinners[j] = hiWinners[j-1];
						}
						// replace with the player
						hiScores[i] = score2;
						hiWinners[i] = name;
						break;
					}
				}
			}
		}
		// Save list of scores to .scores
		try{
			// Create file 
			FileWriter fstream = new FileWriter(".scores.txt");
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i=0; i<10; i++) {
				out.write(hiWinners[i] + " " + hiScores[i]);
				out.newLine();
			}
			out.close();
		}catch (Exception e){//Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	public static void drawHiscores(Graphics backbf) {
		Font f = new Font("Dialog", Font.BOLD, 36);
		backbf.setFont(f);
		backbf.setColor(Color.WHITE);
		backbf.drawString("GAME OVER", GameWindow.xScreen/2 - 110, 100);
		if (player2 != null) {
			if (player1.getNumLives() <= 0) {
				backbf.setColor(Color.YELLOW);
				backbf.drawString("Player 2 Wins!", GameWindow.xScreen/2 - 120, 150);
			} else {
				backbf.setColor(Color.GREEN);
				backbf.drawString("Player 1 Wins!", GameWindow.xScreen/2 - 120, 150);
			}
		}
		f = new Font("Dialog", Font.BOLD, 12);
		backbf.setFont(f);
		backbf.setColor(Color.WHITE);
		backbf.drawString("HISCORES", GameWindow.xScreen/2 - 30, 250);
		for (int i=0; i<10; i++) {
			backbf.drawString(Integer.toString(i+1) + ".", GameWindow.xScreen/2 - 60, 280 + i*20);
			backbf.drawString(hiWinners[i], GameWindow.xScreen/2 - 20, 280 + i*20);
			backbf.drawString(Integer.toString(hiScores[i]), GameWindow.xScreen/2 + 40, 280 + i*20);
		}
		
		backbf.drawString("Press ENTER to Start New Game", GameWindow.xScreen/2 - 60, 500);
	}

	public static void addScore(int player, int i) {
		if (player == 1){
			score1+=i;
		}
		else if (player == 2) {
			score2+=i;
		}

	}

	public static void clearHiScores() {
		// Make a default list to be saved later
		hiWinners[0] = "Alfred";
		hiScores[0] = 1000;
		hiWinners[1] = "Bruce";
		hiScores[1] = 900;
		hiWinners[2] = "Crane";
		hiScores[2] = 800;
		hiWinners[3] = "Dent";
		hiScores[3] = 700;
		hiWinners[4] = "Nigma";
		hiScores[4] = 600;
		hiWinners[5] = "Grayson";
		hiScores[5] = 500;
		hiWinners[6] = "Fries";
		hiScores[6] = 400;
		hiWinners[7] = "Strange";
		hiScores[7] = 300;
		hiWinners[8] = "Gordon";
		hiScores[8] = 200;
		hiWinners[9] = "Kyle";
		hiScores[9] = 100;
	}

	public static int getScore1() { return score1; }
	public static int getScore2() { return score2; }
	public static void setScore1(int score) { score1 = score; }
	public static void setScore2(int score) { score2 = score; }


}
