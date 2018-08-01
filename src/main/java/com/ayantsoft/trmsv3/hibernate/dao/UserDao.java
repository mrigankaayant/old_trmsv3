package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class UserDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -3633780569691435554L;



	public List<String> findAllUserName(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<String> userNames = null;
		try{

			String hql = "SELECT u.userFullName "
					+ "FROM UserProfile u ";
			Query query = session.createQuery(hql);
			userNames = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return userNames;
	}


	public Set<Priority> findPriority(int userId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<Priority> priorities = null;
		try{
			String hql = "SELECT DISTINCT up "
					+ "FROM UserProfile up "
					+ "JOIN FETCH up.priorities p "
					+ "WHERE up.userId = :userId";
			Query query = session.createQuery(hql);
			query.setParameter("userId",userId);
			UserProfile ups = (UserProfile) query.uniqueResult();
			if(ups != null){
				if(ups.getPriorities() != null && ups.getPriorities().size() >0){
					priorities = ups.getPriorities();
				}
			}
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return priorities;
	}
	
	
	public void save(UserProfile up){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(up);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	
	public List<UserProfile> findAllUserByCategory(String category){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<UserProfile> userProfiles = null;
		try{

			String hql = "FROM UserProfile up "
					   + "WHERE up.userCategory = :category";
			Query query = session.createQuery(hql);
			query.setParameter("category",category);
			userProfiles = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return userProfiles;
	}

}
