package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Documents;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;
import com.ayantsoft.trmsv3.jsf.model.DocumentsModel;

@ManagedBean
@ApplicationScoped
public class DocumentsDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 4180413564079149721L;

	public void save(Documents documents){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(documents);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}


	public Documents findDocumentsByCandidateId(int candidateId,String documentsSpecification,String documentFor){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Documents documents = null;
		try{
			Criteria criteria = session.createCriteria(Documents.class,"doc");
			Criterion c1 = Restrictions.eq("doc.candidate.candidateId",candidateId);
			Criterion c2 = Restrictions.eq("doc.documentsSpecification",documentsSpecification);
			Criterion c3 = Restrictions.eq("doc.documentFor",documentFor);
			Criterion c4 = Restrictions.and(c1,c2);
			criteria.add(Restrictions.and(c4,c3));
			documents = (Documents) criteria.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return documents;
	}


	public Documents findDocumentsByCandidateIdWithOtherDocName(int candidateId,String documentsSpecification,String documentFor,String otherDocName){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Documents documents = null;
		try{
			Criteria criteria = session.createCriteria(Documents.class,"doc");
			Criterion c1 = Restrictions.eq("doc.candidate.candidateId",candidateId);
			Criterion c2 = Restrictions.eq("doc.documentsSpecification",documentsSpecification);
			Criterion c3 = Restrictions.eq("doc.documentFor",documentFor);
			Criterion c4 = Restrictions.eq("doc.docOtherName",otherDocName);
			Criterion c5 = Restrictions.and(c1,c2);
			Criterion c6 = Restrictions.and(c5,c3);
			criteria.add(Restrictions.and(c6,c4));
			documents = (Documents) criteria.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return documents;
	}


	public void deleteDocument(Documents documents){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.delete(documents);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}



	public List<DocumentsModel> findAllDocuments(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<DocumentsModel> docs = null;
		try{
			String hql = "SELECT doc.fileName as fileName,doc.filePath as filePath,doc.createdDate as createdDate,doc.extension as extension,doc.docOtherName as docOtherName "
					+ "FROM Documents doc "
					+ "INNER JOIN doc.candidate can "
					+ "WHERE (can.candidateId = :canId AND doc.documentsSpecification = 'receiving') "
					+ "OR (can.candidateId = :canId AND doc.documentsSpecification = 'receiving' AND "
					+ "(doc.docOtherName = 'Offter Letter' OR doc.docOtherName = 'Datasheet Form' OR doc.docOtherName = 'Passport' "
					+ "OR doc.docOtherName = 'Voter Card' OR doc.docOtherName = 'Driving Licence' OR doc.docOtherName = 'SSN Form' "
					+ "OR doc.docOtherName = 'OPT Letter' OR doc.docOtherName = 'CPT Letter' OR doc.docOtherName = 'I983 Form' "
					+ "OR doc.docOtherName = 'Visa' OR doc.docOtherName = 'I9 Form' OR doc.docOtherName = 'W2 Form' "
					+ "OR doc.docOtherName = 'W4 Form' OR doc.docOtherName = 'W4 Form'))";
			Query query = session.createQuery(hql);
			query.setParameter("canId",candidateId);
			query.setResultTransformer(Transformers.aliasToBean(DocumentsModel.class));
			docs = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return docs;
	}



	public List<String> findAttachDocuments(List<Candidate> candidates,String[] documents){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<String> docs = null;
		try{
			String hql = "SELECT d.filePath "
					+ "FROM Documents d "
					+ "INNER JOIN d.candidate can "
					+ "WHERE d.documentsSpecification = 'receiving' AND (";

			for(int i=0;i<candidates.size();i++){
				if(i == candidates.size()-1){
					hql = hql + "can.candidateId ="+candidates.get(i).getCandidateId()+") AND (";
				}else{
					hql = hql + "can.candidateId ="+candidates.get(i).getCandidateId()+" OR ";
				}
			}

			for(int i=0;i<documents.length;i++){
				if( i != documents.length -1 ){
					hql = hql + "d.docOtherName='"+documents[i]+"' OR ";
				}else{
					hql = hql + "d.docOtherName='"+documents[i]+"')";
				}
			}
			Query query = session.createQuery(hql);
			docs = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return docs;
	}




}
