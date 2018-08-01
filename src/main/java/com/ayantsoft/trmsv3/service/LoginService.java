package com.ayantsoft.trmsv3.service;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.LoginDao;
import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;

@ManagedBean
@ApplicationScoped
public class LoginService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7773748071139802724L;
	
	@ManagedProperty("#{loginDao}")
	private LoginDao loginDao;
	
	public UserProfile login(String username, String password){
		return loginDao.loginfromDb(username, password);
	}
	
	public LoginDao getLoginDao() {
		return loginDao;
	}

	public void setLoginDao(LoginDao loginDao) {
		this.loginDao = loginDao;
	}

}
