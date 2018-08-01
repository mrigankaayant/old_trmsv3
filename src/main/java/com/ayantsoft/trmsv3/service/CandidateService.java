package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.SortOrder;

import com.ayantsoft.trmsv3.hibernate.dao.CandidateDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;

@ManagedBean
@ApplicationScoped
public class CandidateService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -7024754119040132095L;
	
	
	@ManagedProperty("#{candidateDao}")
	private CandidateDao candidateDao;
	
	
	public void saveCandidate(Candidate candidate){
		candidateDao.save(candidate);
	}
	
	public Object[] candidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters){
		return candidateDao.candidateFilter(first, pageSize, sortField, sortOrder, filters);
	}
	
	public Object[] terminatedCandidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters){
		return candidateDao.terminatedCandidateFilter(first, pageSize, sortField, sortOrder, filters);
	}
	
	public Candidate findCandidateById(int candidateId){
		return candidateDao.findCandidateById(candidateId);
	}
	
	public boolean checkCandidate(String contactNo,String emailId,int candidateCode){
		return candidateDao.checkCandidate(contactNo, emailId, candidateCode);
	}
	
	public Set<Priority> findPriorityByCandidateId(int candidateId){
		return candidateDao.findPriorityByCandidateId(candidateId);
	}
	
	public List<Candidate> findPrehotlistCandidate(){
		return candidateDao.findPrehotlistCandidate();
	}
	
	public List<Candidate> findHotlistCandidate(){
		return candidateDao.findHotlistCandidate();
	}
	
	
	public List<Candidate> candidateBySearchByAndSearchValue(String searchBy,String searchValue){
		return candidateDao.candidateBySearchByAndSearchValue(searchBy, searchValue);
	}
	
	public Object[] hotlistcandidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters,boolean isSalesManager,List<Priority> priority){
		return candidateDao.hotlistcandidateFilter(first, pageSize, sortField, sortOrder, filters, isSalesManager,priority);
	}
	
	
	public List<Candidate> getcandidateWithReference(){
		return candidateDao.getcandidateWithReference();
	}
	
	public List<Candidate> findCandidateForResumeReport(){
		return candidateDao.findCandidateForResumeReport(); 
	}
	
	public List<Candidate> findMockCandidateByStartdateAndEndDate(Date startDate,Date endDate){
		return candidateDao.findMockCandidateByStartdateAndEndDate(startDate,endDate);
	}
	
	public List<Candidate> candidateBySearchByAndSearchValueForTraning(String searchBy,String searchValue){
		return candidateDao.candidateBySearchByAndSearchValueForTraning(searchBy, searchValue);
	}
	
	public List<Candidate> findCandidatesForEnrollment(String searchBy,String searchValue){
		return candidateDao.findCandidatesForEnrollment(searchBy,searchValue);
	}
	
	public List<Candidate> findCandidatesForEnrollmentWithDateRange(String searchBy,Date startDate,Date endDate){
		return candidateDao.findCandidatesForEnrollmentWithDateRange(searchBy,startDate,endDate);
	}
	
	public List<Candidate> findIncentiveDueCandidate(Date startDate,Date endDate){
		return candidateDao.findIncentiveDueCandidate(startDate,endDate);
	}
	
	public List<Candidate> findEnrommentFormBySearchByDateRange(String searchBy,Date startDate,Date endDate){
		return candidateDao.findEnrommentFormBySearchByDateRange(searchBy,startDate,endDate);
	}
	
	
	public Set<Priority> findCandidatePriority(int candidateId){
		return candidateDao.findCandidatePriority(candidateId);
	}
	
	public List<Candidate> findRemoveCandidateFromHotlist(boolean isSalesManager,List<Priority> priority){
		return candidateDao.findRemoveCandidateFromHotlist(isSalesManager,priority);
	}
	
	
	public List<Candidate> findTrainingCandidate(){
		return candidateDao.findTrainingCandidate();
	}
	
	public List<Candidate> findResumeCandidate(){
		return candidateDao.findResumeCandidate();
	}
	
	public List<Candidate> findMockCandidate(){
		return candidateDao.findMockCandidate();
	}
	
	//setter and getter


	public CandidateDao getCandidateDao() {
		return candidateDao;
	}


	public void setCandidateDao(CandidateDao candidateDao) {
		this.candidateDao = candidateDao;
	}

}
