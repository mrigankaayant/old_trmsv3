package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ayantsoft.trmsv3.hibernate.pojo.Reference;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class ReferenceDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -6676366561063713897L;
	
	
	public void save(Reference reference){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(reference);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	public List<Reference> findReferenceByCandidateId(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Reference> references = null;
		try{
			Criteria criteria = session.createCriteria(Reference.class,"r");
			criteria.add(Restrictions.eq("r.candidate.candidateId",candidateId));
			references = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return references;
	}
	
	
	public Reference findReferenceByReferenceId(int referenceId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Reference reference = null;
		try{
			String hql = "FROM Reference r "
					   + "JOIN FETCH r.candidate can "
					   + "WHERE r.referenceId = :refId";
			Query query = session.createQuery(hql);
			query.setParameter("refId",referenceId);
			reference = (Reference) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return reference;
	}

}
