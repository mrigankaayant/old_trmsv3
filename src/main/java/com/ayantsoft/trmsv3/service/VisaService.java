package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.VisaDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Visa;

@ManagedBean
@ApplicationScoped
public class VisaService implements Serializable{

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 6414094168856187969L;

	@ManagedProperty("#{visaDao}")
	private VisaDao visaDao;
	
	public List<String> findAllVisaName(){
		return visaDao.findAllVisaName();
	}
	
	public void save(Visa visa){
		visaDao.save(visa);
	}
	
	public boolean findVisaByName(String visaName){
		return visaDao.findVisaByName(visaName);
	}

	public VisaDao getVisaDao() {
		return visaDao;
	}

	public void setVisaDao(VisaDao visaDao) {
		this.visaDao = visaDao;
	}

}
