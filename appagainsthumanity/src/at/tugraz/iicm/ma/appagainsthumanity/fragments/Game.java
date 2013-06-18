package at.tugraz.iicm.ma.appagainsthumanity.fragments;

public class Game {

	int maxRounds;
	int maxScore;
	
	boolean isFinished;
	
	public Game(int maxRound, int maxScore, boolean isFinished) {
		this.isFinished = isFinished;
		this.maxRounds = maxRound;
		this.maxScore = maxScore;
		
		System.out.println("finished: " + isFinished);
	}
	
}
