package server;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.apache.tomcat.InstanceManager;
import org.apache.tomcat.SimpleInstanceManager;
import org.eclipse.jetty.annotations.ServletContainerInitializersStarter;
import org.eclipse.jetty.apache.jsp.JettyJasperInitializer;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.plus.annotation.ContainerInitializer;

import config.ConfigFactory;
import config.IConfig;

//https://github.com/jetty-project/embedded-jetty-jsp
public class BotServer extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private IConfig config;
	Server server;
	
	public BotServer() throws Exception {
		this.config = ConfigFactory.getConfig();
		
		this.server = new Server(Integer.valueOf(this.config.getInteger("PORT")));
		
		System.setProperty("org.apache.jasper.compiler.disablejsr199", "false");
		
		WebAppContext webAppContext = new WebAppContext();
		webAppContext.setContextPath("/");
		
		webAppContext.setAttribute("javax.servlet.context.tempdir", this.getScratchDir());
		webAppContext.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern",
          ".*/[^/]*servlet-api-[^/]*\\.jar$|.*/javax.servlet.jsp.jstl-.*\\.jar$|.*/.*taglibs.*\\.jar$");
		webAppContext.setResourceBase("src/main/webapp");
		webAppContext.setAttribute("org.eclipse.jetty.containerInitializers", jspInitializers());
		webAppContext.setAttribute(InstanceManager.class.getName(), new SimpleInstanceManager());
		webAppContext.addBean(new ServletContainerInitializersStarter(webAppContext), true);
		webAppContext.setClassLoader(getUrlClassLoader());

		webAppContext.addServlet(jspServletHolder(), "*.jsp");
		webAppContext.addServlet(new ServletHolder(this), "/bot");
		
        // add static resource handler and servlet context to handler list
        HandlerList hl = new HandlerList();
        hl.addHandler(webAppContext);
		
		// start server using resource handler and servlet
		this.server.setHandler(hl);
        this.server.start();
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
	
	/**
     * Establish Scratch directory for the servlet context (used by JSP compilation)
     */
    private File getScratchDir() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir"));
        File scratchDir = new File(tempDir.toString(), "embedded-jetty-jsp");

        if (!scratchDir.exists())
        {
            if (!scratchDir.mkdirs())
            {
                throw new IOException("Unable to create scratch directory: " + scratchDir);
            }
        }
        return scratchDir;
    }
    
    /**
     * Ensure the jsp engine is initialized correctly
     */
    private List<ContainerInitializer> jspInitializers()
    {
        JettyJasperInitializer sci = new JettyJasperInitializer();
        ContainerInitializer initializer = new ContainerInitializer(sci, null);
        List<ContainerInitializer> initializers = new ArrayList<ContainerInitializer>();
        initializers.add(initializer);
        return initializers;
    }
    
    private ClassLoader getUrlClassLoader()
    {
        ClassLoader jspClassLoader = new URLClassLoader(new URL[0], BotServer.class.getClassLoader());
        return jspClassLoader;
    }
    
    /**
     * Create JSP Servlet (must be named "jsp")
     */
    private ServletHolder jspServletHolder()
    {
        ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
        holderJsp.setInitOrder(0);
        holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
        holderJsp.setInitParameter("fork", "false");
        holderJsp.setInitParameter("xpoweredBy", "false");
        holderJsp.setInitParameter("compilerTargetVM", "1.7");
        holderJsp.setInitParameter("compilerSourceVM", "1.7");
        holderJsp.setInitParameter("keepgenerated", "true");
        return holderJsp;
    }
	
	public static void main(String args[]) throws Exception {
		BotServer bs = new BotServer();
		
		System.out.println("Server started on port " + bs.config.getInteger("PORT"));
		System.out.println("Registering bot on " + bs.config.getString("merkneraURL") + "...");
		/*try {
			System.out.println(bs.register());
		}
		catch (IOException e) {
			System.out.println("Unable to register bot!");
			System.out.println(e.getMessage());
			server.stop();
		}*/
		
        bs.server.join();
		
	}
	
}
