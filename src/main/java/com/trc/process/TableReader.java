/**
 * 
 */
package com.trc.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import com.trc.properties.DatabaseProperties;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class TableReader {
	private static final Logger logger = LoggerFactory.getLogger(TableReader.class);
	/**
	 * 
	 * @param isSingleTable
	 *            This can be run to generate code against a Single table rather
	 *            than the entire schema
	 * @param tableName
	 *            If isSingleTable is true, pass the oracle table name oth
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getTableNames(boolean isSingleTable, String tableName) throws Exception {
		Set<String> allTableNames = new LinkedHashSet<String>();
		StringBuilder tableSql = new StringBuilder("Select table_name from user_tables ");
		Connection con = null;
		ResultSet rsTable = null;
		if (isSingleTable) {
			if (tableName == null || tableName.length() == 0) {
				logger.error("!!! If Single table is true, Table Name must be a valid oracle table name !!! ");
				throw new Exception("!!! If Single table is true, Table Name must be a valid oracle table name !!! ");
			}
			tableSql.append(" Where table_name = '").append(tableName).append("'");
		} else {
			tableSql.append("order by table_name");
		}
		int counter = 0;
		
		try {
			con = DatabaseProperties.getDatabaseConnection();
		} catch (Exception e) {

			e.printStackTrace(System.out);
			throw e;
		}
		try {

			PreparedStatement psTable = con.prepareStatement(tableSql.toString());
			rsTable = psTable.executeQuery();
			while (rsTable != null && rsTable.next()) {
				allTableNames.add(rsTable.getString("table_name"));
				counter++;
			}

		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
			throw ex;
		}
		finally {
			if (rsTable != null) {
				rsTable.close();				
			}
			if (con != null) {
				con.close();
			}
		}
		logger.info(String.format("%s %d", "Total Number of Tables : ", counter));
		
		return allTableNames;

	}
	
	public static Table getTableMetaData(Connection con, String tableName) throws SQLException {
		Table table = new Table();
		table.setName(tableName);
		try {
			table.setPrimaryColumns(ColumnReader.getAllPrimaryKeys(con, tableName));
			table.setColumns(ColumnReader.getColumns(con, tableName));
		} catch (SQLException e) {			
			e.printStackTrace();
			throw e;
		}
		
		return table;
	}
}
