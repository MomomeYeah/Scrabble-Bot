package game;

public class PlayedWord {
	
	public final String word;
	public final int score;
	
	public PlayedWord(String word, int score) {
		this.word = word;
		this.score = score;
	}
	
	public boolean equals(PlayedWord one, PlayedWord two) {
		return one.word.equals(two.word) && one.score == two.score;
	}

}
