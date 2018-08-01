package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.UserDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;

@ManagedBean
@ApplicationScoped
public class UserService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 7911333211608372960L;
	
	
	@ManagedProperty("#{userDao}")
	private UserDao userDao;
	
	
	public List<String> findAllUserName(){
		return userDao.findAllUserName();
	}
	
	public Set<Priority> findPriority(int userId){
		return userDao.findPriority(userId);
	}
	
	public void save(UserProfile up){
		userDao.save(up);
	}
	
	public List<UserProfile> findAllUserByCategory(String category){
		return userDao.findAllUserByCategory(category);
	}


	public UserDao getUserDao() {
		return userDao;
	}


	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}
	
	
	

}
