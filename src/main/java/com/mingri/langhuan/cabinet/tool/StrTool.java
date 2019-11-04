package com.mingri.langhuan.cabinet.tool;

import java.util.Random;
import java.util.UUID;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.core.io.ClassPathResource;

public class StrTool {

	public static final String EMPTY = "";

	public static String toString(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();

	}

	public static String getUUId() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	public static String delStartAndEnd(CharSequence charSequence, String delStr) {
		String srcStr = charSequence.toString();
		if (srcStr.startsWith(delStr)) {
			srcStr = srcStr.substring(delStr.length());
		}
		if (srcStr.endsWith(delStr)) {
			srcStr = srcStr.substring(0, srcStr.length() - delStr.length());
		}
		return srcStr;
	}

	public static final Pattern CAMEL_PATTERN = Pattern.compile("_(\\w)");

	/**
	 * 下划线转驼峰
	 * 
	 * @param str 要转的字符串
	 * @return 驼峰字符串
	 */
	public static String camel(String str) {
		// 利用正则删除下划线，把下划线后一位改成大写
		Matcher matcher = CAMEL_PATTERN.matcher(str);
		StringBuffer sb = new StringBuffer(str);
		if (matcher.find()) {
			sb = new StringBuffer();
			// 将当前匹配子串替换为指定字符串，并且将替换后的子串以及其之前到上次匹配子串之后的字符串段添加到一个StringBuffer对象里。
			// 正则之前的字符和被替换的字符
			matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
			// 把之后的也添加到StringBuffer对象里
			matcher.appendTail(sb);
		} else {
			return sb.toString();
		}
		return camel(sb.toString());
	}

	public static boolean isBlank(String str) {
		return (str + "").length() == 0;
	}

	public static boolean isNotBlank(String str) {
		return !isBlank(str);
	}

	public static boolean isEmpty(String str) {
		return (str == null || str.length() == 0);
	}

	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static boolean checkEmpty(Object obj) {
		if (obj != null) {
			String str = obj.toString().trim();
			return str.length() == 0 || str.equals("null");
		}
		return true;
	}

	public static String trimStr(String src, String trimStr) {
		if (src.indexOf(trimStr) == 0) {
			src = src.replaceFirst(trimStr, "");
		}
		int len = src.length() - trimStr.length();
		if (src.lastIndexOf(trimStr) == len) {
			src = src.substring(0, len);
		}
		return src;
	}

	public static boolean checkNotEmpty(Object obj) {
		return !checkEmpty(obj);
	}

	public static String getSuffix(String str) {
		return str.substring(str.lastIndexOf("."));
	}

	public static String getPrefix(String str) {
		return str.substring(0, str.lastIndexOf("."));
	}

	public static final String BASE_UP_CODE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

	/**
	 * @param length 随机数长度
	 * @return 随机数
	 */
	public static String ranDomEnUpCode(int length) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(BASE_UP_CODE.length());
			sb.append(BASE_UP_CODE.charAt(number));
		}
		return sb.toString();
	}

	public static Integer intOf(String str) {
		try {
			return Integer.parseInt(str.trim());
		} catch (Exception e) {
		}
		return null;
	}

	public static StringBuilder concat(CharSequence... strs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < strs.length; i++) {
			sb.append(strs[i]);
		}
		return sb;
	}

	public static String fill0(String str, int len) {
		str = str == null ? StrTool.EMPTY : str;
		if (str.length() >= len) {
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		while (sb.length() < len) {
			sb.insert(0, "0");
		}
		return sb.toString();
	}

}
