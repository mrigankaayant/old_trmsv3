package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;
import com.ayantsoft.trmsv3.hibernate.pojo.Priority;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;

@ManagedBean
@ApplicationScoped
public class CandidateDao implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -8174330569228906858L;


	public void save(Candidate candidate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		try{
			session.beginTransaction();
			session.saveOrUpdate(candidate);
			session.getTransaction().commit();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
	}



	public Object[] candidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters){

		Session session=HibernateUtil.getSessionFactory().openSession();
		Object[] resultWithCount = new Object[2];
		try
		{
			Criteria criteria = session.createCriteria(Candidate.class,"candidate");

			if (filters != null) {
				filters.forEach((k,v)->{
					if(k.equals("candidateId")){
						criteria.add(Restrictions.eq(k, Integer.parseInt((String)v)));
					}else if(k.equals("totalAmount")){
						criteria.add(Restrictions.eq(k,new BigDecimal((String)v)));
					}else if(k.equals("enrollmentDate") || k.equals("enrollmentNextFollowupDate")|| k.equals("trainingNextFollowupDate")||k.equals("resumeNextFollowupDate")||k.equals("mockNextFollowupDate")||k.equals("referenceNextFollowupDate")||k.equals("preHotlistNextFollowupDate")){
						criteria.add(Restrictions.eq(k,v));
					}else if(k.equals("referenceCheckStatus")){
						criteria.add(Restrictions.eq(k, Boolean.parseBoolean((String)v)));
					}else{
						criteria.add(Restrictions.ilike(k, (String)v, MatchMode.ANYWHERE));
					}

				});
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
			List<Candidate> candidates = criteria.list();
			resultWithCount[1]=candidates;
		}catch(Exception e){
			e.printStackTrace();
			throw new HibernateException("Data access exception.");
		}
		finally{
			session.close();
		}

		return resultWithCount;
	}


	public Candidate findCandidateById(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Candidate candidate = null;
		try{
			Criteria criteria = session.createCriteria(Candidate.class,"can");
			criteria.add(Restrictions.eq("can.candidateId",candidateId));
			candidate = (Candidate) criteria.uniqueResult();
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}

		return candidate;
	}



	public boolean checkCandidate(String contactNo,String emailId,int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		boolean isExist = false;
		try{
			String hql = "SELECT can.candidateId "
					+ "FROM Candidate can "
					+ "WHERE can.candidateId = :candidateId OR can.contactNo = :phNo OR can.emailId = :emailId ";
			Query query = session.createQuery(hql);
			query.setParameter("candidateId",candidateId);
			query.setParameter("phNo",contactNo);
			query.setParameter("emailId",emailId);
			Integer canId = (Integer) query.uniqueResult();
			if(canId != null){
				isExist = true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}

		return isExist;
	}



	public Set<Priority> findPriorityByCandidateId(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<Priority> priorities = null;
		try{
			String hql = "FROM Candidate can "
					+"JOIN FETCH can.priorities p "
					+"WHERE can.candidateId = :canId";
			Query query = session.createQuery(hql);
			query.setParameter("canId",candidateId);
			Candidate candidate = (Candidate) query.uniqueResult();
			if(candidate != null){
				priorities = candidate.getPriorities();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}

		return priorities;
	}



	public List<Candidate> findPrehotlistCandidate(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.referenceCheckStatus is true";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findHotlistCandidate(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.hotlistStatus is true";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}


	public List<Candidate> candidateBySearchByAndSearchValue(String searchBy,String searchValue){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE ";
			if("Skill Wise".equals(searchBy)){
				hql = hql + "can.primarySkill = :canSkill";
			}
			if("Visa Wise".equals(searchBy)){
				hql = hql + "can.visaStatus = :canVisa";
			}
			hql = hql + " AND (can.mockStatus = 'ON MOCK' OR can.mockStatus = 'RE-MOCK' OR can.mockStatus = 'ON GRADUATION' "
					+ "OR can.mockStatus = 'RE-GRADUATION' OR can.mockStatus = 'MOCK COMPLETED' OR can.mockStatus = 'GRADUATION COMPLETED' )";
			Query query = session.createQuery(hql);
			if("Skill Wise".equals(searchBy)){
				query.setParameter("canSkill", searchValue);
			}
			if("Visa Wise".equals(searchBy)){
				query.setParameter("canVisa", searchValue);
			}
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}


	public List<Candidate> getcandidateWithReference(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidateReferenceReportList = null;
		try{
			String hql =  "FROM Candidate can "
					+"WHERE can.mockStatus = 'GRADUATION COMPLETED' ";
			Query query = session.createQuery(hql);
			candidateReferenceReportList = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}

		return candidateReferenceReportList;
	}



	public List<Candidate> findCandidateForResumeReport(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.resumePrepStatus = 'CEB TEST COMPLETED' "
					+ "OR can.resumePrepStatus = 'RESUME DOCS SENT' "
					+ "OR can.resumePrepStatus = 'RESUME PREP ON GOING' ";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}

		return candidates;
	}


	public List<Candidate> findMockCandidateByStartdateAndEndDate(Date startDate,Date endDate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE ((can.mockStatus = 'ON MOCK' OR can.mockStatus = 'RE-MOCK' OR can.mockStatus = 'ON GRADUATION' OR can.mockStatus = 'MOCK COMPLETED' "
					+ "OR can.mockStatus = 'GRADUATION COMPLETED' OR can.mockStatus = 'RE-GRADUATION') AND (can.mockScheduleDate between :sDate and :eDate))";
			Query query = session.createQuery(hql);
			query.setParameter("sDate",startDate);
			query.setParameter("eDate",endDate);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> candidateBySearchByAndSearchValueForTraning(String searchBy,String searchValue){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{

			String hql = "FROM Candidate can "
					+ "WHERE ";
			if("Skill Wise".equals(searchBy)){
				hql = hql + "can.primarySkill = :canSkill";
			}
			if("Batch Wise".equals(searchBy)){
				hql = hql + "can.batchCode = :batchCode";
			}

			if("Trainer Wise".equals(searchBy)){
				hql = hql + "can.trainerName = :trainerName";
			}
			if("Recruiter Wise".equals(searchBy)){
				hql = hql + "can.recruiterName = :recruiterName ";	
			}
			hql = hql + " AND (can.trainingStatus = 'ONGOING' OR can.trainingStatus = 'TFF SENT' OR can.trainingStatus = 'TFF RECEIVED' OR can.trainingStatus = 'COMPLETED')";
			Query query = session.createQuery(hql);
			if("Skill Wise".equals(searchBy)){
				query.setParameter("canSkill", searchValue);
			}
			if("Batch Wise".equals(searchBy)){
				query.setParameter("batchCode", searchValue);
			}
			if("Trainer Wise".equals(searchBy)){
				query.setParameter("trainerName", searchValue);
			}
			if("Recruiter Wise".equals(searchBy)){
				query.setParameter("recruiterName", searchValue);
			}
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findCandidatesForEnrollment(String searchBy,String searchValue){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{

			String hql = "FROM Candidate can "
					+ "WHERE ";
			if("Service Wise".equals(searchBy)){
				hql = hql + "can.service = :service";
			}
			hql = hql + " AND (can.enrollmentDate is not null)";
			Query query = session.createQuery(hql);
			if("Service Wise".equals(searchBy)){
				query.setParameter("service", searchValue);
			}
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}


	public List<Candidate> findCandidatesForEnrollmentWithDateRange(String searchBy,Date startDate,Date endDate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			if("Month Wise".equals(searchBy) || "Year Wise".equals(searchBy) || "Week Wise".equals(searchBy)){
				String hql = "FROM Candidate can "
						+ "WHERE (can.enrollmentDate is not null AND can.enrollmentDate between :sDate and :eDate )";
				Query query = session.createQuery(hql);
				query.setParameter("sDate",startDate);
				query.setParameter("eDate",endDate);
				candidates = query.list();
			}
			if("Day Wise".equals(searchBy)){
				String hql = "FROM Candidate can "
						+ "WHERE can.enrollmentDate = :enrollmentDate";
				Query query = session.createQuery(hql);
				query.setParameter("enrollmentDate",startDate);
				candidates = query.list();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findIncentiveDueCandidate(Date startDate,Date endDate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.enrollmentDate is not null "
					+ "AND can.enrollmentDate between :startDate and :endDate "
					+ "AND can.amountPaid > 350 "
					+ "AND (can.incentivePaidStatus is 'NOT PAID' OR can.incentivePaidStatus is 'DECISION PENDING') ";
			Query query = session.createQuery(hql);
			query.setParameter("startDate",startDate);
			query.setParameter("endDate",endDate);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}




	public List<Candidate> findEnrommentFormBySearchByDateRange(String searchBy,Date startDate,Date endDate){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE ";
			if("Month Wise".equals(searchBy)){
				hql = hql + "(can.enrollmentFormStatus is 'SENT' OR can.enrollmentFormStatus is 'RECEIVED') "
						+ "AND ((can.receivingEnrollmentDate between :startDate and :endDate) "
						+ "OR (can.sendingEnrollmentDate between :startDate and :endDate)) ";
			}
			if("Day Wise".equals(searchBy)){
				hql = hql + "(can.enrollmentFormStatus is 'SENT' OR can.enrollmentFormStatus is 'RECEIVED') "
						+ "AND ((can.receivingEnrollmentDate = :selectedDate ) "
						+ "OR (can.sendingEnrollmentDate = :selectedDate)) ";
			}
			Query query = session.createQuery(hql);
			if("Month Wise".equals(searchBy)){
				query.setParameter("startDate",startDate);
				query.setParameter("endDate",endDate);
			}
			if("Day Wise".equals(searchBy)){
				query.setParameter("selectedDate",startDate);
			}
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}
	
	
	
	public Object[] hotlistcandidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters,boolean isSalesManager,List<Priority> priority){
		Session session=HibernateUtil.getSessionFactory().openSession();
		Object[] resultWithCount = new Object[2];
		try
		{
			Criteria criteria = session.createCriteria(Candidate.class,"candidate");
			if (filters != null) {
				
				filters.forEach((k,v)->{
					if(k.equals("candidateId")){
						criteria.add(Restrictions.eq(k, Integer.parseInt((String)v)));
					}else if(k.equals("totalAmount")){
						criteria.add(Restrictions.eq(k,new BigDecimal((String)v)));
					}else if(k.equals("enrollmentNextFollowupDate")|| k.equals("trainingNextFollowupDate")||k.equals("resumeNextFollowupDate")||k.equals("mockNextFollowupDate")||k.equals("referenceNextFollowupDate")||k.equals("preHotlistNextFollowupDate")||k.equals("dateOnHotlist")){
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
				criteria.createAlias("candidate.priorities","p");
				Criterion c1 = Restrictions.eq("candidate.hotlistStatus",true); 
				Criterion c2 = Restrictions.isNull("candidate.isRemoveFromHotlist");
				Criterion c3 = Restrictions.eq("candidate.isRemoveFromHotlist",false);
				Criterion c4 = Restrictions.or(c2,c3); 
				Criterion c20 = Restrictions.and(c1,c4);
				int size = priority.size();
				if(size == 1){
				   Criterion c5 = Restrictions.eq("p.priority",priority.get(0).getPriority());
				   criteria.add(Restrictions.and(c20,c5));
				}
				if(size == 2){
					Criterion c6 = Restrictions.eq("p.priority",priority.get(0).getPriority());
					Criterion c7 = Restrictions.eq("p.priority",priority.get(1).getPriority());
					Criterion c14 = Restrictions.or(c6, c7);
					criteria.add(Restrictions.and(c20,c14));
				}
				if(size == 3){
					Criterion c8 = Restrictions.eq("p.priority",priority.get(0).getPriority());
					Criterion c9 = Restrictions.eq("p.priority",priority.get(1).getPriority());
					Criterion c10 = Restrictions.eq("p.priority",priority.get(2).getPriority());
					Criterion c15 = Restrictions.or(c8, c9);
					Criterion c16 = Restrictions.or(c15, c10);
					criteria.add(Restrictions.and(c20,c16));
				}
				if(size == 4){
					Criterion c10 = Restrictions.eq("p.priority",priority.get(0).getPriority());
					Criterion c11 = Restrictions.eq("p.priority",priority.get(1).getPriority());
					Criterion c12 = Restrictions.eq("p.priority",priority.get(2).getPriority());
					Criterion c13 = Restrictions.eq("p.priority",priority.get(3).getPriority());
					Criterion c17 = Restrictions.or(c10, c11);
					Criterion c18 = Restrictions.or(c17, c12);
					Criterion c19 = Restrictions.or(c18, c13);
					criteria.add(Restrictions.and(c20,c19));
				}
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
			List<Candidate> candidates = criteria.list();
			resultWithCount[1]=candidates;
			
		}catch(Exception e){
			e.printStackTrace();
			throw new HibernateException("Data access exception.");
		}
		finally{
			session.close();
		}
		
		return resultWithCount;
	}
	


	public Set<Priority> findCandidatePriority(int candidateId){
		Session session = HibernateUtil.getSessionFactory().openSession();
		Set<Priority> priorities = null;
		try{
			String hql = "SELECT DISTINCT can "
					+ "FROM Candidate can "
					+ "JOIN FETCH can.priorities p "
					+ "WHERE can.candidateId = :canId";
			Query query = session.createQuery(hql);
			query.setParameter("canId",candidateId);
			Candidate c = (Candidate) query.uniqueResult();
			if(c != null){
				if(c.getPriorities() != null && c.getPriorities().size() >0){
					priorities = c.getPriorities();
				}
			}
		}catch(HibernateException he){
			he.printStackTrace();
			throw he;
		}finally{
			session.close();
		}
		return priorities;
	}


	public List<Candidate> findRemoveCandidateFromHotlist(boolean isSalesManager,List<Priority> priority){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "SELECT DISTINCT can "
					+ "FROM Candidate can ";
			if(isSalesManager){
				hql = hql + "WHERE can.isRemoveFromHotlist = 1 ";
			}else{
				hql = hql + "INNER JOIN can.priorities p where can.isRemoveFromHotlist = 1 AND ( ";
				for(int i=0;i<=priority.size()-1;i++){
					if(i == priority.size()-1){
						hql = hql + "p.priority is '"+priority.get(i).getPriority()+"')";
					}else{
						hql = hql +"p.priority is '"+priority.get(i).getPriority()+"' OR ";
					}
				}
			}
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findTrainingCandidate(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.enrollmentFormStatus = 'RECEIVED'";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findResumeCandidate(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.trainingStatus = 'TFF RECEIVED' or can.trainingStatus = 'COMPLETED'";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}



	public List<Candidate> findMockCandidate(){
		Session session = HibernateUtil.getSessionFactory().openSession();
		List<Candidate> candidates = null;
		try{
			String hql = "FROM Candidate can "
					+ "WHERE can.resumePrepStatus = 'RESUME PREP COMPLETED'";
			Query query = session.createQuery(hql);
			candidates = query.list();
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			session.close();
		}
		return candidates;
	}
	
	
	public Object[] terminatedCandidateFilter(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String,Object> filters){

		Session session=HibernateUtil.getSessionFactory().openSession();
		Object[] resultWithCount = new Object[2];
		try
		{
			Criteria criteria = session.createCriteria(Candidate.class,"candidate");
			criteria.add(Restrictions.eq("candidate.isTerminated", "TERMINATED"));
			if (filters != null) {
				filters.forEach((k,v)->{
					if(k.equals("candidateId")){
						criteria.add(Restrictions.eq(k, Integer.parseInt((String)v)));
					}else if(k.equals("totalAmount")){
						criteria.add(Restrictions.eq(k,new BigDecimal((String)v)));
					}else if(k.equals("enrollmentDate") || k.equals("enrollmentNextFollowupDate")|| k.equals("trainingNextFollowupDate")||k.equals("resumeNextFollowupDate")||k.equals("mockNextFollowupDate")||k.equals("referenceNextFollowupDate")||k.equals("preHotlistNextFollowupDate")){
						criteria.add(Restrictions.eq(k,v));
					}else if(k.equals("referenceCheckStatus")){
						criteria.add(Restrictions.eq(k, Boolean.parseBoolean((String)v)));
					}else{
						criteria.add(Restrictions.ilike(k, (String)v, MatchMode.ANYWHERE));
					}

				});
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
			List<Candidate> candidates = criteria.list();
			resultWithCount[1]=candidates;
		}catch(Exception e){
			e.printStackTrace();
			throw new HibernateException("Data access exception.");
		}
		finally{
			session.close();
		}

		return resultWithCount;
	}

}
