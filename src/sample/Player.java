package sample;

public class Player {

	private String name;
	private int score;
	private Frame frame;
	
	Player () {
		name = "";
		score = 0;
		frame = new Frame();
	}

	public void setName (String text) {
		name = text;
	}
	
	public String getName () {
		return(name);
	}
	
	public void addScore (int increment) {
		score = score + increment;
	}

	public int getScore() {
		return(score);
	}
	
	public Frame getFrame() {
		return(frame);
	}
	
}
