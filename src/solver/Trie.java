package solver;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Trie {

	public Node root;
	
	public Trie() {
		this.root = new Node(Node.root);
	}
	
	public String toString() {
		String ret = this.root.toString();
		
		return ret;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public void add(String word) {
		word = word + Node.EOW;
		
		Node n = this.root;
		for (int i = 0; i < word.length(); i++) {
			n = n.addChild(word.charAt(i));
		}
	}
	
	public void load(String filename) throws IOException {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));
			String line = "";
			while ((line = br.readLine()) != null) {
				this.add(line);
			}
		}
		finally {
			br.close();
		}
	}
	
	public ArrayList<String> getWords() {
		ArrayList<String> words = new ArrayList<String>();
		
		for (Node n : this.root.getChildren()) {
			ArrayList<String> recWords = getWordsRecursive(n);
			for (String s : recWords) {
				words.add(n.letter + s);
			}
		}
		
		return words;
	}
	
	private ArrayList<String> getWordsRecursive(Node n) {
		ArrayList<String> words = new ArrayList<String>();
		
		for (Node rec : n.getChildren()) {
			if (rec.letter == Node.EOW) {
				words.add("");
			} else {
				ArrayList<String> recWords = getWordsRecursive(rec);
				for (String s : recWords) {
					words.add(rec.letter + s);
				}
			}
		}
		
		return words;
	}
	
	// TODO - what about blanks?
	public ArrayList<String> getWords(ArrayList<Character> childrenFilter) {
		ArrayList<String> words = new ArrayList<String>();
		
		for (Node n : this.root.getChildren(childrenFilter)) {
			Character c = n.letter;
			childrenFilter.remove(c);
			ArrayList<String> recWords = getWordsRecursive(n, childrenFilter);
			for (String s: recWords) {
				words.add(n.letter + s);
			}
			childrenFilter.add(c);
		}
		
		return words;
	}
	
	private ArrayList<String> getWordsRecursive(Node n, ArrayList<Character> childrenFilter) {
		ArrayList<String> words = new ArrayList<String>();
		
		for (Node rec : n.getChildren(childrenFilter)) {
			if (rec.letter == Node.EOW) {
				words.add("");
			} else {
				Character c = rec.letter;
				childrenFilter.remove(c);
				ArrayList<String> recWords = getWordsRecursive(rec, childrenFilter);
				for (String s : recWords) {
					words.add(rec.letter + s);
				}
				childrenFilter.add(c);
			}
		}
		
		return words;
	}
	
	public ArrayList<Character> getValidLettersFromPrefixandSuffix(ArrayList<Character> prefix, ArrayList<Character> suffix) {
		ArrayList<Character> validLetters = new ArrayList<Character>();
		
		Node n = this.root;
		while (prefix.size() > 0 && n != null) {
			n = n.getChild(prefix.remove(0));
		}
		
		// if we get through part or all of the prefix and there are no nodes left
		// there must be no valid letters we can return
		if (n == null) {
			return validLetters;
		}
		
		// if there's no suffix, just return all children of the letter letter of the prefix
		if (suffix.size() == 0) {
			for (Node child : n.getChildren()) {
				if (child.letter != Node.EOW) {
					validLetters.add(child.letter);
				}
			}
		// if there is a suffix, return all children that have the given suffix as a suffix
		} else {
			for (Node poss : n.getChildren()) {
				if (poss.containsSuffix(suffix) && poss.letter != Node.EOW) {
					validLetters.add(poss.letter);
				}
			}
		}
		
		return validLetters;
	}
	
	public static void main(String args[]) {
		
		Trie t = new Trie();
		long startTime = 0, endTime = 0;
		try {
			startTime = System.nanoTime();
			t.load("dictionary.txt");
			endTime = System.nanoTime();
		}
		catch (IOException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Dictionary loaded in " + (endTime - startTime)/1000000 + " milliseconds");
		//t.add("hello");
		//t.add("hell");
		//t.add("help");
		//t.print();
		
		startTime = System.nanoTime();
		ArrayList<String> words = t.getWords();
		endTime = System.nanoTime();
		System.out.println(words.size() + " words found in " + (endTime - startTime)/1000000 + " milliseconds");
		//for (String s : words) {
		//	System.out.println(s);
		//}
		
	}
	
}
