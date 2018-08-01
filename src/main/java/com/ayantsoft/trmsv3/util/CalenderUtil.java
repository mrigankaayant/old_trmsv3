package com.ayantsoft.trmsv3.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CalenderUtil implements Serializable{

	/**
	 *serialVersionUID 
	 */
	private static final long serialVersionUID = -563981020464013800L;
	
	public static Date createStartDate(Date dateFromGUI) throws ParseException
	{
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(dateFromGUI);
		 cal.set(Calendar.DAY_OF_MONTH,cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		 return cal.getTime(); 	
	}
	
	
	public static Date createEndDate(Date dateFromGUI) throws ParseException
	{
		
		 Calendar cal = Calendar.getInstance();
		 cal.setTime(dateFromGUI);
		 cal.set(Calendar.DAY_OF_MONTH,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		 return cal.getTime(); 
	}
	
	
	public static List<String> findWeekOfMonth(int year,int month){
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        List<String> weekdates = new ArrayList<String>();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DAY_OF_MONTH,month);
        while (c.get(Calendar.MONTH) == month) {
              while (c.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                c.add(Calendar.DAY_OF_MONTH, -1);
              }
              String sdate = format.format(c.getTime());
              c.add(Calendar.DAY_OF_MONTH, 6);
              String eDate = format.format(c.getTime());
              c.add(Calendar.DAY_OF_MONTH, 1);
              weekdates.add(sdate+" To "+eDate);
        }
        return weekdates;
	}
	
	
	public static Date createStartDateOfYear(Date currentDate) throws ParseException{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(currentDate);
	    int year = cal.get(Calendar.YEAR);
		String d = "10-01-"+Integer.toString(year);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(d);
		
		Calendar cal2 = Calendar.getInstance();
	    cal2.setTime(date);
		cal2.set(Calendar.DAY_OF_MONTH,cal2.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal2.getTime(); 
	}
	
	
	
	public static Date createEndDateOfYear(Date currentDate) throws ParseException{
		Calendar cal = Calendar.getInstance();
	    cal.setTime(currentDate);
	    int year = cal.get(Calendar.YEAR);
		String d = "10-12-"+Integer.toString(year);
		DateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		Date date = format.parse(d);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date);
		cal2.set(Calendar.DAY_OF_MONTH,cal2.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal2.getTime(); 
	}

}
