package game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BoardTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testCreateBoardSize() throws IOException {
		Board b = new Board();
		assertEquals(15, b.boardsize);
	}
	
	@Test
	public void testCreateCells() throws IOException {
		Board b = new Board();
		assertNotNull(b.cells);
	}
	
	@Test
	public void testCreateDictionary() throws IOException {
		Board b = new Board();
		assertNotNull(b.dictionary);
	}
	
	@Test
	public void testCreateNoAnchors() throws IOException {
		Board b = new Board();
		
		Object expectedAnchors[] = new Object[1];
		expectedAnchors[0] = b.cells[b.boardsize / 2][b.boardsize / 2];
		
		ArrayList<Cell> anchors = new ArrayList<Cell>();
		for (int row = 0; row < b.boardsize; row++) {
			for (int column = 0; column < b.boardsize; column++) {
				if (b.cells[row][column].isAnchor()) {
					anchors.add(b.cells[row][column]);
				}
			}
		}
		
		Assert.assertArrayEquals(expectedAnchors, anchors.toArray());
	}
	
	@Test
	public void testValidateFirstWordOffCentre() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 0, 0));
		placement.add(new TilePlacement(new Tile('A', 1), 0, 1));
		placement.add(new TilePlacement(new Tile('T', 1), 0, 2));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("You must play on the centre tile");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateFirstWordAcrossCentre() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.ACROSS);
		
		assertTrue(playedWords.get(0).equals(new PlayedWord("CAT", 10), playedWords.get(0)));
	}
	
	@Test
	public void testValidateFirstWordDownCentre() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.DOWN);
		
		assertTrue(playedWords.get(0).equals(new PlayedWord("CAT", 10), playedWords.get(0)));
	}
	
	@Test
	public void testValidateWordMisalignedAcross() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 6, 8));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("All tiles must be on the same row");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordMisalignedDown() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 8));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("All tiles must be on the same column");
		
		b.getPlayedWords(placement, PlayDirection.DOWN);
	}
	
	@Test
	public void testValidateWordDuplicateTile() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 6));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("Moves cannot overlap existing tiles");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testWordInDictionary() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.ACROSS);
		
		assertTrue(playedWords.get(0).equals(new PlayedWord("CAT", 10), playedWords.get(0)));
	}
	
	@Test
	public void testWordNotInDictionary() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('T', 1), 7, 6));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("TTT is not a vald word");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordCrosscheckAcrossValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		placement.add(new TilePlacement(new Tile('E', 1), 8, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 8, 9));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.ACROSS);
		
		assertTrue(
				playedWords.get(0).equals(new PlayedWord("AT", 2), playedWords.get(0)) &&
				playedWords.get(1).equals(new PlayedWord("TE", 3), playedWords.get(1)) &&
				playedWords.get(2).equals(new PlayedWord("TEA", 4), playedWords.get(2)));
	}
	
	@Test
	public void testValidateWordCrosscheckAcrossInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('C', 3), 8, 7));
		placement.add(new TilePlacement(new Tile('R', 1), 8, 8));
		placement.add(new TilePlacement(new Tile('Y', 1), 8, 9));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("AC is not a vald word");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordCrosscheckDownValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		placement.add(new TilePlacement(new Tile('E', 1), 8, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 9, 8));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.DOWN);
		
		assertTrue(
				playedWords.get(0).equals(new PlayedWord("AT", 2), playedWords.get(0)) &&
				playedWords.get(1).equals(new PlayedWord("TE", 3), playedWords.get(1)) &&
				playedWords.get(2).equals(new PlayedWord("TEA", 4), playedWords.get(2)));
	}
	
	@Test
	public void testValidateWordCrosscheckDownInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('C', 3), 7, 8));
		placement.add(new TilePlacement(new Tile('R', 1), 8, 8));
		placement.add(new TilePlacement(new Tile('Y', 1), 9, 8));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("AC is not a vald word");
		
		b.getPlayedWords(placement, PlayDirection.DOWN);
	}
	
	@Test
	public void testValidateWordAugmentValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.DOWN);
		
		assertTrue(playedWords.get(0).equals(new PlayedWord("CAT", 5), playedWords.get(0)));
	}
	
	@Test
	public void testValidateWordAugmentPostValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.DOWN);
		
		assertTrue(playedWords.get(0).equals(new PlayedWord("AT", 2), playedWords.get(0)));
	}
	
	@Test
	public void testValidateWordAugmentInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('C', 1), 8, 7));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("CAC is not a vald word");
		
		b.getPlayedWords(placement, PlayDirection.DOWN);
	}
	
	@Test
	public void testValidateWordAugmentPostInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('Z', 1), 8, 7));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("AZ is not a vald word");
		
		b.getPlayedWords(placement, PlayDirection.DOWN);
	}
	
	@Test
	public void testValidateWordOutOfBoundsLeftInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 2));
		placement.add(new TilePlacement(new Tile('B', 3), 7, 3));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 4));
		placement.add(new TilePlacement(new Tile('C', 3), 7, 5));
		placement.add(new TilePlacement(new Tile('U', 1), 7, 6));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 7));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('C', 3), 8, -1));
		placement.add(new TilePlacement(new Tile('R', 3), 8, 0));
		placement.add(new TilePlacement(new Tile('A', 1), 8, 1));
		placement.add(new TilePlacement(new Tile('B', 3), 8, 2));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("That move is out of bounds");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordOutOfBoundsRightInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('B', 3), 7, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 9));
		placement.add(new TilePlacement(new Tile('C', 3), 7, 10));
		placement.add(new TilePlacement(new Tile('U', 1), 7, 11));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 12));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('O', 1), 8, 12));
		placement.add(new TilePlacement(new Tile('V', 4), 8, 13));
		placement.add(new TilePlacement(new Tile('E', 1), 8, 14));
		placement.add(new TilePlacement(new Tile('R', 1), 8, 15));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("That move is out of bounds");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordOutOfBoundsLeftValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 2));
		placement.add(new TilePlacement(new Tile('B', 3), 7, 3));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 4));
		placement.add(new TilePlacement(new Tile('C', 3), 7, 5));
		placement.add(new TilePlacement(new Tile('U', 1), 7, 6));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 7));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('B', 3), 8, 0));
		placement.add(new TilePlacement(new Tile('R', 1), 8, 1));
		placement.add(new TilePlacement(new Tile('A', 1), 8, 2));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.ACROSS);
		
		assertTrue(
				playedWords.get(0).equals(new PlayedWord("AA", 3), playedWords.get(0)) && 
				playedWords.get(1).equals(new PlayedWord("BRA", 6), playedWords.get(1)));
	}
	
	@Test
	public void testValidateWordOutOfBoundsRightValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('B', 3), 7, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 9));
		placement.add(new TilePlacement(new Tile('C', 3), 7, 10));
		placement.add(new TilePlacement(new Tile('U', 1), 7, 11));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 12));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('O', 1), 8, 12));
		placement.add(new TilePlacement(new Tile('W', 4), 8, 13));
		placement.add(new TilePlacement(new Tile('E', 1), 8, 14));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.ACROSS);
		
		assertTrue(
				playedWords.get(0).equals(new PlayedWord("SO", 3), playedWords.get(0)) &&
				playedWords.get(1).equals(new PlayedWord("OWE", 7), playedWords.get(1)));
	}
	
	@Test
	public void testValidateWordEmptyWordInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("Moves must contain between 1 and 7 tiles");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testValidateWordLongWordInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('B', 3), 7, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 9));
		placement.add(new TilePlacement(new Tile('C', 3), 7, 10));
		placement.add(new TilePlacement(new Tile('U', 1), 7, 11));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 12));
		placement.add(new TilePlacement(new Tile('E', 1), 7, 13));
		placement.add(new TilePlacement(new Tile('S', 1), 7, 14));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("Moves must contain between 1 and 7 tiles");
		
		b.getPlayedWords(placement, PlayDirection.ACROSS);
	}
	
	@Test
	public void testCalculateAnchors() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 1), 7, 6));
		placement.add(new TilePlacement(new Tile('A', 3), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 7, 8));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('A', 1), 5, 7));
		placement.add(new TilePlacement(new Tile('B', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('C', 3), 8, 7));
		placement.add(new TilePlacement(new Tile('U', 1), 9, 7));
		placement.add(new TilePlacement(new Tile('S', 1), 10, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		b.print();
		
		Object expectedAnchors[] = new Object[14];
		expectedAnchors[0] = b.cells[4][7];
		expectedAnchors[1] = b.cells[5][6];
		expectedAnchors[2] = b.cells[5][8];
		expectedAnchors[3] = b.cells[6][6];
		expectedAnchors[4] = b.cells[6][8];
		expectedAnchors[5] = b.cells[7][5];
		expectedAnchors[6] = b.cells[7][9];
		expectedAnchors[7] = b.cells[8][6];
		expectedAnchors[8] = b.cells[8][8];
		expectedAnchors[9] = b.cells[9][6];
		expectedAnchors[10] = b.cells[9][8];
		expectedAnchors[11] = b.cells[10][6];
		expectedAnchors[12] = b.cells[10][8];
		expectedAnchors[13] = b.cells[11][7];
		
		ArrayList<Cell> anchors = new ArrayList<Cell>();
		for (int row = 0; row < b.boardsize; row++) {
			for (int column = 0; column < b.boardsize; column++) {
				if (b.cells[row][column].isAnchor()) {
					anchors.add(b.cells[row][column]);
				}
			}
		}
		
		Assert.assertArrayEquals(expectedAnchors, anchors.toArray());
	}
	
	@Test
	public void testValidateWordDoesIntersectValid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('E', 1), 8, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 9, 8));
		placement.add(new TilePlacement(new Tile('T', 1), 10, 8));
		
		ArrayList<PlayedWord> playedWords = b.getPlayedWords(placement, PlayDirection.DOWN);
		
		assertTrue(
				playedWords.get(0).equals(new PlayedWord("TE", 3), playedWords.get(0)) &&
				playedWords.get(1).equals(new PlayedWord("EAT", 4), playedWords.get(1)));
	}
	
	@Test
	public void testValidateWordDoesIntersectInvalid() throws IOException, InvalidMoveException {
		Board b = new Board();
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('C', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('T', 1), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('R', 3), 9, 8));
		placement.add(new TilePlacement(new Tile('A', 1), 10, 8));
		placement.add(new TilePlacement(new Tile('T', 1), 11, 8));
		
		exception.expect(InvalidMoveException.class);
		exception.expectMessage("Moves must intersect with at least one other tile");
		
		b.getPlayedWords(placement, PlayDirection.DOWN);
	}
	
}
