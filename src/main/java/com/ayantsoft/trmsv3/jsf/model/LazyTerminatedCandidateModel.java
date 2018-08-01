package com.ayantsoft.trmsv3.jsf.model;

import java.util.List;
import java.util.Map;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.service.CandidateService;


public class LazyTerminatedCandidateModel extends LazyDataModel<Candidate> {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 981241364400964025L;
	
	private List<Candidate> candidates;
	private CandidateService candidateService;
	
	
	public LazyTerminatedCandidateModel(CandidateService candidateService){
		this.candidateService = candidateService;
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
		Object[] resultWithCount = candidateService.terminatedCandidateFilter(first, pageSize, sortField, sortOrder, filters);
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
}