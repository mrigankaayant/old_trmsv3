package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.Course;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class CourseDao implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -2714234688548786473L;
	
	
	public List<String> findAllCourseName(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<String> courseList = null;
		try{

			String hql = "SELECT c.courseName "
					+ "FROM Course c ";
			Query query = session.createQuery(hql);
			courseList = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return courseList;
	}
	
	
	
	public void save(Course course){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(course);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	public boolean findCourseByName(String courseName){
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean isExist = false;
		try{
			String hql = "FROM Course c "
					   + "WHERE c.courseName = :courseName";
			Query query = session.createQuery(hql);
			query.setParameter("courseName", courseName);
			Course result = (Course) query.uniqueResult();
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
