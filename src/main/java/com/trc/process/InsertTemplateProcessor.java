/**
 * 
 */
package com.trc.process;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @author Ramesh Thalathoty
 * 
 */
public class InsertTemplateProcessor {
	private String fileName = "insertTemplate.txt";
	private String templateText;

	private void readInsertTemplate() throws FileNotFoundException, IOException {
		templateText = MainProcess.readTemplate(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String createInsertTemplate(String className, String queryName) throws FileNotFoundException,
			IOException {		
		String modifiedSource = MainProcess.replaceClassAndSql(templateText, className, queryName);
		return modifiedSource;
	}

	public String getTemplateText() throws FileNotFoundException, IOException {
		readInsertTemplate();
		return templateText;
	}

	public void setTemplateText(String templateText) {
		this.templateText = templateText;
	}

}
