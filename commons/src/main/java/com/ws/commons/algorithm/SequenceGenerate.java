package com.ws.commons.algorithm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 有序id生成器：
 * 
 * 原子性，超性能， 并发量随便配置
 * 
 * 
 * @author ljl·尘无尘
 * @date Oct 26, 2018
 */
@ConfigurationProperties(prefix = "sequence-generate")
public class SequenceGenerate {

	public SequenceGenerate() {
		System.out.println("=============================================序列生成器已启动");
	}

	public void setCps(Integer cps) {
		if (cps != null) {
			MAX_NO = cps + 1;
			MAX_LEN = String.valueOf(cps).length();
		}
	}

	public void setAppNo(Integer appNo) {
		if (appNo != null) {
			APP_NO = appNo;
		}
	}

	/**
	 * 每秒最高并发量
	 */
	private static int MAX_NO = 100001;
	private static int MAX_LEN = String.valueOf(MAX_NO).length();
	/**
	 * 应用编号
	 */
	private static int APP_NO = 0;
	private static final SimpleDateFormat yyMMddHHmmss_SDF = new SimpleDateFormat("yyMMddHHmmss");
	private static final Map<String, AtomicInteger> SEQUENCE_MAP = new HashMap<String, AtomicInteger>();

	private static String getSecond() {
		Date date = new Date();
		return yyMMddHHmmss_SDF.format(date);
	}

	private static String fill0(int srcNum) {
		String strSrc = String.valueOf(srcNum);
		while (strSrc.length() < SequenceGenerate.MAX_LEN) {
			strSrc = "0" + strSrc;
		}
		return strSrc;
	}

	/**
	 * 
	 * @param sequenceKey
	 *            序列key，自定义一个key，每次获取都会在对应的key首次获取序列值的基础上递增
	 * @return
	 */
	public static String nexId(String sequenceKey) {
		return nexId(sequenceKey, SequenceGenerate.MAX_NO);
	}

	/**
	 * 
	 * @param sequenceKey
	 *            序列key，自定义一个key，每次获取都会在对应的key首次获取序列值的基础上递增
	 * @param cps
	 *            每秒最高并发量
	 * @return
	 */
	public static String nexId(String sequenceKey, int cps) {
		AtomicInteger sequence = SEQUENCE_MAP.get(sequenceKey);
		if (sequence == null) {
			synchronized (SEQUENCE_MAP) {
				sequence = SEQUENCE_MAP.get(sequenceKey);
				if (sequence == null) {
					sequence = new AtomicInteger(0);
					SEQUENCE_MAP.put(sequenceKey, sequence);
				}
			}
		}
		int no = sequence.incrementAndGet();
		String strNo = null;
		if (no < cps) {
			strNo = SequenceGenerate.fill0(no);
		} else {
			synchronized (SEQUENCE_MAP) {
				no = sequence.incrementAndGet();
				if (no < cps) {
					strNo = SequenceGenerate.fill0(no);
				} else {
					sequence.set(0);
					no = sequence.incrementAndGet();
					strNo = SequenceGenerate.fill0(no);
				}
			}
		}
		return SequenceGenerate.getSecond().concat(strNo) + APP_NO;
	}

}
