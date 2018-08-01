package com.ayantsoft.trmsv3.jsf.view;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.pojo.MailMessage;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.service.CourseService;
import com.ayantsoft.trmsv3.service.MailContentService;
import com.ayantsoft.trmsv3.service.PriorityService;
import com.ayantsoft.trmsv3.service.UserService;
import com.ayantsoft.trmsv3.service.VisaService;
import com.ayantsoft.trmsv3.util.ProprtiesFileDto;


@ManagedBean(eager=true)
@ApplicationScoped
public class AppDataBean implements Serializable {


	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 5179505795857316352L;

	private ProprtiesFileDto proprtiesFileDto = new ProprtiesFileDto();
	private List<Priority> priorityList = new ArrayList<Priority>();
	private List<String> userList = new ArrayList<String>();
	private List<String> visaList = new ArrayList<String>();
	private List<String> courseList = new ArrayList<String>();
	private Map<String,String> mailMessage = new HashMap<String,String>();

	@ManagedProperty("#{priorityService}")
	private PriorityService priorityService; 

	@ManagedProperty("#{userService}")
	private UserService userService;

	@ManagedProperty("#{visaService}")
	private VisaService visaService;

	@ManagedProperty("#{courseService}")
	private CourseService courseService;

	@ManagedProperty("#{mailContentService}")
	private MailContentService mailContentService;



	@PostConstruct
	public void init(){
		try{
			Properties prop = new Properties();
			InputStream inputStream = getClass().getClassLoader().getResourceAsStream("trmsv3.properties");
			prop.load(inputStream);
			proprtiesFileDto.setEnrollmentFormFolderPath(prop.getProperty("enrollmentFormFolderPath"));
			proprtiesFileDto.setPsrFormFolderPath(prop.getProperty("psrFormFolderPath"));
			proprtiesFileDto.setQuestionerFolderPath(prop.getProperty("questionerFolderPath"));
			proprtiesFileDto.setReferenceFolderPath(prop.getProperty("referenceFolderPath"));
			proprtiesFileDto.setDatasheetFolderPath(prop.getProperty("datasheetFormFolderPath"));
			proprtiesFileDto.setAttachMailFileFolderPath(prop.getProperty("attachMailFileFolderPath"));
			priorityList = priorityService.findAllPriority();
			userList = userService.findAllUserName();
			visaList = visaService.findAllVisaName();
			courseList = courseService.findAllCourseName();
			List<MailMessage> results = mailContentService.findAllMailMessage();
			if(results != null && results.size()>0){
				for(MailMessage m:results){
					if(m.getMessageFor().equals("enrollment_form")){
						mailMessage.put("enrollment_form",m.getMessage());
					}
					if(m.getMessageFor().equals("training_feedback_form")){
						mailMessage.put("training_feedback_form",m.getMessage());
					}
					if(m.getMessageFor().equals("resume")){
						mailMessage.put("resume",m.getMessage());
					}
					if(m.getMessageFor().equals("project_summary_report")){
						mailMessage.put("project_summary_report",m.getMessage());
					}
					if(m.getMessageFor().equals("questioner_form")){
						mailMessage.put("questioner_form",m.getMessage());
					}
					if(m.getMessageFor().equals("reference_check_form")){
						mailMessage.put("reference_check_form",m.getMessage());
					}
					if(m.getMessageFor().equals("sales_template1")){
						mailMessage.put("sales_template1",m.getMessage());
					}
					if(m.getMessageFor().equals("sales_template2")){
						mailMessage.put("sales_template2",m.getMessage());
					}
					if(m.getMessageFor().equals("datasheet_form")){
						mailMessage.put("datasheet_form",m.getMessage());
					}
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public ProprtiesFileDto getProprtiesFileDto() {
		return proprtiesFileDto;
	}

	public void setProprtiesFileDto(ProprtiesFileDto proprtiesFileDto) {
		this.proprtiesFileDto = proprtiesFileDto;
	}

	public List<Priority> getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(List<Priority> priorityList) {
		this.priorityList = priorityList;
	}

	public PriorityService getPriorityService() {
		return priorityService;
	}

	public void setPriorityService(PriorityService priorityService) {
		this.priorityService = priorityService;
	}

	public List<String> getUserList() {
		return userList;
	}

	public void setUserList(List<String> userList) {
		this.userList = userList;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public VisaService getVisaService() {
		return visaService;
	}


	public void setVisaService(VisaService visaService) {
		this.visaService = visaService;
	}


	public CourseService getCourseService() {
		return courseService;
	}


	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}


	public List<String> getVisaList() {
		return visaList;
	}


	public void setVisaList(List<String> visaList) {
		this.visaList = visaList;
	}


	public List<String> getCourseList() {
		return courseList;
	}


	public void setCourseList(List<String> courseList) {
		this.courseList = courseList;
	}


	public MailContentService getMailContentService() {
		return mailContentService;
	}


	public void setMailContentService(MailContentService mailContentService) {
		this.mailContentService = mailContentService;
	}


	public Map<String, String> getMailMessage() {
		return mailMessage;
	}


	public void setMailMessage(Map<String, String> mailMessage) {
		this.mailMessage = mailMessage;
	}


}
