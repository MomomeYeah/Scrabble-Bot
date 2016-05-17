package game;

public class CellType {
	
	public enum ECellType {
		BASE,
		DOUBLE_LETTER,
		TRIPLE_LETTER,
		DOUBLE_WORD,
		TRIPLE_WORD
	}
	
	public ECellType celltype;
	
	public CellType(ECellType celltype) {
		this.celltype = celltype;
	}
	
	public CellType(String celltype) {
		switch (celltype) {
			case "DL": 	this.celltype = ECellType.DOUBLE_LETTER;
						break;
			case "DW":	this.celltype = ECellType.DOUBLE_WORD;
						break;
			case "TL":	this.celltype = ECellType.TRIPLE_LETTER;
						break;
			case "TW":	this.celltype = ECellType.TRIPLE_WORD;
						break;
			default:	this.celltype = ECellType.BASE;
						break;
		}
	}

	public String toString() {
		switch (this.celltype) {
			case BASE:			return "__";
			case DOUBLE_LETTER:	return "DL";
			case DOUBLE_WORD:	return "DW";
			case TRIPLE_LETTER:	return "TL";
			case TRIPLE_WORD:	return "TW";
			default:			return "??";
		}
	}
	
	public void print() {
		System.out.print(this.toString());
	}
	
	public int getTileMultiplier() {
		switch (this.celltype) {
			case DOUBLE_LETTER:	return 2;
			case TRIPLE_LETTER:	return 3;
			default:			return 1;
		}
	}
	
	public int getWordMultiplier() {
		switch (this.celltype) {
			case DOUBLE_WORD:	return 2;
			case TRIPLE_WORD:	return 3;
			default:			return 1;
		}
	}

}
