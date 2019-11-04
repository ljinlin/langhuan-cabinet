package com.mingri.langhuan.cabinet.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class DateTool {
	
	private DateTool () {}
	
	public static final String yMdHms="yyyyMMddHHmmss";
	public static final String yyMMddHHmmss="yyMMddHHmmss";
	public static final String yMdHm="yyyyMMddHHmm";
	public static final String yMdH="yyyyMMddHH";
	public static final String y_M_d="yyyy-MM-dd";
	public static final String y_M_d_h_m_s="yyyy-MM-dd HH:mm:ss";
	public static final String yMd="yyyyMMdd";

	public static final ThreadLocal<DateFormat> y_M_d_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat(y_M_d));
	public static final ThreadLocal<DateFormat> yyyyMMdd_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat(yMd));
	public static final ThreadLocal<DateFormat> MMddyyyy_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat("MM/dd/yyyy"));
	public static final ThreadLocal<DateFormat> DATETIME_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.y_M_d_h_m_s));
	public static final ThreadLocal<DateFormat> yyyyMMddHHmmss_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.yMdHms));
	public static final ThreadLocal<DateFormat> yyyyMMddHHmm_FORMAT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.yMdHm));
	public static final ThreadLocal<DateFormat> yyyyMMddHH_FORMAT =ThreadLocal.withInitial(()->new SimpleDateFormat(DateTool.yMdH));
	
	public static final DateTimeFormatter y_M_d_TFORMAT = DateTimeFormatter.ofPattern(y_M_d);
	public static final DateTimeFormatter yyyyMMdd_TFORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");
	public static final DateTimeFormatter MMddyyyy_TFORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static final DateTimeFormatter DATETIME_TFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter yyMMddHHmmss_TFORMAT = DateTimeFormatter.ofPattern(yyMMddHHmmss);
	public static final DateTimeFormatter yyyyMMddHHmm_TFORMAT = DateTimeFormatter.ofPattern(yMdHm);
	public static final DateTimeFormatter yyyyMMddHH_TFORMAT = DateTimeFormatter.ofPattern(yMdH);
	

	
	public static LocalDateTime getLastTimeOfMonth(int year,int month){
		LocalDateTime ldt=LocalDateTime.of(year, month+1, 1, 0, 0,0);
		ldt=ldt.plusDays(-1);
		return ldt;
	}
	
}
