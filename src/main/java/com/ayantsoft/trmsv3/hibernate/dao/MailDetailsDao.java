package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.MailDetails;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class MailDetailsDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 52130580078036681L;
	
	public void save(MailDetails mailDetails){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(mailDetails);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	public List<MailDetails> findMailDetails(boolean isSalesManager,int userId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<MailDetails> mailDetails = null;
		try{
			String hql = "FROM MailDetails md ";
			if(!isSalesManager){
				hql = hql + "WHERE md.userProfile.userId = :userId ";
			}
			Query query = session.createQuery(hql);
			if(!isSalesManager){
				query.setParameter("userId",userId);
			}
			mailDetails = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return mailDetails;
	}

}
