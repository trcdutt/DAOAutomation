/**
 * 
 */
package com.trc.process;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Ramesh Thalathoty
 ** 
 */
public class ColumnReader {

	private static final Logger LOGGER = LoggerFactory.getLogger(ColumnReader.class.getClass());
	
	public static List<String> getAllPrimaryKeys(Connection con, String tableName) throws SQLException {
		StringBuilder primaryKeySql = new StringBuilder(
				"Select column_name,cols.table_name from all_constraints cons, all_cons_columns cols where constraint_type='P' AND cons.constraint_name = cols.constraint_name and cols.table_name='");
		primaryKeySql.append(tableName).append("'");
		PreparedStatement psPrimaryKey = con.prepareStatement(primaryKeySql.toString());
		ResultSet rsPrimaryKeys = psPrimaryKey.executeQuery();
		ArrayList<String> primKeys = new ArrayList<String>();
		// Find all the fields that are primary keys for that table.
		while (rsPrimaryKeys != null && rsPrimaryKeys.next()) {
			primKeys.add(rsPrimaryKeys.getString("COLUMN_NAME"));
		}
		rsPrimaryKeys.close();
		return primKeys;
	}

	public static List<Column> getColumns(Connection con, String tableName) throws SQLException {
		StringBuilder sql = new StringBuilder(
				"select t.table_name tablename , t.column_name columnname, t.data_type datatype from user_tab_columns t where t.table_name = '");
		sql.append(tableName).append("'");
		PreparedStatement ps = con.prepareStatement(sql.toString());

		ResultSet rs = ps.executeQuery();
		List<Column> columns = new ArrayList<Column>();
		while (rs != null && rs.next()) {
			Column column = new Column();
			column.setColName(rs.getString("columnname"));
			column.setDataType(rs.getString("datatype"));
			columns.add(column);
		}

		rs.close();
		return columns;
	}

}
