package solver;

import static org.junit.Assert.*;

import org.junit.Test;

import solver.Node;

public class NodeTest {

	@Test
	public void testCreateLetter() {
		Node n = new Node('a');
		assertEquals('a', n.letter);
	}
	
	@Test
	public void testCreateChildren() {
		Node n = new Node('a');
		assertEquals(0, n.childrenList.size());
	}
	
	@Test
	public void testAddChildListSize() {
		Node n = new Node('a');
		n.addChild('b');
		assertEquals(1, n.childrenList.size());
	}
	
	@Test
	public void testAddChildMapSize() {
		Node n = new Node('a');
		n.addChild('b');
		assertEquals(1, n.childrenMap.size());
	}
	
	@Test
	public void testAddChildContainsKeyPositive() {
		Node n = new Node('a');
		n.addChild('b');
		assertEquals(true, n.containsKey('b'));
	}
	
	@Test
	public void testAddChildContainsKeyNegative() {
		Node n = new Node('a');
		n.addChild('b');
		assertEquals(false, n.containsKey('c'));
	}

}
