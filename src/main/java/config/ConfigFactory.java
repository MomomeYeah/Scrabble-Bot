package config;

import java.io.FileNotFoundException;

import net.minidev.json.parser.ParseException;

public class ConfigFactory {
	
	public static IConfig getConfig() throws FileNotFoundException, ParseException {
		String deploy_environment = System.getenv("DEPLOY_ENVIRONMENT");
		
		if (deploy_environment != null && deploy_environment.equals("heroku")) {
			return new HerokuConfig();
		}
		
		return new LocalConfig();
	}

}
