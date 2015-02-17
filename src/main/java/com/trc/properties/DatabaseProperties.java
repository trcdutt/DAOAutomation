/**
 * 
 */
package com.trc.properties;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Ramesh Thalathoty *
 */
public class DatabaseProperties {

	private static final String DB_NAME = "databasename";
	private static final String DB_SERVER = "databaseserver";
	private static final String USER = "user";
	private static final String PASSWORD = "password";
	private static final String DB_PORT = "port";
	private static final Properties props;

	static {
		props = DatabaseProperties.getConfigurationProperties();
	}

	/**
	 * Return a jdbc connection to a predefined database
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection getDatabaseConnection() throws Exception {
		Connection connection = null;
		StringBuilder connectionString = new StringBuilder("jdbc:oracle:thin:@");
		connectionString.append(props.getProperty(DB_SERVER)).append(":").append(props.getProperty(DB_PORT))
				.append(":").append(props.getProperty(DB_NAME));

		String user = props.getProperty(USER);
		String password = props.getProperty(PASSWORD);

		connection = DriverManager.getConnection(connectionString.toString(), user, password);
		return connection;
	}

	/**
	 * Get the configuration properties from the config location
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Properties getConfigurationProperties() {
		Properties props = new Properties();
		InputStream stream = ClassLoader.getSystemResourceAsStream("config.properties");

		try {
			props.load(stream);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Configuration properties file missing!!!!.");
		}

		if (props.getProperty(DB_SERVER) == null || props.getProperty(DB_NAME) == null
				|| props.getProperty(USER) == null || props.getProperty(PASSWORD) == null) {
			System.out.println("Configuration properties are wrong!!!!.");
		}

		return props;
	}

}
