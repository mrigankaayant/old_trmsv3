package com.ayantsoft.trmsv3.service;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.MailTemplateDao;
import com.ayantsoft.trmsv3.hibernate.pojo.MailTemplate;

@ManagedBean
@ApplicationScoped
public class MailTemplateService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 7407306431990901710L;
	
	
	@ManagedProperty("#{mailTemplateDao}")
	private MailTemplateDao mailTemplateDao;
	
	
	public MailTemplate findMailTemplate(String subject){
		return mailTemplateDao.findMailTemplate(subject);
	}


	public MailTemplateDao getMailTemplateDao() {
		return mailTemplateDao;
	}


	public void setMailTemplateDao(MailTemplateDao mailTemplateDao) {
		this.mailTemplateDao = mailTemplateDao;
	}
	
	
	

}
