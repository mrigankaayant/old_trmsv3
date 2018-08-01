package com.ayantsoft.trmsv3.jsf.view;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.InterviewSchedule;
import com.ayantsoft.trmsv3.hibernate.pojo.MailDetails;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.pojo.ReSchedule;
import com.ayantsoft.trmsv3.hibernate.pojo.SalesFollowUp;
import com.ayantsoft.trmsv3.hibernate.pojo.UserProfile;
import com.ayantsoft.trmsv3.jsf.model.DocumentsModel;
import com.ayantsoft.trmsv3.jsf.model.SalesLazyCandidateModel;
import com.ayantsoft.trmsv3.jsf.model.ScheduledCandidateLazyModel;
import com.ayantsoft.trmsv3.service.CandidateService;
import com.ayantsoft.trmsv3.service.DocumentsService;
import com.ayantsoft.trmsv3.service.InterviewScheduleService;
import com.ayantsoft.trmsv3.service.MailDetailsService;
import com.ayantsoft.trmsv3.service.MailMessageService;
import com.ayantsoft.trmsv3.service.MailService;
import com.ayantsoft.trmsv3.service.ReScheduleService;
import com.ayantsoft.trmsv3.service.SalesFollowupService;
import com.ayantsoft.trmsv3.service.UserService;
import com.ayantsoft.trmsv3.util.MailDto;
import com.ayantsoft.trmsv3.util.ProprtiesFileDto;

@ManagedBean
@ViewScoped
public class SalesBean implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1185608781551142664L;

	private String action;
	private List<Candidate> hotlistCandidates;
	private Candidate selectedCandidate;
	private List<InterviewSchedule> interviewScheduleList;
	private ReSchedule reSchedule;
	private InterviewSchedule interviewSchedule;
	private SalesFollowUp salesFollowUp;
	private List<SalesFollowUp> salesFollowupList;
	private MailDto mailDto;
	private List<MailDetails> mailHistory;
	private MailDetails mailDetails;
	private List<Candidate> selectedCandidates;
	private String documents[];
	private List<DocumentsModel> documentsList;
	private List<UploadedFile> mailDocuments;
	private List<String> mailFileName;
	private List<UserProfile> users;
	private Integer priorities[];
	private UserProfile user;
	private String reason;
	private LazyDataModel<Candidate> hotlistCandidateLazyModel;
	private LazyDataModel<InterviewSchedule> scheduledCandidateLazyModel;
	private List<InterviewSchedule> selectedinterviewSchedules;

	@ManagedProperty("#{userService}")
	private UserService userService;

	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;     

	@ManagedProperty("#{candidateService}")
	private CandidateService candidateService;

	@ManagedProperty("#{interviewScheduleService}")
	private InterviewScheduleService interviewScheduleService;

	@ManagedProperty("#{reScheduleService}")
	private ReScheduleService reScheduleService;

	@ManagedProperty("#{salesFollowupService}")
	private SalesFollowupService salesFollowupService;

	@ManagedProperty("#{mailDetailsService}")
	private MailDetailsService mailDetailsService;


	@ManagedProperty("#{documentsService}")
	private DocumentsService documentsService;

	@ManagedProperty("#{appDataBean}")
	private AppDataBean appDataBean;


	public void hotlistCandidates(){
		action = "HOTLISTCANDIDATE";
		selectedCandidate = null;
		interviewScheduleList = null;
		selectedCandidates = null;
		documents = null;
		documentsList = null;
		salesFollowUp = null;
		Set<Priority> p = userService.findPriority(loginBean.getUserProfile().getUserId());
		List<Priority> priorities = null;
		if(p != null && p.size() > 0){
			priorities = new ArrayList<Priority>();
			priorities.addAll(p);
		}
		hotlistCandidateLazyModel = new SalesLazyCandidateModel(candidateService,loginBean.hasRole("SALES MANAGER"),priorities);
	}


	public void openFile() {
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String path = params.get("filePath");
		String name = params.get("filename");
		String extension = params.get("extension");

		try {
			File file = new File(path);
			HttpServletResponse response = (HttpServletResponse)FacesContext.getCurrentInstance().getExternalContext().getResponse();
			if (extension.equals(".xls") || extension.equals(".XLS")) {
				response.setContentType("application/xls");
			}else if(extension.equals(".pdf") || extension.equals(".PDF")){
				response.setContentType("application/pdf");
			}else if(extension.equals(".zip") || extension.equals(".ZIP")){
				response.setContentType("application/zip");
			}else if(extension.equals(".docx") || extension.equals(".DOCX")){
				response.setContentType("application/octet-stream");
			}else if(extension.equals(".doc") || extension.equals(".DOC")){
				response.setContentType("application/octet-stream");
			}else if(extension.equals(".txt") || extension.equals(".TXT")){
				response.setContentType("application/text");
			}else if(extension.equals(".xlsx") || extension.equals(".XLSX")){
				response.setContentType("application/xlsx");
			}else if(extension.equals(".csv") || extension.equals(".CSV")){
				response.setContentType("application/csv");
			}else if(extension.equals(".jpg") || extension.equals(".JPG")){
				response.setContentType("application/jpg");
			}else if(extension.equals(".jpeg") || extension.equals(".JPEG")){
				response.setContentType("application/jpeg");
			}else if(extension.equals(".png") || extension.equals(".PNG")){
				response.setContentType("application/png");
			}else if(extension.equals(".svg") || extension.equals(".SVG")){
				response.setContentType("application/svg");
			}else if(extension.equals(".gif") || extension.equals(".GIF")){
				response.setContentType("application/gif");
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Type Not Supported"));
			}
			response.setHeader("Content-Disposition", "inline;filename=\"" + name + "\"");
			ServletOutputStream outputStream = response.getOutputStream();
			byte[] bFile = new byte[(int)file.length()];
			FileInputStream fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
			ByteArrayInputStream inputStream = new ByteArrayInputStream(bFile);
			IOUtils.copy((InputStream)inputStream, (OutputStream)outputStream);
			outputStream.flush();
			outputStream.close();
			FacesContext.getCurrentInstance().responseComplete();
		}
		catch (IOException e) {
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Error" + e.getMessage()));
		}
	}



	public void selectCandidate(Candidate can){
		selectedCandidate = can;
		action = "CANDIDATEDETAILS";
		reSchedule = null;
		interviewSchedule = null;
		interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
	}


	public void rescheduleForm(InterviewSchedule i){
		reSchedule = new ReSchedule();
		reSchedule.setInterviewSchedule(i);
	}


	public void saveReschedule(){
		try{
			Integer id = reSchedule.getId();
			reScheduleService.save(reSchedule);
			SalesFollowUp sFollowup = new SalesFollowUp();
			sFollowup.setFollowUpDate(new Date());
			sFollowup.setFollowUpType("sales");
			sFollowup.setCandidate(reSchedule.getInterviewSchedule().getCandidate());
			sFollowup.setUserProfile(loginBean.getUserProfile());
			sFollowup.setInterviewSchedule(reSchedule.getInterviewSchedule());
			if(id == null){
				sFollowup.setFollowUpRemarks("Interview Re-Schedule Created");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Re-Schedule Successfully Saved"));
			}else{
				sFollowup.setFollowUpRemarks("Interview Re-Schedule Updated");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Re-Schedule Successfully Updated"));
			}
			salesFollowupService.save(sFollowup);
			interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Re-schedule not saved"));
		}
	}


	public void createinterviewSchedule(){
		action = "CREATEINTERVIEWSCHEDULE";
		interviewSchedule = new InterviewSchedule();
		interviewSchedule.setCandidate(selectedCandidate);
		interviewSchedule.setUserProfile(loginBean.getUserProfile());
	}


	public void saveInterviewSchedule(){
		try{
			Integer id = interviewSchedule.getId();
			interviewScheduleService.save(interviewSchedule);
			SalesFollowUp sFollowup = new SalesFollowUp();
			sFollowup.setFollowUpDate(new Date());
			sFollowup.setFollowUpType("sales");
			sFollowup.setCandidate(interviewSchedule.getCandidate());
			sFollowup.setUserProfile(loginBean.getUserProfile());
			sFollowup.setInterviewSchedule(interviewSchedule);
			if(id == null){
				sFollowup.setFollowUpRemarks("Interview Schedule Created");
			}else{
				sFollowup.setFollowUpRemarks("Interview Schedule Updated");
			}
			salesFollowupService.save(sFollowup);
			action = "CANDIDATEDETAILS";
			reSchedule = null;
			interviewSchedule = null;
			interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Interview Schedule Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Interview Schedule Not Saved"));
		}
	}


	public void backToCandidateDetails(){
		try{
			action = "CANDIDATEDETAILS";
			reSchedule = null;
			interviewSchedule = null;
			salesFollowUp = null;
			interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void onRowEdit(RowEditEvent event) {
		try{
			reScheduleService.save((ReSchedule)event.getObject());
			interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Edit Success"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Not Edit"));
		}
	}

	public void onRowCancel(RowEditEvent event) {
		try{
			interviewScheduleList = interviewScheduleService.findinterviewSchedules(selectedCandidate.getCandidateId());
		}catch(Exception e){
			e.getSuppressed();
		}
	}


	public void candidateDetails(){
		try{
			hotlistCandidates();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editInterviewSchedule(InterviewSchedule i){
		action = "CREATEINTERVIEWSCHEDULE";
		interviewSchedule = i;
		interviewSchedule.setCandidate(selectedCandidate);
		interviewSchedule.setUserProfile(loginBean.getUserProfile());
	}

	public void mailDetails(){
		try{
			action = "MAILDETAILS";
			mailDetails = null;
			mailHistory = mailDetailsService.findMailDetails(loginBean.hasRole("SALES MANAGER"),loginBean.getUserProfile().getUserId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void showMailMessage(MailDetails md){
		mailDetails = md;
	}


	public void reasonForm(){

		if(selectedCandidates != null && selectedCandidates.size() >0){
			reason = null;
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Selected"));
		}
	}


	public void removeCandidateFromHotlist(){
		String msg =null;
		try{
			if(!loginBean.hasRole("SALES MANAGER")){
				msg = "You are not authorized person";
				throw new Exception();
			}
			if(selectedCandidates != null && selectedCandidates.size() >0){
				for(Candidate c:selectedCandidates){
					c.setIsRemoveFromHotlist(true);
					c.setHotlistStatusPredefine("REMOVED");
					candidateService.saveCandidate(c);
					SalesFollowUp salesFollowUp = new SalesFollowUp();
					salesFollowUp.setCandidate(c);
					salesFollowUp.setUserProfile(loginBean.getUserProfile());
					salesFollowUp.setFollowUpRemarks(reason);
					salesFollowUp.setFollowUpDate(new Date());
					salesFollowUp.setFollowUpType("sales");
					salesFollowupService.save(salesFollowUp);
				}
				action = "HOTLISTCANDIDATE";
				selectedCandidate = null;
				interviewScheduleList = null;
				Set<Priority> p = userService.findPriority(loginBean.getUserProfile().getUserId());
				List<Priority> priorities = null;
				if(p != null && p.size() > 0){
					priorities = new ArrayList<Priority>();
					priorities.addAll(p);
				}
				hotlistCandidateLazyModel = new SalesLazyCandidateModel(candidateService,loginBean.hasRole("SALES MANAGER"),priorities);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Removed From Hotlist"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Selected"));
			}

		}catch(Exception e){
			e.printStackTrace();
			if(msg == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Removed"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:",msg));
			}

		}
	}


	public void removeCandidateListFromHotlist(){
		try{
			action = "REMOVECANDIDATE";
			selectedCandidates = null;
			Set<Priority> p = userService.findPriority(loginBean.getUserProfile().getUserId());
			List<Priority> priorities = null;
			if(p != null && p.size() > 0){
				priorities = new ArrayList<Priority>();
				priorities.addAll(p);
			}
			hotlistCandidates = candidateService.findRemoveCandidateFromHotlist(loginBean.hasRole("SALES MANAGER"),priorities);
		}catch(Exception e){
			e.printStackTrace();
		}
	}




	public void showAllDocuments(Candidate can){
		try{
			action = "ALLDOCUMENTS";
			documentsList = documentsService.findAllDocuments(can.getCandidateId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editInterviewReshedule(ReSchedule r){
		reSchedule = r;
	}

	public void priorotyForm(){
		try{
			if(loginBean.hasRole("SALES MANAGER")){
				action = "PRIORITYFORM";
				users = userService.findAllUserByCategory("sales");
				priorities = null;
				user = null;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editUserPriority(UserProfile u){
		try{
			user = u;
			Set<Priority> results = userService.findPriority(u.getUserId());
			priorities = new Integer[results.size()];	
			int i=0;
			for(Priority p:results){
				priorities[i]=p.getId();
				i++;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void resetPriority(){
		try{
			Set<Priority> prioritySet = new HashSet<Priority>(0);
			for(Integer id:priorities){
				Priority p = new Priority();
				p.setId(id);
				prioritySet.add(p);
			}
			user.setPriorities(prioritySet);
			userService.save(user);
			Set<Priority> result = userService.findPriority(user.getUserId());
			String priorityList = null;
			if(result != null && result.size()>0){
				for(Priority p:result){
					if(priorityList == null){
						priorityList = p.getPriority();
					}else{
						priorityList = priorityList+","+p.getPriority();
					}
				}
			}
			user.setPriority(priorityList);
			userService.save(user);
			action = "PRIORITYFORM";
			users = userService.findAllUserByCategory("sales");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Priority Successfully Reset"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Priority Successfully Not Reset"));
		}
	}


	public void addToHotlist(){
		String msg = null;
		try{
			if(!loginBean.hasRole("SALES MANAGER")){
				msg = "You are not authorized person";
				throw new Exception();
			}
			if(selectedCandidates != null && selectedCandidates.size() >0){
				for(Candidate c:selectedCandidates){
					c.setIsRemoveFromHotlist(false);
					c.setDateOnHotlist(new Date());
					c.setHotlistStatusPredefine("ON HOTLIST");
					candidateService.saveCandidate(c);
					SalesFollowUp salesFollowUp = new SalesFollowUp();
					salesFollowUp.setCandidate(c);
					salesFollowUp.setUserProfile(loginBean.getUserProfile());
					salesFollowUp.setFollowUpDate(new Date());
					salesFollowUp.setFollowUpRemarks("Candidate added to hotlist from remove list");
					salesFollowUp.setFollowUpType("sales");
					salesFollowupService.save(salesFollowUp);
				}
				action = "HOTLISTCANDIDATE";
				selectedCandidate = null;
				interviewScheduleList = null;
				selectedCandidates = null;
				documents = null;
				documentsList = null;
				Set<Priority> p = userService.findPriority(loginBean.getUserProfile().getUserId());
				List<Priority> priorities = null;
				if(p != null && p.size() > 0){
					priorities = new ArrayList<Priority>();
					priorities.addAll(p);
				}
				hotlistCandidateLazyModel = new SalesLazyCandidateModel(candidateService,loginBean.hasRole("SALES MANAGER"),priorities);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successully Added To Hotlist"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Selected"));
			}
		}catch(Exception e){
			e.printStackTrace();
			if(msg == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Added To Hotlist"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:",msg));
			}

		}
	}

	// mail Sending start


	public void mailForm(){
		if(selectedCandidates != null && selectedCandidates.size() >0){
			action = "MAILFORM";
			mailDetails = null;
			mailDto = new MailDto();
			mailDocuments = new ArrayList<UploadedFile>();
			mailFileName = new ArrayList<String>();
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			if(loginBean.hasRole("SALES MANAGER")){
				mailDto.setRole("SALES MANAGER");
			}else{
				mailDto.setRole("OTHERS");
			}

		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Selected"));
		}

	}



	public void mailDocumentsUpload(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			mailDocuments.add(uploadFile);
			mailFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	public void mailAttachedFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		mailFileName.remove(deletefile);
		Iterator<String> ite = mailFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = mailDocuments.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}



	public void mailSendToVendor(){
		try{

			List<String> filePathList = mailDto.getFilePathList();
			List<String> emailList = new ArrayList<String>(); 
			StringTokenizer st = new StringTokenizer(mailDto.getToEmail(),",");
			while(st.hasMoreTokens()){
				String email = st.nextToken();
				emailList.add(email.trim()); 
			}
			for(String toEmail:emailList){
				MailService mailService = new MailService();
				mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),toEmail,null,filePathList);
				Thread t = new Thread(mailService);
				t.start();
				for(Candidate c:selectedCandidates){
					MailDetails mailDetails = new MailDetails();
					mailDetails.setReceiverEmail(toEmail);
					mailDetails.setSenderEmail(mailDto.getUserName());
					mailDetails.setSubject(mailDto.getSubject());
					mailDetails.setBodyContent(mailDto.getMailMessage());
					mailDetails.setUserProfile(loginBean.getUserProfile());
					mailDetails.setCandidate(c);
					mailDetails.setCreatedDate(new Date());
					mailDetailsService.save(mailDetails);
				}
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Mail Successfully Sent"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Mail Successfully Not Sent"));
		}
	}



	public void chooseSubject(FacesContext context, UIComponent component, Object value){
		String val = (String) value;
		if(val.equals("Template 1")){
			mailDto.setSubject("Details of my candidates");
		}
		if(val.equals("Template 2")){
			mailDto.setSubject("Contact details and rate of the candidates");
		}
	}


	public void previewMessage() throws IOException{

		try{

			if(mailDto.getToEmail() != null){
				List<String> filePathList = null;
				if(documents.length > 0){
					filePathList = documentsService.findAttachDocuments(selectedCandidates, documents);
				}

				if(mailDocuments.size() > 0){
					if(filePathList == null){
						filePathList = new ArrayList<String>();
					}
					for(UploadedFile uploadFile:mailDocuments){
						uploadFile.getContents();
						byte[] bytes = uploadFile.getContents();
						String filename = FilenameUtils.getName(uploadFile.getFileName());
						String fileContent = uploadFile.getContentType();
						int index = filename.indexOf( '.' );
						String extension = filename.substring(index);
						ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
						String p = proprtiesFileDto.getAttachMailFileFolderPath()+"/"+filename;
						BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(p)));
						stream.write(bytes);
						stream.close();
						filePathList.add(p);
					}
				}

				if(filePathList != null && filePathList.size() >0){
					mailDto.setFilePathList(filePathList);
				}

				String msg = null;
				String createdMessage = null;
				if(mailDto.getTemplate().equals("Template 1")){
					createdMessage = MailMessageService.createMessageForTemplate1(selectedCandidates);
					createdMessage = createdMessage + "<br>Your quick response will be appreciated.</br><br>"+ loginBean.getUserProfile().getSignature();
					msg = appDataBean.getMailMessage().get("sales_template1");

				}
				if(mailDto.getTemplate().equals("Template 2")){
					createdMessage = MailMessageService.createMessageForTemplate2(selectedCandidates);
					createdMessage = createdMessage + "<br>Your quick response will be appreciated.</br><br>"+ loginBean.getUserProfile().getSignature();
					msg = appDataBean.getMailMessage().get("sales_template2");
				}
				msg = msg + "<br><br>"+createdMessage;

				mailDto.setMailMessage(msg);
				action = "MAILPREVIEWFORM";

			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Write Destination Email Id"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}



	public void backToMailForm(){
		action = "MAILFORM";
	}




	public void mailFormForCandidate(){
		if(selectedCandidates != null && selectedCandidates.size() >0){
			action = "MAILFORMFORCANDIDATE";
			mailDto = new MailDto();
			mailDocuments = new ArrayList<UploadedFile>();
			mailFileName = new ArrayList<String>();
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			if(loginBean.hasRole("SALES MANAGER")){
				mailDto.setRole("SALES MANAGER");
			}else{
				mailDto.setRole("OTHERS");
			}
		}else{
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Not Selected"));
		}
	}



	public void sendMessageToCandidate(){
		try{

			List<String> filePathList = null;
			if(documents.length > 0){
				filePathList = documentsService.findAttachDocuments(selectedCandidates, documents);
			}

			if(mailDocuments.size() > 0){
				if(filePathList == null){
					filePathList = new ArrayList<String>();
				}
				for(UploadedFile uploadFile:mailDocuments){
					uploadFile.getContents();
					byte[] bytes = uploadFile.getContents();
					String filename = FilenameUtils.getName(uploadFile.getFileName());
					String fileContent = uploadFile.getContentType();
					int index = filename.indexOf( '.' );
					String extension = filename.substring(index);
					ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
					String p = proprtiesFileDto.getAttachMailFileFolderPath()+"/"+filename;
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(p)));
					stream.write(bytes);
					stream.close();
					filePathList.add(p);
				}
			}

			for(Candidate can:selectedCandidates){
				MailService mailService = new MailService();
				mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),can.getEmailId(),null,filePathList);
				Thread t = new Thread(mailService);
				t.start();
			}
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Mail Successfully Sent"));
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void scheduleCandidates(){
		try{
			action = "SCHEDULEDCANDIDATE";
			scheduledCandidateLazyModel = new ScheduledCandidateLazyModel(interviewScheduleService,loginBean.hasRole("SALES MANAGER"),loginBean.getUserProfile().getUserId());
			selectedinterviewSchedules = null;
			mailDetails = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void showScheduleFeedback(String msg){
		mailDetails = new MailDetails();
		mailDetails.setBodyContent(msg);
	}



	public void selectScheduledCandidate(InterviewSchedule i){
		
		try{
			action = "INTERVIEWSCHEDULEDETAILS";
			interviewSchedule = interviewScheduleService.findinterviewSchedulesbyId(i.getId());
			salesFollowupList = salesFollowupService.findSalesFollowupByScheduleId(i.getId());
			salesFollowUp = new SalesFollowUp();
			salesFollowUp.setCandidate(i.getCandidate());
			salesFollowUp.setUserProfile(loginBean.getUserProfile());
			salesFollowUp.setInterviewSchedule(i);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void editInterviewScheduleInDetailsPage(InterviewSchedule i){
		action = "INTERVIEWSCHEDULEEDITINDETAILSPAGE";
		interviewSchedule = i;
		interviewSchedule.setUserProfile(loginBean.getUserProfile());
	}
	

	public void saveInterviewScheduleInDetailsPage(){
		try{
			interviewScheduleService.save(interviewSchedule);
			SalesFollowUp sFollowup = new SalesFollowUp();
			sFollowup.setFollowUpDate(new Date());
			sFollowup.setFollowUpType("sales");
			sFollowup.setCandidate(interviewSchedule.getCandidate());
			sFollowup.setUserProfile(loginBean.getUserProfile());
			sFollowup.setInterviewSchedule(interviewSchedule);
			sFollowup.setFollowUpRemarks("Interview Schedule Updated");
			salesFollowupService.save(sFollowup);
			action = "INTERVIEWSCHEDULEDETAILS";
			interviewSchedule = interviewScheduleService.findinterviewSchedulesbyId(interviewSchedule.getId());
			salesFollowupList = salesFollowupService.findSalesFollowupByScheduleId(interviewSchedule.getId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Interview Schedule Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Interview Schedule Not Updated"));
		}
	}
	
	
	public void rescheduleFormInDetailsPage(InterviewSchedule i){
		reSchedule = new ReSchedule();
		reSchedule.setInterviewSchedule(i);
	}
	
	
	public void saveRescheduleForDetailsPage(){
		try{
			Integer id = reSchedule.getId();
			reScheduleService.save(reSchedule);
			SalesFollowUp sFollowup = new SalesFollowUp();
			sFollowup.setFollowUpDate(new Date());
			sFollowup.setFollowUpType("sales");
			sFollowup.setCandidate(reSchedule.getInterviewSchedule().getCandidate());
			sFollowup.setUserProfile(loginBean.getUserProfile());
			sFollowup.setInterviewSchedule(interviewSchedule);
			if(id == null){
				sFollowup.setFollowUpRemarks("Interview Re-Schedule Created");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Re-Schedule Successfully Saved"));
			}else{
				sFollowup.setFollowUpRemarks("Interview Re-Schedule Updated");
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Re-Schedule Successfully Updated"));
			}
			salesFollowupService.save(sFollowup);
			interviewSchedule = interviewScheduleService.findinterviewSchedulesbyId(interviewSchedule.getId());
			salesFollowupList = salesFollowupService.findSalesFollowupByScheduleId(reSchedule.getInterviewSchedule().getId());
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Re-schedule not saved"));
		}
	}
	
	
	public void editInterviewResheduleInDetailsPage(ReSchedule r){
		reSchedule = r;
	}
	
	
	
	public void saveSalesFollowup(){
		try{
			salesFollowUp.setFollowUpDate(new Date());
			salesFollowUp.setFollowUpType("sales");
			salesFollowupService.save(salesFollowUp);
			salesFollowupList = salesFollowupService.findSalesFollowupByScheduleId(salesFollowUp.getInterviewSchedule().getId());
			salesFollowUp.setFollowUpRemarks(null);
			salesFollowUp.setNextFollowUpDate(null);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Followup Sucessfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Followup Not Saved"));
		}
	}
	
	
	
	public void showAllDocumentsForScheduledCandidate(Candidate can){
		try{
			action = "ALLDOCUMENTSFORSCHEDULEDCANDIDATE";
			documentsList = documentsService.findAllDocuments(can.getCandidateId());
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public void backToScheduledCandidateList(){
		try{
			action = "SCHEDULEDCANDIDATE";
			scheduledCandidateLazyModel = new ScheduledCandidateLazyModel(interviewScheduleService,loginBean.hasRole("SALES MANAGER"),loginBean.getUserProfile().getUserId());
			selectedinterviewSchedules = null;
			mailDetails = null;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	public long hotlistCounter(Candidate can){
		long diff = new Date().getTime() - can.getDateOnHotlist().getTime();
		return diff / 1000 / 60 / 60 / 24;
	}
	
	public String enrollmentCounter(Candidate can){
		if(can.getEnrollmentDate() !=null){
			Long diff = new Date().getTime() - can.getEnrollmentDate().getTime();
			diff = diff / 1000 / 60 / 60 / 24;
			return diff.toString();
		}
		return "Enrollment Date Empty";
	}
	


	//setter and getter

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}


	public UserService getUserService() {
		return userService;
	}


	public void setUserService(UserService userService) {
		this.userService = userService;
	}


	public LoginBean getLoginBean() {
		return loginBean;
	}


	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}


	public CandidateService getCandidateService() {
		return candidateService;
	}


	public void setCandidateService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}


	public List<Candidate> getHotlistCandidates() {
		return hotlistCandidates;
	}


	public void setHotlistCandidates(List<Candidate> hotlistCandidates) {
		this.hotlistCandidates = hotlistCandidates;
	}


	public Candidate getSelectedCandidate() {
		return selectedCandidate;
	}


	public void setSelectedCandidate(Candidate selectedCandidate) {
		this.selectedCandidate = selectedCandidate;
	}


	public InterviewScheduleService getInterviewScheduleService() {
		return interviewScheduleService;
	}


	public void setInterviewScheduleService(InterviewScheduleService interviewScheduleService) {
		this.interviewScheduleService = interviewScheduleService;
	}


	public List<InterviewSchedule> getInterviewScheduleList() {
		return interviewScheduleList;
	}


	public void setInterviewScheduleList(List<InterviewSchedule> interviewScheduleList) {
		this.interviewScheduleList = interviewScheduleList;
	}


	public ReSchedule getReSchedule() {
		return reSchedule;
	}


	public void setReSchedule(ReSchedule reSchedule) {
		this.reSchedule = reSchedule;
	}


	public ReScheduleService getReScheduleService() {
		return reScheduleService;
	}


	public void setReScheduleService(ReScheduleService reScheduleService) {
		this.reScheduleService = reScheduleService;
	}


	public InterviewSchedule getInterviewSchedule() {
		return interviewSchedule;
	}


	public void setInterviewSchedule(InterviewSchedule interviewSchedule) {
		this.interviewSchedule = interviewSchedule;
	}


	public SalesFollowUp getSalesFollowUp() {
		return salesFollowUp;
	}


	public void setSalesFollowUp(SalesFollowUp salesFollowUp) {
		this.salesFollowUp = salesFollowUp;
	}


	public SalesFollowupService getSalesFollowupService() {
		return salesFollowupService;
	}


	public void setSalesFollowupService(SalesFollowupService salesFollowupService) {
		this.salesFollowupService = salesFollowupService;
	}


	public List<SalesFollowUp> getSalesFollowupList() {
		return salesFollowupList;
	}


	public void setSalesFollowupList(List<SalesFollowUp> salesFollowupList) {
		this.salesFollowupList = salesFollowupList;
	}


	public MailDto getMailDto() {
		return mailDto;
	}


	public void setMailDto(MailDto mailDto) {
		this.mailDto = mailDto;
	}


	public MailDetailsService getMailDetailsService() {
		return mailDetailsService;
	}


	public void setMailDetailsService(MailDetailsService mailDetailsService) {
		this.mailDetailsService = mailDetailsService;
	}


	public List<MailDetails> getMailHistory() {
		return mailHistory;
	}


	public void setMailHistory(List<MailDetails> mailHistory) {
		this.mailHistory = mailHistory;
	}


	public MailDetails getMailDetails() {
		return mailDetails;
	}


	public void setMailDetails(MailDetails mailDetails) {
		this.mailDetails = mailDetails;
	}


	public List<Candidate> getSelectedCandidates() {
		return selectedCandidates;
	}


	public void setSelectedCandidates(List<Candidate> selectedCandidates) {
		this.selectedCandidates = selectedCandidates;
	}


	public String[] getDocuments() {
		return documents;
	}


	public void setDocuments(String[] documents) {
		this.documents = documents;
	}


	public List<DocumentsModel> getDocumentsList() {
		return documentsList;
	}


	public void setDocumentsList(List<DocumentsModel> documentsList) {
		this.documentsList = documentsList;
	}


	public DocumentsService getDocumentsService() {
		return documentsService;
	}


	public void setDocumentsService(DocumentsService documentsService) {
		this.documentsService = documentsService;
	}


	public List<UploadedFile> getMailDocuments() {
		return mailDocuments;
	}


	public void setMailDocuments(List<UploadedFile> mailDocuments) {
		this.mailDocuments = mailDocuments;
	}


	public List<String> getMailFileName() {
		return mailFileName;
	}


	public void setMailFileName(List<String> mailFileName) {
		this.mailFileName = mailFileName;
	}


	public List<UserProfile> getUsers() {
		return users;
	}


	public void setUsers(List<UserProfile> users) {
		this.users = users;
	}


	public Integer[] getPriorities() {
		return priorities;
	}


	public void setPriorities(Integer[] priorities) {
		this.priorities = priorities;
	}


	public UserProfile getUser() {
		return user;
	}


	public void setUser(UserProfile user) {
		this.user = user;
	}


	public String getReason() {
		return reason;
	}


	public void setReason(String reason) {
		this.reason = reason;
	}


	public AppDataBean getAppDataBean() {
		return appDataBean;
	}


	public void setAppDataBean(AppDataBean appDataBean) {
		this.appDataBean = appDataBean;
	}


	public LazyDataModel<Candidate> getHotlistCandidateLazyModel() {
		return hotlistCandidateLazyModel;
	}


	public void setHotlistCandidateLazyModel(LazyDataModel<Candidate> hotlistCandidateLazyModel) {
		this.hotlistCandidateLazyModel = hotlistCandidateLazyModel;
	}


	public List<InterviewSchedule> getSelectedinterviewSchedules() {
		return selectedinterviewSchedules;
	}


	public void setSelectedinterviewSchedules(List<InterviewSchedule> selectedinterviewSchedules) {
		this.selectedinterviewSchedules = selectedinterviewSchedules;
	}


	public LazyDataModel<InterviewSchedule> getScheduledCandidateLazyModel() {
		return scheduledCandidateLazyModel;
	}


	public void setScheduledCandidateLazyModel(LazyDataModel<InterviewSchedule> scheduledCandidateLazyModel) {
		this.scheduledCandidateLazyModel = scheduledCandidateLazyModel;
	}
	
	
	

}