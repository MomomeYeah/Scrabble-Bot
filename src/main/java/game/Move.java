package game;

import java.util.ArrayList;

public class Move {

	public ArrayList<TilePlacement> placements;
	public PlayDirection direction;
	public int score;
	
	public Move(ArrayList<TilePlacement> placements, PlayDirection direction, int score) {
		this.placements = placements;
		this.direction = direction;
		this.score = score;
	}
	
	public void setMove(ArrayList<TilePlacement> placements, PlayDirection direction, int score) {
		this.placements = placements;
		this.direction = direction;
		this.score = score;
	}
	
}
