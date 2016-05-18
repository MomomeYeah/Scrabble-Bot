package config;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;

public class LocalConfig implements IConfig {
	
	HashMap<String,Object> configItems;
	
	public LocalConfig(String configFile) throws ParseException, FileNotFoundException {
		this.configItems = new HashMap<String,Object>();
		
		JSONParser parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
		
		JSONObject jo = (JSONObject) parser.parse(new FileReader(configFile));
		Set<Entry<String,Object>> configSet = jo.entrySet();
		for (Entry<String,Object> e : configSet) {
			this.configItems.put(e.getKey(), e.getValue());
		}
	}
	
	public LocalConfig() throws FileNotFoundException, ParseException {
		this("config.json");
	}
	
	public String getString(String key) {
		return (String) this.configItems.get(key);
	}
	
	public Integer getInteger(String key) {
		return (Integer) this.configItems.get(key);
	}
	
	public static void main(String args[]) throws ParseException, FileNotFoundException {
		LocalConfig config = new LocalConfig();
		for (Entry<String,Object> e : config.configItems.entrySet()) {
			if (e.getValue() instanceof String) {
				System.out.format("Key: %s, Value(S): %s\n", e.getKey(), e.getValue());
			} else if (e.getValue() instanceof Integer) {
				System.out.format("Key: %s, Value(I): %d\n", e.getKey(), e.getValue());
			}
		}
	}

}
