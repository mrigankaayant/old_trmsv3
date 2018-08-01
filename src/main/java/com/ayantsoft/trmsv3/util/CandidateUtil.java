package com.ayantsoft.trmsv3.util;

import java.util.Date;

public class CandidateUtil {
	
	private Integer candidateId;
	private String candidateName;
	private String candidateEmail;
	private String candidateWorkMobile;
	private String candidateCurrentLocation;
	private String candidatePreferredLocation;
	private String candidateVisa;
	private Long totalAmount;
	private String recruiterName;
	private String supervisorName;
	private Date enrollmentDate;
	
	
	public Integer getCandidateId() {
		return candidateId;
	}
	public void setCandidateId(Integer candidateId) {
		this.candidateId = candidateId;
	}
	public String getCandidateName() {
		return candidateName;
	}
	public void setCandidateName(String candidateName) {
		this.candidateName = candidateName;
	}
	public String getCandidateEmail() {
		return candidateEmail;
	}
	public void setCandidateEmail(String candidateEmail) {
		this.candidateEmail = candidateEmail;
	}
	public String getCandidateWorkMobile() {
		return candidateWorkMobile;
	}
	public void setCandidateWorkMobile(String candidateWorkMobile) {
		this.candidateWorkMobile = candidateWorkMobile;
	}
	public String getCandidateCurrentLocation() {
		return candidateCurrentLocation;
	}
	public void setCandidateCurrentLocation(String candidateCurrentLocation) {
		this.candidateCurrentLocation = candidateCurrentLocation;
	}
	public String getCandidatePreferredLocation() {
		return candidatePreferredLocation;
	}
	public void setCandidatePreferredLocation(String candidatePreferredLocation) {
		this.candidatePreferredLocation = candidatePreferredLocation;
	}
	public String getCandidateVisa() {
		return candidateVisa;
	}
	public void setCandidateVisa(String candidateVisa) {
		this.candidateVisa = candidateVisa;
	}
	public Long getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(Long totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getRecruiterName() {
		return recruiterName;
	}
	public void setRecruiterName(String recruiterName) {
		this.recruiterName = recruiterName;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public Date getEnrollmentDate() {
		return enrollmentDate;
	}
	public void setEnrollmentDate(Date enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}
}
