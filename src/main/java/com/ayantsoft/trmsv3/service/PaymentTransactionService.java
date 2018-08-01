package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.PaymentTransactionDao;
import com.ayantsoft.trmsv3.hibernate.pojo.PaymentTransaction;

@ManagedBean
@ApplicationScoped
public class PaymentTransactionService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -507108719894672726L;
	
	
	@ManagedProperty("#{paymentTransactionDao}")
	private PaymentTransactionDao paymentTransactionDao;
	
	
	public List<PaymentTransaction> findPaymentTransactionByCandidateId(int candidateId){
		return paymentTransactionDao.findPaymentTransactionByCandidateId(candidateId);
	}
	
	
	public void save(PaymentTransaction paymentTransaction){
		paymentTransactionDao.save(paymentTransaction);
	}
	
	public BigDecimal findTotalPaidAmount(int candidateId){
		return paymentTransactionDao.findTotalPaidAmount(candidateId);
	}
	
	public List<PaymentTransaction> findPaymentTransactionBySearchByAndSearchValue(String searchBy,String searchValue){
		return paymentTransactionDao.findPaymentTransactionBySearchByAndSearchValue(searchBy, searchValue);
	}
	
	public List<PaymentTransaction> findPaymentTransactionByDateRange(String searchBy,Date startDate,Date endDate){
		return paymentTransactionDao.findPaymentTransactionByDateRange(searchBy, startDate, endDate);
	}


	public PaymentTransactionDao getPaymentTransactionDao() {
		return paymentTransactionDao;
	}


	public void setPaymentTransactionDao(PaymentTransactionDao paymentTransactionDao) {
		this.paymentTransactionDao = paymentTransactionDao;
	}
}
