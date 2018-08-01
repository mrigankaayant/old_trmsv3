package com.ayantsoft.trmsv3.hibernate.pojo;
// Generated 14 Sep, 2017 12:05:51 PM by Hibernate Tools 4.3.5.Final

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrFollowUp generated by hbm2java
 */
@Entity
@Table(name = "hr_follow_up", catalog = "trmsv2_hr_sales")
public class HrFollowUp implements java.io.Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 2945757285929343794L;
	
	private Integer hrFollowUpId;
	private Candidate candidate;
	private UserProfile userProfile;
	private Date followUpDate;
	private String followUpRemarks;
	private String followUpType;
	private Date nextFollowUpDate;

	public HrFollowUp() {
	}

	public HrFollowUp(Candidate candidate, UserProfile userProfile, Date followUpDate, String followUpRemarks) {
		this.candidate = candidate;
		this.userProfile = userProfile;
		this.followUpDate = followUpDate;
		this.followUpRemarks = followUpRemarks;
	}

	public HrFollowUp(Candidate candidate, UserProfile userProfile, Date followUpDate, String followUpRemarks,
			String followUpType, Date nextFollowUpDate) {
		this.candidate = candidate;
		this.userProfile = userProfile;
		this.followUpDate = followUpDate;
		this.followUpRemarks = followUpRemarks;
		this.followUpType = followUpType;
		this.nextFollowUpDate = nextFollowUpDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "hr_follow_up_id", unique = true, nullable = false)
	public Integer getHrFollowUpId() {
		return this.hrFollowUpId;
	}

	public void setHrFollowUpId(Integer hrFollowUpId) {
		this.hrFollowUpId = hrFollowUpId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "candidate_id", nullable = false)
	public Candidate getCandidate() {
		return this.candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public UserProfile getUserProfile() {
		return this.userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "follow_up_date", nullable = false, length = 10)
	public Date getFollowUpDate() {
		return this.followUpDate;
	}

	public void setFollowUpDate(Date followUpDate) {
		this.followUpDate = followUpDate;
	}

	@Column(name = "follow_up_remarks", nullable = false, length = 500)
	public String getFollowUpRemarks() {
		return this.followUpRemarks;
	}

	public void setFollowUpRemarks(String followUpRemarks) {
		this.followUpRemarks = followUpRemarks;
	}

	@Column(name = "follow_up_type", length = 45)
	public String getFollowUpType() {
		return this.followUpType;
	}

	public void setFollowUpType(String followUpType) {
		this.followUpType = followUpType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "next_follow_up_date", length = 10)
	public Date getNextFollowUpDate() {
		return this.nextFollowUpDate;
	}

	public void setNextFollowUpDate(Date nextFollowUpDate) {
		this.nextFollowUpDate = nextFollowUpDate;
	}

}