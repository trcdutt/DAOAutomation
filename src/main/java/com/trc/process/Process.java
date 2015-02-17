/**
 * 
 */
package com.trc.process;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import com.trc.properties.DatabaseProperties;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class Process {


	public static void main(String[] args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter a Table name or press enter to scan all tables:\n");
		Set<String> tables = null;

		String tableName = scanner.nextLine();

		try {
			if (tableName != null && tableName.trim().length() > 0) {
				tables = TableReader.getTableNames(true, tableName);
			} else {
				tables = TableReader.getTableNames(false, null);
			}
		}

		catch (Exception e) {

			e.printStackTrace();
			throw e;
		}

		StringBuilder sourceFile = new StringBuilder(1000);
		StringBuilder importTemplate = new StringBuilder(readImportTemplate());
		for (String tblName : tables) {
			Connection con = DatabaseProperties.getDatabaseConnection();
			Table table = TableReader.getTableMetaData(con, tblName);
			con.close();
			String className = table.getClassName();
			StringBuilder insTemplate = createInsertTemplate(className, tblName + "_INSERT");
			StringBuilder updTemplate = createUpdateTemplate(className, tblName + "_UPDATE");
			StringBuilder delTemplate = createDeleteTemplate(className, tblName + "_DELETE");
			sourceFile.append(importTemplate);
			StringBuilder variables = new StringBuilder();
			StringBuilder methods = new StringBuilder();
			List<String> primarycolumns = TableReader.getTableMetaData(con, tableName).getPrimaryColumns();
			for (Column column : table.getColumns()) {
				
				if (primarycolumns.indexOf(column.getColName()) >= 0) {
					// Primary key and should be skipped in insert and update
					
				}

				variables.append("private ").append(column.getJavaType()).append(" ").append(column.getVariableName())
						.append(";\n");

				methods.append("\npublic ").append(column.getJavaType()).append(" ").append(column.getGetMethodName()).append("(){ \n").append("return ")
						.append(column.getVariableName()).append(";\n }\n");

				methods.append("\npublic void ").append(column.getSetMethodName()).append("(")
						.append(column.getJavaType()).append(" ").append(column.getVariableName()).append(")")
						.append("{ \n").append("this.").append(column.getVariableName()).append("=")
						.append(column.getVariableName()).append(";\n }\n");
			}
			Bean bean = new Bean();
			bean.createBeanSource(className,variables,methods);
			sourceFile.append(insTemplate);
			sourceFile.append(updTemplate);
			sourceFile.append(delTemplate);
		}

	}

	private static StringBuilder createDeleteTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readDeleteTemplate());
		replaceClassName(updateTemplate, className, queryName);
		return updateTemplate;
	}

	private static StringBuilder createUpdateTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readUpdateTemplate());
		replaceClassName(updateTemplate, className, queryName);
		return updateTemplate;
	}

	private static StringBuilder createInsertTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readInsertTemplate());
		replaceClassName(updateTemplate, className, queryName);
		return updateTemplate;
	}

	public static String readFile(InputStream is) throws IOException {
		StringBuilder sbValue = new StringBuilder(100);
		Scanner scan = new Scanner(is);
		while (scan.hasNext()) {
			sbValue.append(scan.nextLine() + "\n");
		}

		return sbValue.toString();
	}
	public static String readInsertTemplate() throws FileNotFoundException, IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("insertTemplate.txt");
		return readFile(is);
	}

	public static String readUpdateTemplate() throws FileNotFoundException, IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("updateTemplate.txt");
		return readFile(is);
	}

	public static String readDeleteTemplate() throws FileNotFoundException, IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("deleteTemplate.txt");
		return readFile(is);
	}

	public static String readImportTemplate() throws FileNotFoundException, IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("importsTemplate.txt");
		return readFile(is);
	}

	public static void replaceClassName(StringBuilder data, String className, String queryName) {
		int start = data.indexOf("**classname**");
		int length = "**classname**".length();

		while (data.indexOf("**classname**", start) > 0) {
			start = data.indexOf("**classname**", start);
			data.delete(start, start + length);
			data.insert(start, className);
		}

		start = data.indexOf("**sql**");
		length = "**sql**".length();
		while (data.indexOf("**sql**", start) > 0) {
			start = data.indexOf("**sql**", start);
			data.delete(start, start + length);
			data.insert(start, "AllTablesQueries." + queryName);
		}

	}

}
