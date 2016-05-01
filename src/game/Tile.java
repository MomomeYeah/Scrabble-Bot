package game;

public class Tile {
	
	public char letter;
	public int points;
	
	public Tile(char letter, int points) {
		this.letter = letter;
		this.points = points;
	}
	
	public String toString() {
		return this.letter + " - " + this.points;
	}
	
	public void print() {
		System.out.println(this.toString());
	}

}
