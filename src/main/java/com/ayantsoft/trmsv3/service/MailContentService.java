package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.MailMessageDao;
import com.ayantsoft.trmsv3.hibernate.pojo.MailMessage;

@ManagedBean
@ApplicationScoped
public class MailContentService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -7445809550763294113L;
	
	@ManagedProperty("#{mailMessageDao}")
	private MailMessageDao mailMessageDao;
	
	
	public List<MailMessage> findAllMailMessage(){
		return mailMessageDao.findAllMailMessage();
	}
	

	public MailMessageDao getMailMessageDao() {
		return mailMessageDao;
	}

	public void setMailMessageDao(MailMessageDao mailMessageDao) {
		this.mailMessageDao = mailMessageDao;
	}
	
	
	

}
