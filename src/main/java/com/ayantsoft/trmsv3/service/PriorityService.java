package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.PriorityDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;

@ManagedBean
@ApplicationScoped
public class PriorityService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 5241063028523794560L;
	
	
	@ManagedProperty("#{priorityDao}")
	private PriorityDao priorityDao;


	public List<Priority> findAllPriority(){
		return priorityDao.findAllPriority();
	}
	// setter and getter
	
	public PriorityDao getPriorityDao() {
		return priorityDao;
	}


	public void setPriorityDao(PriorityDao priorityDao) {
		this.priorityDao = priorityDao;
	}

}
