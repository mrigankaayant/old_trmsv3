package com.ayantsoft.trmsv3.service;

import java.io.Serializable;

public class SignatureCreateService implements Serializable{

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = 2568643172303534698L;
	
	
	public static String createSignature(String name,String designation,String telephone,String xtn,String linkedin){
		String template = "<b><span style=\"color: black\"></span></b><b><span style=\"color: black\"></span></b><b><span style=\"color:"
				+ " black\"></span></b><b><span style=\"color: black\"></span></b><p class=\"MsoNormal\" style=\"background: white\">"
				+ "<b><i><span style=\"font-size: 10.0pt; font-family: \" verdana\",sans-serif;=\"\" color:=\"\" #4472c4\"=\"\">"
				+ "Thank you,</span></i></b><span style=\"font-size: 12.0pt; font-family: \" verdana\",sans-serif;=\"\" color:=\"\""
				+ " #4472c4\"=\"\"></span></p><p class=\"MsoNormal\" style=\"background: white\"><b><span style=\"color: #4472C4\">"
				+ name+" </span></b></p><p class=\"MsoNormal\" style=\"background: white\"><b><span style=\"font-size: 9.0pt;"
				+ " color: #002060\">"+designation+" </span></b><span style=\"font-size: 9.0pt; font-family: \" verdana\",sans-serif;=\"\""
				+ " color:=\"\" #002060\"=\"\"></span></p><p class=\"MsoNormal\" style=\"background: white\"><span style=\"font-size: 9.0pt;"
				+ " color: navy\">11210 Steeplecrest Dr. suite 120 </span></p><p class=\"MsoNormal\" style=\"background: white\"><span style=\"font-size: 9.0pt;"
				+ " color: navy\">Houston ,TX 77065 ||T-"+telephone+"</span></p><p class=\"MsoNormal\" style=\"background: white\">"
				+ "<span style=\"font-size: 9.0pt; color: navy\">Desk: 346-444-8100 - Xtn:"+xtn+"</span></p><p class=\"MsoNormal\""
				+ " style=\"background: white\">";
		        
		        if(linkedin != null){
		        	template = template + "<span style=\"font-size: 9.0pt; color: navy\">LinkedIn:"+linkedin+"<br>";
		        }
		
		        template = template + "</span></p><p class=\"MsoNormal\" style=\"background: white\"><span style=\"font-size: 9.0pt; color: navy\">"
				+ "</span><span style=\"color: #4472C4\"><img style=\"width: 1.3229in; height: .4062in\" id=\"Picture_x0020_3\""
				+ " src=\"http://ayantsoft.com:2095/cpsess8120014178/3rdparty/roundcube/?_task=mail&amp;_action=get&amp;_"
				+ "mbox=INBOX&amp;_uid=1351&amp;_token=d4215a8811cd59a2e06a67edab05115b&amp;_part=2&amp;_embed=1&amp;_mimeclass=image\""
				+ " alt=\"Global IT Experts\" height=\"39\" width=\"127\"></span><span style=\"color: #2F5496\"></span></p><p class=\"MsoNormal\""
				+ " style=\"background: white\"><b><span style=\"color: black\">(An E-Verify Company)</span></b><span style=\"font-size: 9.0pt; font-family: \" arial\","
				+ "sans-serif;=\"\" color:=\"\" black\"=\"\"></span></p><div class=\"MsoNormal\" style=\"text-align: center;"
				+ " background: white\" align=\"center\"><span style=\"font-size: 9.0pt; font-family: \" arial\",sans-serif;=\"\" "
				+ "color:=\"\" black\"=\"\"><hr style=\"color: #002740\" size=\"2\" align=\"center\" width=\"100%\">"
				+ "</span></div><b><i><span style=\"font-size: 8.0pt; font-family: \" arial\",sans-serif;=\"\" color:=\"\" black\"=\"\">"
				+ "The greatest compliment to our business is a referral.&nbsp; If you know of someone within&nbsp;the IT or "
				+ "Communications industry who is looking for work, or who is hiring, Global IT Experts can be of assistance.&nbsp;"
				+ " Please feel free to pass on our contact information.&nbsp; Thank you.</span></i></b><b><i><span style=\"font-size: 8.0pt; font-family: \" arial\",sans-serif;=\"\" color:=\"\" black\"=\"\">"
				+ "</span></i></b><span style=\"font-size: 8.0pt; font-family: \" verdana\",sans-serif;=\"\" color:=\"\" black\"=\"\">"
				+ "</span>";
		        
		        return template;
	}

}
