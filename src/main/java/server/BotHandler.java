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

import game.Board;
import game.InvalidMoveException;
import game.ScrabbleException;
import game.Tile;
import net.minidev.json.JSONArray;
import solver.Solver;

public class BotHandler {
	
	public static Object handleStatusPing(JSONRPC2Request reqIn) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("ping", "OK");
		
		return params;
	}
	
	public static Object handleScrabbleNextMove(JSONRPC2Request reqIn) throws IOException, ScrabbleException, InvalidMoveException {
		Map<String,Object> namedParams = reqIn.getNamedParams();
		
		JSONArray ja = (JSONArray) namedParams.get("gamestate");
		ArrayList<String> gameState = new ArrayList<String>();
		for (int i = 0; i < ja.size(); i++) {
			String mark = (String) ja.get(i);
			String finalMark = mark.equals("X") || mark.equals("O") ? mark : null;
			gameState.add(finalMark);
		}
		
		Board b = new Board();
		ArrayList<Tile> hand = new ArrayList<Tile>();
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("position", Solver.getMove(b, hand));
		
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
