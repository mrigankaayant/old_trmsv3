package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class LoginDao implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2042771998725976801L;

	public UserProfile loginfromDb(String username, String password){

		Session session = HibernateUtil.getSessionFactory().openSession();
		UserProfile user = null;
		try{
			String hql ="from UserProfile u join fetch u.roles r where u.userName = :username and u.password = :password";
			Query query = session.createQuery(hql);
			query.setParameter("username", username);
			query.setParameter("password", password);
			user = (UserProfile)query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally {
			session.close();
		}

		return user;
	}

}
