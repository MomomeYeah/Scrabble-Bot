package solver;

import static org.junit.Assert.*;

import java.util.ArrayList;

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
		assertTrue(n.containsKey('b'));
	}
	
	@Test
	public void testAddChildContainsKeyNegative() {
		Node n = new Node('a');
		n.addChild('b');
		assertFalse(n.containsKey('c'));
	}
	
	@Test
	public void testContainsSuffixPositive() {
		Node c = new Node('c');
		c.addChild('a');
		c.getChild('a').addChild('r');
		c.getChild('a').addChild('t');
		
		ArrayList<Character> suffix = new ArrayList<Character>();
		suffix.add('a');
		suffix.add('r');
		assertTrue(c.containsSuffix(suffix));
	}
	
	@Test
	public void testContainsSuffixNegative() {
		Node c = new Node('c');
		c.addChild('a');
		c.getChild('a').addChild('r');
		c.getChild('a').addChild('t');
		
		ArrayList<Character> suffix = new ArrayList<Character>();
		suffix.add('a');
		suffix.add('n');
		assertFalse(c.containsSuffix(suffix));
	}

}
