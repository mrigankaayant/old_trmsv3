package com.ayantsoft.trmsv3.util;

import java.io.Serializable;

public class FileHandleUtil implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 6603773584245407930L;
	
	
	private String filePath;
	private String fileName;
	private String fileExtension;
	
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExtension() {
		return fileExtension;
	}
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
}
