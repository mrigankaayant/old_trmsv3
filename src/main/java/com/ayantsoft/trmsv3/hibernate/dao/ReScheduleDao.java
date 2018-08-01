package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.ReSchedule;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class ReScheduleDao implements Serializable{

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -93025592442291798L;
	
	public void save(ReSchedule reSchedule){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(reSchedule);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}

}
