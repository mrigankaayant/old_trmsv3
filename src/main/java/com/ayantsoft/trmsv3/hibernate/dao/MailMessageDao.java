package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.MailMessage;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class MailMessageDao implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7456925680970085910L;
	
	public List<MailMessage> findAllMailMessage(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<MailMessage> results = null;
		try{

			String hql = "FROM MailMessage m ";
			Query query = session.createQuery(hql);
			results = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return results;
	}

}
