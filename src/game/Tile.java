package game;

public class Tile {
	
	public static final char blank = ' ';
	
	public char letter;
	public int points;
	public boolean isBlank;
	
	public Tile(char letter, int points) {
		this.letter = letter;
		this.points = points;
		this.isBlank = (letter == Tile.blank);
	}
	
	public void setLetter(char letter) throws ScrabbleException {
		if (this.isBlank) {
			this.letter = letter;
		} else {
			throw new ScrabbleException("Letter can only be set for blank tiles");
		}
	}
	
	public String toString() {
		return this.letter + " - " + this.points;
	}
	
	public void print() {
		System.out.println(this.toString());
	}

}
