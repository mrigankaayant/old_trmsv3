package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.MailTemplate;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class MailTemplateDao implements Serializable{

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -4298548962608887740L;
	
	
	public MailTemplate findMailTemplate(String subject){
		Session session = HibernateUtil.getSessionFactory().openSession();
		MailTemplate mailTemplate = null;
		try{
			
			String hql = "FROM MailTemplate m where m.subject = :subject ";
			Query query = session.createQuery(hql);
			query.setParameter("subject", subject);
			mailTemplate = (MailTemplate) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return mailTemplate;
	}

}
