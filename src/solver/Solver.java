package solver;

import java.io.IOException;
import java.util.ArrayList;

import game.Bag;
import game.Board;
import game.InvalidMoveException;
import game.PlayDirection;
import game.ScrabbleException;
import game.Tile;
import game.TilePlacement;

public class Solver {
	
	// playing ACROSS vs. DOWN makes no difference for the first word
	// arbitrarily, let's choose to play ACROSS
	public static ArrayList<TilePlacement> getFirstWord(Board b, ArrayList<Tile> hand) throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placements = new ArrayList<TilePlacement>();
		
		ArrayList<ArrayList<Tile>> words = b.trie.getWords(hand);
		
		int row = b.boardsize / 2;
		int score = 0;
		int bestScore = 0;
		
		// for every word that we found...
		for (ArrayList<Tile> word : words) {	
			// try it in every possible starting position...
			int startLoc = (b.boardsize / 2) - (word.size() - 1);
			for (int column = startLoc; column <= (b.boardsize / 2); column++) {
				score = 0;
				ArrayList<TilePlacement> testPlacements = TilePlacement.getPlacements(word, row, column, PlayDirection.ACROSS);
				
				// find out the score this placement would yield...
				score = b.getScore(testPlacements, PlayDirection.ACROSS);
				
				// if this is the highest score so far, record this
				if (score > bestScore) {
					bestScore = score;
					placements.clear();
					placements.addAll(testPlacements);
				}
				
			}
			
		}
		
		System.out.print("Playing ");
		for (TilePlacement tp : placements) {
			System.out.print(tp.tile.letter);
		}
		System.out.print(" for " + bestScore + " points.");
		System.out.println("");
		
		return placements;
	}
	
	public static void main(String args[]) throws IOException, InvalidMoveException, ScrabbleException {
		long start = System.nanoTime();
		Board board = new Board();
		long finish = System.nanoTime();
		
		System.out.println("Loaded board in " + (finish - start)/1000000 + " milliseconds");
		
		Bag bag = new Bag();
		bag.shuffle();
		
		ArrayList<Tile> tiles = bag.draw();
		ArrayList<Character> tileCharacters = Tile.toCharacterList(tiles);
		
		/*System.out.print("Drew ");
		for (Character c : tileCharacters) {
			System.out.print(c + " ");
		}
		System.out.println("");*/
		
		start = System.nanoTime();
		ArrayList<ArrayList<Tile>> words = board.trie.getWords(tiles);
		finish = System.nanoTime();
		
		/*for (ArrayList<Tile> word : words) {
			System.out.println(Tile.listToString(word));
		}*/
		
		System.out.print("Drew ");
		for (Character c : tileCharacters) {
			System.out.print(c + " ");
		}
		System.out.println("");
		
		System.out.println("Found " + words.size() + " words in " + (finish - start)/1000000 + " milliseconds");
		
		start = System.nanoTime();
		ArrayList<TilePlacement> placements = Solver.getFirstWord(board, tiles);
		finish = System.nanoTime();

		System.out.println("Found best word in " + (finish - start)/1000000 + " milliseconds");
		
		for (TilePlacement tp : placements) {
			System.out.println("Playing " + tp.tile.letter + " on " + tp.row + ", " + tp.column);
		}
		
		System.out.print("Drew ");
		for (Character c : tileCharacters) {
			System.out.print(c + " ");
		}
		System.out.println("");
		
		System.out.println("Played tiles, got score of " + board.placeTiles(placements, PlayDirection.ACROSS));
		
	}

}
