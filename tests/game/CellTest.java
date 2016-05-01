package game;

import static org.junit.Assert.*;

import org.junit.Test;

public class CellTest {

	@Test
	public void testCreateCellType() {
		Cell c = new Cell("DW");
		assertEquals(CellType.ECellType.DOUBLE_WORD, c.getCellType().celltype);
	}
	
	@Test
	public void testCreateHasTile() {
		Cell c = new Cell("DW");
		assertTrue(c.isEmpty());
	}
	
	@Test
	public void testCreateNullTile() {
		Cell c = new Cell("DW");
		assertNull(c.getTile());
	}
	
	@Test
	public void testCreateIsAnchor() {
		Cell c = new Cell("DW");
		assertFalse(c.isAnchor());
	}
	
	@Test
	public void testPlaceTileHasTile() {
		Cell c = new Cell("DW");
		c.placeTile(new Tile('a', 1));
		assertFalse(c.isEmpty());
	}
	
	@Test
	public void testPlaceTileNullTile() {
		Cell c = new Cell("DW");
		c.placeTile(new Tile('a', 1));
		assertNotNull(c.getTile());
	}
	
	@Test
	public void testNewTilePoints() {
		Cell c = new Cell("DW");
		Tile t = new Tile('q', 10);
		assertEquals(t.points * c.getCellType().getTileMultiplier(), c.placeTile(t));
	}
	
	@Test
	public void testExistingTilePoints() {
		Cell c = new Cell("DW");
		Tile t = new Tile('q', 10);
		c.placeTile(t);
		assertEquals(t.points, c.placeTile(t));
	}
	
	@Test
	public void testSetIsAnchor() {
		Cell c = new Cell("DW");
		c.setIsAnchor(true);
		assertTrue(c.isAnchor());
	}

}
