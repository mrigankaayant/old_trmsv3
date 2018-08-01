package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.EnrollmentFormNo;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class EnrollmentFormNoDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 618769654915011198L;
	
	
	public long findMaximumEnrollmentNo(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		int maxEnrollmentNo = 0;
		try{
			String hql = "SELECT max(e.enrollmentFormNumber) "
                        +"from EnrollmentFormNo e ";
			Query query = session.createQuery(hql);
			maxEnrollmentNo = (int) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return maxEnrollmentNo;
	}
	
	
	public void save(EnrollmentFormNo enrollmentFormNo){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(enrollmentFormNo);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}

}
