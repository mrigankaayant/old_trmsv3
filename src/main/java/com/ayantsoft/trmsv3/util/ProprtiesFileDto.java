package com.ayantsoft.trmsv3.util;

import java.io.Serializable;

public class ProprtiesFileDto implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -5466616151671819655L;
	
	
	private String enrollmentFormFolderPath; 
	private String psrFormFolderPath;
	private String questionerFolderPath;
	private String referenceFolderPath;
	private String attachMailFileFolderPath;
	private String datasheetFolderPath;


	public String getEnrollmentFormFolderPath() {
		return enrollmentFormFolderPath;
	}


	public void setEnrollmentFormFolderPath(String enrollmentFormFolderPath) {
		this.enrollmentFormFolderPath = enrollmentFormFolderPath;
	}


	public String getPsrFormFolderPath() {
		return psrFormFolderPath;
	}


	public void setPsrFormFolderPath(String psrFormFolderPath) {
		this.psrFormFolderPath = psrFormFolderPath;
	}


	public String getQuestionerFolderPath() {
		return questionerFolderPath;
	}


	public void setQuestionerFolderPath(String questionerFolderPath) {
		this.questionerFolderPath = questionerFolderPath;
	}


	public String getReferenceFolderPath() {
		return referenceFolderPath;
	}


	public void setReferenceFolderPath(String referenceFolderPath) {
		this.referenceFolderPath = referenceFolderPath;
	}


	public String getAttachMailFileFolderPath() {
		return attachMailFileFolderPath;
	}


	public void setAttachMailFileFolderPath(String attachMailFileFolderPath) {
		this.attachMailFileFolderPath = attachMailFileFolderPath;
	}


	public String getDatasheetFolderPath() {
		return datasheetFolderPath;
	}


	public void setDatasheetFolderPath(String datasheetFolderPath) {
		this.datasheetFolderPath = datasheetFolderPath;
	}
	
	
	
}
