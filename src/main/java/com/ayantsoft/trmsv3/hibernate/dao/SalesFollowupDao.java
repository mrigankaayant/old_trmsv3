package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ayantsoft.trmsv3.hibernate.pojo.HrFollowUp;
import com.ayantsoft.trmsv3.hibernate.pojo.SalesFollowUp;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class SalesFollowupDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -9129976133310558333L;
	
	
	public void save(SalesFollowUp salesFollowUp){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(salesFollowUp);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	public List<SalesFollowUp> findSalesfollowupByCandidateId(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<SalesFollowUp> salesFollowUps = null;
		try{
			Criteria criteria = session.createCriteria(SalesFollowUp.class,"sfollowup");
			criteria.createAlias("sfollowup.userProfile","up");
			criteria.add(Restrictions.eq("sfollowup.candidate.candidateId",candidateId));
			salesFollowUps = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return salesFollowUps;
	}
	
	
	
	public List<SalesFollowUp> findSalesFollowupByScheduleId(int scheduleId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<SalesFollowUp> salesFollowUps = null;
		try{
			Criteria criteria = session.createCriteria(SalesFollowUp.class,"sfollowup");
			criteria.createAlias("sfollowup.userProfile","u");
			criteria.createAlias("sfollowup.interviewSchedule","i");
			criteria.add(Restrictions.eq("i.id",scheduleId));
			salesFollowUps = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return salesFollowUps;
	}
	

}
