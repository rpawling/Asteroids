package game;
import java.io.File;
import javax.sound.sampled.*;

public class SoundAsteroids {

	// every sound has clip object. must loadSound() before use
	private static Clip clipBulletHit;
	private static Clip clipAsteroidHit;
	private static Clip clipBGM;
	private static Clip clipThrust;
	private static Clip clipExplode;

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

			File file3 = new File("../sounds/RoidRage.wav");
			AudioInputStream audioInputStream3 = AudioSystem.getAudioInputStream(file3);
			clipBGM = AudioSystem.getClip();
			clipBGM.open(audioInputStream3);
			
			File file4 = new File("../sounds/thrust.wav");
			AudioInputStream audioInputStream4 = AudioSystem.getAudioInputStream(file4);
			clipThrust = AudioSystem.getClip();
			clipThrust.open(audioInputStream4);
			FloatControl volume = (FloatControl) clipThrust.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) 6.0206);
			
			File file5 = new File("../sounds/ShipExplode.wav");
			AudioInputStream audioInputStream5 = AudioSystem.getAudioInputStream(file5);
			clipExplode = AudioSystem.getClip();
			clipExplode.open(audioInputStream5);
			volume = (FloatControl) clipThrust.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue((float) 6.0206);
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
	//loop clip object.
	private static void loop(Clip clip) {
		// only play if clip is not running
		if (!clip.isRunning()) {
			// rewind
			clip.setFramePosition(0);
			// play
			clip.loop(Clip.LOOP_CONTINUOUSLY); // Start looping
		}
	}
	private static void stop(Clip clip) {
		// stop
		if (clip.isRunning()) {
			clip.stop();
		}
	}

	//play bullet hit sound
	public static void bullet(){
		play(clipBulletHit);
	}

	//play asteroid hit sound
	public static void asteroid(){
		play(clipAsteroidHit);
	}

	//play BGM
	public static void start_music() {
		loop(clipBGM);
	}
	
	public static void stop_music() {
		stop(clipBGM);
	}
	
	public static void start_thrust() {
		loop(clipThrust);
	}
	
	public static void stop_thrust() {
		stop(clipThrust);
	}
	
	public static void explode() {
		play(clipExplode);
	}
}