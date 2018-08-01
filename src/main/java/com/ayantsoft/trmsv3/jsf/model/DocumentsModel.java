package com.ayantsoft.trmsv3.jsf.model;

import java.io.Serializable;
import java.util.Date;

public class DocumentsModel implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -8607830422385670064L;
	
	
	private String fileName;
	private String filePath;
	private Date createdDate;
	private String extension;
	private String docOtherName;
	
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	public String getDocOtherName() {
		return docOtherName;
	}
	public void setDocOtherName(String docOtherName) {
		this.docOtherName = docOtherName;
	}
}
