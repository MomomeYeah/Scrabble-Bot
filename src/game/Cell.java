package game;

public class Cell {
	
	private CellType celltype;
	private boolean hasTile;
	private Tile tile;
	private boolean isAnchor;
	
	public Cell(CellType celltype) {
		this.celltype = celltype;
		this.hasTile = false;
		this.tile = null;
		this.isAnchor = false;
	}
	
	public Cell(String celltype) {
		this(new CellType(celltype));
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

}
