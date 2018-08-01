package com.ayantsoft.trmsv3.service;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService implements Runnable {

	private String userName;
	private String password;
	private String subject;
	private String mailMessage;
	private String toEmail;
	private String bcc;
	private List<String> filePathList;

	@Override
	public void run() {
		this.sendMail();
	}


	public void setParameters(String userName,String password,String subject,String mailMessage,String toEmail,String bcc,List<String> filePathList){
		
		this.userName = userName;
		this.password = password;
		this.subject = subject;
		this.mailMessage = mailMessage;
		this.toEmail = toEmail;
		this.bcc = bcc;
		this.filePathList = filePathList;
	}


	private Session getSession() {
		Session session = null;
		try{
			Properties properties = new Properties();
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.port", "587");
			properties.put("mail.smtp.host", "smtp.office365.com");
			properties.put("mail.smtp.auth", "true");
			
			session = Session.getInstance(properties,new javax.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName.trim(),password.trim());
				}
			});


		}catch(Exception e){
			e.printStackTrace();
		}
		return session;
	}



	private void sendMail(){
		try{
			Session session = getSession();
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(userName));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
			message.setSubject(subject);
			message.setText(mailMessage);
			
			if(bcc != null){
				message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bcc));
			}

			MimeBodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(mailMessage, "text/html");
			
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);

			if (filePathList != null && filePathList.size() > 0) {
				for (String filePath : filePathList) {
					MimeBodyPart attachPart = new MimeBodyPart();
					try {
						attachPart.attachFile(filePath);
					}catch (IOException ex){
						ex.printStackTrace();
					}
					multipart.addBodyPart(attachPart);
				}
			}
			message.setContent(multipart);
			Transport.send(message);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	
}