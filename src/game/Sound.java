package game;
import java.io.File;
import javax.sound.sampled.*;

public class Sound {

	// every sound has clip object. must loadSound() before use
	private static Clip clipBulletHit;
	private static Clip clipAsteroidHit;

	// initialize all sounds
	public static void loadSound() {

		// allocate audio clip sound file
		try {
			File file1 = new File("../sounds/laser.wav");
			AudioInputStream audioInputStream1 = AudioSystem.getAudioInputStream(file1);
			clipBulletHit = AudioSystem.getClip();
			clipBulletHit.open(audioInputStream1);
			
			File file2 = new File("../sounds/boom.wav");
			AudioInputStream audioInputStream2 = AudioSystem.getAudioInputStream(file2);
			clipAsteroidHit = AudioSystem.getClip();
			clipAsteroidHit.open(audioInputStream2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//play clip object.
	private static void play(Clip clip) {
		// stop
		if (clip.isRunning()) {
			clip.stop();
		}
		// rewind
		clip.setFramePosition(0);
		// play
		clip.start(); // Start playing
	}
	
	//play bullet hit sound
	public static void bullet(){
		play(clipBulletHit);
	}
	
	//play asteroid hit sound
	public static void asteroid(){
		play(clipAsteroidHit);
	}
}