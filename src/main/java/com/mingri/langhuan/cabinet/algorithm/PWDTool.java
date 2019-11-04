package com.mingri.langhuan.cabinet.algorithm;

import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PWDTool {
    private static final Logger LOGGER = LoggerFactory.getLogger(PWDTool.class);
	private PWDTool () {}
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b
	 *            字节数组
	 * @return 16进制字串
	 */
	private static String byteArrayToHexString(byte[] b) {
		StringBuilder resultSb = new StringBuilder();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n += 256;
		int d1 = n / 16;
		int d2 = n % 16;
		return HEX_DIGITS[d1] +""+ HEX_DIGITS[d2];
	}

	/**
	 * MD5加密
	 * 
	 * @param origin 要加密的参数
	 * @return
	 */
	public static String md5Encode(String origin) {
		String resultString = null;
		try {
			resultString = String.valueOf(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			LOGGER.error("",ex);
		}
		return resultString;
	}

}
