/**
 * 
 */
package com.trc.process;

import java.util.List;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class Table {
	private String name;
	private String className;
	private List<String> primaryColumns;

	public String getClassName() {
		return className;
	}

	public List<String> getPrimaryColumns() {
		return primaryColumns;
	}
	public void setPrimaryColumns(List<String> primaryColumns) {
		this.primaryColumns = primaryColumns;
	}
	private List<Column> columns;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		setClassName();
	}
	public List<Column> getColumns() {
		return columns;
	}
	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}
	private void setClassName() {
		StringBuilder sbCharacter = new StringBuilder();
		char c = name.charAt(0);

		sbCharacter.append(new Character(c).toString().toUpperCase().charAt(0));
		className = name.toLowerCase();
		for (int i = 1; i < className.length(); i++) {
			char c1 = className.charAt(i);
			if (i >= 1 && className.charAt(i - 1) == '_') {
				c1 = new Character(c1).toString().toUpperCase().charAt(0);
			}
			sbCharacter.append(c1);
		}

		className = sbCharacter.toString().replace("_", "");
	}

}
