package com.ayantsoft.trmsv3.jsf.model;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.service.CandidateService;

public class SalesLazyCandidateModel extends LazyDataModel<Candidate> {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 3732996769387027185L;

	private List<Candidate> candidates;
	private CandidateService candidateService;
	private boolean isSalesManager;
	private List<Priority> priority;
	
	
	public SalesLazyCandidateModel(CandidateService candidateService,boolean isSalesManager,List<Priority> priority){
		this.candidateService = candidateService;
		this.isSalesManager = isSalesManager;
		this.priority = priority;
	}
	
	
	@Override
	public Object getRowKey(Candidate candidate) {
		return candidate.getCandidateId();
	}
	
	
	@Override
	public Candidate getRowData(String rowKey) {
		for(Candidate can : candidates) {
			if(can.getCandidateId() == Integer.parseInt(rowKey))
				return can;
		}

		return null;
	}
	
	
	@Override
	public List<Candidate> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters) {
		Object[] resultWithCount = candidateService.hotlistcandidateFilter(first, pageSize, sortField, sortOrder, filters, isSalesManager,priority);
		this.setRowCount(((Long)resultWithCount[0]).intValue());
		candidates=(List<Candidate>) resultWithCount[1];
		return (List<Candidate>) resultWithCount[1];
	}


	public List<Candidate> getCandidates() {
		return candidates;
	}


	public void setCandidates(List<Candidate> candidates) {
		this.candidates = candidates;
	}


	public CandidateService getCandidateService() {
		return candidateService;
	}


	public void setCandidateService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}


	public boolean isSalesManager() {
		return isSalesManager;
	}


	public void setSalesManager(boolean isSalesManager) {
		this.isSalesManager = isSalesManager;
	}


	public List<Priority> getPriority() {
		return priority;
	}


	public void setPriority(List<Priority> priority) {
		this.priority = priority;
	}
	
	
	
}
