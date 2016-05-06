package game;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

public class BagTest {

	@Test
	public void testDraw() throws IOException {
		Bag b = new Bag();
		ArrayList<Tile> tiles = b.draw();
		
		assertEquals(7, tiles.size());
	}
	
	@Test
	public void testDrawCount() throws IOException {
		Bag b = new Bag();
		ArrayList<Tile> tiles = b.draw(98);
		tiles = b.draw();
		
		assertEquals(2, tiles.size());
	}
	
	@Test
	public void testReplace() throws IOException {
		Bag b = new Bag();
		ArrayList<Tile> tiles = b.draw();
		b.replace(tiles);
		
		assertEquals(100, b.getTileCount());
	}
	
	@Test
	public void testExchangeBagCount() throws IOException {
		Bag b = new Bag();
		ArrayList<Tile> tiles = b.draw();
		b.exchange(tiles);
		
		assertEquals(93, b.getTileCount());
	}
	
	@Test
	public void testExchangeHandCount() throws IOException {
		Bag b = new Bag();
		ArrayList<Tile> tiles = b.draw();
		b.exchange(tiles);
		
		assertEquals(7, tiles.size());
	}
}
