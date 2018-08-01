package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Visa;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class VisaDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 222936189886877243L;
	
	
	public List<String> findAllVisaName(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<String> visaList = null;
		try{

			String hql = "SELECT v.visaName "
					+ "FROM Visa v ";
			Query query = session.createQuery(hql);
			visaList = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return visaList;
	}
	
	
	public void save(Visa visa){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(visa);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	
	public boolean findVisaByName(String visaName){
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean isExist = false;
		try{
			String hql = "FROM Visa v "
					   + "WHERE v.visaName = :visaName";
			Query query = session.createQuery(hql);
			query.setParameter("visaName", visaName);
			Visa result = (Visa) query.uniqueResult();
			if(result != null){
				isExist = true;
			}
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return isExist;
	}

}
