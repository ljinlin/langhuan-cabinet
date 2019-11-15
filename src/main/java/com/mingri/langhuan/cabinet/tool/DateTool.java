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
	public static final String yyMdHms="yyMMddHHmmss";
	public static final String yMdHm="yyyyMMddHHmm";
	public static final String yMdH="yyyyMMddHH";
	public static final String y_M_d="yyyy-MM-dd";
	public static final String y_M_d_h_m_s="yyyy-MM-dd HH:mm:ss";
	public static final String yMd="yyyyMMdd";

	public static final ThreadLocal<DateFormat> y_M_d_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat(y_M_d));
	public static final ThreadLocal<DateFormat> yMd_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat(yMd));
	public static final ThreadLocal<DateFormat> Mdy_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat("MM/dd/yyyy"));
	public static final ThreadLocal<DateFormat> y_M_d_h_m_s_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.y_M_d_h_m_s));
	public static final ThreadLocal<DateFormat> yMdHms_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.yMdHms));
	public static final ThreadLocal<DateFormat> yyyyMMddHHmm_FMT = ThreadLocal.withInitial(()-> new SimpleDateFormat(DateTool.yMdHm));
	public static final ThreadLocal<DateFormat> yyyyMMddHH_FMT =ThreadLocal.withInitial(()->new SimpleDateFormat(DateTool.yMdH));
	
	public static final DateTimeFormatter y_M_d_FMTS = DateTimeFormatter.ofPattern(y_M_d);
	public static final DateTimeFormatter yMd_FMTS = DateTimeFormatter.ofPattern(yMd);
	public static final DateTimeFormatter MMddyyyy_FMTS = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	public static final DateTimeFormatter y_M_d_h_m_s_FMTS = DateTimeFormatter.ofPattern(y_M_d_h_m_s);
	public static final DateTimeFormatter yyMdHms_FMTS = DateTimeFormatter.ofPattern(yyMdHms);
	public static final DateTimeFormatter yMdHm_FMTS = DateTimeFormatter.ofPattern(yMdHm);
	public static final DateTimeFormatter yMdH_FMTS = DateTimeFormatter.ofPattern(yMdH);
	

	
}
