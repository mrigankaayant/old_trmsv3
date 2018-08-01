package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import com.ayantsoft.trmsv3.hibernate.dao.IncentiveStructureDao;
import com.ayantsoft.trmsv3.hibernate.pojo.IncentiveStructure;

@ManagedBean
@ApplicationScoped
public class IncentiveStructureService implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1597137891550454903L;
	
	@ManagedProperty("#{incentiveStructureDao}")
	private IncentiveStructureDao incentiveStructureDao;
	
	List<IncentiveStructure> individualIncentiveStructures;
	List<IncentiveStructure> leaderIncentiveStructures;
	
	
	@PostConstruct
	public void init()
	{
		individualIncentiveStructures = incentiveStructureDao.getIncentiveStructure("individual");
		leaderIncentiveStructures = incentiveStructureDao.getIncentiveStructure("team leader");
	}
	
	public double getIndividualIncentive(final double totalPaidAmount)
	{

		double incentive = 0.0;
		for(IncentiveStructure incentiveStructure:individualIncentiveStructures){
			
			final double tagetLowerLimit = incentiveStructure.getTargetLowerLimit().doubleValue();
			final double tagetHigherLimit = incentiveStructure.getTargetHigherLimit().doubleValue();
			if(tagetLowerLimit <= totalPaidAmount && tagetHigherLimit > (totalPaidAmount+1)){
				
				incentive = incentiveStructure.getIncentiveInInr().doubleValue();
				break;
		    }
		}
		return incentive;
		
	}
	
	
	public Double getLeaderIncentive(Double totalPaidAmount)
	{
		double incentive = 0.0;
		for(IncentiveStructure incentiveStructure:leaderIncentiveStructures){
			
			final double tagetLowerLimit = incentiveStructure.getTargetLowerLimit().doubleValue();
			final double tagetHigherLimit = incentiveStructure.getTargetHigherLimit().doubleValue();
			if(tagetLowerLimit <= totalPaidAmount && tagetHigherLimit > (totalPaidAmount+1)){
				
				incentive = incentiveStructure.getIncentiveInInr().doubleValue();
				break;
		    }
		}
		return incentive;
	}

	public IncentiveStructureDao getIncentiveStructureDao() {
		return incentiveStructureDao;
	}

	public void setIncentiveStructureDao(IncentiveStructureDao incentiveStructureDao) {
		this.incentiveStructureDao = incentiveStructureDao;
	}

	public List<IncentiveStructure> getIndividualIncentiveStructures() {
		return individualIncentiveStructures;
	}

	public void setIndividualIncentiveStructures(List<IncentiveStructure> individualIncentiveStructures) {
		this.individualIncentiveStructures = individualIncentiveStructures;
	}

	public List<IncentiveStructure> getLeaderIncentiveStructures() {
		return leaderIncentiveStructures;
	}

	public void setLeaderIncentiveStructures(List<IncentiveStructure> leaderIncentiveStructures) {
		this.leaderIncentiveStructures = leaderIncentiveStructures;
	}

}
