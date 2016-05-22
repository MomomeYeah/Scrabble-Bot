package game;

import java.util.ArrayList;

import net.minidev.json.JSONObject;

public class TilePlacement {
	
	public Tile tile;
	public int row;
	public int column;
	
	public TilePlacement(Tile tile, int row, int column) {
		this.tile = tile;
		this.row = row;
		this.column = column;
	}
	
	public boolean equals(TilePlacement other) {
		return this.tile.equals(other.tile) && this.row == other.row && this.column == other.column;
	}
	
	public static ArrayList<TilePlacement> getPlacements(ArrayList<Tile> tiles, int startingRow, int startingColumn, PlayDirection direction) {
		ArrayList<TilePlacement> placements = new ArrayList<TilePlacement>();

		int row = startingRow;
		int column = startingColumn;
		for (Tile t : tiles) {
			placements.add(new TilePlacement(t, row, column));
			
			if (direction == PlayDirection.ACROSS) {
				column++;
			} else {
				row++;
			}
		}
		
		return placements;
	}
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("tile", this.tile.toJSON());
		obj.put("row", this.row);
		obj.put("column", this.column);
			
		return obj;
	}

}
