package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.SortOrder;

import com.ayantsoft.trmsv3.hibernate.dao.InterviewScheduleDao;
import com.ayantsoft.trmsv3.hibernate.pojo.InterviewSchedule;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;

@ManagedBean
@ApplicationScoped
public class InterviewScheduleService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -8539640231298289206L;
	
	@ManagedProperty("#{interviewScheduleDao}")
	private InterviewScheduleDao interviewScheduleDao;
	
	
	public List<InterviewSchedule> findinterviewSchedules(int candidateId){
		return interviewScheduleDao.findinterviewSchedules(candidateId);
	}
	
	public void save(InterviewSchedule interviewSchedule){
		interviewScheduleDao.save(interviewSchedule);
	}
	
	public String findMaxBillRate(int candidateId){
		return interviewScheduleDao.findMaxBillRate(candidateId);
	}
	
	public Object[] schedulecandidatesFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters,boolean isSalesManager,int userId){
		return interviewScheduleDao.schedulecandidatesFilter(first, pageSize, sortField, sortOrder, filters, isSalesManager,userId);
	}
	
	
	/*public List<InterviewSchedule> schedulecandidates(boolean isSalesManager,int userId){
		return interviewScheduleDao.schedulecandidates(isSalesManager, userId);
	}*/
	
	public InterviewSchedule findinterviewSchedulesbyId(int interviewScheduleId){
		return interviewScheduleDao.findinterviewSchedulesbyId(interviewScheduleId);
	}
	
	
	// setter and getter

	public InterviewScheduleDao getInterviewScheduleDao() {
		return interviewScheduleDao;
	}

	public void setInterviewScheduleDao(InterviewScheduleDao interviewScheduleDao) {
		this.interviewScheduleDao = interviewScheduleDao;
	}
	
	

}
