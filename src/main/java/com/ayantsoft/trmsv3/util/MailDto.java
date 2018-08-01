package com.ayantsoft.trmsv3.util;

import java.io.Serializable;
import java.util.List;

public class MailDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7093796050838564063L;
	
	
	private String userName;
	private String password;
	private String subject;
	private String mailMessage;
	private String toEmail;
	private List<String> filePathList;
	private String extention;
	private String fileName;
	private String resumePath;
	private String template;
	private String actualMessage;
	private String role;
	private String utilityFilePath;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getMailMessage() {
		return mailMessage;
	}
	public void setMailMessage(String mailMessage) {
		this.mailMessage = mailMessage;
	}
	public String getToEmail() {
		return toEmail;
	}
	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}
	public List<String> getFilePathList() {
		return filePathList;
	}
	public void setFilePathList(List<String> filePathList) {
		this.filePathList = filePathList;
	}
	public String getExtention() {
		return extention;
	}
	public void setExtention(String extention) {
		this.extention = extention;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getResumePath() {
		return resumePath;
	}
	public void setResumePath(String resumePath) {
		this.resumePath = resumePath;
	}
	public String getTemplate() {
		return template;
	}
	public void setTemplate(String template) {
		this.template = template;
	}
	public String getActualMessage() {
		return actualMessage;
	}
	public void setActualMessage(String actualMessage) {
		this.actualMessage = actualMessage;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getUtilityFilePath() {
		return utilityFilePath;
	}
	public void setUtilityFilePath(String utilityFilePath) {
		this.utilityFilePath = utilityFilePath;
	}
	
	
}
