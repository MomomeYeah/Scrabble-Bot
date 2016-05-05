package game;

public class PlayedWord {
	
	public final String word;
	public final int score;
	
	public PlayedWord(String word, int score) {
		this.word = word;
		this.score = score;
	}
	
	public boolean equals(PlayedWord other) {
		return this.word.equals(other.word) && this.score == other.score;
	}

}
