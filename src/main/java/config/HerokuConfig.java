package config;

public class HerokuConfig implements IConfig {

	public String getString(String key) {
		return System.getenv(key);
	}

	public Integer getInteger(String key) {
		return Integer.parseInt(System.getenv(key));
	}
	
	public static void main(String args[]) {
		
		HerokuConfig config = new HerokuConfig();
		System.out.println(config.getString("XPC_SERVICE_NAME"));
		System.out.println(config.getString("Apple_PubSub_Socket_Render"));
		System.out.println(config.getString("SSH_AUTH_SOCK"));
		System.out.println(config.getInteger("JAVA_STARTED_ON_FIRST_THREAD_262"));
		
	}

}
