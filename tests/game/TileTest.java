package game;

import static org.junit.Assert.*;

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

}
