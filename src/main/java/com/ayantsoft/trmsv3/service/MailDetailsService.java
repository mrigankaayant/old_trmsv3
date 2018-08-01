package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.MailDetailsDao;
import com.ayantsoft.trmsv3.hibernate.pojo.MailDetails;

@ManagedBean
@ApplicationScoped
public class MailDetailsService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -1120368193004998004L;
	
	
	@ManagedProperty("#{mailDetailsDao}")
	private MailDetailsDao mailDetailsDao;
	
	
	public void save(MailDetails mailDetails){
		mailDetailsDao.save(mailDetails);
	}
	
	public List<MailDetails> findMailDetails(boolean isSalesManager,int userId){
		return mailDetailsDao.findMailDetails(isSalesManager,userId);
	}


	public MailDetailsDao getMailDetailsDao() {
		return mailDetailsDao;
	}


	public void setMailDetailsDao(MailDetailsDao mailDetailsDao) {
		this.mailDetailsDao = mailDetailsDao;
	}
}
