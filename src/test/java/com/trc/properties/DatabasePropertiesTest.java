/**
 * 
 */
package com.trc.properties;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * @author Ramesh Thalathoty
 * *
 */
public class DatabasePropertiesTest {

	/**
	 * Test method for
	 * {@link com.trc.properties.DatabaseProperties#getDatabaseConnection()}.
	 */
	@Test()
	public void testGetDatabaseConnection() {
		try {
			assertNotNull(DatabaseProperties.getDatabaseConnection());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Thrown Exception");
		}
	}
	@Test
	public void testGetMethod() {
		
	}

}
