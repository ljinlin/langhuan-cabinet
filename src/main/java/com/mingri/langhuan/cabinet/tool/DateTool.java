package com.mingri.langhuan.cabinet.tool;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

public class DateTool {

	private DateTool() {
	}

	/**
	 * 是否同一天
	 * 
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return true:是同一天，false:不是
	 */
	public static boolean isSameDay(Date date1, Date date2) {
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date1);
		return cal1.get(Calendar.ERA) == cal2.get(0) && cal1.get(Calendar.ERA) == cal2.get(Calendar.YEAR)
				&& cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
	}

	public static LocalDateTime toLocalDateTime(Date date) {
		Instant instant = date.toInstant();
		ZoneId zone = ZoneId.systemDefault();
		return LocalDateTime.ofInstant(instant, zone);
	}

	public static Date toDate(LocalDateTime localDateTime) {
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zdt = localDateTime.atZone(zoneId);
		return Date.from(zdt.toInstant());
	}

	public static class Fmt {
		public static final String yMdHms = "yyyyMMddHHmmss";
		public static final String yyMdHms = "yyMMddHHmmss";
		public static final String yMdHm = "yyyyMMddHHmm";
		public static final String yMdH = "yyyyMMddHH";
		public static final String y_M_d = "yyyy-MM-dd";
		public static final String y_M_d_H_m_s = "yyyy-MM-dd HH:mm:ss";
		public static final String yMd = "yyyyMMdd";
	}

	public static enum FmtEnum {

		yMd(Fmt.yMd, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			return (dt.getYear()) + "" + (month < 10 ? "0" + month : month) + (d < 10 ? "0" + d : d);
		}), 
		//</br>
		yMdH(Fmt.yMdH, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			int h = dt.getHour();
			return (dt.getYear() % 1000) + "" + (month < 10 ? "0" + month : month) + (d < 10 ? "0" + d : d)
					+ (h < 10 ? "0" + h : h);
		}),
		//</br>
		yMdHm(Fmt.yMdHm, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			int h = dt.getHour();
			int m = dt.getMinute();
			return (dt.getYear() % 1000) + "" + (month < 10 ? "0" + month : month) + (d < 10 ? "0" + d : d)
					+ (h < 10 ? "0" + h : h) + (m < 10 ? "0" + m : m);
		}),
		//</br>
		y_M_d(Fmt.y_M_d, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			return (dt.getYear()) + "-" + (month < 10 ? "0" + month : month) + "-" + (d < 10 ? "0" + d : d);
		}),
		//</br>
		y_M_d_H_m_s(Fmt.y_M_d_H_m_s, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			int h = dt.getHour();
			int m = dt.getMinute();
			int sc = dt.getSecond();
			return (dt.getYear()) + "-" + (month < 10 ? "0" + month : month) + "-" + (d < 10 ? "0" + d : d) + " "
					+ (h < 10 ? "0" + h : h) + ":" + (m < 10 ? "0" + m : m) + ":" + (sc < 10 ? "0" + sc : sc);
		}), 
		//</br>
		yMdHms(Fmt.yMdHms, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			int h = dt.getHour();
			int m = dt.getMinute();
			int sc = dt.getSecond();
			return dt.getYear() + "" + (month < 10 ? "0" + month : month) + (d < 10 ? "0" + d : d)
					+ (h < 10 ? "0" + h : h) + (m < 10 ? "0" + m : m) + (sc < 10 ? "0" + sc : sc);
		}), 
		//</br>
		yyMdHms(Fmt.yyMdHms, dt -> {
			int month = dt.getMonthValue();
			int d = dt.getDayOfMonth();
			int h = dt.getHour();
			int m = dt.getMinute();
			int sc = dt.getSecond();
			return (dt.getYear() % 1000) + "" + (month < 10 ? "0" + month : month) + (d < 10 ? "0" + d : d)
					+ (h < 10 ? "0" + h : h) + (m < 10 ? "0" + m : m) + (sc < 10 ? "0" + sc : sc);
		});

		public final String pattern;
		public final ThreadLocal<DateFormat> dtFmt;
		public final DateTimeFormatter dttFmt;
		public final Function<LocalDateTime, String> parse;

		public String getPattern() {
			return pattern;
		}

		public ThreadLocal<DateFormat> getDtFmt() {
			return dtFmt;
		}

		public DateTimeFormatter getDttFmt() {
			return dttFmt;
		}

		public Function<LocalDateTime, String> getParse() {
			return parse;
		}
		public String parse(LocalDateTime dt) {
			return parse.apply(dt);
		}

		private FmtEnum(String pattern, Function<LocalDateTime, String> parse) {
			this.pattern = pattern;
			this.dtFmt = ThreadLocal.withInitial(() -> new SimpleDateFormat(pattern));
			this.dttFmt = DateTimeFormatter.ofPattern(pattern);
			this.parse = parse;
		};

	}

}
