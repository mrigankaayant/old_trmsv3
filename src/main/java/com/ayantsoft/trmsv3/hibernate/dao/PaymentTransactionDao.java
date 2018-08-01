package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.PaymentTransaction;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class PaymentTransactionDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 9051490750028011135L;
	
	
	public List<PaymentTransaction> findPaymentTransactionByCandidateId(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<PaymentTransaction> paymentTransactions = null;
		try{
			Criteria criteria = session.createCriteria(PaymentTransaction.class,"cp");
			criteria.createAlias("cp.candidate","c");
			criteria.createAlias("cp.userProfile","u");
			criteria.add(Restrictions.eq("c.candidateId",candidateId));
			paymentTransactions = criteria.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return paymentTransactions;
	}
	
	
	
	public void save(PaymentTransaction paymentTransaction){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(paymentTransaction);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	public BigDecimal findTotalPaidAmount(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		BigDecimal totalPaidAmount = null;
		try{
			String hql = "SELECT sum(pt.paidAmount) "
                        +"from PaymentTransaction pt "
                        +"inner join pt.candidate can " 
                        +"where can.candidateId = :candidateId ";
			Query query = session.createQuery(hql);
			query.setParameter("candidateId",candidateId);
			totalPaidAmount = (BigDecimal) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		
		return totalPaidAmount;
	}
	
	
	
	public List<PaymentTransaction> findPaymentTransactionBySearchByAndSearchValue(String searchBy,String searchValue){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<PaymentTransaction> paymentTransactions = null;
		try{
			String hql = "FROM PaymentTransaction p "
					   + "JOIN FETCH p.candidate can "
					   + "WHERE ";
			if("Skill Wise".equals(searchBy)){
				hql = hql + "can.primarySkill = :primarySkill ";
			}
			if("Recruiter Wise".equals(searchBy)){
				hql = hql + "can.recruiterName = :recruiterName ";
			}
			Query query = session.createQuery(hql);
			if("Skill Wise".equals(searchBy)){
				query.setParameter("primarySkill",searchValue);
			}
			if("Recruiter Wise".equals(searchBy)){
				query.setParameter("recruiterName",searchValue);
			}
			paymentTransactions = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return paymentTransactions;
	}
	
	
	public List<PaymentTransaction> findPaymentTransactionByDateRange(String searchBy,Date startDate,Date endDate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<PaymentTransaction> paymentTransactions = null;
		try{
			String hql = "FROM PaymentTransaction p "
					   + "JOIN FETCH p.candidate can "
					   + "WHERE ";
			if("Month Wise".equals(searchBy)){
				hql = hql + "p.paymentDate between :startDate and :endDate";
			}
			if("Day Wise".equals(searchBy)){
				hql = hql + "p.paymentDate = :paymentDate";
			}
			Query query = session.createQuery(hql);
			if("Month Wise".equals(searchBy)){
				query.setParameter("startDate",startDate);
				query.setParameter("endDate",endDate);
			}
			if("Day Wise".equals(searchBy)){
				query.setParameter("paymentDate",startDate);
			}
			paymentTransactions = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return paymentTransactions;
	}
}
