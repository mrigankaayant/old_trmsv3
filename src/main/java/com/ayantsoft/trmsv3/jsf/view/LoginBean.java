package com.ayantsoft.trmsv3.jsf.view;

import java.io.Serializable;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.ayantsoft.trmsv3.hibernate.pojo.Role;
import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;
import com.ayantsoft.trmsv3.service.LoginService;

@ManagedBean
@SessionScoped
public class LoginBean implements Serializable{
	
	
	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -499923003571168048L;
	
	private String username;
	private String password;
	private boolean admin;
	private boolean loginCheck;
	private UserProfile userProfile;

	@ManagedProperty("#{loginService}")
	private LoginService loginService;

	public String login(){
		admin = false;

		userProfile = loginService.login(username, password);

		if(userProfile != null){
			if(hasRole("ADMIN")){
				admin=true;
			}
		}

		if(userProfile != null){
			loginCheck = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_INFO,"Sucess : ", "Welcome, " + userProfile.getUserName()  +"Login Success"));
			if(hasRole("HR")){
				return "/hr/hr.xhtml?faces-redirect=true&i=0";
			}else if(hasRole("SALES")){
				return "/sales/sales.xhtml?faces-redirect=true&i=1";
			}else{
				return null;
			}
		}else{
			loginCheck = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
					FacesMessage.SEVERITY_ERROR,"Error:", "Login Failed, Username Or Password Incorrect."));
			return null;
		}
	}

	public boolean hasRole(String role){
		for(Role r: userProfile.getRoles()){
			if(r.getRoleType().equals(role)){
				return true;
			}
		}
		return false;
	}


	public String logout() {
		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		Map<String, Object> sessionMap = externalContext.getSessionMap();
		sessionMap.clear();
		userProfile = null;
		loginCheck = false;
		return "/login.xhtml?faces-redirect=true";
	}
	
	public LoginService getLoginService() {
		return loginService;
	}
	public void setLoginService(LoginService loginService) {
		this.loginService = loginService;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public boolean getLoginCheck() {
		return loginCheck;
	}
	public void setLoginCheck(boolean loginCheck) {
		this.loginCheck = loginCheck;
	}

	public UserProfile getUserProfile() {
		return userProfile;
	}

	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
}