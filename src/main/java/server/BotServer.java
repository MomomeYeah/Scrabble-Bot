package server;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;

import config.ConfigFactory;
import config.IConfig;
import net.minidev.json.parser.ParseException;

public class BotServer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IConfig config;
	
	public BotServer() throws IOException, ParseException {
		this.config = ConfigFactory.getConfig();
	}
	
	public String getRegistrationString() {
		String method = "RegistrationService.Register";
		int id = 1;
		
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("token", config.getString("token"));
		params.put("botname", config.getString("botname"));
		params.put("botversion", config.getString("botversion"));
		params.put("game", "SCRABBLE");
		params.put("rpcendpoint", config.getString("endpointURL"));
		params.put("programminglanguage", "Java");
		params.put("website", config.getString("website"));
		params.put("description", config.getString("description"));
		
		return Utils.JSONRPC2RequestString(method, params, id);
	}
	
	public String register() throws IOException {
		String registrationString = getRegistrationString();
		return Utils.getJSONRPCResponse(config.getString("merkneraURL"), registrationString);
	}
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.sendRedirect("/");
    }
	
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String response = BotHandler.handle(req);
		resp.getWriter().print(response);
    }
	
	public static void main(String args[]) throws Exception {
		BotServer bs = new BotServer();
		
		System.out.println("Starting server on port " + bs.config.getInteger("PORT") + "...");
		
		Server server = new Server(Integer.valueOf(bs.config.getInteger("PORT")));
		
		// set up resource handler for static pages
		ResourceHandler resourceHandler = new ResourceHandler();
		resourceHandler.setDirectoriesListed(false);
		resourceHandler.setWelcomeFiles(new String[]{ "index.html" });
		resourceHandler.setResourceBase("src/main/webapp");
		
		// set up servlet context for bot
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(bs), "/bot");
        
        // add static resource handler and servlet context to handler list
        HandlerList hl = new HandlerList();
		hl.addHandler(resourceHandler);
		hl.addHandler(context);
		
		// start server using resource handler and servlet
		server.setHandler(hl);
        server.start();
		
		System.out.println("Registering bot on " + bs.config.getString("merkneraURL") + "...");
		/*try {
			System.out.println(bs.register());
		}
		catch (IOException e) {
			System.out.println("Unable to register bot!");
			System.out.println(e.getMessage());
			server.stop();
		}*/
		
        server.join();
		
	}
	
}
