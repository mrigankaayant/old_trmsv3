package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.primefaces.model.SortOrder;

import com.ayantsoft.trmsv3.hibernate.pojo.InterviewSchedule;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.pojo.ReSchedule;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class InterviewScheduleDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 7772230412578282358L;
	
	
	public List<InterviewSchedule> findinterviewSchedules(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<InterviewSchedule> candidates = null;
		try{
			String hql = "SELECT DISTINCT i "
					  + "FROM InterviewSchedule i "
					  + "JOIN FETCH i.candidate can "
					  + "LEFT OUTER JOIN FETCH i.reSchedules res "
					  + "WHERE can.candidateId = :canId";
			Query query = session.createQuery(hql);
			query.setParameter("canId",candidateId);
			candidates = query.list();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return candidates;
	}
	
	
	
	public void save(InterviewSchedule interviewSchedule){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(interviewSchedule);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}
	
	
	
	public String findMaxBillRate(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		String billRate = null;
		try{
			String hql = "SELECT max(i.billRate) "
					  + "FROM InterviewSchedule i "
					  + "INNER JOIN i.candidate c "
					  + "where c.candidateId = :canId ";
			Query query = session.createQuery(hql);
			query.setParameter("canId",candidateId);
			billRate = (String) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return billRate;
	}
	
	
	
	public Object[] schedulecandidatesFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters,boolean isSalesManager,int userId){

		Session session=HibernateUtil.getSessionFactory().openSession();
		Object[] resultWithCount = new Object[2];
		try
		{
			Criteria criteria = session.createCriteria(InterviewSchedule.class,"interviewSchedule");
			criteria.createAlias("interviewSchedule.candidate","candidate");
			criteria.createAlias("interviewSchedule.userProfile","userProfile");
			
			if (filters != null) {
				
				filters.forEach((k,v)->{
					if(k.equals("interviewRound")){
						criteria.add(Restrictions.eq(k, Integer.parseInt((String)v)));
					}else if(k.equals("interviewExpenseAmount")){
						criteria.add(Restrictions.eq(k,new BigDecimal((String)v)));
					}else if(k.equals("interviewDate")){
						criteria.add(Restrictions.eq(k,v));
					}else{
						criteria.add(Restrictions.ilike(k, (String)v, MatchMode.ANYWHERE));
					}
				});
			}
			
			if(isSalesManager){
				Criterion c1 = Restrictions.eq("candidate.hotlistStatus",true);
				Criterion c2 = Restrictions.isNull("candidate.isRemoveFromHotlist");
				Criterion c3 = Restrictions.eq("candidate.isRemoveFromHotlist",false);
				Criterion c4 = Restrictions.or(c2,c3);
				criteria.add(Restrictions.and(c1,c4));
			}else{
				Criterion c1 = Restrictions.eq("candidate.hotlistStatus",true); 
				Criterion c2 = Restrictions.isNull("candidate.isRemoveFromHotlist");
				Criterion c3 = Restrictions.eq("candidate.isRemoveFromHotlist",false);
				Criterion c4 = Restrictions.or(c2,c3); 
				Criterion c20 = Restrictions.and(c1,c4);
				Criterion c21 = Restrictions.eq("userProfile.userId",userId);
				criteria.add(Restrictions.and(c20,c21));
			}
			
			
			criteria.setProjection(Projections.rowCount());
			Long resultCount = (Long)criteria.uniqueResult();
			resultWithCount[0]=resultCount;
			criteria.setProjection(null);

			if(sortField != null){
				if(SortOrder.ASCENDING == sortOrder){
					criteria.addOrder(Order.asc(sortField));
				}else if(SortOrder.DESCENDING == sortOrder){
					criteria.addOrder(Order.desc(sortField));
				}
			}

			criteria.setFirstResult(first);
			criteria.setMaxResults(pageSize);

			criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
			List<InterviewSchedule> interviewSchedules = criteria.list();
			resultWithCount[1]=interviewSchedules;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new HibernateException("Data access exception.");
		}
		finally{
			session.close();
		}
		
		return resultWithCount;
	}
	
	
	public InterviewSchedule findinterviewSchedulesbyId(int interviewScheduleId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		InterviewSchedule interviewSchedule = null;
		try{
			String hql = "FROM InterviewSchedule i "
					  + "JOIN FETCH i.candidate can "
					  + "LEFT OUTER JOIN FETCH i.reSchedules res "
					  + "WHERE i.id = :isid";
			Query query = session.createQuery(hql);
			query.setParameter("isid",interviewScheduleId);
			interviewSchedule = (InterviewSchedule) query.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return interviewSchedule;
	}

}
