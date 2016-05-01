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

}
