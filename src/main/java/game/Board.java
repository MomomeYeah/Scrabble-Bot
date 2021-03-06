package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import net.minidev.json.JSONArray;
import solver.Trie;

public class Board {
	
	public int boardsize;
	public boolean wordsPlayed;
	public Cell[][] cells;
	public ArrayList<Cell> anchors;
	public HashSet<String> dictionary;
	public Trie trie;
	
	public Board() throws IOException {
		this.boardsize = 15;
		this.wordsPlayed = false;
		this.cells = new Cell[this.boardsize][this.boardsize];
		this.anchors = new ArrayList<Cell>();
		
		int row = 0, column = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("src/main/resources/initialboard.txt"));
			String line = "";
			String boardCells[] = new String[this.boardsize];
			while ((line = br.readLine()) != null) {
				boardCells = line.split(",");
				for (column = 0; column < this.boardsize; column++) {
					this.cells[row][column] = new Cell(row, column, boardCells[column]);
				}
				row++;
			}
		}
		finally {
			if (br != null) {
				br.close();
			}
		}
		
		br = null;
		this.dictionary = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader("src/main/resources/dictionary.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				this.dictionary.add(line);
			}
		}
		finally {
			if (br != null) {
				br.close();
			}
		}
		
		this.trie = new Trie();
		trie.load("src/main/resources/dictionary.txt");
		
		this.calculateAnchorsAndCrossChecks();
	}
	
	public void reset() {
		this.wordsPlayed = false;
		for (int row = 0; row < this.boardsize; row++) {
			for (int column = 0; column < this.boardsize; column++) {
				this.cells[row][column].reset();
			}
		}
		this.calculateAnchorsAndCrossChecks();
	}
	
	public String toString() {
		String ret = "";
		for (int row = 0; row < this.boardsize; row++) {
			for (int column = 0; column < this.boardsize; column++) {
				Cell c = this.cells[row][column];
				if (c.isAnchor()) {
					//ret += "* ";
					ret += c.getCellType().toString();
				} else if (c.getTile() != null) {
					ret += c.getTile().letter + " ";
				} else {
					ret += c.getCellType().toString();
				}
			}
			ret += System.lineSeparator();
		}
		
		return ret;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public JSONArray toJSON() {
		JSONArray arr = new JSONArray();
		for (int row = 0; row < this.boardsize; row++) {
			for (int column = 0; column < this.boardsize; column++) {
				arr.add(this.cells[row][column].toJSON());
			}
		}
		
		return arr;
	}
	
	public boolean inBounds(int row, int column) {
		if (row < 0 || row >= this.boardsize || column < 0 || column >= this.boardsize) {
			return false;
		}
		return true;
	}
	
	public int decrementIfInBounds(int num) {
		if (num - 1 >= 0) {
			return num - 1;
		}
		
		return num;
	}
	
	public int incrementIfInBounds(int num) {
		if (num + 1 <= this.boardsize - 1) {
			return num + 1;
		}
		
		return num;
	}
	
	public boolean hasNorthSouthNeighbors(int row, int column) {
		if (row > 0 && !this.cells[row - 1][column].isEmpty()) {
			return true;
		}
		if (row < this.boardsize - 1 && !this.cells[row + 1][column].isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public boolean hasEastWestNeighbors(int row, int column) {
		if (column > 0 && !this.cells[row][column - 1].isEmpty()) {
			return true;
		}
		if (column < this.boardsize - 1 && !this.cells[row][column + 1].isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public ArrayList<Character> getPrefixForDirection(int row, int column, PlayDirection direction) {
		ArrayList<Character> prefix = new ArrayList<Character>();
		
		int prefixRow = row;
		int prefixColumn = column;
		
		// move back until we find an empty square
		do {
			if (direction == PlayDirection.ACROSS) {
				prefixColumn = this.decrementIfInBounds(prefixColumn);
				if (prefixColumn == 0) {
					break;
				}
			} else {
				prefixRow = this.decrementIfInBounds(prefixRow);
				if (prefixRow == 0) {
					break;
				}
			}
		} while (!this.cells[prefixRow][prefixColumn].isEmpty());
		
		// if we haven't moved, there's no prefix
		if (prefixRow == row && prefixColumn == column) {
			return prefix;
		}
		
		// we have moved, so if the square we are on is empty, we've moved one too far
		if (this.cells[prefixRow][prefixColumn].isEmpty()) {
			if (direction == PlayDirection.ACROSS) {
				prefixColumn++;
			} else {
				prefixRow++;
			}
		}
		
		while (prefixRow != row || prefixColumn != column) {
			prefix.add(this.cells[prefixRow][prefixColumn].getTile().letter);
			if (direction == PlayDirection.ACROSS) {
				prefixColumn++;
			} else {
				prefixRow++;
			}
		}
		
		return prefix;
	}
	
	public ArrayList<Character> getSuffixForDirection(int row, int column, PlayDirection direction) {
		ArrayList<Character> suffix = new ArrayList<Character>();
		
		int suffixRow = row;
		int suffixColumn = column;
		
		if (direction == PlayDirection.ACROSS) {
			suffixColumn = this.incrementIfInBounds(suffixColumn);
		} else {
			suffixRow = this.incrementIfInBounds(suffixRow);
		}
		
		// if we haven't moved, there's no suffix
		if (suffixRow == row && suffixColumn == column) {
			return suffix;
		}
		
		while (!this.cells[suffixRow][suffixColumn].isEmpty()) {
			suffix.add(this.cells[suffixRow][suffixColumn].getTile().letter);
			if (direction == PlayDirection.ACROSS) {
				suffixColumn++;
				if (suffixColumn == this.boardsize) {
					break;
				}
			} else {
				suffixRow++;
				if (suffixRow == this.boardsize) {
					break;
				}
			}
		}
		
		return suffix;
	}
	
	// if the given cell were to be used in an ACROSS word, what would the valid letters be
	// given the letters above and below it (if any)
	public void calculateCrossChecksDown(int row, int column) {
		ArrayList<Character> prefix = this.getPrefixForDirection(row, column, PlayDirection.DOWN);
		ArrayList<Character> suffix = this.getSuffixForDirection(row, column, PlayDirection.DOWN);
		ArrayList<Character> letters = this.trie.getValidLettersFromPrefixandSuffix(prefix, suffix);
		this.cells[row][column].setDownCrossCheck(letters);
	}
	
	// if the given cell were to be used in a DOWN word, what would the valid letters be
	// given the letters to the left and right of it (if any)
	public void calculateCrossChecksAcross(int row, int column) {
		ArrayList<Character> prefix = this.getPrefixForDirection(row, column, PlayDirection.ACROSS);
		ArrayList<Character> suffix = this.getSuffixForDirection(row, column, PlayDirection.ACROSS);
		ArrayList<Character> letters = this.trie.getValidLettersFromPrefixandSuffix(prefix, suffix);
		this.cells[row][column].setAcrossCrossCheck(letters);
	}
	
	public void calculateAnchorsAndCrossChecks() {
		this.anchors.clear();
		
		if (!this.wordsPlayed) {
			Cell cell = this.cells[this.boardsize / 2][this.boardsize / 2];
			cell.setIsAnchor(true);
			this.anchors.add(cell);
		} else {
			for (int row = 0; row < this.boardsize; row++) {
				for (int column = 0; column < this.boardsize; column++) {
					Cell cell = this.cells[row][column];
					cell.clearCrossChecks();
					cell.setIsAnchor(false);
					if (cell.isEmpty()) {
						if (this.hasNorthSouthNeighbors(row, column) || this.hasEastWestNeighbors(row, column)) {
							cell.setIsAnchor(true);
							this.anchors.add(cell);
							calculateCrossChecksDown(row, column);
							calculateCrossChecksAcross(row, column);
						}
					}
				}
			}
		}
	}
	
	public PlayedWord buildWordAndCalculateScore(ArrayList<TilePlacement> placements, PlayDirection direction) {
		String fullWord = "";
		int score = 0;
		int multiplier = 1;
		int row = placements.get(0).row;
		int column = placements.get(0).column;
		
		// there may be a pre-existing prefix already on the board, so move back a space and check
		if (direction == PlayDirection.ACROSS) {
			column = this.decrementIfInBounds(column);
		} else {
			row = this.decrementIfInBounds(row);
		}
		// move back until we find an empty square
		while (!this.cells[row][column].isEmpty()) {
			if (direction == PlayDirection.ACROSS) {
				column = this.decrementIfInBounds(column);
				if (column == 0) {
					break;
				}
			} else {
				row = this.decrementIfInBounds(row);
				if (row == 0) {
					break;
				}
			}
		}
		
		// the empty square is the square BEFORE the start of the word, so move forward a space
		if (this.cells[row][column].isEmpty() && (row != placements.get(0).row || column != placements.get(0).column)) {
			if (direction == PlayDirection.ACROSS) {
				column++;
			} else {
				row++;
			}
		}
		
		// for every tile we're given, add it to the word we're building up
		// for gaps in the tiles, fill the word with tiles from the board
		for (TilePlacement tp : placements) {
			if (direction == PlayDirection.ACROSS) {
				while (!(tp.column == column)) {
					fullWord += this.cells[row][column].getTile().letter;
					score += this.cells[row][column].getTile().points;
					column = this.incrementIfInBounds(column);
				}
				fullWord += tp.tile.letter;
				score += tp.tile.points * this.cells[row][column].getCellType().getTileMultiplier();
				multiplier *= this.cells[row][column].getCellType().getWordMultiplier();
				column = this.incrementIfInBounds(column);
			} else {
				while(!(tp.row == row)) {
					fullWord += this.cells[row][column].getTile().letter;
					score += this.cells[row][column].getTile().points;
					row = this.incrementIfInBounds(row);
				}
				fullWord += tp.tile.letter;
				score += tp.tile.points * this.cells[row][column].getCellType().getTileMultiplier();
				multiplier *= this.cells[row][column].getCellType().getWordMultiplier();
				row = this.incrementIfInBounds(row);
			}
		}
		
		// for every tile on the board forming our suffix, add it to the word we're building
		while (!this.cells[row][column].isEmpty()) {
			fullWord += this.cells[row][column].getTile().letter;
			score += this.cells[row][column].getTile().points;
			
			if (direction == PlayDirection.ACROSS) {
				if (column < this.boardsize - 1) {
					column++;
				} else {
					break;
				}
			} else {
				if (row < this.boardsize - 1) {
					row++;
				} else {
					break;
				}
			}
		}
		
		score *= multiplier;
		
		if (placements.size() == 7) {
			score += 50;
		}
		
		return new PlayedWord(fullWord, score);
	}
	
	public ArrayList<PlayedWord> getPlayedWords(ArrayList<TilePlacement> placements, PlayDirection direction) throws InvalidMoveException {
		// must play between 1 and 7 tiles
		if (placements.size() < 1 || placements.size() > 7) {
			throw new InvalidMoveException("Moves must contain between 1 and 7 tiles");
		}
		
		// if this is the first word to be played, must intersect centre square
		if (!this.wordsPlayed) {
			boolean centrePlayed = false;
			for (TilePlacement tp : placements) {
				if (tp.row == this.boardsize / 2 && tp.column == this.boardsize / 2) {
					centrePlayed = true;
				}
			}
			if (!centrePlayed) {
				throw new InvalidMoveException("You must play on the centre tile");
			}
		}
		
		ArrayList<PlayedWord> playedWords = new ArrayList<PlayedWord>();
		int row = placements.get(0).row;
		int column = placements.get(0).column;
		boolean playedOnAnchor = false;
		for (TilePlacement tp : placements) {
			// must be in bounds
			if (!this.inBounds(tp.row, tp.column)) {
				throw new InvalidMoveException("That move is out of bounds");
			}
			
			// if blank, letter must be set
			if (tp.tile.letter == Tile.blank) {
				throw new InvalidMoveException("A letter must be chosen for blank tiles");
			}
			
			// if playing across, row must be all the same
			if (direction == PlayDirection.ACROSS) {
				if (tp.row != row) {
					throw new InvalidMoveException("All tiles must be on the same row");
				}
			// if playing down, column must be all the same
			} else {
				if (tp.column != column) {
					throw new InvalidMoveException("All tiles must be on the same column");
				}
			}
			
			// target cell must be empty
			if (!this.cells[tp.row][tp.column].isEmpty()) {
				throw new InvalidMoveException("Moves cannot overlap existing tiles");
			}
			
			// is this tile being played on an anchor point?
			if (this.cells[tp.row][tp.column].isAnchor()) {
				playedOnAnchor = true;
			}
			
			// check intersection words
			boolean intersects = false;
			if (direction == PlayDirection.ACROSS && this.hasNorthSouthNeighbors(tp.row, tp.column)) {
				intersects = true;
			}
			if (direction == PlayDirection.DOWN && this.hasEastWestNeighbors(tp.row, tp.column)) {
				intersects = true;
			}
			if (intersects) {
				ArrayList<TilePlacement> intersectionPlacement = new ArrayList<TilePlacement>();
				intersectionPlacement.add(tp);
				PlayDirection inverseDirection = direction == PlayDirection.ACROSS ? PlayDirection.DOWN : PlayDirection.ACROSS;
				playedWords.add(this.buildWordAndCalculateScore(intersectionPlacement, inverseDirection));
			}
		}
		
		// if we haven't played on at least one anchor point, we aren't intersecting an existing word
		if (!playedOnAnchor) {
			throw new InvalidMoveException("Moves must intersect with at least one other tile");
		}
		
		playedWords.add(this.buildWordAndCalculateScore(placements, direction));
		
		for (PlayedWord pw : playedWords) {
			if (!this.dictionary.contains(pw.word)) {
				throw new InvalidMoveException(pw.word + " is not a vald word");
			}
		}
		
		return playedWords;
	}
	
	public int getScore(ArrayList<TilePlacement> placements, PlayDirection direction) throws InvalidMoveException {
		ArrayList<PlayedWord> playedWords = this.getPlayedWords(placements, direction);
		
		int score = 0;
		for (PlayedWord pw : playedWords) {
			score += pw.score;
		}
		
		return score;
	}
	
	public int placeTiles(ArrayList<TilePlacement> placements, PlayDirection direction) throws InvalidMoveException {
		int score = this.getScore(placements, direction);
		
		for (TilePlacement tp : placements) {
			this.cells[tp.row][tp.column].placeTile(tp.tile);
		}
		
		this.wordsPlayed = true;
		this.calculateAnchorsAndCrossChecks();
		
		return score;
	}
	
	public static void main(String args[]) throws IOException {
		Board b = new Board();
		b.print();
	}

}
