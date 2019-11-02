package com.mingri.commons.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

public class DateTool {
	public static final String yMdHms="yyyyMMddHHmmss";
	public static final String yMdHm="yyyyMMddHHmm";
	public static final String y_M_d="yyyy-MM-dd";
	public static final DateFormat y_M_d_FORMAT = new SimpleDateFormat(y_M_d);
	public static final DateFormat yyyyMMdd_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat MMddyyyy_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat yyyyMMddHHmmss_FORMAT = new SimpleDateFormat(yMdHms);
	public static final DateFormat yyyyMMddHHmm_FORMAT = new SimpleDateFormat(yMdHm);
	
	
	
	

	
	public static LocalDateTime getLastTimeOfMonth(int year,int month){
		LocalDateTime ldt=LocalDateTime.of(year, month+1, 1, 0, 0,0);
		ldt=ldt.plusDays(-1);
		return ldt;
	}
}
