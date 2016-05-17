package game;

import static org.junit.Assert.*;

import org.junit.Test;

import game.CellType;

public class CellTypeTest {

	@Test
	public void testCreateStringDL() {
		CellType ct = new CellType("DL");
		assertEquals(CellType.ECellType.DOUBLE_LETTER, ct.celltype);
	}
	
	@Test
	public void testCreateStringDW() {
		CellType ct = new CellType("DW");
		assertEquals(CellType.ECellType.DOUBLE_WORD, ct.celltype);
	}
	
	@Test
	public void testCreateStringTL() {
		CellType ct = new CellType("TL");
		assertEquals(CellType.ECellType.TRIPLE_LETTER, ct.celltype);
	}
	
	@Test
	public void testCreateStringTW() {
		CellType ct = new CellType("TW");
		assertEquals(CellType.ECellType.TRIPLE_WORD, ct.celltype);
	}
	
	@Test
	public void testCreateStringBase() {
		CellType ct = new CellType("  ");
		assertEquals(CellType.ECellType.BASE, ct.celltype);
	}
	
	@Test
	public void testMultiplierBase() {
		CellType ct = new CellType(CellType.ECellType.BASE);
		assertEquals(1, ct.getTileMultiplier());
	}
	
	@Test
	public void testMultiplierDL() {
		CellType ct = new CellType(CellType.ECellType.DOUBLE_LETTER);
		assertEquals(2, ct.getTileMultiplier());
	}
	
	@Test
	public void testMultiplierDW() {
		CellType ct = new CellType(CellType.ECellType.DOUBLE_WORD);
		assertEquals(2, ct.getWordMultiplier());
	}
	
	@Test
	public void testMultiplierTL() {
		CellType ct = new CellType(CellType.ECellType.TRIPLE_LETTER);
		assertEquals(3, ct.getTileMultiplier());
	}
	
	@Test
	public void testMultiplierTW() {
		CellType ct = new CellType(CellType.ECellType.TRIPLE_WORD);
		assertEquals(3, ct.getWordMultiplier());
	}

}
