package com.ahmad.satkerpolri.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class dateformat {
	
	public static Logger logger = LogManager.getLogger(dateformat.class.getName());
	

	public static String formatDate5(String tanggal) {
		Date result =null;
		String hasil = "";
		try{
			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			Calendar cal = Calendar.getInstance();
			cal.setTime(df.parse(tanggal));
			result = cal.getTime();
			
			SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
			hasil = df2.format(result);
		}catch(Exception e){
			logger.error("Error formatDate5 "+e);
		}
		return hasil;
	}
}