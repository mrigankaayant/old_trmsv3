package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.SalesFollowupDao;
import com.ayantsoft.trmsv3.hibernate.pojo.SalesFollowUp;

@ManagedBean
@ApplicationScoped
public class SalesFollowupService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -103454880532253639L;
	
	@ManagedProperty("#{salesFollowupDao}")
	private SalesFollowupDao salesFollowupDao;
	
	
	public void save(SalesFollowUp salesFollowUp){
		salesFollowupDao.save(salesFollowUp);
	}
	
	public List<SalesFollowUp> findSalesfollowupByCandidateId(int candidateId){
		return salesFollowupDao.findSalesfollowupByCandidateId(candidateId);
	}
	
	public List<SalesFollowUp> findSalesFollowupByScheduleId(int scheduleId){
		return salesFollowupDao.findSalesFollowupByScheduleId(scheduleId);
	}
	
	// setter and getter

	public SalesFollowupDao getSalesFollowupDao() {
		return salesFollowupDao;
	}

	public void setSalesFollowupDao(SalesFollowupDao salesFollowupDao) {
		this.salesFollowupDao = salesFollowupDao;
	}
	
	

}
