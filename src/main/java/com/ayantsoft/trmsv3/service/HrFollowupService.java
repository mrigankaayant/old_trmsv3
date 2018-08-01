package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.HrFollowupDao;
import com.ayantsoft.trmsv3.hibernate.pojo.HrFollowUp;

@ManagedBean
@ApplicationScoped
public class HrFollowupService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -5703082207920677288L;
	
	
	@ManagedProperty("#{hrFollowupDao}")
	private HrFollowupDao hrFollowupDao;
	
	
	public void save(HrFollowUp hrFollowUp){
		hrFollowupDao.save(hrFollowUp);
	}
	
	public List<HrFollowUp> findHrfollowupByType(int candidateId,String followUpType){
		return hrFollowupDao.findHrfollowupByType(candidateId, followUpType);
	}
	
	
	public List<HrFollowUp> findHrfollowupByCandidateId(int candidateId){
		return hrFollowupDao.findHrfollowupByCandidateId(candidateId);
	}


	public HrFollowupDao getHrFollowupDao() {
		return hrFollowupDao;
	}


	public void setHrFollowupDao(HrFollowupDao hrFollowupDao) {
		this.hrFollowupDao = hrFollowupDao;
	}
}
