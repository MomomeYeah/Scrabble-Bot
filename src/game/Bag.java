package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class Bag {
	
	private ArrayList<Tile> tiles;
	
	public Bag() throws IOException {
		this.tiles = new ArrayList<Tile>();
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("tiles.txt"));
			String line = "";
			String values[] = new String[3];
			while ((line = br.readLine()) != null) {
				values = line.split(",");
				for (int tileCount = 0; tileCount < Integer.parseInt(values[1]); tileCount++) {
					this.tiles.add(new Tile(values[0].charAt(0), Integer.parseInt(values[2])));
				}
			}
		}
		finally {
			br.close();
		}
	}
	
	public String toString() {
		String ret = "";
		for (Tile t : this.tiles) {
			ret += t.toString() + System.lineSeparator();
		}
		
		return ret;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public void shuffle() {
		Collections.shuffle(this.tiles);
	}
	
	public boolean isEmpty() {
		return this.tiles.size() == 0;
	}
	
	public int getTileCount() {
		return this.tiles.size();
	}
	
	public Tile drawOne() {
		return this.tiles.remove(this.tiles.size() - 1);
	}
	
	public ArrayList<Tile> draw(int count) {
		int handSize = Math.min(count, this.tiles.size());
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		for (int i = 0; i < handSize; i++) {
			hand.add(this.drawOne());
		}
		
		return hand;
	}
	
	public ArrayList<Tile> draw() {
		return this.draw(7);
	}
	
	public void replace(ArrayList<Tile> tiles)  {
		this.tiles.addAll(tiles);
		Collections.shuffle(this.tiles);
	}
	
	public ArrayList<Tile> exchange(ArrayList<Tile> tiles) {
		int numTiles = tiles.size();
		
		this.replace(tiles);
		return this.draw(numTiles);
	}
	
	public static void main(String args[]) throws IOException {
		Bag b = new Bag();
		b.print();
		
		System.out.println("Shuffling..." + System.lineSeparator());
		
		b.shuffle();
		b.print();
		
		System.out.println("Let's draw some tiles!" + System.lineSeparator());
		
		ArrayList<Tile> hand = b.draw(7);
		for (Tile t : hand) {
			t.print();
		}
	}

}
