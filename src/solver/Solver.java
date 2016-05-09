package solver;

import java.io.IOException;
import java.util.ArrayList;

import game.Bag;
import game.Board;
import game.Cell;
import game.InvalidMoveException;
import game.Move;
import game.PlayDirection;
import game.ScrabbleException;
import game.Tile;
import game.TilePlacement;

public class Solver {
	
	public static Move getFirstMove(Board b, ArrayList<Tile> hand, PlayDirection direction) throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placements = new ArrayList<TilePlacement>();
		Move move = new Move(placements, direction);
		
		ArrayList<ArrayList<Tile>> words = b.trie.getWords(hand);

		int row = b.boardsize / 2;
		int column = b.boardsize / 2;
		int score = 0;
		int bestScore = 0;
		
		// for every word that we found...
		for (ArrayList<Tile> word : words) {	
			// try it in every possible starting position...
			int startLoc = (b.boardsize / 2) - (word.size() - 1);
			for (int incrementer = startLoc; incrementer <= (b.boardsize / 2); incrementer++) {
				score = 0;
				ArrayList<TilePlacement> testPlacements = null;
				
				if (direction == PlayDirection.ACROSS) {
					testPlacements = TilePlacement.getPlacements(word, row, incrementer, PlayDirection.ACROSS);
				} else {
					testPlacements = TilePlacement.getPlacements(word, incrementer, column, PlayDirection.DOWN);
				}
				
				// find out the score this placement would yield...
				score = b.getScore(testPlacements, direction);
				
				// if this is the highest score so far, record this
				if (score > bestScore) {
					bestScore = score;
					placements.clear();
					placements.addAll(testPlacements);
					move.setMove(placements, direction);
				}
				
			}
			
		}
		
		return move;
	}
	
	private static ArrayList<ArrayList<TilePlacement>> getMoveRecursive(Board b, Cell c, Node n, ArrayList<Tile> hand, PlayDirection direction) throws ScrabbleException {
		ArrayList<ArrayList<TilePlacement>> words = new ArrayList<ArrayList<TilePlacement>>();
		
		// if we're playing ACROSS and are at the right edge of the board, no words to make
		if (direction == PlayDirection.ACROSS && c.getColumn() == b.boardsize - 1) {
			return words;
		}
		
		// if we're playing DOWN and are at the bottom edge of the board, no words to make
		if (direction == PlayDirection.DOWN && c.getRow() == b.boardsize - 1) {
			return words;
		}
		
		Cell nextCell = null;
		if (direction == PlayDirection.ACROSS) {
			nextCell = b.cells[c.getRow()][c.getColumn() + 1];
		} else {
			nextCell = b.cells[c.getRow() + 1][c.getColumn()];
		}
		
		// if this cell has a tile in it, check to see if the tile's letter is a valid placement here
		// if so, continue on to the next cell
		// if not, return empty word list
		if (!c.isEmpty()) {
			Node nextNode = n.getChild(c.getTile().letter);
			if (nextNode != null) {
				return getMoveRecursive(b, nextCell, nextNode, hand, direction);
				
			}
			
			return words;
		}
		
		// if this is an anchor square, get all children of the current node, filtered by
		// the tiles in our hand, and the valid cross checks for this cell
		// if this is not an anchor square, filter only by the tiles in our hand
		ArrayList<Node> nodeChildren = null;
		if (c.isAnchor()) {
			if (direction == PlayDirection.ACROSS) {
				nodeChildren = n.getChildren(hand, c.getValidCrossChecksDown());
			} else {
				nodeChildren = n.getChildren(hand, c.getValidCrossChecksAcross());
			}
		} else {
			nodeChildren = n.getChildren(hand);
		}
		
		// if our hand is empty, nodeChildren will be empty, and we will return empty word list
		for (Node rec : nodeChildren) {
			if (rec.letter == Node.EOW) {
				words.add(new ArrayList<TilePlacement>());
			} else {
				Tile t = Tile.remove(hand, rec.letter);
				if (t == null) {
					t = Tile.remove(hand, Node.blank);
					t.setLetter(rec.letter);
				}
				TilePlacement placement = new TilePlacement(t, c.getRow(), c.getColumn());
				ArrayList<ArrayList<TilePlacement>> recWords = getMoveRecursive(b, nextCell, rec, hand, direction);
				for (ArrayList<TilePlacement> word : recWords) {
					word.add(0, placement);
					words.add(word);
				}
				if (t.isBlank) {
					Tile copy = t.copy();
					copy.setLetter(Tile.blank);
					hand.add(copy);
				} else {
					hand.add(t);
				}
			}
		}
		
		return words;
	}
	
	public static Move getMove(Board b, ArrayList<Tile> hand) throws ScrabbleException, InvalidMoveException {
		ArrayList<TilePlacement> placements = new ArrayList<TilePlacement>();
		PlayDirection direction = PlayDirection.ACROSS;
		
		Move move = new Move(placements, direction);
		
		int score = 0;
		int bestScore = 0;
		Node rootNode = b.trie.root;
		
		// for each anchor on the board
		for (Cell c : b.anchors) {
			int row = c.getRow();
			int column = c.getColumn();
			int columnMin = column - (hand.size() - 1);
			
			// find ACROSS moves first
			direction = PlayDirection.ACROSS;
			
			// find the leftmost point we can start at
			// do not overlap anchors in the same row as that anchor will 
			// cover squares both to the left of it and also itself
			if (column > 0 && b.cells[row][column - 1].isEmpty()) {
				// if the next leftmost cell is empty, move back as far as we can
				// up to the limit of the number of tiles in our hand
				while (column > 0 && column > columnMin && !b.cells[row][column - 1].isAnchor()) {
					column--;
				}
			} else if (column > 0 && !b.cells[row][column - 1].isEmpty()) {
				// if the next leftmost cell has a tile, this must be the prefix immediately to the
				// left of the cell we're starting on.  Move left until we hit an anchor
				while (column > 0 && !b.cells[row][column - 1].isAnchor()) {
					column--;
				}
			}
			
			// column variable is now our starting point for extending right
			// if we are on an empty cell, generate all words we can, then shift column
			// variable to the right and repeat, continuing until we are generating words
			// starting from the original anchor square.
			// if we are on a cell with a tile, our anchor must have had a prefix, so 
			// only generate words starting from this cell
			int columnMax = b.cells[row][column].isEmpty() ? c.getColumn() : column;
			while (column <= columnMax) {
				// get all words we can starting at (row, column)
				Cell startingCell = b.cells[row][column];
				
				ArrayList<ArrayList<TilePlacement>> words = Solver.getMoveRecursive(b, startingCell, rootNode, hand, direction);
				
				// for every word that we found...
				for (ArrayList<TilePlacement> word : words) {
					
					// TODO - when playing at end of prefix, prefix will be a valid word, and 
					//        current algorithm will produce an empty TilePlacement list.  
					//        Currently we ignore empty placements lists.
					//        what if we have a bug? need to come up with a way to avoid returning
					//        an empty placement list when playing after an existing prefix
					if (word.size() == 0) {
						continue;
					}
					
					// find out the score this placement would yield...
					try {
						score = b.getScore(word, direction);
						
						// if this is the highest score so far, record this
						if (score > bestScore) {
							bestScore = score;
							placements.clear();
							placements.addAll(word);
							move.setMove(placements, direction);
						}
					} catch (InvalidMoveException e) {
						// TODO - what to do with words that don't intersect?  
						//        current algorithm will move left as far as it can and generate 
						//        all words of all lengths, but these might not reach all the 
						//        way back to the original anchor cell.
						//        how to efficiently throw these away without checking against
						//        exception message?
						if (!e.getMessage().equals("Moves must intersect with at least one other tile")) {
							throw e;
						}
						/*System.out.println("Trying to play...");
						for (TilePlacement tp : word) {
							System.out.print(tp.tile.letter);
						}
						System.out.println("");
						System.out.println("Starting from " + row + ", " + column);*/
						//throw e;
					}
					
				}
				
				column++;
			}
			
			// now find DOWN moves
			direction = PlayDirection.DOWN;
			// reset column value
			column = c.getColumn();
			int rowMin = row - (hand.size() - 1);
			
			
			// find the topmost point we can start at
			// do not overlap anchors in the same column as that anchor will 
			// cover squares both above of it and also itself
			if (row > 0 && b.cells[row - 1][column].isEmpty()) {
				// if the next topmost cell is empty, move back as far as we can
				// up to the limit of the number of tiles in our hand
				while (row > 0 && row > rowMin && !b.cells[row - 1][column].isAnchor()) {
					row--;
				}
			} else if (row > 0 && !b.cells[row - 1][column].isEmpty()) {
				// if the next topmost cell has a tile, this must be the prefix immediately above
				// the cell we're starting on.  Move up until we hit an anchor
				while (row > 0 && !b.cells[row - 1][column].isAnchor()) {
					row--;
				}
			}
			
			// row variable is now our starting point for extending down
			// if we are on an empty cell, generate all words we can, then shift row
			// variable down and repeat, continuing until we are generating words
			// starting from the original anchor square.
			// if we are on a cell with a tile, our anchor must have had a prefix, so 
			// only generate words starting from this cell
			int rowMax = b.cells[row][column].isEmpty() ? c.getRow() : row;
			while (row <= rowMax) {
				// get all words we can starting at (row, column)
				Cell startingCell = b.cells[row][column];
				
				ArrayList<ArrayList<TilePlacement>> words = Solver.getMoveRecursive(b, startingCell, rootNode, hand, direction);
				
				// for every word that we found...
				for (ArrayList<TilePlacement> word : words) {
					
					// TODO - when playing at end of prefix, prefix will be a valid word, and 
					//        current algorithm will produce an empty TilePlacement list.  
					//        Currently we ignore empty placements lists.
					//        what if we have a bug? need to come up with a way to avoid returning
					//        an empty placement list when playing after an existing prefix
					if (word.size() == 0) {
						continue;
					}
					
					// find out the score this placement would yield...
					try {
						score = b.getScore(word, direction);
						
						// if this is the highest score so far, record this
						if (score > bestScore) {
							bestScore = score;
							placements.clear();
							placements.addAll(word);
							move.setMove(placements, direction);
						}
					} catch (InvalidMoveException e) {
						// TODO - what to do with words that don't intersect?  
						//        current algorithm will move left as far as it can and generate 
						//        all words of all lengths, but these might not reach all the 
						//        way back to the original anchor cell.
						//        how to efficiently throw these away without checking against
						//        exception message?
						if (!e.getMessage().equals("Moves must intersect with at least one other tile")) {
							throw e;
						}
						/*System.out.println("Trying to play...");
						for (TilePlacement tp : word) {
							System.out.print(tp.tile.letter);
						}
						System.out.println("");
						System.out.println("Starting from " + row + ", " + column);*/
						//throw e;
					}
					
				}
				
				row++;
			}
			
		}
		
		return move;
	}
	
	public static void main(String args[]) throws IOException, InvalidMoveException, ScrabbleException {
		long start = System.nanoTime();
		Board board = new Board();
		long finish = System.nanoTime();
		
		System.out.println("Loaded board in " + (finish - start)/1000000 + " milliseconds");
		
		PlayDirection firstMoveDirection = PlayDirection.DOWN;
		
		Bag bag = new Bag();
		bag.shuffle();
		
		ArrayList<Tile> tiles = bag.draw();
		int score = 0;
		int totalScore = 0;
		
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
		for (Tile t : tiles) {
			System.out.print(t.letter + " ");
		}
		System.out.println("");
		
		System.out.println("Found " + words.size() + " words in " + (finish - start)/1000000 + " milliseconds");
		
		start = System.nanoTime();
		Move move = Solver.getFirstMove(board, tiles, firstMoveDirection);
		finish = System.nanoTime();

		System.out.println("Found best word in " + (finish - start)/1000000 + " milliseconds");
		
		for (TilePlacement tp : move.placements) {
			System.out.println("Playing " + tp.tile.letter + " on " + tp.row + ", " + tp.column);
		}
		
		System.out.print("Drew ");
		for (Tile t : tiles) {
			System.out.print(t.letter + " ");
		}
		System.out.println("");
		
		if (move.placements.size() > 0) {
			System.out.print("Playing ");
			for (TilePlacement tp : move.placements) {
				System.out.print(tp.tile.letter);
			}
			score = board.placeTiles(move.placements, firstMoveDirection);
			totalScore += score;
			System.out.println(" for a score of " + score);
			System.out.println("Total score is " + totalScore);
			board.print();
		} else {
			System.out.println("No words found, not playing");
		}
		
		while (bag.getTileCount() > 0) {
			
			for (TilePlacement tp : move.placements) {
				for (int i = 0; i < tiles.size(); i++) {
					if (tp.tile.equalsOrBlank(tiles.get(i))) {
						tiles.remove(i);
						break;
					}
				}
			}
			
			if (move.placements.size() == 0) {
				ArrayList<Tile> newTiles = bag.exchange(tiles);
				tiles.clear();
				tiles.addAll(newTiles);
			} else {
				tiles.addAll(bag.draw(7 - tiles.size()));
			}
			
			System.out.print("Hand is now ");
			for (Tile t : tiles) {
				System.out.print(t.letter + " ");
			}
			System.out.println("");
			System.out.println("Bag has " + bag.getTileCount() + " tiles left");
			
			// TODO - this sometimes hangs here, need to diagnose this
			start = System.nanoTime();
			move = Solver.getMove(board, tiles);
			finish = System.nanoTime();
			
			System.out.println("Found best word in " + (finish - start)/1000000 + " milliseconds");
			
			for (TilePlacement tp : move.placements) {
				System.out.println("Playing " + tp.tile.letter + " on " + tp.row + ", " + tp.column);
			}
			
			System.out.print("Drew ");
			for (Tile t : tiles) {
				System.out.print(t.letter + " ");
			}
			System.out.println("");
			if (move.placements.size() > 0) {
				System.out.print("Playing ");
				for (TilePlacement tp : move.placements) {
					System.out.print(tp.tile.letter);
				}
				score = board.placeTiles(move.placements, move.direction);
				totalScore += score;
				System.out.println(" for a score of " + score);
				System.out.println("Total score is " + totalScore);
				board.print();
			} else {
				System.out.println("No words found, not playing");
			}
			
		}
		
	}

}
