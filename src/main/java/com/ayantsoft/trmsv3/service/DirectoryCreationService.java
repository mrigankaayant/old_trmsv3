package com.ayantsoft.trmsv3.service;

import java.io.File;
import java.io.Serializable;

public class DirectoryCreationService implements Serializable {

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -250667509550691118L;
	
	public static boolean createDirectories(String rootPath, String rootDirName){
		boolean isCreate=false;
		try{
			
			 String folderNames[] = {"enrollment_form","Offter Letter","feedback_form","Datasheet Form","referencecheck_form","resume","PSR_form","questioner_form","Passport","Voter Card","Driving Licence","SSN Form","OPT Letter","CPT Letter","I983 Form","Visa","I9 Form","W2 Form","W4 Form","Others"};
			 boolean d = new File(rootPath+"/"+rootDirName).mkdir();
			 boolean s = new File(rootPath+"/"+rootDirName+"/generate").mkdir();
			 boolean w = new File(rootPath+"/"+rootDirName+"/receiving").mkdir();
			 boolean t = new File(rootPath+"/"+rootDirName+"/sending").mkdir();
			 for(int i=0;i<folderNames.length;i++){
				 new File(rootPath+"/"+rootDirName+"/generate/"+folderNames[i]).mkdir(); 
				 new File(rootPath+"/"+rootDirName+"/receiving/"+folderNames[i]).mkdir();
				 new File(rootPath+"/"+rootDirName+"/sending/"+folderNames[i]).mkdir(); 
			 }
			 isCreate = true;
		}catch(Exception e){
			e.printStackTrace();
		}
		return isCreate; 
	}

}
