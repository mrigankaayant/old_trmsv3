package com.ayantsoft.trmsv3.filter;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ayantsoft.trmsv3.jsf.view.LoginBean;

public class LoginPhaseListener implements PhaseListener {
	
	
	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -8732696700949287970L;

	public void afterPhase(PhaseEvent event) {
		
		FacesContext facesContext = event.getFacesContext();
		HttpSession httpSession = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);		
		LoginBean loginBean = (LoginBean) httpSession.getAttribute("loginBean");
		
		if(loginBean == null){
			NavigationHandler navigationHandler = facesContext.getApplication().getNavigationHandler();
			navigationHandler.handleNavigation(facesContext, null, "login.xhtml");
			 
			ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
			configurableNavigationHandler.performNavigation("/login.xhtml");
		}else{
			if(!facesContext.getViewRoot().getViewId().equals("/login.xhtml")){
				if(loginBean.getUserProfile() == null || loginBean.getLoginCheck() == false){
					ConfigurableNavigationHandler configurableNavigationHandler = (ConfigurableNavigationHandler) FacesContext.getCurrentInstance().getApplication().getNavigationHandler();
					configurableNavigationHandler.performNavigation("/login.xhtml");
				}
			}
		}
		
	}
	
	public void beforePhase(PhaseEvent event) {
		FacesContext facesContext = event.getFacesContext();
		HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
		response.addHeader("Pragma", "no-cache");
		response.addHeader("Cache-Control", "no-cache");
		response.addHeader("Cache-Control", "no-store");
		response.addHeader("Cache-Control", "must-revalidate");
	}
	
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}
}