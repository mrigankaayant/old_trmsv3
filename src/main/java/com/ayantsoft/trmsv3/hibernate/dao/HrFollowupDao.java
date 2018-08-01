package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.ayantsoft.trmsv3.hibernate.pojo.HrFollowUp;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class HrFollowupDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 4879930653193388233L;
	
	
	public void save(HrFollowUp hrFollowUp){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(hrFollowUp);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	public List<HrFollowUp> findHrfollowupByType(int candidateId,String followUpType){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<HrFollowUp> hrFollowUps = null;
		try{
			Criteria criteria = session.createCriteria(HrFollowUp.class,"hfu");
			criteria.createAlias("hfu.userProfile","up");
			Criterion c1 = Restrictions.eq("hfu.candidate.candidateId",candidateId);
			Criterion c2 = Restrictions.eq("hfu.followUpType",followUpType);
			criteria.add(Restrictions.and(c1,c2));
			hrFollowUps = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return hrFollowUps;
	}
	
	
	
	public List<HrFollowUp> findHrfollowupByCandidateId(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<HrFollowUp> hrFollowUps = null;
		try{
			Criteria criteria = session.createCriteria(HrFollowUp.class,"hfu");
			criteria.createAlias("hfu.userProfile","up");
			criteria.add(Restrictions.eq("hfu.candidate.candidateId",candidateId));
			hrFollowUps = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return hrFollowUps;
	}

}
