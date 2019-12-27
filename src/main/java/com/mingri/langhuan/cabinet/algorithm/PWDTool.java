package com.mingri.langhuan.cabinet.algorithm;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.xmlbeans.impl.util.Base64;

public class PWDTool {
	private PWDTool () {}
	
	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e',
			'f' };
	
	private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41,
			0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b, 0x65, 0x79 };// "thisIsASecretKey";

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
	 * @return  返回加密后的数据
	 */
	public static String md5Encode(String origin) {
		String resultString = null;
		try {
			resultString = String.valueOf(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			throw new IllegalArgumentException("加密失败："+origin);
		}
	}
	
	/**
	 * 加密函数.
	 * 
	 * @param origin  要加密的参数
	 * @return 返回加密字符串
	 */
	public static String encrypt(String origin) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			final byte[] encryptedString = Base64.encode(cipher
					.doFinal(origin.getBytes()));
			return new String(encryptedString, "UTF-8");
		} catch (Exception e) {
			throw new IllegalArgumentException("加密失败："+origin);
		}

	}

	/**
	 * 解密函数.
	 * 
	 * @param cipherStr 要解密的参数
	 * @return 返回解密字符串
	 */
	public static String decrypt(String cipherStr) {
		try {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			final SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			final String decryptedString = new String(cipher.doFinal(Base64
					.decode(cipherStr.getBytes("UTF-8"))));
			return decryptedString;
		} catch (Exception e) {
			throw new IllegalArgumentException("解密失败："+cipherStr);
		}
	}

}
