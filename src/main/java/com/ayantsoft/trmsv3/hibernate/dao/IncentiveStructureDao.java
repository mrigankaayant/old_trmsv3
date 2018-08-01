package com.ayantsoft.trmsv3.hibernate.dao;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.hibernate.Session;

import com.ayantsoft.trmsv3.hibernate.pojo.IncentiveStructure;
import com.ayantsoft.trmsv3.hibernate.util.HibernateUtil;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

@ManagedBean
@ApplicationScoped
public class IncentiveStructureDao implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4896404450265359003L;
	
	public List<IncentiveStructure> getIncentiveStructure(String incentiveFor){

		Session session=HibernateUtil.getSessionFactory().openSession();
		List<IncentiveStructure> IncentiveStructures = null;

		try{
			
			Criteria criteria = session.createCriteria(IncentiveStructure.class).addOrder(Order.asc("targetLowerLimit"));
			criteria.add(Restrictions.eq("typeFor",incentiveFor));
			IncentiveStructures = criteria.list();
			
		}catch(Exception e){
			e.printStackTrace();
			throw new HibernateException("Data access Exception");
		}finally{
			session.close();
		}
		return IncentiveStructures;
	}


}
