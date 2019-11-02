package com.mingri.commons.tool;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StrTool {
	
	public static final String EMPTY="";
	
	/**
	 * @MethodName : setFieldValueByName
	 * @Description : 根据字段名给对象的字段赋值
	 * @param fieldName
	 *            字段名
	 * @param fieldValue
	 *            字段值
	 * @param o
	 *            对象
	 */
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

	/**
	 * 下划线转驼峰
	 * 
	 * @param str
	 * @return
	 */
	public static final Pattern CAMEL_PATTERN = Pattern.compile("_(\\w)");

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
	public static String trimStr(String src,String trimStr) {
		if(src.indexOf(trimStr)==0) {
			src=src.replaceFirst(trimStr, "");
		}
		int len=src.length()-trimStr.length();
		if(src.lastIndexOf(trimStr)==len) {
			src=src.substring(0, len);
		}
		return src;
	}
	

	public static boolean checkNotEmpty(Object obj) {
		return !checkEmpty(obj);
	}
	
	public static String getSuffix(String str){
		return 	str.substring(str.lastIndexOf("."));
	}
	public static String getPrefix(String str){
		return 	str.substring(0,str.lastIndexOf("."));
	}
	@SuppressWarnings("unchecked")
	public static <E> List<E> arrayToList(Object[] ary, Class<E> E) {
		List<E> list = new ArrayList<E>();
		for (Object e : ary) {
			list.add((E) e);
		}
		return list;
	}

	
}
