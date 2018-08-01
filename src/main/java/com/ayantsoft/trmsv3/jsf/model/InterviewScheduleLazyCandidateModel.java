/*package com.ayantsoft.trmsv3.jsf.model;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ayantsoft.trmsv3.hibernate.pojo.InterviewSchedule;
import com.ayantsoft.trmsv3.service.InterviewScheduleService;

public class InterviewScheduleLazyCandidateModel extends LazyDataModel<InterviewSchedule> {

	*//**
	 *serialVersionUID 
	 *//*
	private static final long serialVersionUID = 4515846773416534124L;
	
	
	private List<InterviewSchedule> interviewSchedules;
	private InterviewScheduleService interviewScheduleService;
	private boolean isSalesManager;
	private int userId;
	
	
	public InterviewScheduleLazyCandidateModel(InterviewScheduleService interviewScheduleService,boolean isSalesManager,int userId){
		this.interviewScheduleService = interviewScheduleService;
		this.isSalesManager = isSalesManager;
		this.userId = userId;
		System.out.println("cccccccccccccccccccccccccccccccccccccccccccccccccccccccc");
	}
	
	
	@Override
	public Object getRowKey(InterviewSchedule interviewSchedule) {
		return interviewSchedule.getId();
	}
	
	
	@Override
	public InterviewSchedule getRowData(String rowKey) {
		for(InterviewSchedule i : interviewSchedules) {
			if(i.getId() == Integer.parseInt(rowKey))
				return i;
		}
		return null;
	}
	
	
	@Override
	public List<InterviewSchedule> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		System.out.println("load load load load load loadload load load load load load load");
		Object[] resultWithCount = interviewScheduleService.schedulecandidatesFilter(first, pageSize, sortField, sortOrder, filters, isSalesManager,userId);
		this.setRowCount(((Long)resultWithCount[0]).intValue());
		interviewSchedules=(List<InterviewSchedule>) resultWithCount[1];
		return (List<InterviewSchedule>) resultWithCount[1];
	}


	public List<InterviewSchedule> getInterviewSchedules() {
		return interviewSchedules;
	}


	public void setInterviewSchedules(List<InterviewSchedule> interviewSchedules) {
		this.interviewSchedules = interviewSchedules;
	}


	public InterviewScheduleService getInterviewScheduleService() {
		return interviewScheduleService;
	}


	public void setInterviewScheduleService(InterviewScheduleService interviewScheduleService) {
		this.interviewScheduleService = interviewScheduleService;
	}


	public boolean isSalesManager() {
		return isSalesManager;
	}


	public void setSalesManager(boolean isSalesManager) {
		this.isSalesManager = isSalesManager;
	}


	public int getUserId() {
		return userId;
	}


	public void setUserId(int userId) {
		this.userId = userId;
	}
}
*/