package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Error;
import com.thetransactioncompany.jsonrpc2.JSONRPC2ParseException;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;

import db.TopScores;
import game.Board;
import game.InvalidMoveException;
import game.Move;
import game.ScrabbleException;
import game.Tile;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import solver.Solver;

public class BotHandler {
	
	public static Object handleStatusPing(JSONRPC2Request reqIn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ping", "OK");
		
		return params;
	}
	
	@SuppressWarnings("unchecked")
	public static Object handleScrabbleNextMove(JSONRPC2Request reqIn) throws IOException, ScrabbleException, InvalidMoveException {
		Map<String,Object> namedParams = reqIn.getNamedParams();
		
		// get the current state of the Board
		JSONArray gamestateJSON = (JSONArray) namedParams.get("gamestate");
		ArrayList<String> gameState = new ArrayList<String>();
		for (int i = 0; i < gamestateJSON.size(); i++) {
			String mark = (String) gamestateJSON.get(i);
			String finalMark = mark.equals("X") || mark.equals("O") ? mark : null;
			gameState.add(finalMark);
		}
		// construct Board object
		Board b = new Board();
		
		// get the current state of our Hand
		JSONArray handJSON = (JSONArray) namedParams.get("hand");
		ArrayList<Tile> hand = new ArrayList<Tile>();
		for (int i = 0; i < handJSON.size(); i++) {
			Map<String,Object> tile = (Map<String,Object>) handJSON.get(i);
			hand.add(new Tile((char) tile.get("letter"), (int) tile.get("points")));
		}
		
		// get the number of Tiles remaining 
		int tilesRemaining = Integer.parseInt((String) namedParams.get("tilesRemaining"));
		
		// determine best Move
		Move move = null;
		if (b.wordsPlayed) {
			move = Solver.getMove(b, hand);
		} else {
			move = Solver.getFirstMove(b, hand);
		}
		
		// determine what we should do based on best available Move
		Map<String,Object> params = new HashMap<String,Object>();
		if (move.score == -1) {
			if (tilesRemaining == 0) {
				// no move found and no tiles available to exchange
				// just PASS
			} else {
				// int tilesToExchange = Math.min(hand.size(), tilesRemaining);
				// no move found but there are tiles available to exchange
				// EXCHANGE tilesToExchange tiles
			}
		} else {
			// get JSONObject for combined Move + Board
			JSONObject obj = new JSONObject();
			obj.put("move", move.toJSON());
			obj.put("board", b.toJSON());
			
			// add this move to the DB
			TopScores ts = null;
			try {
				ts = new TopScores();
				ts.addKey(move.score, obj);
			}
			finally {
				ts.closeDB();
			}
			
			// return this move
			params.put("position", move);
		}
		
		return params;
	}
	
	public static Object handleScrabbleComplete(JSONRPC2Request reqIn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", "OK");
		
		return params;
	}
	
	public static Object handleScrabbleError(JSONRPC2Request reqIn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", "OK");
		
		return params;
	}
	
	public static Object getErrorJSON() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("status", "Error");
		
		return params;
	}
	
	public static String parseRequest(String requestString) {
		JSONRPC2Request reqIn = null;
		try {
			reqIn = JSONRPC2Request.parse(requestString);
		} catch (JSONRPC2ParseException e) {
			return new JSONRPC2Error(0, e.getMessage()).toString();
		}
		
		JSONRPC2Response respOut = null;
		
		String method = reqIn.getMethod();
		if (method.equals("Status.Ping")) {
			respOut = new JSONRPC2Response(handleStatusPing(reqIn), reqIn.getID());
		} else if (method.equals("Scrabble.NextMove")) {
			try {
				respOut = new JSONRPC2Response(handleScrabbleNextMove(reqIn), reqIn.getID());
			} catch (Exception e) {
				respOut = new JSONRPC2Response(getErrorJSON(), reqIn.getID());
			}
		} else if (method.equals("Scrabble.Complete")) {
			respOut = new JSONRPC2Response(handleScrabbleComplete(reqIn), reqIn.getID());
		} else if (method.equals("Scrabble.Error")) {
			respOut = new JSONRPC2Response(handleScrabbleError(reqIn), reqIn.getID());
		} else {
			return new JSONRPC2Error(0, "Not Recognised: " + reqIn.getMethod()).toString();
		}
		
		return respOut.toString();
	}
	
	public static String handle(HttpServletRequest req) throws IOException {
		
		// get request body
		InputStream requestBodyStream = req.getInputStream();
		StringWriter writer = new StringWriter();
		IOUtils.copy(requestBodyStream, writer);
		String requestBody = writer.toString();
		
		// get response
		String responseString = parseRequest(requestBody);

		return responseString;
		
	}
	
}
