package com.ayantsoft.trmsv3.service;

import java.io.Serializable;
import java.util.List;

import com.ayantsoft.trmsv3.hibernate.pojo.Candidate;

public class MailMessageService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -1000154305332835177L;

	public static String setupMessageForTemplate1(String name,String skill,String currentLoc,String prefLocation,String visa){

		String msg = "<span style=\"font-weight: bold;\"><span style=\"font-family: Narrow;\"><span style=\"color: rgb(0, 0, 153);\">Details Of "+name+"</span>"
				+ "</span></span><br><br><table border=\"1\"><tbody><tr><th>Description</th><th>Value</th></tr><tr><td>Name</td><td>"+name+"</td></tr><tr><td>";
		if(skill != null){
			msg = msg + "Primary Skill</td><td>"+skill+"</td></tr>";
		}else{
			msg = msg + "Primary Skill</td><td>N/A</td></tr>";
		}
		if(currentLoc != null){
			msg = msg + "<tr><td>Current Location</td><td>"+currentLoc+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Current Location</td><td>N/A</td></tr>";
		}
		if(prefLocation != null){
			msg = msg + "<tr><td>Prefered Location</td><td>"+prefLocation+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Prefered Location</td><td>N/A</td></tr>";
		}
		if(visa != null){
			msg = msg + "<tr><td>Visa</td><td>"+visa+"</td></tr></tbody></table>";
		}else{
			msg = msg + "<tr><td>Visa</td><td>N/A</td></tr></tbody></table>";
		}

		return msg;

	}


	public static String setupMessageForTemplate2(String name,String skill,String currentLoc,String prefLocation,String visa,String email,String phoneNo){

		String msg = "<span style=\"font-weight: bold;\"><span style=\"font-family: Narrow;\"><span style=\"color: rgb(0, 0, 153);\">Details Of "+name+"</span>"
				+ "</span></span><br><br><table border=\"1\"><tbody><tr><th>Description</th><th>Value</th></tr><tr><td>Name</td><td>"+name+"</td></tr><tr><td>";
		if(skill != null){
			msg = msg + "Primary Skill</td><td>"+skill+"</td></tr>";
		}else{
			msg = msg + "Primary Skill</td><td>N/A</td></tr>";
		}
		if(currentLoc != null){
			msg = msg + "<tr><td>Current Location</td><td>"+currentLoc+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Current Location</td><td>N/A</td></tr>";
		}
		if(prefLocation != null){
			msg = msg + "<tr><td>Prefered Location</td><td>"+prefLocation+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Prefered Location</td><td>N/A</td></tr>";
		}
		if(visa != null){
			msg = msg + "<tr><td>Visa</td><td>"+visa+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Visa</td><td>N/A</td></tr>";
		}
		if(email != null){
			msg = msg + "<tr><td>Email</td><td>"+email+"</td></tr>";
		}else{
			msg = msg + "<tr><td>Email</td><td>N/A</td></tr>";
		}
		if(phoneNo != null){
			msg = msg + "<tr><td>Phone No</td><td>"+phoneNo+"</td></tr></tbody></table>";
		}else{
			msg = msg + "<tr><td>Phone No</td><td>N/A</td></tr></tbody></table>";
		}
		return msg;
	}


	public static String createMessageForTemplate1(List<Candidate> candidates){
		String msg = null;
		for(Candidate can:candidates){
			if(msg == null){
				msg = setupMessageForTemplate1(can.getCandidateName(),can.getPrimarySkill(),can.getCurrentLoc(),can.getPreferredLoc(),can.getVisaStatus());
			}else{
				msg = msg + "<br>" + setupMessageForTemplate1(can.getCandidateName(),can.getPrimarySkill(),can.getCurrentLoc(),can.getPreferredLoc(),can.getVisaStatus());
			    
			}
		}

		return msg;
	}
	
	
	public static String createMessageForTemplate2(List<Candidate> candidates){
		String msg = null;
		for(Candidate can:candidates){
			if(msg == null){
				msg = setupMessageForTemplate2(can.getCandidateName(),can.getPrimarySkill(),can.getCurrentLoc(),can.getPreferredLoc(),can.getVisaStatus(),can.getEmailId(),can.getContactNo());
			}else{
				msg = msg + "<br>" + setupMessageForTemplate2(can.getCandidateName(),can.getPrimarySkill(),can.getCurrentLoc(),can.getPreferredLoc(),can.getVisaStatus(),can.getEmailId(),can.getContactNo());
			}
		}

		return msg;
	}


	public static String createSalesMessage(String toEmail,String formEmail,String subject,String message){
		String msg = "<span style=\"font-weight: bold;\">To :</span> &nbsp; &nbsp; &nbsp; &nbsp;"+toEmail+"<div><span style=\"font-weight: bold;\">"
				+ "Form :</span> &nbsp;"+formEmail+"</div><div><span style=\"font-weight: bold;\">Subject :</span><span style=\"font-weight: normal;\">"+subject+""
				+ "</span></div><div><br></div><div>"+message+"</div>";
		return msg;
	}



}
