/**
 * 
 */
package com.trc.process;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.slf4j.*;

import com.trc.properties.DatabaseProperties;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class MainProcess {
	private static final Logger Logger = LoggerFactory.getLogger(MainProcess.class);
	private static final String dir = (String) DatabaseProperties.getConfigurationProperties().get(Constants.DIR_LOCATION_PROPERTY);

	public static void main(String[] args) throws Exception {
		InsertTemplateProcessor insertProcessor = new InsertTemplateProcessor();
		insertProcessor.getTemplateText();
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
		for (String tblName : tables) {
			Connection con = DatabaseProperties.getDatabaseConnection();
			System.out.println("Table Name:" + tblName);
			Table table = TableReader.getTableMetaData(con, tblName);

			String className = table.getClassName();
			StringBuilder insTemplate = createInsertTemplate(className, tblName + "_INSERT");
			StringBuilder updTemplate = createUpdateTemplate(className, tblName + "_UPDATE");
			StringBuilder delTemplate = createDeleteTemplate(className, tblName + "_DELETE");
			StringBuilder variables = new StringBuilder();
			StringBuilder methods = new StringBuilder();
			List<String> primarycolumns = table.getPrimaryColumns();
			StringBuilder insertQuery = new StringBuilder(Constants.STRING_DECLARATION).append(tblName)
					.append("_INSERT = ").append("\" INSERT INTO ");
			StringBuilder insertQueryValues = new StringBuilder(" ) VALUES ( ");
			StringBuilder updateQuery = new StringBuilder(Constants.STRING_DECLARATION).append(tblName)
					.append("_UPDATE = ").append("\" UPDATE ");
			StringBuilder updateQueryValues = new StringBuilder(" set ( ");
			StringBuilder deleteQuery = new StringBuilder(Constants.STRING_DECLARATION).append(tblName)
					.append("_DELETE = \"").append(" DELETE FROM ");
			StringBuilder deleteQueryValues = new StringBuilder(" WHERE 1=1 ");
			StringBuilder selectQuery = new StringBuilder(Constants.STRING_DECLARATION).append(tblName).append(
					"_SELECT = \"");
			selectQuery.append(" SELECT  ");
			StringBuilder selectQueryValues = new StringBuilder(" WHERE 1=1 ");

			insertQuery.append(tblName).append("( ");
			updateQuery.append(tblName).append(" ");
			deleteQuery.append(tblName).append(" ");
			int i = 0;
			int size = table.getColumns().size();
			for (Column column : table.getColumns()) {
				i++;
				variables.append("private ").append(column.getJavaType()).append(" ").append(column.getVariableName())
						.append(";\n");

				methods.append("\npublic ").append(column.getJavaType()).append(" ").append(column.getGetMethodName())
						.append("(){ \n").append("return ").append(column.getVariableName()).append(";\n }\n");

				methods.append("\npublic void ").append(column.getSetMethodName()).append("(")
						.append(column.getJavaType()).append(" ").append(column.getVariableName()).append(")")
						.append("{ \n").append("this.").append(column.getVariableName()).append("=")
						.append(column.getVariableName()).append(";\n }\n");
				if (primarycolumns.indexOf(column.getColName()) >= 0) {
					// Primary key and should be skipped in insert and update
					if (column.getDataType().equals("String")) {
						
					}
					else {
						
					}
					continue;

				}
				insertQuery.append(column.getColName());
				insertQueryValues.append(":").append(column.getVariableName());
				selectQuery.append(column.getColName());
				updateQueryValues.append(column.getColName());
				updateQueryValues.append(" = :").append(column.getVariableName());
				if (i != size) {
					insertQuery.append(", ");
					selectQuery.append(", ");
					updateQueryValues.append(",");
					insertQueryValues.append(",");

				}

			}
			Bean bean = new Bean();
			bean.createBeanSource(className, variables, methods);

			insertQuery.append(insertQueryValues).append(")\";");;
			deleteQuery.append(deleteQueryValues).append("\";");;
			updateQuery.append(updateQueryValues).append(")\";");
			selectQuery.append(" FROM ").append(tblName).append(" ").append(selectQueryValues).append("\";");;

			createQueryFile(className, insertQuery, updateQuery, deleteQuery, selectQuery);
			sourceFile.append(insTemplate);
			sourceFile.append(updTemplate);
			sourceFile.append(delTemplate);
			createDaoImplFile(className, sourceFile);
			createDaoFile(className, sourceFile);
			con.close();

		}

	}

	private static void createQueryFile(String className, StringBuilder insertQuery, StringBuilder updateQuery,
			StringBuilder deleteQuery, StringBuilder selectQuery) throws IOException {

//		String dir = (String) DatabaseProperties.getConfigurationProperties().get("generatedfilelocation");
		File file = new File(dir + "/" + className + "Queries.java");
		file.delete();
		if (!file.exists()) {
			file.createNewFile();
		}
		StringBuilder classDeclaration = new StringBuilder();
		classDeclaration.append("public class ").append(className).append("Queries").append(" { \n");
		classDeclaration.append(insertQuery).append(updateQuery).append(deleteQuery).append(selectQuery);
		classDeclaration.append("}");
		OutputStream os = new FileOutputStream(file);
		os.write(classDeclaration.toString().getBytes());

		os.close();

	}

	private static void createDaoImplFile(String className, StringBuilder source) throws IOException {

//		String dir = (String) DatabaseProperties.getConfigurationProperties().get(Constants.DIR_LOCATION_PROPERTY);
		File file = new File(dir + "/" + className + "DaoImpl.java");
		file.delete();
		if (!file.exists()) {
			file.createNewFile();
		}
		StringBuilder classDeclaration = new StringBuilder();
		StringBuilder importTemplate = new StringBuilder(readImportTemplate());
		classDeclaration.append(importTemplate);
		classDeclaration.append("public class ").append(className).append("DaoImpl implements ").append(className).append("Dao { \n");
		classDeclaration.append(source);
		classDeclaration.append("}");
		OutputStream os = new FileOutputStream(file);
		os.write(classDeclaration.toString().getBytes());

		os.close();

	}

	private static void createDaoFile(String className, StringBuilder source) throws IOException {

//		String dir = (String) DatabaseProperties.getConfigurationProperties().get("generatedfilelocation");
		File file = new File(dir + "/" + className + "Dao.java");
		file.delete();
		if (!file.exists()) {
			file.createNewFile();
		}
		StringBuilder classDeclaration = new StringBuilder();
		StringBuilder template = new StringBuilder(readInterfaceTemplate());
		classDeclaration.append(template);
		replaceClassNameOnly(classDeclaration, className);
		OutputStream os = new FileOutputStream(file);
		os.write(classDeclaration.toString().getBytes());

		os.close();

	}
	private static String readInterfaceTemplate() throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("interfaceTemplate.txt");
		return readFile(is);
	}

	private static StringBuilder createDeleteTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readDeleteTemplate());
		String template = replaceClassAndSql(updateTemplate.toString(), className, queryName);
		updateTemplate.setLength(0);
		updateTemplate.append(template);
		return updateTemplate;
	}

	private static StringBuilder createUpdateTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readUpdateTemplate());
		String template = replaceClassAndSql(updateTemplate.toString(), className, queryName);
		updateTemplate.setLength(0);
		updateTemplate.append(template);		
		return updateTemplate;
	}

	private static StringBuilder createInsertTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {
		StringBuilder updateTemplate = new StringBuilder(readInsertTemplate());
		String template = replaceClassAndSql(updateTemplate.toString(), className, queryName);
		updateTemplate.setLength(0);
		updateTemplate.append(template);
		return updateTemplate;
	}

	public static String readFile(InputStream is) throws IOException {
		StringBuilder sbValue = new StringBuilder(100);
		Scanner scan = new Scanner(is);
		while (scan.hasNext()) {
			sbValue.append(scan.nextLine() + "\n");
		}
		scan.close();
		return sbValue.toString();
	}
	
	public static String readTemplate(String templateName) throws IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream(templateName);
		return readFile(is);		
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
	
	public static void replaceClassNameOnly(StringBuilder data, String className) {
		int start = data.indexOf("**classname**");
		int length = "**classname**".length();

		while (data.indexOf("**classname**", start) > 0) {
			start = data.indexOf("**classname**", start);
			data.delete(start, start + length);
			data.insert(start, className);
		}
	}

	public static String replaceClassAndSql(String source, String className, String queryName) {
		StringBuilder data = new StringBuilder(source);
		int start = data.indexOf("**classname**");
		int length = "**classname**".length();
		replaceClassNameOnly(data, className);
		start = data.indexOf("**sql**");
		length = "**sql**".length();
		while (data.indexOf("**sql**", start) > 0) {
			start = data.indexOf("**sql**", start);
			data.delete(start, start + length);
			data.insert(start, className + "Queries." + queryName);
		}
		return data.toString();
	}

}
