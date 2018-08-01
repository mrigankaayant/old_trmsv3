package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.DocumentsDao;
import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Documents;
import com.ayantsoft.trmsv3.jsf.model.DocumentsModel;

@ManagedBean
@ApplicationScoped
public class DocumentsService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 4117554138971513314L;
	
	@ManagedProperty("#{documentsDao}")
	private DocumentsDao documentsDao;
	
	
	public void save(Documents documents){
		documentsDao.save(documents);
	}
	
	public Documents findDocumentsByCandidateId(int candidateId,String documentsSpecification,String documentFor){
		return documentsDao.findDocumentsByCandidateId(candidateId, documentsSpecification,documentFor);
	}
	
	public Documents findDocumentsByCandidateIdWithOtherDocName(int candidateId,String documentsSpecification,String documentFor,String otherDocName){
		return documentsDao.findDocumentsByCandidateIdWithOtherDocName(candidateId, documentsSpecification, documentFor, otherDocName);
	}
	
	public List<DocumentsModel> findAllDocuments(int candidateId){
		return documentsDao.findAllDocuments(candidateId);
	}
	
	public void deleteDocument(Documents documents){
		documentsDao.deleteDocument(documents);
	}
	
	public List<String> findAttachDocuments(List<Candidate> candidates,String[] documents){
		return documentsDao.findAttachDocuments(candidates,documents);
	}

	public DocumentsDao getDocumentsDao() {
		return documentsDao;
	}

	public void setDocumentsDao(DocumentsDao documentsDao) {
		this.documentsDao = documentsDao;
	}
	
	
	

}
