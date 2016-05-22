package game;

import java.util.ArrayList;

import net.minidev.json.JSONObject;

public class Tile {
	
	public static final char blank = ' ';
	
	public char letter;
	public int points;
	public boolean isBlank;
	
	private Tile(char letter, int points, boolean isBlank) {
		this.letter = letter;
		this.points = points;
		this.isBlank = isBlank;
	}
	
	public Tile(char letter, int points) {
		this(letter, points, letter == Tile.blank);
	}
	
	public boolean equals(Tile other) {
		return this.letter == other.letter && this.points == other.points && this.isBlank == other.isBlank;
	}
	
	public boolean equalsOrBlank(Tile other) {
		if (this.letter == other.letter && this.points == other.points) {
			return true;
		} else {
			return other.isBlank;
		}
	}
	
	public void setLetter(char letter) throws ScrabbleException {
		if (this.isBlank) {
			this.letter = letter;
		} else {
			throw new ScrabbleException("Letter can only be set for blank tiles");
		}
	}
	
	public Tile copy() {
		return new Tile(this.letter, this.points, this.isBlank);
	}
	
	public static ArrayList<Character> toCharacterList(ArrayList<Tile> tiles) {
		ArrayList<Character> tileChars = new ArrayList<Character>();
		for (Tile t : tiles) {
			tileChars.add(t.letter);
		}
		
		return tileChars;
	}
	
	public static boolean contains(ArrayList<Tile> tiles, Character c) {
		for (Tile t : tiles) {
			if (t.letter == c) {
				return true;
			}
		}
		
		return false;
	}
	
	public static Tile remove(ArrayList<Tile> tiles, Character c) {
		Tile t = null;
		
		for (int i = 0; i < tiles.size(); i++) {
			t = tiles.get(i);
			if (t.letter == c) {
				tiles.remove(i);
				return t;
			}
		}
		
		return null;
	}
	
	public static String listToString(ArrayList<Tile> tiles) {
		String s = "";
		for (Tile t : tiles) {
			s += t.letter;
		}
		
		return s;
	}
	
	public String toString() {
		return this.letter + " - " + this.points;
	}
	
	public JSONObject toJSON() {
		// implicitly cast this.letter to String
		// if we leave it as char, seems to come out as empty object
		JSONObject obj = new JSONObject();
		obj.put("letter", this.letter+"");
		obj.put("points", this.points);
		obj.put("isBlank", this.isBlank);
		
		return obj;
	}
	
	public void print() {
		System.out.println(this.toString());
	}

}
