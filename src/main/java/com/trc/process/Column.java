/**
 * 
 */
package com.trc.process;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class Column {
	private String colName;
	private String variableName;
	private String getMethodName;
	private String setMethodName;
	private static final List<String> mappedKeyValues = new ArrayList<String>();
	private static final Map<String, String> mappedVariables = new HashMap<String, String>();
	private String dataType;
	private String javaType;

	static {
		mappedKeyValues.add("interface");
		mappedKeyValues.add("class");

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

	public String getColName() {
		return colName;
	}
	public void setColName(String colName) {
		if (mappedKeyValues.indexOf(colName) >= 0) {
			colName = colName + "1";
		}
		this.colName = colName;

		setVariableName();

	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
		setJavaType(mappedVariables.get(dataType));		
	}

	public void setVariableName() {
		StringBuilder sbCharacter = new StringBuilder();
		variableName = colName.toLowerCase();

		for (int i = 0; i < variableName.length(); i++) {
			char c = variableName.charAt(i);
			if (i >= 1 && variableName.charAt(i - 1) == '_') {
				c = new Character(c).toString().toUpperCase().charAt(0);
			}
			sbCharacter.append(c);
		}

		variableName = sbCharacter.toString().replace("_", "");
		setMethodNames();

	}
	public void setMethodNames() {
		StringBuilder sbCharacter = new StringBuilder();
		char c = variableName.charAt(0);
		sbCharacter.append(new Character(c).toString().toUpperCase().charAt(0));
		sbCharacter.append(variableName.substring(1));
		getMethodName = "get" + sbCharacter.toString();
		setMethodName = "set" + sbCharacter.toString();
	}
	public String getVariableName() {
		return variableName;
	}
	public String getGetMethodName() {
		return getMethodName;
	}
	public String getSetMethodName() {
		return setMethodName;
	}
	public String getJavaType() {
		return javaType;
	}
	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}
}
