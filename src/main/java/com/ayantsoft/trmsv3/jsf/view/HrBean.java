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
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.UploadedFile;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Course;
import com.ayantsoft.trmsv3.hibernate.pojo.Documents;
import com.ayantsoft.trmsv3.hibernate.pojo.EnrollmentFormNo;
import com.ayantsoft.trmsv3.hibernate.pojo.HrFollowUp;
import com.ayantsoft.trmsv3.hibernate.pojo.MailTemplate;
import com.ayantsoft.trmsv3.hibernate.pojo.PaymentTransaction;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.pojo.Reference;
import com.ayantsoft.trmsv3.hibernate.pojo.Visa;
import com.ayantsoft.trmsv3.jsf.model.DocumentsModel;
import com.ayantsoft.trmsv3.jsf.model.LazyCandidateDataModel;
import com.ayantsoft.trmsv3.jsf.model.LazyTerminatedCandidateModel;
import com.ayantsoft.trmsv3.service.CandidateService;
import com.ayantsoft.trmsv3.service.CourseService;
import com.ayantsoft.trmsv3.service.DirectoryCreationService;
import com.ayantsoft.trmsv3.service.DocumentsService;
import com.ayantsoft.trmsv3.service.EnrollmentFormNoService;
import com.ayantsoft.trmsv3.service.HrFollowupService;
import com.ayantsoft.trmsv3.service.IncentiveStructureService;
import com.ayantsoft.trmsv3.service.MailService;
import com.ayantsoft.trmsv3.service.MailTemplateService;
import com.ayantsoft.trmsv3.service.PaymentTransactionService;
import com.ayantsoft.trmsv3.service.PdfGenerator;
import com.ayantsoft.trmsv3.service.ReferenceService;
import com.ayantsoft.trmsv3.service.VisaService;
import com.ayantsoft.trmsv3.util.CalenderUtil;
import com.ayantsoft.trmsv3.util.CandidateUtil;
import com.ayantsoft.trmsv3.util.FileHandleUtil;
import com.ayantsoft.trmsv3.util.MailDto;
import com.ayantsoft.trmsv3.util.ProprtiesFileDto;
import com.ayantsoft.trmsv3.util.SoaURISignature;
import com.ayantsoft.trmsv3rest.util.JerseyClientUtil;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.GenericType;
import com.sun.jersey.api.client.WebResource;

@ManagedBean
@ViewScoped
public class HrBean implements Serializable {


	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 8221454991212694058L;

	private String action;
	private String importBy;
	private String importValue;
	private List<CandidateUtil> candidateUtils;
	private CandidateUtil selectedCandidateUtil;
	private LazyDataModel<Candidate> candidateLazyModel;
	private LazyDataModel<Candidate> terminatedCandidateLazyModel;
	private Candidate selectedCandidate;
	private Candidate candidate;
	private boolean enrollmentEditPanel = false;
	private boolean enrollmentLabelPanel = true;
	private FileHandleUtil enrollmentFileHandleUtil; 
	private FileHandleUtil sendingEnrollmentFileHandleUtil;
	private FileHandleUtil receivingEnrollmentFileHandleUtil;
	private List<UploadedFile> sendingEnrollmentDocument; 
	private List<String> sendingEnrollmentFileName ;
	private List<UploadedFile> receivingEnrollmentDocument;
	private List<String> receivingEnrollmentFileName ;
	private HrFollowUp hrFollowup;
	private List<HrFollowUp> enrollmentFollowUpList;
	private List<HrFollowUp> allHrFollowuList;
	private boolean trainingEditPanel = false;
	private boolean trainingLabelPanel = true; 
	private FileHandleUtil trainingFileHandleUtil; 
	private List<UploadedFile> sendingTrainingDocument; 
	private List<String> sendingTrainingFileName ;
	private FileHandleUtil sendingTrainingFileHandleUtil;
	private List<UploadedFile> receivingTrainingDocument;
	private List<String> receivingTrainingFileName ;
	private FileHandleUtil receivingTrainingFileHandleUtil;
	private List<HrFollowUp> trainingFollowUpList;
	private boolean resumeEditPanel = false;
	private boolean resumeLabelPanel = true;
	private List<UploadedFile> sendingResumeDocument;
	private List<String> sendingresumeFileName;
	private FileHandleUtil receivingResumeFileHandleUtil;
	private List<HrFollowUp> resumeFollowUpList;
	private boolean mockAndGraduationEditPanel = false;
	private boolean mockAndGraduationLabelPanel = true;
	private List<HrFollowUp> mockAndGraduationFollowUpList;
	private FileHandleUtil psrFileHandleUtil;
	private FileHandleUtil questionerFileHandleUtil;
	private List<UploadedFile> sendingPSRDocument; 
	private List<String> sendingPSRFileName ;
	private List<UploadedFile> receivingQuestionDocument; 
	private List<String> receivingQuestionFileName ;
	private FileHandleUtil sendingPSRFileHandleUtil;
	private FileHandleUtil receivingQuestionFileHandleUtil;
	private boolean preHotlistEditPanel = false;
	private boolean preHotlistLabelPanel = true;
	private List<HrFollowUp> preHotlistFollowUpList;
	private Integer priorities[];
	private String priorityList;
	private boolean referenceEditPanel = false;
	private boolean referenceLabelPanel = true;
	private List<Reference> references;
	private Reference reference;
	private List<HrFollowUp> referenceFollowUpList;
	private FileHandleUtil referenceCheckFileHandleUtil;
	private List<UploadedFile> sendingReferenceDocument;
	private List<String> sendingReferenceFileName;
	private FileHandleUtil receivingReferenceFileHandleUtil;
	private List<UploadedFile> othersDocument; 
	private List<String> otherDocumentsFileName ;
	private String fileDocumentsName;
	private List<DocumentsModel> documentsList;
	private MailDto mailDto;
	private List<PaymentTransaction> paymentTransactionList;
	private PaymentTransaction paymentTransaction;
	private List<Candidate> preHotlistCandidates;
	private String selectedOption;
	private boolean weekWise;
	private boolean monthWise;
	private boolean skillWise;
	private boolean visaWise;
	private String optionValue;
	private Date searcingDate;
	private boolean weekrangeSelector;
	private List<String> weeks;
	private boolean batchWise;
	private List<PaymentTransaction> paymentDetails;
	private boolean serviceWise;
	private boolean yearWise;
	private boolean dayWise;
	private List<Candidate> sportIncentiveCandidate;
	private boolean recruiterWise;
	private List<PaymentTransaction> paymentForTeamLeader;
	private List<Candidate> trainingCandidate;
	private List<Candidate> resumeCandidate;
	private List<Candidate> mockCandidates;
	private String contentOption;
	private String contentValue;


	@ManagedProperty("#{candidateService}")
	private CandidateService candidateService;

	@ManagedProperty("#{hrFollowupService}")
	private HrFollowupService hrFollowupService;

	@ManagedProperty("#{loginBean}")
	private LoginBean loginBean;

	@ManagedProperty("#{appDataBean}")
	private AppDataBean appDataBean;

	@ManagedProperty("#{documentsService}")
	private DocumentsService documentsService;

	@ManagedProperty("#{referenceService}")
	private ReferenceService referenceService;

	@ManagedProperty("#{paymentTransactionService}")
	private PaymentTransactionService paymentTransactionService;

	@ManagedProperty("#{enrollmentFormNoService}")
	private EnrollmentFormNoService enrollmentFormNoService;

	@ManagedProperty("#{incentiveStructureService}")
	private IncentiveStructureService incentiveStructureService;

	@ManagedProperty("#{mailTemplateService}")
	private MailTemplateService mailTemplateService;

	@ManagedProperty("#{courseService}")
	private CourseService courseService;

	@ManagedProperty("#{visaService}")
	private VisaService visaService;


	public void importCandidate(){
		action = "IMPORTCANDIDATE";
		importBy = null;
		importValue = null;
		candidateUtils = null;
		selectedCandidateUtil = null;
		candidateLazyModel = null;
		selectedCandidate = null;
	}

	public void searchImportCandidate(){
		try{
			ClientResponse clientResponse = null;
			Client client = JerseyClientUtil.getClientConfig();
			WebResource.Builder builder = JerseyClientUtil.getWebResourceBuilder(client,SoaURISignature.SOA_SERVICE_URI+"/hr/candidates/"+importBy.trim()+"/"+importValue.trim());
			clientResponse = builder.get(ClientResponse.class);
			if (clientResponse.getStatus() == 200) {
				candidateUtils = clientResponse.getEntity(new GenericType<List<CandidateUtil>>(){});
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void importCandidateInhrModule(){
		try{
			if(!candidateService.checkCandidate(selectedCandidateUtil.getCandidateWorkMobile(),selectedCandidateUtil.getCandidateEmail(),selectedCandidateUtil.getCandidateId())){
				int id = 0;
				if(selectedCandidateUtil != null){
					action = "LISTCANDIDATE";
					Candidate candidate = new Candidate();
					if(selectedCandidateUtil.getCandidateId() != null){
						candidate.setCandidateId(selectedCandidateUtil.getCandidateId());
						id = selectedCandidateUtil.getCandidateId();
					}
					if(selectedCandidateUtil.getCandidateName() != null){
						candidate.setCandidateName(selectedCandidateUtil.getCandidateName().trim());
					}
					if(selectedCandidateUtil.getCandidateEmail() != null){
						candidate.setEmailId(selectedCandidateUtil.getCandidateEmail().trim());
					}
					if(selectedCandidateUtil.getCandidateWorkMobile() != null){
						candidate.setContactNo(selectedCandidateUtil.getCandidateWorkMobile().replaceAll("[^0-9]+",""));
					}
					if(selectedCandidateUtil.getCandidateCurrentLocation() != null){
						candidate.setCurrentLoc(selectedCandidateUtil.getCandidateCurrentLocation());
					}
					if(selectedCandidateUtil.getCandidatePreferredLocation() != null){
						candidate.setPreferredLoc(selectedCandidateUtil.getCandidatePreferredLocation());
					}
					if(selectedCandidateUtil.getCandidateVisa() != null){
						candidate.setVisaStory(selectedCandidateUtil.getCandidateVisa());
					}
					if(selectedCandidateUtil.getTotalAmount() != null){
						candidate.setTotalAmount(new BigDecimal(selectedCandidateUtil.getTotalAmount()));
					}
					if(selectedCandidateUtil.getRecruiterName() != null){
						candidate.setRecruiterName(selectedCandidateUtil.getRecruiterName());
					}
					if(selectedCandidateUtil.getSupervisorName() != null){
						candidate.setSupervisorName(selectedCandidateUtil.getSupervisorName());
					}
					if(selectedCandidateUtil.getEnrollmentDate() != null){
						candidate.setEnrollmentDate(selectedCandidateUtil.getEnrollmentDate());
					}else{
						candidate.setSupervisorName("Team Leader");
					}

					candidate.setIncentivePaidStatus("DECISION PENDING");
					candidate.setEnrollmentFormStatus("NOT RECEIVED");
					candidate.setHotlistStatusPredefine("ON BENCH");
					candidate.setImportedDate(new Date());
					candidate.setReferenceCheckStatus(false);
					candidateService.saveCandidate(candidate);
					Candidate can = new Candidate();
					can.setCandidateId(id);
					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(can);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Candidate imported");
					hrFollowUp.setFollowUpType("enrollment");
					hrFollowupService.save(hrFollowUp);

					candidateLazyModel = new LazyCandidateDataModel(candidateService);

					if(id >0){
						ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();	
						DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+id);
					}
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Imported"));
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","No Candidate Selected"));
				}	
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","This Candidate Already Exist"));
			}
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Imported"));
		}

	}

	public void listCandidate(){
		action = "LISTCANDIDATE";
		try{
			enrollmentEditPanel = false;
			enrollmentLabelPanel = true;
			trainingEditPanel = false;
			trainingLabelPanel = true;
			resumeEditPanel = false;
			resumeLabelPanel = true;
			mockAndGraduationEditPanel = false;
			mockAndGraduationLabelPanel = true;
			preHotlistEditPanel = false;
			preHotlistLabelPanel = true;
			referenceEditPanel = false;
			reference = null;
			references = null;
			referenceLabelPanel = true;
			enrollmentFileHandleUtil = null;
			psrFileHandleUtil = null;
			questionerFileHandleUtil = null;
			referenceCheckFileHandleUtil = null;
			trainingFileHandleUtil = null;
			sendingEnrollmentFileHandleUtil = null;
			sendingPSRFileHandleUtil = null;
			receivingQuestionFileHandleUtil = null;
			sendingTrainingFileHandleUtil = null;
			receivingEnrollmentFileHandleUtil = null;
			receivingTrainingFileHandleUtil = null;
			receivingResumeFileHandleUtil = null;
			receivingReferenceFileHandleUtil = null;
			sendingEnrollmentDocument = null;
			sendingEnrollmentFileName = null;
			othersDocument = null;
			otherDocumentsFileName = null;
			fileDocumentsName = null;
			documentsList = null;
			sendingPSRDocument = null; 
			sendingPSRFileName = null;
			receivingQuestionDocument = null;
			receivingQuestionFileName = null;
			sendingTrainingDocument = null;
			sendingResumeDocument = null;
			sendingresumeFileName = null;
			sendingReferenceDocument = null;
			sendingReferenceFileName = null;
			sendingTrainingFileName = null;
			receivingEnrollmentDocument = null;
			receivingEnrollmentFileName = null;
			receivingTrainingDocument = null;
			receivingTrainingFileName = null;
			hrFollowup = null;
			enrollmentFollowUpList = null;
			trainingFollowUpList = null;
			resumeFollowUpList = null;
			mockAndGraduationFollowUpList = null;
			preHotlistFollowUpList = null;
			priorityList = null;
			priorities = null;
			referenceFollowUpList = null;
			allHrFollowuList = null;
			mailDto = null;
			paymentTransactionList = null;
			preHotlistCandidates = null;
			paymentTransaction = null;
			candidateLazyModel = new LazyCandidateDataModel(candidateService);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void onRowSelectInCandidateList(SelectEvent event){
		try{
			action = "CANDIDATEDETAILS";
			enrollmentFileHandleUtil = null;
			psrFileHandleUtil = null;
			questionerFileHandleUtil = null;
			referenceCheckFileHandleUtil = null;
			trainingFileHandleUtil = null;
			sendingEnrollmentFileHandleUtil = null;
			sendingPSRFileHandleUtil = null;
			receivingQuestionFileHandleUtil = null;
			sendingTrainingFileHandleUtil = null;
			receivingEnrollmentFileHandleUtil = null;
			receivingTrainingFileHandleUtil = null;
			references = null;
			reference = null;
			priorities = null;
			fileDocumentsName = null;
			documentsList = null;
			receivingResumeFileHandleUtil = null;
			receivingReferenceFileHandleUtil = null;
			priorityList = null;
			mailDto = null;
			paymentTransactionList = null;
			preHotlistCandidates = null;
			paymentTransaction = null;
			sendingEnrollmentDocument = new ArrayList<UploadedFile>();
			sendingEnrollmentFileName = new ArrayList<String>();
			othersDocument = new ArrayList<UploadedFile>();
			otherDocumentsFileName = new ArrayList<String>();
			sendingPSRDocument = new ArrayList<UploadedFile>(); 
			sendingPSRFileName = new ArrayList<String>();
			receivingQuestionDocument = new ArrayList<UploadedFile>();  
			receivingQuestionFileName = new ArrayList<String>();
			sendingTrainingDocument = new ArrayList<UploadedFile>();
			sendingTrainingFileName = new ArrayList<String>();
			sendingResumeDocument = new ArrayList<UploadedFile>();
			sendingresumeFileName = new ArrayList<String>();
			sendingReferenceDocument = new ArrayList<UploadedFile>();
			sendingReferenceFileName = new ArrayList<String>();
			receivingEnrollmentDocument = new ArrayList<UploadedFile>();
			receivingEnrollmentFileName = new ArrayList<String>();
			receivingTrainingDocument = new ArrayList<UploadedFile>(); 
			receivingTrainingFileName = new ArrayList<String>();
			hrFollowup = new HrFollowUp();
			candidate = candidateService.findCandidateById(selectedCandidate.getCandidateId());
			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
			resumeFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"resume");
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			preHotlistFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"prehotlist");
			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			Documents doc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","enrollment");
			if(doc != null){
				enrollmentFileHandleUtil = new FileHandleUtil();
				enrollmentFileHandleUtil.setFilePath(doc.getFilePath());
				enrollmentFileHandleUtil.setFileName(doc.getFileName());
				enrollmentFileHandleUtil.setFileExtension(doc.getExtension());
			}
			Documents sef = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","enrollment");
			if(sef != null){
				sendingEnrollmentFileHandleUtil = new FileHandleUtil();
				sendingEnrollmentFileHandleUtil.setFilePath(sef.getFilePath());
				sendingEnrollmentFileHandleUtil.setFileName(sef.getFileName());
				sendingEnrollmentFileHandleUtil.setFileExtension(sef.getExtension());
			}
			Documents ref = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","enrollment");
			if(ref != null){
				receivingEnrollmentFileHandleUtil = new FileHandleUtil();
				receivingEnrollmentFileHandleUtil.setFilePath(ref.getFilePath());
				receivingEnrollmentFileHandleUtil.setFileName(ref.getFileName());
				receivingEnrollmentFileHandleUtil.setFileExtension(ref.getExtension());
			}

			Documents tdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","training");
			if(tdoc != null){
				trainingFileHandleUtil = new FileHandleUtil();
				trainingFileHandleUtil.setFilePath(tdoc.getFilePath());
				trainingFileHandleUtil.setFileName(tdoc.getFileName());
				trainingFileHandleUtil.setFileExtension(tdoc.getExtension());
			}

			Documents stff = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","training");
			if(stff != null){
				sendingTrainingFileHandleUtil = new FileHandleUtil();
				sendingTrainingFileHandleUtil.setFilePath(stff.getFilePath());
				sendingTrainingFileHandleUtil.setFileName(stff.getFileName());
				sendingTrainingFileHandleUtil.setFileExtension(stff.getExtension());
			}

			Documents rtfbf = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","training");
			if(rtfbf != null){
				receivingTrainingFileHandleUtil = new FileHandleUtil();
				receivingTrainingFileHandleUtil.setFilePath(rtfbf.getFilePath());
				receivingTrainingFileHandleUtil.setFileName(rtfbf.getFileName());
				receivingTrainingFileHandleUtil.setFileExtension(rtfbf.getExtension());
			}
			Documents rresume = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","resume");
			if(rresume != null){
				receivingResumeFileHandleUtil = new FileHandleUtil();
				receivingResumeFileHandleUtil.setFilePath(rresume.getFilePath());
				receivingResumeFileHandleUtil.setFileName(rresume.getFileName());
				receivingResumeFileHandleUtil.setFileExtension(rresume.getExtension());
			}

			Documents referece = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","reference","Reference Check form");
			if(referece != null){
				referenceCheckFileHandleUtil = new FileHandleUtil();
				referenceCheckFileHandleUtil.setFilePath(referece.getFilePath());
				referenceCheckFileHandleUtil.setFileName(referece.getFileName());
				referenceCheckFileHandleUtil.setFileExtension(referece.getExtension());
			}
			Documents rreference = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","reference","Reference Check form");
			if(rreference != null){
				receivingReferenceFileHandleUtil = new FileHandleUtil();
				receivingReferenceFileHandleUtil.setFilePath(rreference.getFilePath());
				receivingReferenceFileHandleUtil.setFileName(rreference.getFileName());
				receivingReferenceFileHandleUtil.setFileExtension(rreference.getExtension());
			}


			Documents psrDocument = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","PSR form");
			if(psrDocument != null){
				psrFileHandleUtil = new FileHandleUtil();
				psrFileHandleUtil.setFilePath(psrDocument.getFilePath());
				psrFileHandleUtil.setFileName(psrDocument.getFileName());
				psrFileHandleUtil.setFileExtension(psrDocument.getExtension());
			}
			Documents questionDocument = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","QUESTIONER form");
			if(questionDocument != null){
				questionerFileHandleUtil = new FileHandleUtil();
				questionerFileHandleUtil.setFilePath(questionDocument.getFilePath());
				questionerFileHandleUtil.setFileName(questionDocument.getFileName());
				questionerFileHandleUtil.setFileExtension(questionDocument.getExtension());
			}

			Documents rpsr = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","PSR form");
			if(rpsr != null){
				sendingPSRFileHandleUtil = new FileHandleUtil();
				sendingPSRFileHandleUtil.setFilePath(rpsr.getFilePath());
				sendingPSRFileHandleUtil.setFileName(rpsr.getFileName());
				sendingPSRFileHandleUtil.setFileExtension(rpsr.getExtension());
			}
			Documents rquetion = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","QUESTIONER form");
			if(rquetion != null){
				receivingQuestionFileHandleUtil = new FileHandleUtil();
				receivingQuestionFileHandleUtil.setFilePath(rquetion.getFilePath());
				receivingQuestionFileHandleUtil.setFileName(rquetion.getFileName());
				receivingQuestionFileHandleUtil.setFileExtension(rquetion.getExtension());
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void enrollmentDetails(){
		try{
			if(candidate != null){
				action = "ENROLLMENTDETAILS";
				enrollmentEditPanel = false;
				enrollmentLabelPanel = true;
				paymentTransactionList = paymentTransactionService.findPaymentTransactionByCandidateId(candidate.getCandidateId());
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editEmrollmentDetails(){
		enrollmentEditPanel = true;
		enrollmentLabelPanel = false;
	}

	public void saveEmrollmentDetails(){
		try{
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in enrollment phase");
			hrFollowUp.setFollowUpType("enrollment");
			hrFollowupService.save(hrFollowUp);
			enrollmentEditPanel = false;
			enrollmentLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}


	public void generateEnrollmentForm(){
		String errorMessage = "The following information is not present - : ";
		boolean error = false;
		PaymentTransaction txn = null;
		BigDecimal paidAmount = null;
		try{

			if(candidate.getPrimarySkill() == null){
				errorMessage = errorMessage+"Course Name, ";
				error = true;
			}
			if(candidate.getEnrollmentDate() == null){
				errorMessage = errorMessage+"Date Of Enrollment, ";
				error = true;
			}
			if(candidate.getTentativeTrainingStartDate() == null){
				errorMessage = errorMessage+"Tentative Training Start Date, ";
				error = true;
			}

			List<PaymentTransaction> result = paymentTransactionService.findPaymentTransactionByCandidateId(candidate.getCandidateId());
			if(result != null && result.size() >0){
				txn = result.get(0);
				paidAmount = paymentTransactionService.findTotalPaidAmount(candidate.getCandidateId());
			}else{
				errorMessage = errorMessage+"Total Amount, Payment Date,Paid Amount,Transaction Id,Payment Mode.";
				error = true;
			}

			if(error){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:",errorMessage));
			}else{

				Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","enrollment");
				if(reviousdoc != null){
					documentsService.deleteDocument(reviousdoc);
					File file = new File(reviousdoc.getFilePath());
					file.delete();
					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(candidate);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Generated Enrollment Form Deleted For Again Generation");
					hrFollowUp.setFollowUpType("enrollment");
					hrFollowupService.save(hrFollowUp);
				}


				ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();	
				boolean isCreate = DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
				if(isCreate){
					String folderPathWithFIle = proprtiesFileDto.getEnrollmentFormFolderPath()+"/"+"Candidate_"+candidate.getCandidateId()+"/generate/enrollment_form/"+candidate.getCandidateName()+"_enrollment_form.pdf";
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					Long enrollmentFormNo = enrollmentFormNoService.findMaximumEnrollmentNo();

					if(txn.getTotalAmount().compareTo(txn.getPaidAmount()) == 0 ?true:false){
						PdfGenerator.createenrollmentFormPdf(Calendar.getInstance().get(Calendar.YEAR),enrollmentFormNo+1,candidate.getPrimarySkill(),df.format(candidate.getEnrollmentDate()),df.format(candidate.getTentativeTrainingStartDate()),txn.getTotalAmount(),df.format(txn.getPaymentDate()),txn.getTotalAmount(),txn.getPaidAmount(),txn.getTransactionId(),"N/A","N/A",txn.getPaymentMode(),folderPathWithFIle);
					}else{
						PdfGenerator.createenrollmentFormPdf(Calendar.getInstance().get(Calendar.YEAR),enrollmentFormNo+1,candidate.getPrimarySkill(),df.format(candidate.getEnrollmentDate()),df.format(candidate.getTentativeTrainingStartDate()),txn.getTotalAmount(),df.format(txn.getPaymentDate()),txn.getTotalAmount(),txn.getPaidAmount(),txn.getTransactionId(),txn.getDueAmount().toString(),df.format(txn.getDueDate()),txn.getPaymentMode(),folderPathWithFIle);
					}


					Documents documents = new Documents();
					documents.setCandidate(candidate);
					documents.setFileName(candidate.getCandidateName()+"_enrollment_form.pdf");
					documents.setFilePath(folderPathWithFIle);
					documents.setCreatedDate(new Date());
					documents.setDocumentType("application/pdf");
					documents.setDocumentFor("enrollment");
					documents.setDocumentsSpecification("generate");
					documents.setExtension(".pdf");
					documentsService.save(documents);

					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(candidate);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Enrollment Form Generated");
					hrFollowUp.setFollowUpType("enrollment");
					hrFollowupService.save(hrFollowUp);

					Documents doc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","enrollment");
					if(doc != null){
						enrollmentFileHandleUtil = new FileHandleUtil();
						enrollmentFileHandleUtil.setFilePath(doc.getFilePath());
						enrollmentFileHandleUtil.setFileName(doc.getFileName());
						enrollmentFileHandleUtil.setFileExtension(doc.getExtension());
					}
					enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
					allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
					candidate.setReferenceFormNo("GITES"+Calendar.getInstance().get(Calendar.YEAR)+"/T"+Integer.valueOf(enrollmentFormNo.intValue()+1));
					candidateService.saveCandidate(candidate);
					EnrollmentFormNo e = new EnrollmentFormNo();
					e.setEnrollmentFormNumber(Integer.valueOf(enrollmentFormNo.intValue()+1));
					enrollmentFormNoService.save(e);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Enrollment Form Successfully Created"));
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Directory Creation Fail"));
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Enrollment Successfully Not Generated"));
		}	
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

	public void saveSendingEnrollmentForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			sendingEnrollmentDocument.add(uploadFile);
			sendingEnrollmentFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}

	public void sendingEnrollmentFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		sendingEnrollmentFileName.remove(deletefile);
		Iterator<String> ite = sendingEnrollmentFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = sendingEnrollmentDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}


	public void saveSendingEnrollmentFormInDatabase(){
		try{
			for(UploadedFile uploadFile:sendingEnrollmentDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","enrollment");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				File file = new File(reviousdoc.getFilePath());
				file.delete();
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Sending Enrollment Form Deleted");
				hrFollowUp.setFollowUpType("enrollment");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:sendingEnrollmentDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/sending/enrollment_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("enrollment");
				documents.setDocumentsSpecification("sending");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Sending Enrollment Form Uploaded");
				hrFollowUp.setFollowUpType("enrollment");
				hrFollowupService.save(hrFollowUp);
			}
			Documents sef = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","enrollment");
			if(sef != null){
				sendingEnrollmentFileHandleUtil = new FileHandleUtil();
				sendingEnrollmentFileHandleUtil.setFilePath(sef.getFilePath());
				sendingEnrollmentFileHandleUtil.setFileName(sef.getFileName());
				sendingEnrollmentFileHandleUtil.setFileExtension(sef.getExtension());
			}
			sendingEnrollmentDocument = new ArrayList<UploadedFile>();
			sendingEnrollmentFileName = new ArrayList<String>();
			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			sendingEnrollmentDocument = new ArrayList<UploadedFile>();
			sendingEnrollmentFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}


	public void cancelSendingEnrollmentFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		sendingEnrollmentDocument = new ArrayList<UploadedFile>();
		sendingEnrollmentFileName = new ArrayList<String>();
	}



	public void saveReceivingEnrollmentForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			receivingEnrollmentDocument.add(uploadFile);
			receivingEnrollmentFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}

	public void receivingEnrollmentFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		receivingEnrollmentFileName.remove(deletefile);
		Iterator<String> ite = receivingEnrollmentFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = receivingEnrollmentDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}


	public void saveReceivingEnrollmentFormInDatabase(){

		try{
			for(UploadedFile uploadFile:receivingEnrollmentDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			String enrollmentFormPath = null;
			String enrollmentFormPathExtension = null;
			String enrollmentFormName = null;
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","enrollment");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				File file = new File(reviousdoc.getFilePath());
				file.delete();
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Receiving Enrollment Form Deleted");
				hrFollowUp.setFollowUpType("enrollment");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:receivingEnrollmentDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/enrollment_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				enrollmentFormName = filename;
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				enrollmentFormPath = expenseFileFolderPath+"/"+filename;
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("enrollment");
				documents.setDocumentsSpecification("receiving");
				documents.setDocOtherName("Enrollment Form");
				documents.setExtension(extension);
				enrollmentFormPathExtension = extension;
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Receiving Enrollment Form Uploaded");
				hrFollowUp.setFollowUpType("enrollment");
				hrFollowupService.save(hrFollowUp);
			}
			Documents ref = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","enrollment");
			if(ref != null){
				receivingEnrollmentFileHandleUtil = new FileHandleUtil();
				receivingEnrollmentFileHandleUtil.setFilePath(ref.getFilePath());
				receivingEnrollmentFileHandleUtil.setFileName(ref.getFileName());
				receivingEnrollmentFileHandleUtil.setFileExtension(ref.getExtension());
			}
			receivingEnrollmentDocument = new ArrayList<UploadedFile>();
			receivingEnrollmentFileName = new ArrayList<String>();
			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			candidate.setReceivingEnrollmentDate(new Date());
			candidate.setEnrollmentFormStatus("RECEIVED");
			candidate.setEnrollmentFormPath(enrollmentFormPath);
			candidate.setEnrollmentFormFileExtension(enrollmentFormPathExtension);
			candidate.setEnrollmentFormName(enrollmentFormName);
			candidateService.saveCandidate(candidate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			receivingEnrollmentDocument = new ArrayList<UploadedFile>();
			receivingEnrollmentFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}

	public void cancelReceivingEnrollmentFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		receivingEnrollmentDocument = new ArrayList<UploadedFile>();
		receivingEnrollmentFileName = new ArrayList<String>();
	}


	public void saveEnrollmentFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("enrollment");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setEnrollmentNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}



	public void trainingDetails(){
		try{
			if(candidate != null){
				action = "TRAININGDETAILS";
				trainingEditPanel = false;
				trainingLabelPanel = true;
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editTrainingDetails(){
		trainingEditPanel = true;
		trainingLabelPanel = false;
	}


	public void saveTrainerDetails(){
		try{
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in training phase");
			hrFollowUp.setFollowUpType("training");
			hrFollowupService.save(hrFollowUp);
			trainingEditPanel = false;
			trainingLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}



	public void generatetrainingFeedbackForm(){
		String errorMessage = "The following information is not present : ";
		boolean error = false;
		PaymentTransaction txn = null;
		try{

			if(candidate.getCandidateName() == null){
				errorMessage = errorMessage+"Candidate Name, ";
				error = true;
			}
			if(candidate.getPrimarySkill() == null){
				errorMessage = errorMessage+"Course Name, ";
				error = true;
			}
			if(candidate.getBatchCode() == null){
				errorMessage = errorMessage+"Batch Code, ";
				error = true;
			}
			if(candidate.getTrainerName() == null){
				errorMessage = errorMessage+"Trainer's Name, ";
				error = true;
			}
			if(candidate.getTrainingTiming() == null){
				errorMessage = errorMessage+"Training Timing, ";
				error = true;
			}
			if(candidate.getTrainingStartDate() == null){
				errorMessage = errorMessage+"Training Start Date, ";
				error = true;
			}
			if(candidate.getTrainingExpectedEndDate() == null){
				errorMessage = errorMessage+"Training End Date, ";
				error = true;
			}
			if(candidate.getReferenceFormNo() == null){
				errorMessage = errorMessage+"Enrollment Form Reference No ,";
				error = true;
			}

			List<PaymentTransaction> result = paymentTransactionService.findPaymentTransactionByCandidateId(candidate.getCandidateId());
			if(result != null && result.size() >0){
				txn = result.get(0);
			}else{
				errorMessage = errorMessage+" Transaction Id";
				error = true;
			}

			if(error){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:",errorMessage));
			}else{

				Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","training");
				if(reviousdoc != null){
					documentsService.deleteDocument(reviousdoc);
					File file = new File(reviousdoc.getFilePath());
					file.delete();
					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(candidate);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Generated Training Feedback Form Deleted For Again Generation");
					hrFollowUp.setFollowUpType("training");
					hrFollowupService.save(hrFollowUp);
				}


				ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();	
				boolean isCreate = DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
				if(isCreate){
					String folderPathWithFIle = proprtiesFileDto.getEnrollmentFormFolderPath()+"/"+"Candidate_"+candidate.getCandidateId()+"/generate/feedback_form/"+candidate.getCandidateName()+"_training_feedback_form.pdf";
					DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
					PdfGenerator.createFeedbackForm(candidate.getCandidateName(),candidate.getPrimarySkill(),candidate.getBatchCode(),candidate.getTrainerName(),candidate.getTrainingTiming(),df.format(candidate.getTrainingStartDate()),df.format(candidate.getTrainingExpectedEndDate()),txn.getTransactionId(),candidate.getReferenceFormNo(),folderPathWithFIle);
					Documents documents = new Documents();
					documents.setCandidate(candidate);
					documents.setFileName(candidate.getCandidateName()+"_training_feedback_form.pdf");
					documents.setFilePath(folderPathWithFIle);
					documents.setCreatedDate(new Date());
					documents.setDocumentType("application/pdf");
					documents.setDocumentFor("training");
					documents.setDocumentsSpecification("generate");
					documents.setExtension(".pdf");
					documentsService.save(documents);

					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(candidate);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Training Feedback Form Generated");
					hrFollowUp.setFollowUpType("training");
					hrFollowupService.save(hrFollowUp);

					Documents doc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"generate","training");
					if(doc != null){
						trainingFileHandleUtil = new FileHandleUtil();
						trainingFileHandleUtil.setFilePath(doc.getFilePath());
						trainingFileHandleUtil.setFileName(doc.getFileName());
						trainingFileHandleUtil.setFileExtension(doc.getExtension());
					}
					trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
					allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Training Feedback Form Successfully Created"));
				}else{
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Directory Creation Fail"));
				}
			}

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Training Feedback Form Not Generated"));
		}	
	}



	public void savetrainingEnrollmentForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			sendingTrainingDocument.add(uploadFile);
			sendingTrainingFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}



	public void sendingTrainingFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		sendingTrainingFileName.remove(deletefile);
		Iterator<String> ite = sendingTrainingFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = sendingTrainingDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}



	public void saveSendingTrainingFormInDatabase(){
		try{
			for(UploadedFile uploadFile:sendingTrainingDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","training");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				File file = new File(reviousdoc.getFilePath());
				file.delete();
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Sending Training Feedback Form Deleted");
				hrFollowUp.setFollowUpType("training");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:sendingTrainingDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/sending/feedback_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("training");
				documents.setDocumentsSpecification("sending");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Sending Training Feedback Form Uploaded");
				hrFollowUp.setFollowUpType("training");
				hrFollowupService.save(hrFollowUp);
			}
			Documents sef = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"sending","training");
			if(sef != null){
				sendingTrainingFileHandleUtil = new FileHandleUtil();
				sendingTrainingFileHandleUtil.setFilePath(sef.getFilePath());
				sendingTrainingFileHandleUtil.setFileName(sef.getFileName());
				sendingTrainingFileHandleUtil.setFileExtension(sef.getExtension());
			}
			sendingTrainingDocument = new ArrayList<UploadedFile>();
			sendingTrainingFileName = new ArrayList<String>();
			trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			sendingTrainingDocument = new ArrayList<UploadedFile>();
			sendingTrainingFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}


	public void cancelSendingTrainingFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		sendingTrainingDocument = new ArrayList<UploadedFile>();
		sendingTrainingFileName = new ArrayList<String>();
	}


	public void saveReceivingTrainingFeedbackForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			receivingTrainingDocument.add(uploadFile);
			receivingTrainingFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}


	public void receivingTrainingFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		receivingTrainingFileName.remove(deletefile);
		Iterator<String> ite = receivingTrainingFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = receivingTrainingDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}


	public void saveReceivingTrainingFormInDatabase(){
		try{
			for(UploadedFile uploadFile:receivingTrainingDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","training");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				File file = new File(reviousdoc.getFilePath());
				file.delete();
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Receiving Training Feedback Form Deleted");
				hrFollowUp.setFollowUpType("training");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:receivingTrainingDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/feedback_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("training");
				documents.setDocumentsSpecification("receiving");
				documents.setDocOtherName("Training Feedback Form");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Receiving Training Feedback Form Uploaded");
				hrFollowUp.setFollowUpType("training");
				hrFollowupService.save(hrFollowUp);
			}
			Documents ref = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","training");
			if(ref != null){
				receivingTrainingFileHandleUtil = new FileHandleUtil();
				receivingTrainingFileHandleUtil.setFilePath(ref.getFilePath());
				receivingTrainingFileHandleUtil.setFileName(ref.getFileName());
				receivingTrainingFileHandleUtil.setFileExtension(ref.getExtension());
			}
			receivingTrainingDocument = new ArrayList<UploadedFile>();
			receivingTrainingFileName = new ArrayList<String>();
			trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			receivingTrainingDocument = new ArrayList<UploadedFile>();
			receivingTrainingFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}


	public void cancelReceivingTrainingFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		receivingTrainingDocument = new ArrayList<UploadedFile>();
		receivingTrainingFileName = new ArrayList<String>();
	}


	public void saveTrainingFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("training");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setTrainingNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			trainingFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"training");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}


	public void resumeDetails(){
		try{
			if(candidate != null){
				action = "RESUMEDETAILS";
				resumeEditPanel = false;
				resumeLabelPanel = true;
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editResumeDetails(){
		resumeEditPanel = true;
		resumeLabelPanel = false;
	}


	public void saveResumeDetails(){
		try{
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in resume phase");
			hrFollowUp.setFollowUpType("resume");
			hrFollowupService.save(hrFollowUp);
			resumeEditPanel = false;
			resumeLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			resumeFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"resume");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}


	public void saveSendingResume(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			sendingResumeDocument.add(uploadFile);
			sendingresumeFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}


	public void saveSendingResumeFormInDatabase(){
		try{
			for(UploadedFile uploadFile:sendingResumeDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			String resumePath = null;
			String resumePathExtension = null;
			String resumeName = null;
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","resume");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				File file = new File(reviousdoc.getFilePath());
				file.delete();
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Upload Resume Deleted");
				hrFollowUp.setFollowUpType("resume");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:sendingResumeDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/resume";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				resumeName = filename;
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				resumePath = expenseFileFolderPath+"/"+filename;
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("resume");
				documents.setDocumentsSpecification("receiving");
				documents.setDocOtherName("Resume");
				documents.setExtension(extension);
				resumePathExtension = extension;
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Resume Uploaded");
				hrFollowUp.setFollowUpType("resume");
				hrFollowupService.save(hrFollowUp);
			}
			Documents ser = documentsService.findDocumentsByCandidateId(candidate.getCandidateId(),"receiving","resume");
			if(ser != null){
				receivingResumeFileHandleUtil = new FileHandleUtil();
				receivingResumeFileHandleUtil.setFilePath(ser.getFilePath());
				receivingResumeFileHandleUtil.setFileName(ser.getFileName());
				receivingResumeFileHandleUtil.setFileExtension(ser.getExtension());
			}
			sendingResumeDocument = new ArrayList<UploadedFile>();
			sendingresumeFileName = new ArrayList<String>();
			resumeFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"resume");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			candidate.setResumePath(resumePath);
			candidate.setResumeFileExtension(resumePathExtension);
			candidate.setResumeFileName(resumeName);
			candidateService.saveCandidate(candidate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Saved Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			sendingResumeDocument = new ArrayList<UploadedFile>();
			sendingresumeFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}


	public void sendingResumeDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		sendingresumeFileName.remove(deletefile);
		Iterator<String> ite = sendingresumeFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = sendingResumeDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}


	public void cancelSendingResumeFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		sendingResumeDocument = new ArrayList<UploadedFile>();
		sendingresumeFileName = new ArrayList<String>();
	}


	public void saveResumeFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("resume");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setResumeNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			resumeFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"resume");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}



	public void mockAndGraduationDetails(){
		try{
			if(candidate != null){
				action = "MOCKANDGRADUATIONDETAILS";
				mockAndGraduationEditPanel = false;
				mockAndGraduationLabelPanel = true;
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void editGraduationAndMockDetails(){
		mockAndGraduationEditPanel = true;
		mockAndGraduationLabelPanel = false;
	}



	public void saveMockAndGraduationDetails(){
		try{
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in mock and graduation phase");
			hrFollowUp.setFollowUpType("mock");
			hrFollowupService.save(hrFollowUp);
			mockAndGraduationEditPanel = false;
			mockAndGraduationLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}




	public void saveMockAndGraduationFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("mock");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setMockNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}



	public void preHotlistDetails(){
		try{
			if(candidate != null){
				action = "PREHOTLISTDETAILS";
				preHotlistEditPanel = false;
				preHotlistLabelPanel = true;

				Set<Priority> results = candidateService.findPriorityByCandidateId(candidate.getCandidateId());
				if(results != null && results.size()>0){
					for(Priority p:results){
						if(priorityList == null){
							priorityList = p.getPriority();
						}else{
							priorityList = priorityList+","+p.getPriority();
						}
					}
				}
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void editPreHotListDetails(){
		preHotlistEditPanel = true;
		preHotlistLabelPanel = false;
		try{

			Set<Priority> results = candidateService.findPriorityByCandidateId(candidate.getCandidateId());
			if(results != null && results.size()>0){
				priorities = new Integer[results.size()];	
				int i=0;
				for(Priority p:results){
					priorities[i]=p.getId();
					i++;
				}
			}

			if(results != null && results.size()>0){
				for(Priority p:results){
					if(priorityList == null){
						priorityList = p.getPriority();
					}else{
						priorityList = priorityList+","+p.getPriority();
					}
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void savePreHotListDetails(){
		try{
			priorityList = null;
			Set<Priority> prioritySet = getPrioritySet();
			candidate.setPriorities(prioritySet);
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in pre-hot phase");
			hrFollowUp.setFollowUpType("prehotlist");
			hrFollowupService.save(hrFollowUp);
			preHotlistEditPanel = false;
			preHotlistLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			preHotlistFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"prehotlist");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			Set<Priority> results = candidateService.findPriorityByCandidateId(candidate.getCandidateId());
			if(results != null && results.size()>0){
				for(Priority p:results){
					if(priorityList == null){
						priorityList = p.getPriority();
					}else{
						priorityList = priorityList+","+p.getPriority();
					}
				}
			}

			if(results != null && results.size()>0){
				String p = null;
				for(Priority pr:results){
					if(p == null){
						p = pr.getPriority();
					}else{
						p = p + ","+pr.getPriority();
					}
				}
				candidate.setPriority(p);
				candidateService.saveCandidate(candidate);
			}

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}



	public Set<Priority> getPrioritySet()
	{
		Set<Priority> prioritySet = new HashSet<Priority>(0);
		for(Integer id:priorities){
			Priority priority = new Priority();
			priority.setId(id);
			prioritySet.add(priority);
		}
		return prioritySet;
	}


	public void addCandidateToHotlist(){
		String msg = "Please provide ";
		try{
			Set<Priority> pList = candidateService.findCandidatePriority(candidate.getCandidateId());
			if(pList == null ){
				msg = msg + "priority,";
				throw new Exception();
			}
			if(candidate.getResumePath() == null){
				msg = msg + "Resume";
				throw new Exception();
			}
			String p = null;
			for(Priority pr:pList){
				if(p == null){
					p = pr.getPriority();
				}else{
					p = p + ","+pr.getPriority();
				}
			}
			candidate.setHotlistStatus(true);
			candidate.setHotlistStatusPredefine("ON HOTLIST");
			candidate.setDateOnHotlist(new Date());
			candidate.setPriority(p);
			candidateService.saveCandidate(candidate);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate add to hotlist");
			hrFollowUp.setFollowUpType("prehotlist");
			hrFollowupService.save(hrFollowUp);
			preHotlistFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"prehotlist");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());

			MailService mailService = new MailService();
			mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New candidate has been added to hotlist","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"marketing@globalitexperts.com",null,null);
			Thread t = new Thread(mailService);
			t.start();

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Successfully added in hotlist"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", msg));
		}
	}



	public void savePreHotlistFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("prehotlist");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setPreHotlistNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			preHotlistFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"prehotlist");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}


	public void referenceDetails(){
		try{
			if(candidate != null){
				action = "REFERENCEDETAILS";
				referenceEditPanel = false;
				referenceLabelPanel = true;
				references = referenceService.findReferenceByCandidateId(candidate.getCandidateId());
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void editReferenceDetails(){
		referenceEditPanel = true;
		referenceLabelPanel = false;
	}



	public void saveReferenceDetails(){
		try{
			candidateService.saveCandidate(candidate);
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate updated in reference check phase");
			hrFollowUp.setFollowUpType("reference");
			hrFollowupService.save(hrFollowUp);
			referenceEditPanel = false;
			referenceLabelPanel = true;
			candidate = candidateService.findCandidateById(candidate.getCandidateId());
			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Candidate Successfully Updated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Candidate Successfully Not Updated"));
		}
	}


	public void addReference(){
		reference = new Reference();
		reference.setCandidate(candidate);
	}


	public void addMoreReference(){
		Integer id = reference.getReferenceId();
		try{
			referenceService.save(reference);
			reference = null;
			references = referenceService.findReferenceByCandidateId(candidate.getCandidateId());
			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			if(id == null){
				hrFollowUp.setFollowUpRemarks("Reference added in reference phase");
			}else{
				hrFollowUp.setFollowUpRemarks("Reference updated in reference phase");
			}
			hrFollowUp.setFollowUpType("reference");
			hrFollowupService.save(hrFollowUp);
			if(id == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Reference Successfully Saved"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Reference Successfully Updated"));
			}
			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
		}catch(Exception e){
			e.printStackTrace();
			if(id == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Reference Not Saved"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Reference Not Updated"));
			}

		}
	}

	public void editReference(Reference r){
		reference = r;
		reference.setCandidate(candidate);
	}



	public void saveReferenceFollowup(){
		try{
			hrFollowup.setFollowUpDate(new Date());
			hrFollowup.setFollowUpType("reference");
			hrFollowup.setCandidate(candidate);
			hrFollowup.setUserProfile(loginBean.getUserProfile());
			hrFollowupService.save(hrFollowup);
			if(hrFollowup.getNextFollowUpDate() != null){
				selectedCandidate.setReferenceNextFollowupDate(hrFollowup.getNextFollowUpDate());
				candidateService.saveCandidate(selectedCandidate);
			}
			hrFollowup = new HrFollowUp();
			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Follow Up Successfully Saved"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Follow Up Not Saved"));
		}
	}


	public void generateReferenceForm(){
		try{

			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","reference","Reference Check form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Generated Reference Check Form Deleted For Again Generation");
				hrFollowUp.setFollowUpType("reference");
				hrFollowupService.save(hrFollowUp);
			}

			Documents documents = new Documents();
			documents.setCandidate(candidate);
			documents.setFileName("reference_ form.docx");
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();	
			documents.setFilePath(proprtiesFileDto.getReferenceFolderPath());
			documents.setCreatedDate(new Date());
			documents.setDocumentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			documents.setDocumentFor("reference");
			documents.setDocOtherName("Reference Check form");
			documents.setDocumentsSpecification("generate");
			documents.setExtension(".docx");
			documentsService.save(documents);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Reference Check Form Generated");
			hrFollowUp.setFollowUpType("reference");
			hrFollowupService.save(hrFollowUp);

			Documents doc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","reference","Reference Check form");
			if(doc != null){
				referenceCheckFileHandleUtil = new FileHandleUtil();
				referenceCheckFileHandleUtil.setFilePath(doc.getFilePath());
				referenceCheckFileHandleUtil.setFileName(doc.getFileName());
				referenceCheckFileHandleUtil.setFileExtension(doc.getExtension());
			}

			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Reference Check Form Successfully Created"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Reference Check Form Successfully Not Generated"));
		}
	}


	public void saveSendingReferenceCheck(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			sendingReferenceDocument.add(uploadFile);
			sendingReferenceFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}



	public void sendingReferenceDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		sendingReferenceFileName.remove(deletefile);
		Iterator<String> ite = sendingReferenceFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = sendingReferenceDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}



	public void saveSendingReferenceFormInDatabase(){
		try{
			for(UploadedFile uploadFile:sendingReferenceDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","reference","Reference Check form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Uploaded Reference Check Form Deleted");
				hrFollowUp.setFollowUpType("reference");
				hrFollowupService.save(hrFollowUp);
			}

			for(UploadedFile uploadFile:sendingReferenceDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/referencecheck_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("reference");
				documents.setDocOtherName("Reference Check form");
				documents.setDocumentsSpecification("receiving");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Receiving Reference Check Form Uploaded");
				hrFollowUp.setFollowUpType("reference");
				hrFollowupService.save(hrFollowUp);
			}

			Documents ser = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","reference","Reference Check form");
			if(ser != null){
				receivingReferenceFileHandleUtil = new FileHandleUtil();
				receivingReferenceFileHandleUtil.setFilePath(ser.getFilePath());
				receivingReferenceFileHandleUtil.setFileName(ser.getFileName());
				receivingReferenceFileHandleUtil.setFileExtension(ser.getExtension());
			}
			sendingReferenceDocument = new ArrayList<UploadedFile>();
			sendingReferenceFileName = new ArrayList<String>();
			referenceFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"reference");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Saved Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			sendingReferenceDocument = new ArrayList<UploadedFile>();
			sendingReferenceFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}




	public void cancelSendingReferenceFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		sendingReferenceDocument = new ArrayList<UploadedFile>();
		sendingReferenceFileName = new ArrayList<String>();
	}




	public void generatePSRForm(){

		try{
			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","PSR form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Generated Project Summary Report Form Deleted For Again Generation");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}

			Documents documents = new Documents();
			documents.setCandidate(candidate);
			documents.setFileName("Project_ Summary.docx");
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();	
			documents.setFilePath(proprtiesFileDto.getPsrFormFolderPath());

			documents.setCreatedDate(new Date());
			documents.setDocumentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			documents.setDocumentFor("mock");
			documents.setDocOtherName("PSR form");
			documents.setDocumentsSpecification("generate");
			documents.setExtension(".docx");
			documentsService.save(documents);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Project Summary Report Form Generated");
			hrFollowUp.setFollowUpType("mock");
			hrFollowupService.save(hrFollowUp);

			Documents psrDoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","PSR form");
			if(psrDoc != null){
				psrFileHandleUtil = new FileHandleUtil();
				psrFileHandleUtil.setFilePath(psrDoc.getFilePath());
				psrFileHandleUtil.setFileName(psrDoc.getFileName());
				psrFileHandleUtil.setFileExtension(psrDoc.getExtension());
			}
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","PSR Form Successfully Created"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","PSR FORM Successfully Not Generated"));
		}	
	}




	public void generateQuestionerForm(){

		try{
			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","QUESTIONER form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Generated Questioner Deleted For Again Generation");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}

			Documents documents = new Documents();
			documents.setCandidate(candidate);
			documents.setFileName("Questions.docx");
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			documents.setFilePath(proprtiesFileDto.getQuestionerFolderPath());

			documents.setCreatedDate(new Date());
			documents.setDocumentType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
			documents.setDocumentFor("mock");
			documents.setDocOtherName("QUESTIONER form");
			documents.setDocumentsSpecification("generate");
			documents.setExtension(".docx");
			documentsService.save(documents);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Questioner Generated");
			hrFollowUp.setFollowUpType("mock");
			hrFollowupService.save(hrFollowUp);

			Documents qDoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"generate","mock","QUESTIONER form");
			if(qDoc != null){
				questionerFileHandleUtil = new FileHandleUtil();
				questionerFileHandleUtil.setFilePath(qDoc.getFilePath());
				questionerFileHandleUtil.setFileName(qDoc.getFileName());
				questionerFileHandleUtil.setFileExtension(qDoc.getExtension());
			}
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Sucess:","Question Successfully Created"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Question Successfully Not Generated"));
		}	
	}



	public void saveSendingPSRForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			sendingPSRDocument.add(uploadFile);
			sendingPSRFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}



	public void sendingPSRFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		sendingPSRFileName.remove(deletefile);
		Iterator<String> ite = sendingPSRFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = sendingPSRDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}



	public void saveSendingPSRFormInDatabase(){
		try{
			for(UploadedFile uploadFile:sendingPSRDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","PSR form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Receiving PSR Form Deleted");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:sendingPSRDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/PSR_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("mock");
				documents.setDocOtherName("PSR form");
				documents.setDocumentsSpecification("receiving");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Receiving PSR Form Uploaded");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}
			Documents rpsr = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","PSR form");
			if(rpsr != null){
				sendingPSRFileHandleUtil = new FileHandleUtil();
				sendingPSRFileHandleUtil.setFilePath(rpsr.getFilePath());
				sendingPSRFileHandleUtil.setFileName(rpsr.getFileName());
				sendingPSRFileHandleUtil.setFileExtension(rpsr.getExtension());
			}
			sendingPSRDocument = new ArrayList<UploadedFile>();
			sendingPSRFileName = new ArrayList<String>();
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			sendingPSRDocument = new ArrayList<UploadedFile>();
			sendingPSRFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}


	public void cancelSendingPSRFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		sendingPSRDocument = new ArrayList<UploadedFile>();
		sendingPSRFileName = new ArrayList<String>();
	}


	public void saveReceivingQuestionerForm(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			receivingQuestionDocument.add(uploadFile);
			receivingQuestionFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}



	public void receivingQuestionFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		receivingQuestionFileName.remove(deletefile);
		Iterator<String> ite = receivingQuestionFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = receivingQuestionDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}



	public void saveReceivingQuestionFormInDatabase(){
		try{
			for(UploadedFile uploadFile:receivingQuestionDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(! new File(proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()).exists()){
				DirectoryCreationService.createDirectories(proprtiesFileDto.getEnrollmentFormFolderPath(),"Candidate_"+candidate.getCandidateId());
			}
			Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","QUESTIONER form");
			if(reviousdoc != null){
				documentsService.deleteDocument(reviousdoc);
				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Previous Receiving Question Form Deleted");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}
			for(UploadedFile uploadFile:receivingQuestionDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/questioner_form";
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("mock");
				documents.setDocOtherName("QUESTIONER form");
				documents.setDocumentsSpecification("receiving");
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks("Receiving Question Form Uploaded");
				hrFollowUp.setFollowUpType("mock");
				hrFollowupService.save(hrFollowUp);
			}
			Documents rpsr = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","mock","QUESTIONER form");
			if(rpsr != null){
				receivingQuestionFileHandleUtil = new FileHandleUtil();
				receivingQuestionFileHandleUtil.setFilePath(rpsr.getFilePath());
				receivingQuestionFileHandleUtil.setFileName(rpsr.getFileName());
				receivingQuestionFileHandleUtil.setFileExtension(rpsr.getExtension());
			}
			receivingQuestionDocument = new ArrayList<UploadedFile>();
			receivingQuestionFileName = new ArrayList<String>();
			mockAndGraduationFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"mock");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			receivingQuestionDocument = new ArrayList<UploadedFile>();
			receivingQuestionFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}



	public void cancelReceivingQuestionFileUpload(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		receivingQuestionDocument = new ArrayList<UploadedFile>();
		receivingQuestionFileName = new ArrayList<String>();
	}



	public void documentsDetails(){
		try{
			if(candidate != null){
				action = "OTHERDOCUMENTSDETAILS";
				documentsList = documentsService.findAllDocuments(candidate.getCandidateId());	
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Fail:","Please Refereh Your Browser"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	public void othersDocumentsList(FileUploadEvent event) throws IOException{
		try{
			UploadedFile uploadFile=event.getFile();
			uploadFile.getContents();
			othersDocument.add(uploadFile);
			otherDocumentsFileName.add(uploadFile.getFileName());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "File Not Uploaded"));
		}
	}



	public void otherDocumentsFileDelete(){
		FacesContext fc = FacesContext.getCurrentInstance();
		Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
		String deletefile = params.get("deletefile");
		otherDocumentsFileName.remove(deletefile);
		Iterator<String> ite = otherDocumentsFileName.iterator();
		while(ite.hasNext()) {
			String value = ite.next();
			if(value.equals(deletefile))
				ite.remove();
		}
		Iterator<UploadedFile> ite2 = othersDocument.iterator();
		while(ite2.hasNext()) {
			UploadedFile uploadedFile = ite2.next();
			if(uploadedFile.getFileName().equals(deletefile)){
				ite2.remove();
			}
		}
	}

	public void cancelOthersDocumentsInDatabase(){
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Cancel"));
		othersDocument = new ArrayList<UploadedFile>();
		otherDocumentsFileName = new ArrayList<String>();
	}





	public void saveOthersDocumentsInDatabase(){
		try{
			for(UploadedFile uploadFile:othersDocument){
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				int index = filename.indexOf( '.' );
				if(index < 1){
					throw new Exception("File Extension Not Avaiable! Please Provide File Extension");
				}
			}
			ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
			if(!"Others".equals(fileDocumentsName)){
				Documents reviousdoc = documentsService.findDocumentsByCandidateIdWithOtherDocName(candidate.getCandidateId(),"receiving","others",fileDocumentsName);
				if(reviousdoc != null){
					documentsService.deleteDocument(reviousdoc);
					File file = new File(reviousdoc.getFilePath());
					file.delete();
					HrFollowUp hrFollowUp = new HrFollowUp();
					hrFollowUp.setCandidate(candidate);
					hrFollowUp.setUserProfile(loginBean.getUserProfile());
					hrFollowUp.setFollowUpDate(new Date());
					hrFollowUp.setFollowUpRemarks("Previous Receiving "+fileDocumentsName+" Deleted");
					hrFollowUp.setFollowUpType("others");
					hrFollowupService.save(hrFollowUp);
				}
			}
			for(UploadedFile uploadFile:othersDocument){
				uploadFile.getContents();
				byte[] bytes = uploadFile.getContents();
				String filename = FilenameUtils.getName(uploadFile.getFileName());
				String fileContent = uploadFile.getContentType();
				int index = filename.indexOf( '.' );
				String extension = filename.substring(index);
				String expenseFileFolderPath = proprtiesFileDto.getEnrollmentFormFolderPath()+"/Candidate_"+candidate.getCandidateId()+"/receiving/"+fileDocumentsName;
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(expenseFileFolderPath+"/"+filename)));
				stream.write(bytes);
				stream.close();

				Documents documents = new Documents();
				documents.setCandidate(candidate);
				documents.setFileName(filename);
				documents.setFilePath(expenseFileFolderPath+"/"+filename);
				documents.setCreatedDate(new Date());
				documents.setDocumentType(fileContent);
				documents.setDocumentFor("others");
				documents.setDocumentsSpecification("receiving");
				documents.setDocOtherName(fileDocumentsName);
				documents.setExtension(extension);
				documentsService.save(documents);

				HrFollowUp hrFollowUp = new HrFollowUp();
				hrFollowUp.setCandidate(candidate);
				hrFollowUp.setUserProfile(loginBean.getUserProfile());
				hrFollowUp.setFollowUpDate(new Date());
				hrFollowUp.setFollowUpRemarks(fileDocumentsName+" Uploaded");
				hrFollowUp.setFollowUpType("others");
				hrFollowupService.save(hrFollowUp);
			}
			othersDocument = new ArrayList<UploadedFile>();
			otherDocumentsFileName = new ArrayList<String>();
			fileDocumentsName = null;
			documentsList = documentsService.findAllDocuments(candidate.getCandidateId());
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "File Uploaded Succesfully"));
		}catch(Exception e){
			e.printStackTrace();
			othersDocument = new ArrayList<UploadedFile>();
			otherDocumentsFileName = new ArrayList<String>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",e.getMessage()));
		}
	}



	public void mailSendFormForEnrollmentForm(){
		mailDto = new MailDto();
		if(enrollmentFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(enrollmentFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("enrollment_form");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}


	public void mailSendFormForTrainingFeedbackForm(){
		mailDto = new MailDto();
		if(trainingFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(trainingFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("training_feedback_form");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}


	public void mailSendFormForResume(){
		mailDto = new MailDto();
		if(receivingResumeFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(receivingResumeFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<div style=\"font-family: Calibri, Helvetica, sans-serif, serif, EmojiFont; font-size: 16px; margin-top: 0px; margin-bottom: 0px\"> "
					+ "Hello "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("resume");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}


	public void mailSendFormForDataSheet(){
		mailDto = new MailDto();
		mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
		mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
		mailDto.setToEmail(candidate.getEmailId());
		mailDto.setSubject("Resume Documents Submission - Urgent");
		List fileList = new ArrayList<String>();
		ProprtiesFileDto proprtiesFileDto = appDataBean.getProprtiesFileDto();
		fileList.add(proprtiesFileDto.getDatasheetFolderPath());
		mailDto.setFilePathList(fileList);
		mailDto.setUtilityFilePath(proprtiesFileDto.getDatasheetFolderPath());
		String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",<br></br><br></br>";
		bodyContent = bodyContent + appDataBean.getMailMessage().get("datasheet_form");
		bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
		mailDto.setMailMessage(bodyContent);
	}


	public void sendMailForDatasheet(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Datasheet Successfully Sent"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Datasheet Successfully Not Sent"));
		}
	}


	public void mailSendFormForPsrForm(){
		mailDto = new MailDto();
		if(psrFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(psrFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("project_summary_report");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}

	public void mailSendFormForQuestionerForm(){
		mailDto = new MailDto();
		if(questionerFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(questionerFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("questioner_form");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}


	public void mailSendFormForReferenceCheckForm(){
		mailDto = new MailDto();
		if(referenceCheckFileHandleUtil != null){
			mailDto.setUserName(loginBean.getUserProfile().getUserEmail());
			mailDto.setPassword(loginBean.getUserProfile().getUserEmailPassword());
			mailDto.setToEmail(candidate.getEmailId());
			List fileList = new ArrayList<String>();
			fileList.add(referenceCheckFileHandleUtil.getFilePath());
			mailDto.setFilePathList(fileList);
			String bodyContent = "<pre>Hi "+selectedCandidate.getCandidateName()+",";
			bodyContent = bodyContent + appDataBean.getMailMessage().get("reference_check_form");
			bodyContent = bodyContent + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(bodyContent);
		}
	}


	public void sendMail(){
		String msg = "Please Provide ";
		try{
			MailService mailService = new MailService();
			if(mailDto.getUserName() == null){
				msg = msg+" From Email Id, ";
				throw new Exception();
			}
			if(mailDto.getPassword() == null){
				msg = msg+"User Password,";
				throw new Exception();
			}
			if(mailDto.getSubject() == null){
				msg = msg+"Mail Subject,";
				throw new Exception();
			}
			if(mailDto.getMailMessage() == null){
				msg = msg+"Mail Message,";
				throw new Exception();
			}
			if(mailDto.getToEmail() == null){
				msg = msg+"To Email Id,";
				throw new Exception();
			}
			if(mailDto.getFilePathList().size() == 0 ){
				msg = msg+"Enrollment Form";
				throw new Exception();
			}

			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			candidate.setSendingEnrollmentDate(new Date());
			candidate.setEnrollmentFormStatus("SENT");
			candidateService.saveCandidate(candidate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "",msg));
		}
	}



	public void sendMailForTraining(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			candidate.setTrainingFeedbackFormSendDate(new Date());
			candidate.setTrainingStatus("TFF SENT");
			candidateService.saveCandidate(candidate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Mail Successfully Not Send"));
		}
	}



	public void sendMailForResume(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			candidate.setResumeDocSendDate(new Date());
			candidate.setResumePrepStatus("RESUME DOCS SENT");
			candidateService.saveCandidate(candidate);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Mail Successfully Not Send"));
		}
	}



	public void sendMailForPSR(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Mail Successfully Not Send"));
		}
	}


	public void sendMailForQuestioner(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Mail Successfully Not Send"));
		}
	}



	public void sendMailForReferenceCheck(){
		try{
			MailService mailService = new MailService();
			mailService.setParameters(mailDto.getUserName(),mailDto.getPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),mailDto.getFilePathList());
			Thread t = new Thread(mailService);
			t.start(); 
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Send"));

		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "","Mail Successfully Not Send"));
		}
	}


	public void paymentForm(){
		paymentTransaction = new PaymentTransaction();
		paymentTransaction.setCandidate(candidate);
		paymentTransaction.setUserProfile(loginBean.getUserProfile());
	}


	public void savePayment(){
		Integer id = paymentTransaction.getId();
		try{
			paymentTransaction.setTransactionDate(new Date());
			paymentTransactionService.save(paymentTransaction);
			BigDecimal paidAmount = paymentTransactionService.findTotalPaidAmount(candidate.getCandidateId());
			candidate.setTransactionId(paymentTransaction.getTransactionId());
			candidate.setTotalAmount(paymentTransaction.getTotalAmount());
			candidate.setAmountPaid(paidAmount);
			candidate.setPaymentDate(paymentTransaction.getPaymentDate());
			candidate.setPaymentMode(paymentTransaction.getPaymentMode());
			candidate.setAmountDue(paymentTransaction.getDueAmount());
			candidate.setDueDate(paymentTransaction.getDueDate());
			candidateService.saveCandidate(candidate);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			if(id == null){
				hrFollowUp.setFollowUpRemarks("Payment Saved - Transaction Id: "+paymentTransaction.getTransactionId()+",Total Amount: "+paymentTransaction.getTotalAmount()+",Paid Amount: "+paymentTransaction.getPaidAmount()+",Payment Date: "+paymentTransaction.getPaymentDate()+",Payment Mode: "+paymentTransaction.getPaymentMode()+",Due Amount: "+paymentTransaction.getDueAmount()+",Due Date: "+paymentTransaction.getDueDate());
			}else{
				hrFollowUp.setFollowUpRemarks("Payment Edit - Transaction Id: "+paymentTransaction.getTransactionId()+",Total Amount: "+paymentTransaction.getTotalAmount()+",Paid Amount: "+paymentTransaction.getPaidAmount()+",Payment Date: "+paymentTransaction.getPaymentDate()+",Payment Mode: "+paymentTransaction.getPaymentMode()+",Due Amount: "+paymentTransaction.getDueAmount()+",Due Date: "+paymentTransaction.getDueDate());
			}

			hrFollowUp.setFollowUpType("enrollment");
			hrFollowupService.save(hrFollowUp);
			paymentTransactionList = paymentTransactionService.findPaymentTransactionByCandidateId(candidate.getCandidateId());
			if(id == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Payment Successfully Saved"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Payment Successfully Updated"));
			}

			enrollmentFollowUpList = hrFollowupService.findHrfollowupByType(candidate.getCandidateId(),"enrollment");
			allHrFollowuList =  hrFollowupService.findHrfollowupByCandidateId(candidate.getCandidateId());

		}catch(Exception e){
			e.printStackTrace();
			if(id == null){
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Payment Not Saved"));
			}else{
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Payment Not Updated"));
			}

		}
	}



	public void editPaymentForm(PaymentTransaction pt){
		paymentTransaction = pt;
		paymentTransaction.setCandidate(candidate);
		paymentTransaction.setUserProfile(loginBean.getUserProfile());
	}





	public void hrReport(){
		action = "HRREPORT";
		weekrangeSelector = false;
		selectedOption = null;
		weekWise = false;
		monthWise = false;
		skillWise = false;
		visaWise = false;
		weekrangeSelector = false;
		optionValue = null;
		searcingDate = null;
		weeks = null;
		trainingCandidate = null;
		resumeCandidate = null;
		mockCandidates = null;
	}

	public void prehotlistReport(){
		try{

			action = "PREHOTLISTREPORT";
			preHotlistCandidates = candidateService.findPrehotlistCandidate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void hotlistReport(){
		try{

			action = "HOTLISTREPORT";
			preHotlistCandidates = candidateService.findHotlistCandidate();

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void mockReport(){
		action ="MOCKREPORT";
		selectedOption = null;
		weekWise = false;
		monthWise = false;
		skillWise = false;
		visaWise = false;
		weekrangeSelector = false;
		weeks = new ArrayList<String>();
		optionValue = null;
		searcingDate = null;
		preHotlistCandidates = null;
		try{
			mockCandidates = candidateService.findMockCandidate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void checkMockReportOptions(FacesContext context, UIComponent component, Object value){

		String searchBy = (String)value;
		if("Week Wise".equals(searchBy)){
			weekWise = true;
			monthWise = false;
			skillWise = false;
			visaWise = false;
			searcingDate = null;
		}
		if("Month Wise".equals(searchBy)){
			weekWise = false;
			monthWise = true;
			skillWise = false;
			visaWise = false;
			weeks = new ArrayList<String>();
			weekrangeSelector = false;
			searcingDate = null;
		}
		if("Skill Wise".equals(searchBy)){
			weekWise = false;
			monthWise = false;
			skillWise = true;
			visaWise = false;
			searcingDate = null;
			weeks = new ArrayList<String>();
			weekrangeSelector = false;
		}
		if("Visa Wise".equals(searchBy)){
			weekWise = false;
			monthWise = false;
			skillWise = false;
			visaWise = true;
			searcingDate = null;
			weeks = new ArrayList<String>();
			weekrangeSelector = false;
		}

	}


	public void checkYearAndMonth(FacesContext context, UIComponent component, Object value){
		Date selectedDate = (Date) value;
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(selectedDate);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		weeks = CalenderUtil.findWeekOfMonth(year,month-1);
		weekrangeSelector = true;
	}


	public void searchForMockReport(){
		try{
			if("Skill Wise".equals(selectedOption) || "Visa Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.candidateBySearchByAndSearchValue(selectedOption,optionValue);
			}

			if("Month Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findMockCandidateByStartdateAndEndDate(CalenderUtil.createStartDate(searcingDate),CalenderUtil.createEndDate(searcingDate));
			}

			if("Week Wise".equals(selectedOption)){

				String [] s = new String[3];
				int i = 0;
				for(String w:optionValue.split("\\s",0)){  
					s[i] = w;
					i++;
				}

				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date startdate = formatter.parse(s[0]);
				Date endDate = formatter.parse(s[2]);				
				preHotlistCandidates = candidateService.findMockCandidateByStartdateAndEndDate(startdate,endDate);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void referenceReport(){
		try{
			action = "REFERENCEREPORT";
			selectedCandidate = null;
			references = null;
			preHotlistCandidates = candidateService.getcandidateWithReference();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void onRowSelectCandidateForReference(SelectEvent event){
		try{
			references = referenceService.findReferenceByCandidateId(selectedCandidate.getCandidateId());
			action = "CANDIDATEREFERENCEDETAILSDETAILS";
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void resumeReport(){
		try{
			action = "RESUMEREPORT";
			preHotlistCandidates = candidateService.findCandidateForResumeReport();
			resumeCandidate = candidateService.findResumeCandidate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}



	public void traningReport(){
		action = "TRANINGREPORT";
		skillWise=false;
		batchWise=false;
		recruiterWise = false;
		optionValue = null;
		selectedOption = null;
		preHotlistCandidates = null;
		try{
			trainingCandidate = candidateService.findTrainingCandidate();
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void checkTraningReportOptions(FacesContext context, UIComponent component, Object value){

		String searchBy = (String)value;

		if("Skill Wise".equals(searchBy)){
			skillWise = true;
			batchWise = false;
			recruiterWise = false;
			optionValue = null;
		}
		if("Batch Wise".equals(searchBy)){
			skillWise = false;
			batchWise = true;
			recruiterWise = false;
			optionValue = null;
		}
		if("Trainer Wise".equals(searchBy)){
			skillWise = false;
			batchWise = true;
			recruiterWise = false;
			optionValue = null;
		}
		if("Recruiter Wise".equals(searchBy)){
			skillWise = false;
			batchWise = false;
			recruiterWise = true;
			optionValue = null;
		}

	}


	public void searchForTrainingReport(){
		try{
			preHotlistCandidates = candidateService.candidateBySearchByAndSearchValueForTraning(selectedOption,optionValue);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void enrollmentReport(){
		action = "ENROLLMENTREPORT";
		selectedOption = null;
		searcingDate = null;
		optionValue = null;
		serviceWise = false;
		preHotlistCandidates = null;
		monthWise = false;
		yearWise = false;
		dayWise = false;
		weekrangeSelector = false;
		weekWise = false;
	}

	public void checkEnrolllmentReportOptions(FacesContext context, UIComponent component, Object value){
		String searchBy = (String)value;
		if("Service Wise".equals(searchBy)){
			serviceWise = true;
			yearWise = false;
			monthWise = false;
			dayWise = false;
			weekWise = false;
			searcingDate = null;
			weekrangeSelector = false;
		}
		if("Month Wise".equals(searchBy)){
			monthWise = true;
			serviceWise = false;
			dayWise = false;
			weekWise = false;
			searcingDate = null;
			yearWise = false;
			weekrangeSelector = false;
		}
		if("Year Wise".equals(searchBy)){
			monthWise = false;
			serviceWise = false;
			dayWise = false;
			weekWise = false;
			searcingDate = null;
			yearWise = true;
			weekrangeSelector = false;
		}
		if("Day Wise".equals(searchBy)){
			monthWise = false;
			serviceWise = false;
			dayWise = false;
			weekWise = false;
			searcingDate = null;
			yearWise = false;
			dayWise = true;
			weekrangeSelector = false;
		}
		if("Week Wise".equals(searchBy)){
			monthWise = false;
			serviceWise = false;
			dayWise = false;
			weekWise = true;
			searcingDate = null;
			yearWise = false;
			dayWise = false;
			weekrangeSelector = false;
		}

	}


	public void searchForEnrollmentReport(){
		try{
			if("Service Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findCandidatesForEnrollment(selectedOption,optionValue);
			}
			if("Month Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findCandidatesForEnrollmentWithDateRange(selectedOption,CalenderUtil.createStartDate(searcingDate),CalenderUtil.createEndDate(searcingDate));
			}
			if("Year Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findCandidatesForEnrollmentWithDateRange(selectedOption,CalenderUtil.createStartDateOfYear(searcingDate),CalenderUtil.createEndDateOfYear(searcingDate));
			}
			if("Day Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findCandidatesForEnrollmentWithDateRange(selectedOption,searcingDate,null);
			}
			if("Week Wise".equals(selectedOption)){
				String [] s = new String[3];
				int i = 0;
				for(String w:optionValue.split("\\s",0)){  
					s[i] = w;
					i++;
				}
				SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
				Date startdate = formatter.parse(s[0]);
				Date endDate = formatter.parse(s[2]);				
				preHotlistCandidates = candidateService.findCandidatesForEnrollmentWithDateRange(selectedOption,startdate,endDate);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void sportIncentiveReport(){
		action = "SPORTINCENTIVE";
		searcingDate = null;
		optionValue = null;
		weeks = null;
		sportIncentiveCandidate = null;
	}


	public void searchForSportIncentiveReport(){
		try{
			String [] s = new String[3];
			int i = 0;
			for(String w:optionValue.split("\\s",0)){  
				s[i] = w;
				i++;
			}
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date startdate = formatter.parse(s[0]);
			Date endDate = formatter.parse(s[2]);
			sportIncentiveCandidate = candidateService.findIncentiveDueCandidate(startdate,endDate);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public double calculateSportIncentive(String recruiterName){
		double amount = 0.0;
		if(sportIncentiveCandidate != null && sportIncentiveCandidate.size()>0){
			int i = 0;
			for(Candidate can:sportIncentiveCandidate){
				if(can.getRecruiterName().equals(recruiterName)){
					i = i+1;
				}
			}
			if(i>0){
				int k = i/2;
				if(k>0){
					amount = k*500;
				}
			}
		}
		return amount;
	}

	public void paymentReport(){
		action = "PAYMENTREPORT";
		selectedOption = null;
		searcingDate = null;
		optionValue = null;
		monthWise = false;
		dayWise = false;
		skillWise = false;
		recruiterWise = false;
		paymentDetails = null;
	}


	public void checkPaymentReportOptions(FacesContext context, UIComponent component, Object value){
		String searchBy = (String)value;
		if("Skill Wise".equals(searchBy)){
			skillWise = true;
			monthWise = false;
			dayWise = false;
			recruiterWise = false;
			searcingDate = null;
		}
		if("Recruiter Wise".equals(searchBy)){
			skillWise = false;
			monthWise = false;
			dayWise = false;
			recruiterWise = true;
			searcingDate = null;
		}
		if("Month Wise".equals(searchBy)){
			skillWise = false;
			monthWise = true;
			dayWise = false;
			recruiterWise = false;
			searcingDate = null;
		}
		if("Day Wise".equals(searchBy)){
			skillWise = false;
			monthWise = false;
			dayWise = true;
			recruiterWise = false;
			searcingDate = null;
		}

	}


	public void searchForPaymentReport(){
		try{
			if("Skill Wise".equals(selectedOption) || "Recruiter Wise".equals(selectedOption)){
				paymentDetails = paymentTransactionService.findPaymentTransactionBySearchByAndSearchValue(selectedOption,optionValue);
			}
			if("Month Wise".equals(selectedOption)){
				paymentDetails = paymentTransactionService.findPaymentTransactionByDateRange(selectedOption,CalenderUtil.createStartDate(searcingDate),CalenderUtil.createEndDate(searcingDate));
			}
			if("Day Wise".equals(selectedOption)){
				paymentDetails = paymentTransactionService.findPaymentTransactionByDateRange(selectedOption,searcingDate,null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void enrollmentFormReport(){
		action = "ENROLLMENTFORMREPORT";
		monthWise = false;
		dayWise = false;
		selectedOption = null;
		searcingDate = null;
		preHotlistCandidates = null;
	}


	public void checkEnrolllmentFormReportOptions(FacesContext context, UIComponent component, Object value){
		String searchBy = (String)value;
		if("Month Wise".equals(searchBy)){
			monthWise = true;
			dayWise = false;
			searcingDate = null;
		}
		if("Day Wise".equals(searchBy)){
			monthWise = false;
			dayWise = true;
			searcingDate = null;
		}
	}


	public void searchForEnrollmentFormReport(){
		try{
			if("Month Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findEnrommentFormBySearchByDateRange(selectedOption,CalenderUtil.createStartDate(searcingDate),CalenderUtil.createEndDate(searcingDate));
			}
			if("Day Wise".equals(selectedOption)){
				preHotlistCandidates = candidateService.findEnrommentFormBySearchByDateRange(selectedOption,searcingDate,null);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void incentiveReport(){
		action = "INCENTIVEREPORT";
		searcingDate = null;
		paymentDetails = null;
		paymentForTeamLeader = null;
	}


	public void searchForIncentiveReport(){
		try{
			paymentDetails = paymentTransactionService.findPaymentTransactionByDateRange("Month Wise",CalenderUtil.createStartDate(searcingDate),CalenderUtil.createEndDate(searcingDate));

			for(PaymentTransaction ptx : paymentDetails){
				if(!ptx.getCandidate().getSupervisorName().equals("Team Leader")){
					if(paymentForTeamLeader != null){
						paymentForTeamLeader.add(ptx);
					}else{
						paymentForTeamLeader = new ArrayList<PaymentTransaction>();
						paymentForTeamLeader.add(ptx);
					}

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public double calculateTotalPaidAmountForIndividual(String recruiterName){
		double totalPaidAmount = 0.0;
		if(paymentDetails != null){
			for(PaymentTransaction ptx : paymentDetails){
				if(ptx.getCandidate().getRecruiterName().equals(recruiterName)){
					totalPaidAmount = totalPaidAmount + ptx.getPaidAmount().doubleValue();
				}
			}	
		}
		return totalPaidAmount;
	}



	public double individualIncentiveCalculation(String recruiterName){
		double totalPaidAmount = 0.0;
		double totalIncentive = 0.0;
		if(paymentDetails != null){
			for(PaymentTransaction ptx : paymentDetails){
				if(ptx.getCandidate().getRecruiterName().equals(recruiterName)){
					totalPaidAmount = totalPaidAmount + ptx.getPaidAmount().doubleValue();
				}
			}	
			totalIncentive = incentiveStructureService.getIndividualIncentive(totalPaidAmount);	
		}
		return totalIncentive;
	}


	public double calculateTotalPaidAmountOfTeamLeader(String supervisorName){
		double totalPaidAmount = 0.0;
		if(paymentForTeamLeader != null){
			for(PaymentTransaction ptx : paymentForTeamLeader){
				if(ptx.getCandidate().getSupervisorName().equals(supervisorName)){
					totalPaidAmount = totalPaidAmount + ptx.getPaidAmount().doubleValue();
				}
			}	
		}
		return totalPaidAmount;
	}



	public double individualIncentiveForTeamLeaderCalculation(String supervisorName){
		double totalPaidAmount = 0.0;
		double totalIncentive = 0.0;
		if(paymentForTeamLeader != null){
			for(PaymentTransaction ptx : paymentForTeamLeader){
				if(ptx.getCandidate().getSupervisorName().equals(supervisorName)){
					totalPaidAmount = totalPaidAmount + ptx.getPaidAmount().doubleValue();
				}
			}	
			totalIncentive = incentiveStructureService.getLeaderIncentive(totalPaidAmount);
		}
		return totalIncentive;
	}


	public void resumeNotificationByServiceWise(FacesContext context, UIComponent component, Object value){
		try{
			String status = (String)value;
			if(selectedCandidate.getEnrollmentFormStatus().equals("RECEIVED") && status.equals("TRAINING")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Training","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"training@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
			if(selectedCandidate.getEnrollmentFormStatus().equals("RECEIVED") && status.equals("BOTH")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Training","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"training@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
				MailService mailService2 = new MailService();
				mailService2.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Direct Marketing","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email :</span> "+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill :</span> "+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"cv_prep@globalitexperts.com",null,null);
				Thread t2 = new Thread(mailService2);
				t2.start();
			}
			if(selectedCandidate.getEnrollmentFormStatus().equals("RECEIVED") && status.equals("DIRECT MARKETING") ){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Direct Marketing","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email :</span> "+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill :</span> "+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"cv_prep@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void resumeNotification(FacesContext context, UIComponent component, Object value){
		try{
			String status = (String)value;
			if(status.equals("COMPLETED")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"Training Completed Start Resume Preparation","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"cv_prep@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void mockNotification(FacesContext context, UIComponent component, Object value){
		try{
			String status = (String)value;
			if(status.equals("RESUME PREP COMPLETED")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Grooming Session","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"mock@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
				MailService mailService2 = new MailService();
				mailService2.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"Collect Onboarding Documents From This Candidate","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"onboarding@globalitexperts.com",null,null);
				Thread t2 = new Thread(mailService2);
				t2.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void referenceNotification(FacesContext context, UIComponent component, Object value){
		try{
			String status = (String)value;

			if(status.equals("MOCK COMPLETED")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Technical Session","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"grad@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
			if(status.equals("GRADUATION COMPLETED")){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Reference Collection","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"reference@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void prehotlistNotification(FacesContext context, UIComponent component, Object value){
		try{
			Boolean status = (Boolean)value;
			if(status){
				MailService mailService = new MailService();
				mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),"New Candidate For Pre-hotlist","<span style=\"color:blue\">The Details Of This Candidate :</span> <br><br><span style=\"color:teal\">ERP ID :</span> "+selectedCandidate.getCandidateId()+"<br><span style=\"color:teal\">Name :</span> "+selectedCandidate.getCandidateName()+"<br><span style=\"color:teal\">Email : </span>"+selectedCandidate.getEmailId()+"<br><span style=\"color:teal\">Phone No :</span> "+selectedCandidate.getContactNo() +"<br><span style=\"color:teal\">Skill : </span>"+selectedCandidate.getPrimarySkill()+"<br><br><br><br>"+loginBean.getUserProfile().getSignature(),"prehotlist@globalitexperts.com",null,null);
				Thread t = new Thread(mailService);
				t.start();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void terminateForm(){
		action = "TERMINATEFORM";
	}

	public void terminateCandidate(){
		try{
			candidate.setIsTerminated("TERMINATED");
			candidate.setTreminationDate(new Date());
			candidateService.saveCandidate(candidate);

			HrFollowUp hrFollowUp = new HrFollowUp();
			hrFollowUp.setCandidate(candidate);
			hrFollowUp.setUserProfile(loginBean.getUserProfile());
			hrFollowUp.setFollowUpDate(new Date());
			hrFollowUp.setFollowUpRemarks("Candidate terminated");
			if("Enrollment Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("enrollment");
			}
			if("Training Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("training");
			}
			if("Resume Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("resume");
			}
			if("Mock Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("mock");
			}
			if("Graduation Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("mock");
			}
			if("Reference Check Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("reference");
			}
			if("Pre-hotlist Phase".equals(candidate.getTerminatedFrom())){
				hrFollowUp.setFollowUpType("prehotlist");
			}
			hrFollowupService.save(hrFollowUp);

			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Candidate Successfully Terminated"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Candidate Successfully Not Terminated"));
		}
	}


	public void mailTemplateForm(){
		action = "MAILTEMPLATEFORM";
		mailDto = new MailDto();
	}


	public void getMailTemplate(){
		try{
			FacesContext fc = FacesContext.getCurrentInstance();
			Map<String,String> params =fc.getExternalContext().getRequestParameterMap();
			String subject = params.get("subject");
			MailTemplate mailTemplate = mailTemplateService.findMailTemplate(subject);
			String message = null;
			message = "Hi "+candidate.getCandidateName()+",";
			message = message + "<br></br><br></br>"+mailTemplate.getContent();
			message = message + loginBean.getUserProfile().getSignature();
			mailDto.setMailMessage(message);
			mailDto.setSubject(mailTemplate.getSubject());
			mailDto.setToEmail(candidate.getEmailId());
		}catch(Exception e){
			e.printStackTrace();
		}


	}	

	public void sendMailTemplateToCandidate(){
		try{

			System.out.println(mailDto.getMailMessage());

			MailService mailService = new MailService();
			mailService.setParameters(loginBean.getUserProfile().getUserEmail(),loginBean.getUserProfile().getUserEmailPassword(),mailDto.getSubject(),mailDto.getMailMessage(),mailDto.getToEmail(),loginBean.getUserProfile().getUserEmail(),null);
			Thread t = new Thread(mailService);
			t.start();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", "Mail Successfully Sent"));
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Mail Successfully Not Sent"));
		}
	}


	public void terminatedCandidates(){
		action = "TERMINATEDCANDIDATES";
		try{
			terminatedCandidateLazyModel = new LazyTerminatedCandidateModel(candidateService);
		}catch(Exception e){
			e.printStackTrace();
		}
	}


	public void addContentForm(){
		action = "ADDCONTENT";
		contentOption = null;
		contentValue = null;
	}

	public void addContentInMasterTable(){
		try{

			if("course".equals(contentOption)){
				if(courseService.findCourseByName(contentValue.trim())){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", contentValue + " already exist"));
				}else{
					Course course = new Course();
					course.setCourseName(contentValue.trim());
					courseService.save(course);
					List<String> courseList = courseService.findAllCourseName();
					appDataBean.setCourseList(courseList);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", contentValue+" Successfully Added"));
				}
			}
			
			if("visa".equals(contentOption)){
				if(visaService.findVisaByName(contentValue.trim())){
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", contentValue + " already exist"));
				}else{
					Visa visa = new Visa();
					visa.setVisaName(contentValue.trim());
					visaService.save(visa);
					List<String> visaList = visaService.findAllVisaName();
					appDataBean.setVisaList(visaList);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,"Success", contentValue+" Successfully Added"));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "", "Successfully Not Added"));
		}
	}


	// setter and getter


	public String getAction() {
		return action;
	}


	public void setAction(String action) {
		this.action = action;
	}

	public String getImportBy() {
		return importBy;
	}

	public void setImportBy(String importBy) {
		this.importBy = importBy;
	}

	public String getImportValue() {
		return importValue;
	}

	public void setImportValue(String importValue) {
		this.importValue = importValue;
	}

	public List<CandidateUtil> getCandidateUtils() {
		return candidateUtils;
	}

	public void setCandidateUtils(List<CandidateUtil> candidateUtils) {
		this.candidateUtils = candidateUtils;
	}

	public CandidateUtil getSelectedCandidateUtil() {
		return selectedCandidateUtil;
	}

	public void setSelectedCandidateUtil(CandidateUtil selectedCandidateUtil) {
		this.selectedCandidateUtil = selectedCandidateUtil;
	}

	public CandidateService getCandidateService() {
		return candidateService;
	}

	public void setCandidateService(CandidateService candidateService) {
		this.candidateService = candidateService;
	}

	public LazyDataModel<Candidate> getCandidateLazyModel() {
		return candidateLazyModel;
	}

	public void setCandidateLazyModel(LazyDataModel<Candidate> candidateLazyModel) {
		this.candidateLazyModel = candidateLazyModel;
	}

	public Candidate getSelectedCandidate() {
		return selectedCandidate;
	}

	public void setSelectedCandidate(Candidate selectedCandidate) {
		this.selectedCandidate = selectedCandidate;
	}

	public Candidate getCandidate() {
		return candidate;
	}

	public void setCandidate(Candidate candidate) {
		this.candidate = candidate;
	}

	public boolean isEnrollmentEditPanel() {
		return enrollmentEditPanel;
	}

	public void setEnrollmentEditPanel(boolean enrollmentEditPanel) {
		this.enrollmentEditPanel = enrollmentEditPanel;
	}

	public boolean isEnrollmentLabelPanel() {
		return enrollmentLabelPanel;
	}

	public void setEnrollmentLabelPanel(boolean enrollmentLabelPanel) {
		this.enrollmentLabelPanel = enrollmentLabelPanel;
	}

	public HrFollowupService getHrFollowupService() {
		return hrFollowupService;
	}

	public void setHrFollowupService(HrFollowupService hrFollowupService) {
		this.hrFollowupService = hrFollowupService;
	}

	public LoginBean getLoginBean() {
		return loginBean;
	}

	public void setLoginBean(LoginBean loginBean) {
		this.loginBean = loginBean;
	}

	public AppDataBean getAppDataBean() {
		return appDataBean;
	}

	public void setAppDataBean(AppDataBean appDataBean) {
		this.appDataBean = appDataBean;
	}

	public DocumentsService getDocumentsService() {
		return documentsService;
	}

	public void setDocumentsService(DocumentsService documentsService) {
		this.documentsService = documentsService;
	}

	public FileHandleUtil getEnrollmentFileHandleUtil() {
		return enrollmentFileHandleUtil;
	}

	public void setEnrollmentFileHandleUtil(FileHandleUtil enrollmentFileHandleUtil) {
		this.enrollmentFileHandleUtil = enrollmentFileHandleUtil;
	}

	public List<UploadedFile> getSendingEnrollmentDocument() {
		return sendingEnrollmentDocument;
	}

	public void setSendingEnrollmentDocument(List<UploadedFile> sendingEnrollmentDocument) {
		this.sendingEnrollmentDocument = sendingEnrollmentDocument;
	}

	public List<String> getSendingEnrollmentFileName() {
		return sendingEnrollmentFileName;
	}

	public void setSendingEnrollmentFileName(List<String> sendingEnrollmentFileName) {
		this.sendingEnrollmentFileName = sendingEnrollmentFileName;
	}

	public FileHandleUtil getSendingEnrollmentFileHandleUtil() {
		return sendingEnrollmentFileHandleUtil;
	}

	public void setSendingEnrollmentFileHandleUtil(FileHandleUtil sendingEnrollmentFileHandleUtil) {
		this.sendingEnrollmentFileHandleUtil = sendingEnrollmentFileHandleUtil;
	}

	public List<UploadedFile> getReceivingEnrollmentDocument() {
		return receivingEnrollmentDocument;
	}

	public void setReceivingEnrollmentDocument(List<UploadedFile> receivingEnrollmentDocument) {
		this.receivingEnrollmentDocument = receivingEnrollmentDocument;
	}

	public List<String> getReceivingEnrollmentFileName() {
		return receivingEnrollmentFileName;
	}

	public void setReceivingEnrollmentFileName(List<String> receivingEnrollmentFileName) {
		this.receivingEnrollmentFileName = receivingEnrollmentFileName;
	}

	public FileHandleUtil getReceivingEnrollmentFileHandleUtil() {
		return receivingEnrollmentFileHandleUtil;
	}

	public void setReceivingEnrollmentFileHandleUtil(FileHandleUtil receivingEnrollmentFileHandleUtil) {
		this.receivingEnrollmentFileHandleUtil = receivingEnrollmentFileHandleUtil;
	}

	public HrFollowUp getHrFollowup() {
		return hrFollowup;
	}

	public void setHrFollowup(HrFollowUp hrFollowup) {
		this.hrFollowup = hrFollowup;
	}

	public List<HrFollowUp> getEnrollmentFollowUpList() {
		return enrollmentFollowUpList;
	}

	public void setEnrollmentFollowUpList(List<HrFollowUp> enrollmentFollowUpList) {
		this.enrollmentFollowUpList = enrollmentFollowUpList;
	}

	public List<HrFollowUp> getAllHrFollowuList() {
		return allHrFollowuList;
	}

	public void setAllHrFollowuList(List<HrFollowUp> allHrFollowuList) {
		this.allHrFollowuList = allHrFollowuList;
	}

	public boolean isTrainingEditPanel() {
		return trainingEditPanel;
	}

	public void setTrainingEditPanel(boolean trainingEditPanel) {
		this.trainingEditPanel = trainingEditPanel;
	}

	public boolean isTrainingLabelPanel() {
		return trainingLabelPanel;
	}

	public void setTrainingLabelPanel(boolean trainingLabelPanel) {
		this.trainingLabelPanel = trainingLabelPanel;
	}

	public FileHandleUtil getTrainingFileHandleUtil() {
		return trainingFileHandleUtil;
	}

	public void setTrainingFileHandleUtil(FileHandleUtil trainingFileHandleUtil) {
		this.trainingFileHandleUtil = trainingFileHandleUtil;
	}

	public List<UploadedFile> getSendingTrainingDocument() {
		return sendingTrainingDocument;
	}

	public void setSendingTrainingDocument(List<UploadedFile> sendingTrainingDocument) {
		this.sendingTrainingDocument = sendingTrainingDocument;
	}

	public List<String> getSendingTrainingFileName() {
		return sendingTrainingFileName;
	}

	public void setSendingTrainingFileName(List<String> sendingTrainingFileName) {
		this.sendingTrainingFileName = sendingTrainingFileName;
	}

	public FileHandleUtil getSendingTrainingFileHandleUtil() {
		return sendingTrainingFileHandleUtil;
	}

	public void setSendingTrainingFileHandleUtil(FileHandleUtil sendingTrainingFileHandleUtil) {
		this.sendingTrainingFileHandleUtil = sendingTrainingFileHandleUtil;
	}

	public List<UploadedFile> getReceivingTrainingDocument() {
		return receivingTrainingDocument;
	}

	public void setReceivingTrainingDocument(List<UploadedFile> receivingTrainingDocument) {
		this.receivingTrainingDocument = receivingTrainingDocument;
	}

	public List<String> getReceivingTrainingFileName() {
		return receivingTrainingFileName;
	}

	public void setReceivingTrainingFileName(List<String> receivingTrainingFileName) {
		this.receivingTrainingFileName = receivingTrainingFileName;
	}

	public FileHandleUtil getReceivingTrainingFileHandleUtil() {
		return receivingTrainingFileHandleUtil;
	}

	public void setReceivingTrainingFileHandleUtil(FileHandleUtil receivingTrainingFileHandleUtil) {
		this.receivingTrainingFileHandleUtil = receivingTrainingFileHandleUtil;
	}

	public List<HrFollowUp> getTrainingFollowUpList() {
		return trainingFollowUpList;
	}

	public void setTrainingFollowUpList(List<HrFollowUp> trainingFollowUpList) {
		this.trainingFollowUpList = trainingFollowUpList;
	}

	public boolean isResumeEditPanel() {
		return resumeEditPanel;
	}

	public void setResumeEditPanel(boolean resumeEditPanel) {
		this.resumeEditPanel = resumeEditPanel;
	}

	public boolean isResumeLabelPanel() {
		return resumeLabelPanel;
	}

	public void setResumeLabelPanel(boolean resumeLabelPanel) {
		this.resumeLabelPanel = resumeLabelPanel;
	}

	public List<UploadedFile> getSendingResumeDocument() {
		return sendingResumeDocument;
	}

	public void setSendingResumeDocument(List<UploadedFile> sendingResumeDocument) {
		this.sendingResumeDocument = sendingResumeDocument;
	}

	public List<String> getSendingresumeFileName() {
		return sendingresumeFileName;
	}

	public void setSendingresumeFileName(List<String> sendingresumeFileName) {
		this.sendingresumeFileName = sendingresumeFileName;
	}

	public FileHandleUtil getReceivingResumeFileHandleUtil() {
		return receivingResumeFileHandleUtil;
	}

	public void setReceivingResumeFileHandleUtil(FileHandleUtil receivingResumeFileHandleUtil) {
		this.receivingResumeFileHandleUtil = receivingResumeFileHandleUtil;
	}

	public List<HrFollowUp> getResumeFollowUpList() {
		return resumeFollowUpList;
	}

	public void setResumeFollowUpList(List<HrFollowUp> resumeFollowUpList) {
		this.resumeFollowUpList = resumeFollowUpList;
	}

	public boolean isMockAndGraduationEditPanel() {
		return mockAndGraduationEditPanel;
	}

	public void setMockAndGraduationEditPanel(boolean mockAndGraduationEditPanel) {
		this.mockAndGraduationEditPanel = mockAndGraduationEditPanel;
	}

	public boolean isMockAndGraduationLabelPanel() {
		return mockAndGraduationLabelPanel;
	}

	public void setMockAndGraduationLabelPanel(boolean mockAndGraduationLabelPanel) {
		this.mockAndGraduationLabelPanel = mockAndGraduationLabelPanel;
	}

	public List<HrFollowUp> getMockAndGraduationFollowUpList() {
		return mockAndGraduationFollowUpList;
	}

	public void setMockAndGraduationFollowUpList(List<HrFollowUp> mockAndGraduationFollowUpList) {
		this.mockAndGraduationFollowUpList = mockAndGraduationFollowUpList;
	}

	public boolean isPreHotlistEditPanel() {
		return preHotlistEditPanel;
	}

	public void setPreHotlistEditPanel(boolean preHotlistEditPanel) {
		this.preHotlistEditPanel = preHotlistEditPanel;
	}

	public boolean isPreHotlistLabelPanel() {
		return preHotlistLabelPanel;
	}

	public void setPreHotlistLabelPanel(boolean preHotlistLabelPanel) {
		this.preHotlistLabelPanel = preHotlistLabelPanel;
	}

	public List<HrFollowUp> getPreHotlistFollowUpList() {
		return preHotlistFollowUpList;
	}

	public void setPreHotlistFollowUpList(List<HrFollowUp> preHotlistFollowUpList) {
		this.preHotlistFollowUpList = preHotlistFollowUpList;
	}

	public boolean isReferenceEditPanel() {
		return referenceEditPanel;
	}

	public void setReferenceEditPanel(boolean referenceEditPanel) {
		this.referenceEditPanel = referenceEditPanel;
	}

	public boolean isReferenceLabelPanel() {
		return referenceLabelPanel;
	}

	public void setReferenceLabelPanel(boolean referenceLabelPanel) {
		this.referenceLabelPanel = referenceLabelPanel;
	}

	public List<Reference> getReferences() {
		return references;
	}

	public void setReferences(List<Reference> references) {
		this.references = references;
	}

	public ReferenceService getReferenceService() {
		return referenceService;
	}

	public void setReferenceService(ReferenceService referenceService) {
		this.referenceService = referenceService;
	}

	public Reference getReference() {
		return reference;
	}

	public void setReference(Reference reference) {
		this.reference = reference;
	}

	public List<HrFollowUp> getReferenceFollowUpList() {
		return referenceFollowUpList;
	}

	public void setReferenceFollowUpList(List<HrFollowUp> referenceFollowUpList) {
		this.referenceFollowUpList = referenceFollowUpList;
	}

	public FileHandleUtil getReferenceCheckFileHandleUtil() {
		return referenceCheckFileHandleUtil;
	}

	public void setReferenceCheckFileHandleUtil(FileHandleUtil referenceCheckFileHandleUtil) {
		this.referenceCheckFileHandleUtil = referenceCheckFileHandleUtil;
	}

	public List<UploadedFile> getSendingReferenceDocument() {
		return sendingReferenceDocument;
	}

	public void setSendingReferenceDocument(List<UploadedFile> sendingReferenceDocument) {
		this.sendingReferenceDocument = sendingReferenceDocument;
	}

	public List<String> getSendingReferenceFileName() {
		return sendingReferenceFileName;
	}

	public void setSendingReferenceFileName(List<String> sendingReferenceFileName) {
		this.sendingReferenceFileName = sendingReferenceFileName;
	}

	public FileHandleUtil getReceivingReferenceFileHandleUtil() {
		return receivingReferenceFileHandleUtil;
	}

	public void setReceivingReferenceFileHandleUtil(FileHandleUtil receivingReferenceFileHandleUtil) {
		this.receivingReferenceFileHandleUtil = receivingReferenceFileHandleUtil;
	}

	public FileHandleUtil getPsrFileHandleUtil() {
		return psrFileHandleUtil;
	}

	public void setPsrFileHandleUtil(FileHandleUtil psrFileHandleUtil) {
		this.psrFileHandleUtil = psrFileHandleUtil;
	}

	public FileHandleUtil getQuestionerFileHandleUtil() {
		return questionerFileHandleUtil;
	}

	public void setQuestionerFileHandleUtil(FileHandleUtil questionerFileHandleUtil) {
		this.questionerFileHandleUtil = questionerFileHandleUtil;
	}

	public List<UploadedFile> getSendingPSRDocument() {
		return sendingPSRDocument;
	}

	public void setSendingPSRDocument(List<UploadedFile> sendingPSRDocument) {
		this.sendingPSRDocument = sendingPSRDocument;
	}

	public List<String> getSendingPSRFileName() {
		return sendingPSRFileName;
	}

	public void setSendingPSRFileName(List<String> sendingPSRFileName) {
		this.sendingPSRFileName = sendingPSRFileName;
	}

	public FileHandleUtil getSendingPSRFileHandleUtil() {
		return sendingPSRFileHandleUtil;
	}

	public void setSendingPSRFileHandleUtil(FileHandleUtil sendingPSRFileHandleUtil) {
		this.sendingPSRFileHandleUtil = sendingPSRFileHandleUtil;
	}

	public List<String> getReceivingQuestionFileName() {
		return receivingQuestionFileName;
	}

	public void setReceivingQuestionFileName(List<String> receivingQuestionFileName) {
		this.receivingQuestionFileName = receivingQuestionFileName;
	}

	public List<UploadedFile> getReceivingQuestionDocument() {
		return receivingQuestionDocument;
	}

	public void setReceivingQuestionDocument(List<UploadedFile> receivingQuestionDocument) {
		this.receivingQuestionDocument = receivingQuestionDocument;
	}

	public FileHandleUtil getReceivingQuestionFileHandleUtil() {
		return receivingQuestionFileHandleUtil;
	}

	public void setReceivingQuestionFileHandleUtil(FileHandleUtil receivingQuestionFileHandleUtil) {
		this.receivingQuestionFileHandleUtil = receivingQuestionFileHandleUtil;
	}

	public List<UploadedFile> getOthersDocument() {
		return othersDocument;
	}

	public void setOthersDocument(List<UploadedFile> othersDocument) {
		this.othersDocument = othersDocument;
	}

	public List<String> getOtherDocumentsFileName() {
		return otherDocumentsFileName;
	}

	public void setOtherDocumentsFileName(List<String> otherDocumentsFileName) {
		this.otherDocumentsFileName = otherDocumentsFileName;
	}

	public String getFileDocumentsName() {
		return fileDocumentsName;
	}

	public void setFileDocumentsName(String fileDocumentsName) {
		this.fileDocumentsName = fileDocumentsName;
	}

	public List<DocumentsModel> getDocumentsList() {
		return documentsList;
	}

	public void setDocumentsList(List<DocumentsModel> documentsList) {
		this.documentsList = documentsList;
	}

	public Integer[] getPriorities() {
		return priorities;
	}

	public void setPriorities(Integer[] priorities) {
		this.priorities = priorities;
	}

	public String getPriorityList() {
		return priorityList;
	}

	public void setPriorityList(String priorityList) {
		this.priorityList = priorityList;
	}

	public MailDto getMailDto() {
		return mailDto;
	}

	public void setMailDto(MailDto mailDto) {
		this.mailDto = mailDto;
	}

	public List<PaymentTransaction> getPaymentTransactionList() {
		return paymentTransactionList;
	}

	public void setPaymentTransactionList(List<PaymentTransaction> paymentTransactionList) {
		this.paymentTransactionList = paymentTransactionList;
	}

	public PaymentTransactionService getPaymentTransactionService() {
		return paymentTransactionService;
	}

	public void setPaymentTransactionService(PaymentTransactionService paymentTransactionService) {
		this.paymentTransactionService = paymentTransactionService;
	}

	public PaymentTransaction getPaymentTransaction() {
		return paymentTransaction;
	}

	public void setPaymentTransaction(PaymentTransaction paymentTransaction) {
		this.paymentTransaction = paymentTransaction;
	}

	public List<Candidate> getPreHotlistCandidates() {
		return preHotlistCandidates;
	}

	public void setPreHotlistCandidates(List<Candidate> preHotlistCandidates) {
		this.preHotlistCandidates = preHotlistCandidates;
	}

	public String getSelectedOption() {
		return selectedOption;
	}

	public void setSelectedOption(String selectedOption) {
		this.selectedOption = selectedOption;
	}

	public boolean isWeekWise() {
		return weekWise;
	}

	public void setWeekWise(boolean weekWise) {
		this.weekWise = weekWise;
	}

	public boolean isMonthWise() {
		return monthWise;
	}

	public void setMonthWise(boolean monthWise) {
		this.monthWise = monthWise;
	}

	public boolean isSkillWise() {
		return skillWise;
	}

	public void setSkillWise(boolean skillWise) {
		this.skillWise = skillWise;
	}

	public boolean isVisaWise() {
		return visaWise;
	}

	public void setVisaWise(boolean visaWise) {
		this.visaWise = visaWise;
	}

	public String getOptionValue() {
		return optionValue;
	}

	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	public Date getSearcingDate() {
		return searcingDate;
	}

	public void setSearcingDate(Date searcingDate) {
		this.searcingDate = searcingDate;
	}

	public boolean isWeekrangeSelector() {
		return weekrangeSelector;
	}

	public void setWeekrangeSelector(boolean weekrangeSelector) {
		this.weekrangeSelector = weekrangeSelector;
	}

	public List<String> getWeeks() {
		return weeks;
	}

	public void setWeeks(List<String> weeks) {
		this.weeks = weeks;
	}

	public boolean isBatchWise() {
		return batchWise;
	}

	public void setBatchWise(boolean batchWise) {
		this.batchWise = batchWise;
	}

	public boolean isServiceWise() {
		return serviceWise;
	}

	public void setServiceWise(boolean serviceWise) {
		this.serviceWise = serviceWise;
	}

	public boolean isYearWise() {
		return yearWise;
	}

	public void setYearWise(boolean yearWise) {
		this.yearWise = yearWise;
	}

	public boolean isDayWise() {
		return dayWise;
	}

	public void setDayWise(boolean dayWise) {
		this.dayWise = dayWise;
	}

	public EnrollmentFormNoService getEnrollmentFormNoService() {
		return enrollmentFormNoService;
	}

	public void setEnrollmentFormNoService(EnrollmentFormNoService enrollmentFormNoService) {
		this.enrollmentFormNoService = enrollmentFormNoService;
	}

	public List<Candidate> getSportIncentiveCandidate() {
		return sportIncentiveCandidate;
	}

	public void setSportIncentiveCandidate(List<Candidate> sportIncentiveCandidate) {
		this.sportIncentiveCandidate = sportIncentiveCandidate;
	}

	public List<PaymentTransaction> getPaymentDetails() {
		return paymentDetails;
	}

	public void setPaymentDetails(List<PaymentTransaction> paymentDetails) {
		this.paymentDetails = paymentDetails;
	}

	public boolean isRecruiterWise() {
		return recruiterWise;
	}

	public void setRecruiterWise(boolean recruiterWise) {
		this.recruiterWise = recruiterWise;
	}

	public IncentiveStructureService getIncentiveStructureService() {
		return incentiveStructureService;
	}

	public void setIncentiveStructureService(IncentiveStructureService incentiveStructureService) {
		this.incentiveStructureService = incentiveStructureService;
	}

	public List<PaymentTransaction> getPaymentForTeamLeader() {
		return paymentForTeamLeader;
	}

	public void setPaymentForTeamLeader(List<PaymentTransaction> paymentForTeamLeader) {
		this.paymentForTeamLeader = paymentForTeamLeader;
	}

	public List<Candidate> getTrainingCandidate() {
		return trainingCandidate;
	}

	public void setTrainingCandidate(List<Candidate> trainingCandidate) {
		this.trainingCandidate = trainingCandidate;
	}

	public List<Candidate> getResumeCandidate() {
		return resumeCandidate;
	}

	public void setResumeCandidate(List<Candidate> resumeCandidate) {
		this.resumeCandidate = resumeCandidate;
	}

	public List<Candidate> getMockCandidates() {
		return mockCandidates;
	}

	public void setMockCandidates(List<Candidate> mockCandidates) {
		this.mockCandidates = mockCandidates;
	}

	public MailTemplateService getMailTemplateService() {
		return mailTemplateService;
	}

	public void setMailTemplateService(MailTemplateService mailTemplateService) {
		this.mailTemplateService = mailTemplateService;
	}

	public LazyDataModel<Candidate> getTerminatedCandidateLazyModel() {
		return terminatedCandidateLazyModel;
	}

	public void setTerminatedCandidateLazyModel(LazyDataModel<Candidate> terminatedCandidateLazyModel) {
		this.terminatedCandidateLazyModel = terminatedCandidateLazyModel;
	}

	public String getContentOption() {
		return contentOption;
	}

	public void setContentOption(String contentOption) {
		this.contentOption = contentOption;
	}

	public String getContentValue() {
		return contentValue;
	}

	public void setContentValue(String contentValue) {
		this.contentValue = contentValue;
	}

	public CourseService getCourseService() {
		return courseService;
	}

	public void setCourseService(CourseService courseService) {
		this.courseService = courseService;
	}

	public VisaService getVisaService() {
		return visaService;
	}

	public void setVisaService(VisaService visaService) {
		this.visaService = visaService;
	}
}
