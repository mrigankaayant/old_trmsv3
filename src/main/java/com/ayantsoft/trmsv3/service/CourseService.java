package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.CourseDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Course;

@ManagedBean
@ApplicationScoped
public class CourseService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 7422070491524142146L;
	
	
	@ManagedProperty("#{courseDao}")
	private CourseDao courseDao;
	
	public List<String> findAllCourseName(){
		return courseDao.findAllCourseName();
	}
	
	
	public void save(Course course){
		courseDao.save(course);
	}
	
	public boolean findCourseByName(String courseName){
		return courseDao.findCourseByName(courseName);
	}


	public CourseDao getCourseDao() {
		return courseDao;
	}


	public void setCourseDao(CourseDao courseDao) {
		this.courseDao = courseDao;
	}
	
	
	

}
