package com.ws.commons.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateTool {
	public static final DateFormat _yyyyMMdd_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	public static final DateFormat yyyyMMdd_FORMAT = new SimpleDateFormat("yyyyMMdd");
	public static final DateFormat MMddyyyy_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final DateFormat yyyyMMddHHmmss_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");
}
