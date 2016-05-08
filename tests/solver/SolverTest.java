package solver;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import game.Board;
import game.InvalidMoveException;
import game.PlayDirection;
import game.ScrabbleException;
import game.Tile;
import game.TilePlacement;

public class SolverTest {
	
	public static Board b;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		b = new Board();
	}
	
	@Before
	public void clearBoard() {
		b.reset();
	}

	@Test
	public void testAcrossTopOfDown() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('J', 8), 6, 7));
		placement.add(new TilePlacement(new Tile('I', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('N', 1), 8, 7));
		placement.add(new TilePlacement(new Tile('N', 1), 9, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile('D', 2));
		hand.add(new Tile('O', 1));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		assertTrue(
				placement.size() == 2 &&
				placement.get(0).equals(new TilePlacement(new Tile('O', 1), 5, 6)) &&
				placement.get(1).equals(new TilePlacement(new Tile('D', 2), 5, 7))
				);
	}

	@Test
	public void testAcrossBottomOfDown() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('J', 8), 6, 7));
		placement.add(new TilePlacement(new Tile('I', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('N', 1), 8, 7));
		placement.add(new TilePlacement(new Tile('N', 1), 9, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile('S', 1));
		hand.add(new Tile('O', 1));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		assertTrue(
				placement.size() == 2 &&
				placement.get(0).equals(new TilePlacement(new Tile('O', 1), 10, 6)) &&
				placement.get(1).equals(new TilePlacement(new Tile('S', 1), 10, 7))
				);
	}

	@Test
	public void testAcrossFullIntersectDown() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('J', 8), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('M', 3), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile('D', 2));
		hand.add(new Tile('I', 1));
		hand.add(new Tile('N', 1));
		hand.add(new Tile('N', 1));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		assertTrue(
				placement.size() == 4 &&
				placement.get(0).equals(new TilePlacement(new Tile('D', 2), 6, 6)) &&
				placement.get(1).equals(new TilePlacement(new Tile('I', 1), 6, 8)) &&
				placement.get(2).equals(new TilePlacement(new Tile('N', 1), 6, 9)) &&
				placement.get(3).equals(new TilePlacement(new Tile('N', 1), 6, 10))
				);
	}

	@Test
	public void testAcrossIntersectSuffixDown() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('J', 8), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('M', 3), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('I', 1), 6, 8));
		placement.add(new TilePlacement(new Tile('N', 1), 6, 9));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile('D', 2));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		assertTrue(
				placement.size() == 1 &&
				placement.get(0).equals(new TilePlacement(new Tile('D', 2), 6, 6))
				);
	}

	@Test
	public void testAcrossIntersectPrefixDown() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('J', 8), 6, 7));
		placement.add(new TilePlacement(new Tile('A', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('M', 3), 8, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		placement.clear();
		placement.add(new TilePlacement(new Tile('D', 2), 6, 6));
		placement.add(new TilePlacement(new Tile('I', 1), 6, 8));
		placement.add(new TilePlacement(new Tile('N', 1), 6, 9));
		b.placeTiles(placement, PlayDirection.ACROSS);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile('N', 1));
		hand.add(new Tile('I', 1));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		assertTrue(
				placement.size() == 2 &&
				placement.get(0).equals(new TilePlacement(new Tile('N', 1), 6, 10)) &&
				placement.get(1).equals(new TilePlacement(new Tile('I', 1), 6, 11))
				);
	}

	@Test
	public void testAcrossBlank() throws InvalidMoveException, ScrabbleException {
		ArrayList<TilePlacement> placement = new ArrayList<TilePlacement>();
		placement.add(new TilePlacement(new Tile('Z', 10), 3, 7));
		placement.add(new TilePlacement(new Tile('O', 1), 4, 7));
		placement.add(new TilePlacement(new Tile('O', 1), 5, 7));
		placement.add(new TilePlacement(new Tile('M', 3), 6, 7));
		placement.add(new TilePlacement(new Tile('I', 1), 7, 7));
		placement.add(new TilePlacement(new Tile('N', 1), 8, 7));
		placement.add(new TilePlacement(new Tile('G', 2), 9, 7));
		b.placeTiles(placement, PlayDirection.DOWN);
		
		ArrayList<Tile> hand = new ArrayList<Tile>();
		hand.add(new Tile(Tile.blank, 0));
		hand.add(new Tile('O', 1));
		
		placement.clear();
		placement = Solver.getMove(b, hand);
		
		System.out.println(placement.size());
		for (TilePlacement tp : placement) {
			System.out.println(tp.tile.letter + " (" + tp.tile.points + "): " + tp.row + ", " + tp.column);
		}
		
		Tile blankTile = new Tile(' ', 0);
		blankTile.setLetter('C');
		
		assertTrue(
				placement.size() == 2 &&
				placement.get(0).equals(new TilePlacement(blankTile, 3, 5)) &&
				placement.get(1).equals(new TilePlacement(new Tile('O', 1), 3, 6))
				);
	}

}
