package game;

import java.util.ArrayList;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

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
	
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		obj.put("score", this.score);
		
		JSONArray arr = new JSONArray();
		for (TilePlacement tp : this.placements) {
			arr.add(tp.toJSON());
		}
		
		obj.put("placements", arr);
		
		return obj;
	}
	
}
