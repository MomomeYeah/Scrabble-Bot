package server;

import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

import config.ConfigFactory;
import config.IConfig;
import net.minidev.json.parser.ParseException;

public class BotTester {
	
	public static String ping() {
		String method = "Status.Ping";
		int id = 1;
		
		Map<String,Object> params = new HashMap<String,Object>();
		
		return Utils.JSONRPC2RequestString(method, params, id);
	}
	
	public static String nextMove() {
		String method = "TicTacToe.NextMove";
		int id = 1;
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("gameid", 78);
		params.put("mark", "X");
		
		String board[] = new String[]{"O", "X", "O", null, "O", null, null, "X", null};
		ArrayList<String> gamestate = new ArrayList<String>();
		for (String s : board) {
			gamestate.add(s);
		}
		params.put("gamestate", gamestate);
		
		return Utils.JSONRPC2RequestString(method, params, id);
	}
	
	public static String complete() {
		String method = "TicTacToe.Complete";
		int id = 1;
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("gameid", 21);
		params.put("winner", true);
		params.put("mark", "X");
		
		String board[] = new String[]{"O", "X", "O", null, "O", "X", "O", "X", null};
		ArrayList<String> gamestate = new ArrayList<String>();
		for (String s : board) {
			gamestate.add(s);
		}
		params.put("gamestate", gamestate);
		
		return Utils.JSONRPC2RequestString(method, params, id);
	}
	
	public static String error() {
		String method = "TicTacToe.Error";
		int id = 1;
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("gameid", 21);
		params.put("message", "You played an invalid move, 'X' was already played here.");
		params.put("errorcode", 1548);
		
		return Utils.JSONRPC2RequestString(method, params, id);
	}
	
	public static void main(String args[]) throws IOException, ParseException {
		
		IConfig config = ConfigFactory.getConfig();
		String urlEndpoint = config.getString("endpointURL");
		
		String requestString = BotTester.ping();
		//String requestString = BotTester.nextMove();
		//String requestString = BotTester.complete();
		//String requestString = BotTester.error();

		System.out.println(requestString);
		
		long start = System.nanoTime();
		String responseString = Utils.getJSONRPCResponse(urlEndpoint, requestString);
		long finish = System.nanoTime();
		
		DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.HALF_UP);
		float seconds = (float) (finish - start) / (float) 1000000000;
		
		System.out.println("Got response in " + df.format(seconds) + " seconds");
		System.out.println(responseString);
		
	}

}
