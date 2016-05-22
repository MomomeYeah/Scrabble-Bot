package db;

import static org.fusesource.leveldbjni.JniDBFactory.asString;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.iq80.leveldb.DBIterator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TopScoresTest {
	
	public static Map<String,String> tsBackup;
	public static TopScores ts;
	
	@BeforeClass
	public static void beforeClass() throws IOException {
		ts = new TopScores();
		tsBackup = new HashMap<String,String>();
		DBIterator iterator = ts.getIterator();
		try {
			for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
				Entry<byte[], byte[]> next = iterator.peekNext();
				tsBackup.put(asString(next.getKey()), asString(next.getValue()));
			}
		} finally {
			iterator.close();
		}
	}
	
	@Before
	public void clearScores() throws IOException {
		ts.deleteAll();
	}
	
	@AfterClass
	public static void restoreScores() throws IOException {
		ts.deleteAll();
		for (Entry<String,String> entry : tsBackup.entrySet()) {
			ts.addKey(Integer.parseInt(entry.getKey()), entry.getValue());
		}
		ts.closeDB();
	}

	@Test
	public void testAddScoreRecordCount() throws IOException {
		ts.addKey(10, "Number 10");
		ts.addKey(20, "Number 20");
		
		assertEquals(2, ts.getRecordCount());
	}
	
	@Test
	public void testRetrieveKey() throws IOException {
		ts.addKey(10, "Number 10");
		
		assertEquals("Number 10", ts.get(10));
	}
	
	@Test
	public void testAddDuplicateKeyOverridesValue() throws IOException {
		ts.addKey(10, "Key 1");
		ts.addKey(10, "Key 2");
		
		assertEquals("Key 2", ts.get(10));
	}
	
	@Test
	public void testGetLeastKey() throws IOException {
		ts.addKey(12, "Key 12");
		ts.addKey(9, "Key 9");
		ts.addKey(16, "Key 16");
		
		assertEquals(9, Integer.parseInt(ts.getLeastKey()));
	}
	
	@Test
	public void testReplaceLeastValue() throws IOException {
		ts.addKey(10, "Key 1");
		ts.addKey(20, "Key 2");
		ts.addKey(30, "Key 3");
		ts.addKey(40, "Key 4");
		
		assertNull(ts.get(10));
	}
	
	@Test
	public void testKeepLeastValue() throws IOException {
		ts.addKey(10, "Key 1");
		ts.addKey(20, "Key 2");
		ts.addKey(30, "Key 3");
		ts.addKey(5, "Key 4");
		
		assertNull(ts.get(5));
	}

}
