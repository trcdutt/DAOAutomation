/**
 * 
 */
package com.trc.properties;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

import com.trc.process.TableReader;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class TableReaderTest {

	/**
	 * Test method for
	 * {@link com.trc.process.TableReader#getTableNames(boolean, java.lang.String)}
	 * .
	 */
	@Test
	public void testGetTableNames() {
		
		Set<String> tables = null;
		try {
			tables = TableReader.getTableNames(true, "AIR_ACTIVITY");
		} catch (Exception e) {

			e.printStackTrace();
			fail("Failed to extract the table names");
		}
		assertTrue("Unable to read the tables", tables.size() > 0);
		
	}

}
