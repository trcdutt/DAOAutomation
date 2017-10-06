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
import java.util.HashMap;
import java.util.Map;

import com.trc.properties.DatabaseProperties;


/**
 * @author Ramesh Thalathoty
 * 
 */
public class Bean {
	private static final Map<String, String> mappedVariables = new HashMap<String, String>();
	static {
		mappedVariables.put("VARCHAR2", "String");
		mappedVariables.put("VARCHAR", "String");
		mappedVariables.put("NUMBER", "Double");
		mappedVariables.put("CHAR", "String");
		mappedVariables.put("DATE", "Date");
		mappedVariables.put("TIMESTAMP(6) WITH LOCAL TIME ZONE", "Date");
		mappedVariables.put("LONG", "Long");
		mappedVariables.put("RAW", "byte[]");
		mappedVariables.put("BLOB", "java.io.InputStream");
		mappedVariables.put("CLOB", "java.io.InputStream");
		mappedVariables.put("XMLTYPE", "java.io.InputStream");
	}

	public String readBeanTemplate() throws FileNotFoundException, IOException {
		InputStream is = ClassLoader.getSystemResourceAsStream("Bean.txt");
		return MainProcess.readFile(is);
	}

	public void createBeanSource(String className, StringBuilder variables, StringBuilder methods)
			throws FileNotFoundException, IOException {
		String template = readBeanTemplate();
		StringBuilder beanTemplate = new StringBuilder(template);
		StringBuilder varMethods = new StringBuilder(variables);
		varMethods.append(methods);

		int start = beanTemplate.indexOf(Constants.VAR_NAME);
		beanTemplate.replace(start, start + Constants.VAR_NAME_LENGTH, varMethods.toString());
		
		start = beanTemplate.indexOf(Constants.CLASS_NAME);
		beanTemplate.replace(start, start +  Constants.CLASS_NAME_LENGTH, className);
		String dir = (String) DatabaseProperties.getConfigurationProperties().get("generatedfilelocation");
		File file = new File(dir + "/" + className + ".java");
		file.delete();
		if (!file.exists()) {
			file.createNewFile();
		}
		OutputStream os = new FileOutputStream(file);
		os.write(beanTemplate.toString().getBytes());
		os.close();

	}

};
