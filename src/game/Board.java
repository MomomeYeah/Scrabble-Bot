package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

public class Board {
	
	public int boardsize;
	public boolean wordsPlayed;
	public Cell[][] cells;
	public HashSet<String> dictionary;
	
	public Board() throws IOException {
		this.boardsize = 15;
		this.wordsPlayed = false;
		this.cells = new Cell[this.boardsize][this.boardsize];
		
		int row = 0, column = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("initialboard.txt"));
			String line = "";
			String cells[] = new String[this.boardsize];
			while ((line = br.readLine()) != null) {
				cells = line.split(",");
				for (column = 0; column < this.boardsize; column++) {
					this.cells[row][column] = new Cell(cells[column]);
				}
				row++;
			}
		}
		finally {
			br.close();
		}
		
		this.dictionary = new HashSet<String>();
		try {
			br = new BufferedReader(new FileReader("dictionary.txt"));
			String line = "";
			while ((line = br.readLine()) != null) {
				this.dictionary.add(line);
			}
		}
		finally {
			br.close();
		}
		
		this.calculateAnchors();
	}
	
	public String toString() {
		String ret = "";
		for (int row = 0; row < this.boardsize; row++) {
			for (int column = 0; column < this.boardsize; column++) {
				if (this.cells[row][column].getTile() != null) {
					ret += this.cells[row][column].getTile().letter + " ";
				} else {
					ret += this.cells[row][column].getCellType().toString();
				}
			}
			ret += System.lineSeparator();
		}
		
		return ret;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public boolean inBounds(int row, int column) {
		if (row < 0 || row >= this.boardsize || column < 0 || column >= this.boardsize) {
			return false;
		}
		return true;
	}
	
	public int decrementIfInBounds(int num) {
		return Math.max(0, num - 1);
	}
	
	public int incrementIfInBounds(int num) {
		return Math.min(this.boardsize - 1, num + 1);
	}
	
	public boolean isCellAdjacentToFilledCell(int row, int column) {
		if (row > 0 && !this.cells[row-1][column].isEmpty()) {
			return true;
		}
		if (row < this.boardsize - 1 && !this.cells[row+1][column].isEmpty()) {
			return true;
		}
		
		if (column > 0 && !this.cells[row][column-1].isEmpty()) {
			return true;
		}
		if (column < this.boardsize - 1 && !this.cells[row][column+1].isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	public void calculateAnchors() {
		if (!this.wordsPlayed) {
			this.cells[this.boardsize / 2][this.boardsize / 2].setIsAnchor(true);
		} else {
			for (int row = 0; row < this.boardsize; row++) {
				for (int column = 0; column < this.boardsize; column++) {
					boolean isAnchor = false;
					if (this.isCellAdjacentToFilledCell(row, column) && this.cells[row][column].isEmpty()) {
						isAnchor = true;
					}
					
					this.cells[row][column].setIsAnchor(isAnchor);
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
			} else {
				row = this.decrementIfInBounds(row);
			}
		}
		// the empty square is the square BEFORE the start of the word, so move forward a space
		if (row != placements.get(0).row || column != placements.get(0).column) {
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
					column++;
				}
				fullWord += tp.tile.letter;
				score += tp.tile.points * this.cells[row][column].getCellType().getTileMultiplier();
				multiplier *= this.cells[row][column].getCellType().getWordMultiplier();
				column++;
			} else {
				while(!(tp.row == row)) {
					fullWord += this.cells[row][column].getTile().letter;
					score += this.cells[row][column].getTile().points;
					row++;
				}
				fullWord += tp.tile.letter;
				score += tp.tile.points * this.cells[row][column].getCellType().getTileMultiplier();
				multiplier *= this.cells[row][column].getCellType().getWordMultiplier();
				row++;
			}
		}
		// there might be a pre-existing suffix on the board
		// move forward a space in preparation for checking this
		if (direction == PlayDirection.ACROSS) {
			column = this.incrementIfInBounds(column);
		} else {
			row = this.incrementIfInBounds(row);
		}
		
		// for every tile on the board forming our suffix, add it to the word we're building
		while (!this.cells[row][column].isEmpty()) {
			fullWord += this.cells[row][column].getTile().letter;
			score += this.cells[row][column].getTile().points;
			
			if (direction == PlayDirection.ACROSS) {
				column = this.incrementIfInBounds(column);
			} else {
				row = this.incrementIfInBounds(row);
			}
		}
		
		score *= multiplier;
		
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
			if (direction == PlayDirection.ACROSS && !this.cells[tp.row - 1][tp.column].isEmpty()) {
				intersects = true;
			}
			if (direction == PlayDirection.DOWN && !this.cells[tp.row][tp.column - 1].isEmpty()) {
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
	
	public int placeTiles(ArrayList<TilePlacement> placements, PlayDirection direction) throws InvalidMoveException {
		ArrayList<PlayedWord> playedWords = this.getPlayedWords(placements, direction);
		
		int score = 0;
		for (PlayedWord pw : playedWords) {
			score += pw.score;
		}
		
		for (TilePlacement tp : placements) {
			this.cells[tp.row][tp.column].placeTile(tp.tile);
		}
		
		this.wordsPlayed = true;
		this.calculateAnchors();
		
		return score;
	}
	
	public static void main(String args[]) throws IOException {
		Board b = new Board();
		b.print();
	}

}