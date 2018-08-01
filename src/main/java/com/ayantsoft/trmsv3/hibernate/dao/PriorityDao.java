package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class PriorityDao implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1677598733175814869L;
	
	
	public List<Priority> findAllPriority(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Priority> priorities = null;
		try{
			Criteria criteria = session.createCriteria(Priority.class);
			priorities = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return priorities;
	}

}
