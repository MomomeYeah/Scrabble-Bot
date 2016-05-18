package server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Map;

import org.apache.commons.io.IOUtils;

import com.thetransactioncompany.jsonrpc2.JSONRPC2Notification;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;

public class Utils {

	public static String JSONRPC2RequestString(String method, Map<String,Object> params, int id) {
		JSONRPC2Request reqOut = new JSONRPC2Request(method, params, id);
		return reqOut.toString();
	}
	
	public static String JSONRPC2NotificationString(String method, Map<String,Object> params) {
		JSONRPC2Notification notOut = new JSONRPC2Notification(method, params);
		return notOut.toString();
	}
	
	public static String getJSONRPCResponse(String urlEndpoint, String requestString) throws IOException {
		URL url = new URL(urlEndpoint);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setReadTimeout(10000);
		connection.setRequestProperty("Content-Type", "application/json");
		connection.connect();
		
		OutputStream out = null;
		InputStream response = null;
		try {
		    out = connection.getOutputStream();
		    
		    out.write(requestString.getBytes());
		    out.flush();
		    out.close();

		    int statusCode = connection.getResponseCode();
		    System.out.println(statusCode);
		    
		    response = connection.getInputStream();
		    StringWriter writer = new StringWriter();
			IOUtils.copy(response, writer);
			String requestBody = writer.toString();
			
			response.close();
			
			return requestBody;
		} catch (SocketTimeoutException e) {
			return "No response received after 3 seconds";
		} finally {
		    if (out != null) {
		        out.close();
		    }
		    if (response != null) {
		    	response.close();
		    }
		}
	}
	
}
