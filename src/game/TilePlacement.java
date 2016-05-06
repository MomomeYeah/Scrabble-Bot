package game;

import java.util.ArrayList;

public class TilePlacement {
	
	public Tile tile;
	public int row;
	public int column;
	
	public TilePlacement(Tile tile, int row, int column) {
		this.tile = tile;
		this.row = row;
		this.column = column;
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

}
