package com.ayantsoft.trmsv3.service;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.ReScheduleDao;
import com.ayantsoft.trmsv3.hibernate.pojo.ReSchedule;

@ManagedBean
@ApplicationScoped
public class ReScheduleService implements Serializable{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1615898940784020692L;
	
	@ManagedProperty("#{reScheduleDao}")
	private ReScheduleDao reScheduleDao;
	
	
	public void save(ReSchedule reSchedule){
		reScheduleDao.save(reSchedule);
	}

	public ReScheduleDao getReScheduleDao() {
		return reScheduleDao;
	}

	public void setReScheduleDao(ReScheduleDao reScheduleDao) {
		this.reScheduleDao = reScheduleDao;
	}
	
	
	

}
