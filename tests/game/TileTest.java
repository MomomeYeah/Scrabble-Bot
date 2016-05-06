package game;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TileTest {
	
	@Rule
	public ExpectedException exception = ExpectedException.none();

	@Test
	public void testSetLetterValid() throws ScrabbleException {
		Tile t = new Tile(' ', 0);
		t.setLetter('A');
		
		assertEquals(t.letter, 'A');
	}
	
	@Test
	public void testSetLetterInvalid() throws ScrabbleException {
		Tile t = new Tile('B', 0);
		
		exception.expect(ScrabbleException.class);
		exception.expectMessage("Letter can only be set for blank tiles");
		
		t.setLetter('A');
	}
	
	@Test
	public void testContainsTrue() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile('A', 1));
		tiles.add(new Tile('T', 1));
		
		assertTrue(Tile.contains(tiles, 'A'));
	}
	
	@Test
	public void testContainsFalse() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile('A', 1));
		tiles.add(new Tile('T', 1));
		
		assertFalse(Tile.contains(tiles, 'S'));
	}
	
	@Test
	public void testRemoveTrue() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		Tile t = new Tile('A', 1);
		tiles.add(t);
		tiles.add(new Tile('T', 1));
		
		assertEquals(t, Tile.remove(tiles, 'A'));
	}
	
	@Test
	public void testRemoveFalse() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile('A', 1));
		tiles.add(new Tile('T', 1));
		
		assertNull(Tile.remove(tiles, 'S'));
	}
	
	@Test
	public void testRemoveBlank() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile('A', 1));
		Tile t = new Tile(' ', 0);
		tiles.add(t);
		
		assertEquals(t, Tile.remove(tiles, ' '));
	}
	
	@Test
	public void testListToString() {
		ArrayList<Tile> tiles = new ArrayList<Tile>();
		tiles.add(new Tile('A', 1));
		tiles.add(new Tile('B', 1));
		tiles.add(new Tile('A', 1));
		tiles.add(new Tile('C', 1));
		tiles.add(new Tile('U', 1));
		tiles.add(new Tile('S', 1));
		
		assertEquals("ABACUS", Tile.listToString(tiles));
	}

}
