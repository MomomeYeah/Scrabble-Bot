package solver;

import java.util.ArrayList;
import java.util.HashMap;

public class Node {
	
	public static final char root = '_';
	public static final char EOW = '$';
	
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
		
	}
	
}
