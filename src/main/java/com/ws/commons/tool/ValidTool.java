package com.ws.commons.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidTool {
	public static final Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");  
	public static final Pattern CHINESE_PATTERN  = Pattern.compile("[\\u4e00-\\u9fa5]");

		/**
		 * 是否是数字
		 * @param str
		 * @return
		 */
	  public static boolean notNaN(String str) {  
	        return INTEGER_PATTERN.matcher(str).matches();  
	  }

	  /**
	   * 判断是否含有中文
	   * @param str
	   * @return
	   */
	  public static boolean containChinese(String str) {
			Matcher matcher = CHINESE_PATTERN.matcher(str);
			boolean flg = false;
			if (matcher.find())
				flg = true;

			return flg;
		}

}
