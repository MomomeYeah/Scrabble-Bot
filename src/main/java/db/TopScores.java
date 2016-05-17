package db;

import org.iq80.leveldb.*;
import static org.fusesource.leveldbjni.JniDBFactory.*;
import java.io.*;

public class TopScores {
	
	private static final int maxDBSize = 3;
	private static DB db;
	
	public TopScores() throws IOException {
		if (db == null) {
			createDB();
		}
	}
	
	private static void createDB() throws IOException {
		Options options = new Options();
		options.createIfMissing(true);
		db = factory.open(new File("src/main/database/db"), options);
	}
	
	private void put(Integer key, String value) {
		db.put(bytes(key.toString()), bytes(value));
	}
	
	public String get(int key) {
		return asString(db.get(bytes(Integer.toString(key))));
	}
	
	public String get(String key) {
		return asString(db.get(bytes(key)));
	}
	
	private void delete(String key) {
		db.delete(bytes(key));
	}
	
	public void deleteAll() throws IOException {
		DBIterator iterator = db.iterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				this.delete(asString(iterator.peekNext().getKey()));
			}
		} finally {
			iterator.close();
		}
	}
	
	public int getRecordCount() throws IOException {
		int count = 0;
		DBIterator iterator = db.iterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				count++;
			}
		} finally {
			iterator.close();
		}
		
		return count;
	}
	
	public String getLeastKey() throws IOException {
		DBIterator iterator = db.iterator();
		
		String leastKey = null;
		int leastKeyAsInt = Integer.MAX_VALUE;
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				String key = asString(iterator.peekNext().getKey());
				int keyAsInt = Integer.parseInt(key);
				
				if (keyAsInt < leastKeyAsInt) {
					leastKey = key;
					leastKeyAsInt = keyAsInt;
				}
			}
		} finally {
			iterator.close();
		}
		
		return leastKey;
	}
	
	public void addKey(int key, String value) throws IOException {
		if (this.getRecordCount() < maxDBSize) {
			this.put(key, value);
		} else {
			String leastKey = this.getLeastKey();
			int leastKeyAsInt = Integer.parseInt(leastKey);
			
			if (key > leastKeyAsInt) {
				this.delete(leastKey);
				this.put(key, value);
			}
		}
	}
	
	public static void main(String args[]) throws IOException {
		TopScores ts = new TopScores();
		ts.deleteAll();
		
		try {
			ts.addKey(10, "NoPenis");
			ts.addKey(20, "HugePenis");
			ts.addKey(30, "TinyPenis");
			
			System.out.println(ts.get("10"));
			System.out.println(ts.get("20"));
			System.out.println(ts.get("30"));
			
			System.out.println("DB has " + ts.getRecordCount() + " values");
			
			ts.addKey(5, "Number 5");
			
			System.out.println(ts.get("5"));
			System.out.println(ts.get("10"));
			System.out.println(ts.get("20"));
			System.out.println(ts.get("30"));
			
			System.out.println("DB has " + ts.getRecordCount() + " values");
			
			ts.addKey(15, "Funfzehn");
			
			System.out.println(ts.get("5"));
			System.out.println(ts.get("10"));
			System.out.println(ts.get("15"));
			System.out.println(ts.get("20"));
			System.out.println(ts.get("30"));
			
			System.out.println("DB has " + ts.getRecordCount() + " values");
		} finally {
			db.close();
		}
	}

}
