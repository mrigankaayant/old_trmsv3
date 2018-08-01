package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.ReferenceDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Reference;

@ManagedBean
@ApplicationScoped
public class ReferenceService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 4262911479775477350L;
	
	@ManagedProperty("#{referenceDao}")
	private ReferenceDao referenceDao;
	
	public void save(Reference reference){
		referenceDao.save(reference);
	}
	
	public List<Reference> findReferenceByCandidateId(int candidateId){
		return referenceDao.findReferenceByCandidateId(candidateId);
	}
	
	public Reference findReferenceByReferenceId(int referenceId){
		return referenceDao.findReferenceByReferenceId(referenceId);
	}

	public ReferenceDao getReferenceDao() {
		return referenceDao;
	}

	public void setReferenceDao(ReferenceDao referenceDao) {
		this.referenceDao = referenceDao;
	}
	
	
	

}
