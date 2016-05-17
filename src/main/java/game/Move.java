package game;

import java.util.ArrayList;

public class Move {

	public ArrayList<TilePlacement> placements;
	public PlayDirection direction;
	
	public Move(ArrayList<TilePlacement> placements, PlayDirection direction) {
		this.placements = placements;
		this.direction = direction;
	}
	
	public void setMove(ArrayList<TilePlacement> placements, PlayDirection direction) {
		this.placements = placements;
		this.direction = direction;
	}
	
}
