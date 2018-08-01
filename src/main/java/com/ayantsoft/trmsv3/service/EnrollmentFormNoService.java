package com.ayantsoft.trmsv3.service;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.EnrollmentFormNoDao;
import com.ayantsoft.trmsv3.hibernate.pojo.EnrollmentFormNo;

@ManagedBean
@ApplicationScoped
public class EnrollmentFormNoService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -17422164049407723L;
	
	@ManagedProperty("#{enrollmentFormNoDao}")
	private EnrollmentFormNoDao enrollmentFormNoDao;
	
	
	public long findMaximumEnrollmentNo(){
		return enrollmentFormNoDao.findMaximumEnrollmentNo();
	}
	
	public void save(EnrollmentFormNo enrollmentFormNo){
		enrollmentFormNoDao.save(enrollmentFormNo);
	}


	public EnrollmentFormNoDao getEnrollmentFormNoDao() {
		return enrollmentFormNoDao;
	}


	public void setEnrollmentFormNoDao(EnrollmentFormNoDao enrollmentFormNoDao) {
		this.enrollmentFormNoDao = enrollmentFormNoDao;
	}
}
