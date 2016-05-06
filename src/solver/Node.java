package solver;

import java.util.ArrayList;
import java.util.HashMap;

import game.Tile;

public class Node {
	
	public static final char root = '_';
	public static final char EOW = '$';
	public static final char blank = ' ';
	
	public char letter;
	public HashMap<Character,Node> childrenMap;
	public ArrayList<Node> childrenList;
	
	public Node(char letter) {
		this.letter = letter;
		this.childrenMap = new HashMap<Character, Node>();
		this.childrenList = new ArrayList<Node>();
	}
	
	public String toString() {
		String ret = this.letter + " ";
		for (Node n : this.childrenList) {
			ret += "(" + n.toString() + ")";
		}
		
		return ret;
	}
	
	public void print() {
		System.out.println(this.toString());
	}
	
	public boolean containsKey(char child) {
		return this.childrenMap.containsKey(child);
	}
	
	public boolean containsSuffix(ArrayList<Character> suffix) {
		Node n = this;
		for (int i = 0; i < suffix.size(); i++) {
			n = n.getChild(suffix.get(i));
			if (n == null) {
				return false;
			}
		}
		
		return n.containsKey(Node.EOW);
	}
	
	public Node getChild(char child) {
		return this.childrenMap.get(child);
	}
	
	public Node addChild(char child) {
		if (!this.containsKey(child)) {
			Node n = new Node(child);
			this.childrenMap.put(child, n);
			this.childrenList.add(n);
			return n;
		}
		
		return this.childrenMap.get(child);
	}
	
	public ArrayList<Node> getChildren() {
		return this.childrenList;
	}
	
	public ArrayList<Node> getChildren(ArrayList<Tile> childrenFilter) {
		if (Tile.contains(childrenFilter, Node.blank)) {
			return this.childrenList;
		}
		
		ArrayList<Node> children = new ArrayList<Node>();
		for (Node n : this.childrenList) {
			if (Tile.contains(childrenFilter, n.letter) || n.letter == Node.EOW) {
				children.add(n);
			}
		}
		
		return children;
	}
	
	public static void main(String args[]) {
		
		Node node = new Node('a');
		node.print();
		
		node.addChild('b');
		node.addChild('c');
		node.addChild('d');
		
		node.print();
		
		node.addChild('c');
		node.addChild('d');
		node.addChild('e');
		node.addChild('f');
		
		node.print();
		
		ArrayList<Tile> childrenFilter = new ArrayList<Tile>();
		childrenFilter.add(new Tile('a', 1));
		childrenFilter.add(new Tile('c', 3));
		childrenFilter.add(new Tile('f', 4));
		ArrayList<Node> children = node.getChildren(childrenFilter);
		
		System.out.println("Selected children are...");
		for (Node n : children) {
			System.out.println(n.letter);
		}
		
	}
	
}
