package com.mingri.langhuan.cabinet.tool;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.binary.Base64;

public class PWDTool {
	private PWDTool() {
	}

	private static final char[] HEX_DIGITS = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',
			'e', 'f' };

//	private static byte[] key = { 0x74, 0x68, 0x69, 0x73, 0x49, 0x73, 0x41, 0x53, 0x65, 0x63, 0x72, 0x65, 0x74, 0x4b,
//			0x65, 0x79 };// "thisIsASecretKey";

	/**
	 * 转换字节数组为16进制字串
	 *
	 * @param b 字节数组
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
		return HEX_DIGITS[d1] + "" + HEX_DIGITS[d2];
	}

	/**
	 * MD5加密
	 * 
	 * @param origin 要加密的参数
	 * @return 返回加密后的数据
	 */
	public static String md5Encode(String origin) {
		String resultString = null;
		try {
			resultString = String.valueOf(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			return byteArrayToHexString(md.digest(resultString.getBytes()));
		} catch (Exception ex) {
			throw new IllegalArgumentException("加密失败：" + origin);
		}
	}

	/**
	 * AES默认加密（使用 AES/GCM/NoPadding）
	 * 
	 * @param origin 要加密的参数
	 * @return 返回加密字符串
	 */
	public static String aesDftEncrypt(String origin) {
		try {
			return aesEncode("AES/GCM/NoPadding", origin);
		} catch (Exception e) {
			throw new IllegalArgumentException("加密失败：" + origin);
		}

	}

	/**
	 * AES默认解密（使用 AES/GCM/NoPadding）
	 * 
	 * @param cipherStr 要解密的参数
	 * @return 返回解密字符串
	 */
	public static String aesDftDecrypt(String cipherStr) {
		try {
			return aesDecode("AES/GCM/NoPadding", cipherStr);
		} catch (Exception e) {
			throw new IllegalArgumentException("解密失败：" + cipherStr);
		}
	}

	/**
	 * AES加密
	 * 
	 * @param encodeRules 加密规则
	 * @param content     加密内容
	 * @return 加密结果
	 */
	public static String aesEncode(String encodeRules, String content) {
		try {
			// 5.根据字节数组生成AES密钥
			SecretKey key = generateAESKey(encodeRules);
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.ENCRYPT_MODE, key);
			// 8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
			byte[] byteEncode = content.getBytes(CharEncoding.UTF_8);
			// 9.根据密码器的初始化方式--加密：将数据加密
			byte[] byteAes = cipher.doFinal(byteEncode);
			// 10.将加密后的数据转换为字符串
			return new String(Base64.encodeBase64(byteAes), CharEncoding.UTF_8);

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	/**
	 * AES 解密
	 * 
	 * @param encodeRules 解密规则
	 * @param content     解密内容
	 * @return 解密结果
	 */
	public static String aesDecode(String encodeRules, String content) {
		try {
			// 5.根据字节数组生成AES密钥
			SecretKey key = generateAESKey(encodeRules);
			// 6.根据指定算法AES自成密码器
			Cipher cipher = Cipher.getInstance("AES");
			// 7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
			cipher.init(Cipher.DECRYPT_MODE, key);
			// 8.将加密并编码后的内容解码成字节数组
			byte[] byteContent = Base64.decodeBase64(content);
			/*
			 * 解密
			 */
			byte[] byteDecode = cipher.doFinal(byteContent);
			String aesDecode = new String(byteDecode, CharEncoding.UTF_8);
			return aesDecode;
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 如果有错就返加nulll
		return null;
	}

	public static SecretKey generateAESKey(String encodeRules) throws NoSuchAlgorithmException {
		// 1.构造密钥生成器，指定为AES算法,不区分大小写
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
		random.setSeed(encodeRules.getBytes());
		// 生成一个128位的随机源,根据传入的字节数组
//		keygen.init(128, new SecureRandom(encodeRules.getBytes()));
		keygen.init(128, random);
		// 3.产生原始对称密钥
		SecretKey originalKey = keygen.generateKey();
		// 4.获得原始对称密钥的字节数组
		byte[] raw = originalKey.getEncoded();
		// 5.根据字节数组生成AES密钥
		SecretKey key = new SecretKeySpec(raw, "AES");
		return key;
	}

}
