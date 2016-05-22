package game;

import java.util.ArrayList;

import net.minidev.json.JSONObject;

public class Cell {
	
	private int row;
	private int column;
	private CellType celltype;
	private boolean hasTile;
	private Tile tile;
	private boolean isAnchor;
	private ArrayList<Character> validCrossChecksAcross;
	private ArrayList<Character> validCrossChecksDown;
	
	public Cell(int row, int column, CellType celltype) {
		this.row = row;
		this.column = column;
		this.celltype = celltype;
		this.hasTile = false;
		this.tile = null;
		this.isAnchor = false;
		this.validCrossChecksAcross = new ArrayList<Character>();
		this.validCrossChecksDown = new ArrayList<Character>();
	}
	
	public Cell(int row, int column, String celltype) {
		this(row, column, new CellType(celltype));
	}
	
	public void reset() {
		this.hasTile = false;
		this.tile = null;
		this.isAnchor = false;
		this.validCrossChecksAcross.clear();
		this.validCrossChecksDown.clear();
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		if (this.hasTile) {
			obj.put("tile", this.tile.toJSON());
		} else {
			obj.put("cell", this.celltype);
		}
		
		return obj;
	}
	
	public int getRow() {
		return this.row;
	}
	
	public int getColumn() {
		return this.column;
	}
	
	public CellType getCellType() {
		return this.celltype;
	}
	
	public boolean isEmpty() {
		return !this.hasTile;
	}
	
	public Tile getTile() {
		return this.tile;
	}
	
	public int placeTile(Tile tile) {
		if (!this.hasTile) {
			this.hasTile = true;
			this.tile = tile;
			return tile.points * this.celltype.getTileMultiplier();
		}
		return tile.points;
	}
	
	public boolean isAnchor() {
		return this.isAnchor;
	}
	
	public void setIsAnchor(boolean isAnchor) {
		this.isAnchor = isAnchor;
	}
	
	public void clearCrossChecks() {
		this.validCrossChecksAcross.clear();
		this.validCrossChecksDown.clear();
	}
	
	public ArrayList<Character> getValidCrossChecksAcross() {
		return this.validCrossChecksAcross;
	}
	
	public ArrayList<Character> getValidCrossChecksDown() {
		return this.validCrossChecksDown;
	}
	
	// when playing DOWN - letters that will form valid ACROSS words
	public void setAcrossCrossCheck(ArrayList<Character> validCrossChecksAcross) {
		this.validCrossChecksAcross = validCrossChecksAcross;
	}
	
	// when playing ACROSS - letters that will form valid DOWN words
	public void setDownCrossCheck(ArrayList<Character> validCrossChecksDown) {
		this.validCrossChecksDown = validCrossChecksDown;
	}

}
