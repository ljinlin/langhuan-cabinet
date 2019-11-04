package com.mingri.langhuan.cabinet.tool;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidTool {
	public static final Pattern INTEGER_PATTERN = Pattern.compile("^[-\\+]?[\\d]*$");  
	public static final Pattern CHINESE_PATTERN  = Pattern.compile("[\\u4e00-\\u9fa5]");


	public static boolean isNumeric(String str){
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if( !isNum.matches() ){
			return false;
		}
		return true;
	}
		/**
		 * 是否是数字
		 * @param str 要计算的字符串
		 * @return 是数字返回true
		 */
	  public static boolean notNaN(String str) {  
	        return INTEGER_PATTERN.matcher(str).matches();  
	  }

	  /**
	   * 判断是否含有中文
	   * @param str 要计算的字符串
	   * @return 是中文返回true
	   */
	  public static boolean containChinese(String str) {
			Matcher matcher = CHINESE_PATTERN.matcher(str);
			boolean flg = false;
			if (matcher.find())
				flg = true;

			return flg;
		}


	  
}
