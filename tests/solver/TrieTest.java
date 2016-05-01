package solver;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import solver.Node;
import solver.Trie;

public class TrieTest {

	@Test
	public void testCreate() {
		Trie t = new Trie();
		assertEquals(Node.root, t.root.letter);
	}
	
	@Test
	public void testCreateEmpty() {
		Trie t = new Trie();
		assertEquals(Node.root, t.root.letter);
		
		ArrayList<String> words = t.getWords();
		assertEquals(0, words.size());
	}
	
	@Test
	public void addWordSize() {
		Trie t = new Trie();
		t.add("hello");
		
		ArrayList<String> words = t.getWords();
		assertEquals(1, words.size());
	}
	
	@Test
	public void addWordContents() {
		Trie t = new Trie();
		t.add("hello");
		
		ArrayList<String> words = t.getWords();
		assertEquals("hello", words.get(0));
	}
	
	@Test
	public void addManyWordsSize() {
		Trie t = new Trie();
		t.add("hello");
		t.add("howdy");
		t.add("world");
		
		ArrayList<String> words = t.getWords();
		assertEquals(3, words.size());
	}
	
	@Test
	public void addManyWordsContents() {
		Trie t = new Trie();
		t.add("hello");
		t.add("howdy");
		t.add("world");
		
		ArrayList<String> words = t.getWords();
		assertEquals("hello", words.get(0));
		assertEquals("howdy", words.get(1));
		assertEquals("world", words.get(2));
	}

}
