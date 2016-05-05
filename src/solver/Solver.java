package solver;

import java.io.IOException;
import java.util.ArrayList;

import game.Bag;
import game.Tile;

public class Solver {
	
	private Trie trie;
	
	public Solver() throws IOException {
		this.trie = new Trie();
		this.trie.load("dictionary.txt");
	}
	
	public static void main(String args[]) throws IOException {
		Solver s = new Solver();
		Bag b = new Bag();
		b.shuffle();
		
		ArrayList<Tile> tiles = b.draw();
		ArrayList<Character> tileCharacters = new ArrayList<Character>();
		for (Tile t : tiles) {
			tileCharacters.add(t.letter);
		}
		
		ArrayList<String> words = s.trie.getWords(tileCharacters);
		
		System.out.print("Drew ");
		for (Character c : tileCharacters) {
			System.out.print(c + " ");
		}
		System.out.println("");
		
		System.out.println("Found " + words.size() + " words");
		for (String word : words) {
			System.out.println(word);
		}
		
	}

}