package solver;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;

import solver.Node;
import solver.Trie;

public class TrieTest {
	
	public static final String dictionaryFile = "src/main/resources/dictionary.txt";
	
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
		assertTrue(words.get(0).equals("hello") &&
				words.get(1).equals("howdy") &&
				words.get(2).equals("world"));
	}
	
	@Test
	public void getValidLettersNoSuffixPositive() throws IOException {
		Trie t = new Trie();
		t.load(dictionaryFile);
		
		ArrayList<Character> prefix = new ArrayList<Character>();
		ArrayList<Character> suffix = new ArrayList<Character>();
		prefix.add('A');
		prefix.add('A');
		
		ArrayList<Character> validLettersExpected = new ArrayList<Character>();
		validLettersExpected.add('H');
		validLettersExpected.add('L');
		validLettersExpected.add('S');

		ArrayList<Character> validLetters = t.getValidLettersFromPrefixandSuffix(prefix, suffix);
		
		assertArrayEquals(validLettersExpected.toArray(), validLetters.toArray());
	}
	
	@Test
	public void getValidLettersNoPrefixPositive() throws IOException {
		Trie t = new Trie();
		t.load(dictionaryFile);
		
		ArrayList<Character> prefix = new ArrayList<Character>();
		ArrayList<Character> suffix = new ArrayList<Character>();
		suffix.add('A');
		suffix.add('N');
		suffix.add('T');
		
		ArrayList<Character> validLettersExpected = new ArrayList<Character>();
		validLettersExpected.add('B');
		validLettersExpected.add('C');
		validLettersExpected.add('D');
		validLettersExpected.add('G');
		validLettersExpected.add('H');
		validLettersExpected.add('K');
		validLettersExpected.add('L');
		validLettersExpected.add('P');
		validLettersExpected.add('R');
		validLettersExpected.add('S');
		validLettersExpected.add('V');
		validLettersExpected.add('W');

		ArrayList<Character> validLetters = t.getValidLettersFromPrefixandSuffix(prefix, suffix);
		
		assertArrayEquals(validLettersExpected.toArray(), validLetters.toArray());
	}
	
	@Test
	public void getValidLettersPositive() throws IOException {
		Trie t = new Trie();
		t.load(dictionaryFile);
		
		ArrayList<Character> prefix = new ArrayList<Character>();
		ArrayList<Character> suffix = new ArrayList<Character>();
		prefix.add('B');
		prefix.add('E');
		suffix.add('A');
		suffix.add('N');
		suffix.add('T');
		
		ArrayList<Character> validLettersExpected = new ArrayList<Character>();
		validLettersExpected.add('J');
		validLettersExpected.add('Z');

		ArrayList<Character> validLetters = t.getValidLettersFromPrefixandSuffix(prefix, suffix);
		
		assertArrayEquals(validLettersExpected.toArray(), validLetters.toArray());
	}
	
	@Test
	public void getValidLettersNoResultsPositive() throws IOException {
		Trie t = new Trie();
		t.load(dictionaryFile);
		
		ArrayList<Character> prefix = new ArrayList<Character>();
		ArrayList<Character> suffix = new ArrayList<Character>();
		prefix.add('Z');
		prefix.add('Z');
		prefix.add('Z');
		prefix.add('Z');
		
		ArrayList<Character> validLettersExpected = new ArrayList<Character>();

		ArrayList<Character> validLetters = t.getValidLettersFromPrefixandSuffix(prefix, suffix);
		
		assertArrayEquals(validLettersExpected.toArray(), validLetters.toArray());
	}

}
