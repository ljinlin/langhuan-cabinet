package com.mingri.commons.algorithm;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 序列号生成器：
 * 
 * 有序的、原子性，超性能， 并发量随便配置
 * 
 * 
 * @author ljl·尘无尘
 * @date Oct 26, 2018
 */
public class SequenceGenerate {
	public SequenceGenerate() {
		if (!SEQUENCE_MAP.isEmpty()) {
			throw new RuntimeException("必须在使用前配置");
		}
		if (CONFIG_FLAG) {
			throw new RuntimeException("已经配置过了");
		}
		CONFIG_FLAG = true;
		System.out.println(
				"=============================================序列生成器已启动，应用编号：" + APP_NO + "   支持每秒并发量:" + (MAX_NO - 1));
	}

	public void setCps(Integer cps) {
		if (cps != null) {
			MAX_NO = cps + 1;
			MAX_LEN = String.valueOf(cps).length();
		}
	}

	public void setAppNo(String appNo) {
		if (appNo != null) {
			APP_NO = appNo;
		}
	}

	/**
	 * 配置标记
	 */
	private static boolean CONFIG_FLAG = false;
	/**
	 * 每秒最高并发量
	 */
	private static int MAX_NO = 100001;
	/**
	 * 序号长度
	 */
	private static int MAX_LEN = String.valueOf(MAX_NO).length();

	/**
	 * 应用编号
	 */
	private static String APP_NO = "0";
	/**
	 * 开始序号
	 */
	private static byte START_NUM = 0;
	private static final SimpleDateFormat yyMMddHHmmss_SDF = new SimpleDateFormat("yyMMddHHmmss");
	private static final Map<String, AtomicInteger> SEQUENCE_MAP = new HashMap<String, AtomicInteger>();

	/**
	 * 设置续接起始序号： 使用场景：序号默认是从1开始，累计到cps（默认10w）后，或者服务重启后，又从1开始累计，
	 * 若是累计到5w断了，重启服务需要从50001开始累计，则用此函数设置
	 * 
	 * @param sequenceKey
	 *            序列key
	 * @param num
	 *            序号
	 */
	public static void setStartNum(String sequenceKey, int startNum) {
		synchronized (SEQUENCE_MAP) {
			AtomicInteger sequence = SEQUENCE_MAP.get(sequenceKey);
			sequence.set(startNum - 1);
		}
	}

	/**
	 * 初始化配置
	 * 
	 * @param cps
	 *            默认值100000，默认每秒最高并发量，也是最大序号，自增到该序号后从1开始重新自增
	 * @param appNo
	 *            默认值0，应用编号
	 */
	public static void initConfiguraion(int cps, String appNo) {
		if (!SEQUENCE_MAP.isEmpty()) {
			throw new RuntimeException("必须在使用前配置");
		}
		if (CONFIG_FLAG) {
			throw new RuntimeException("已经配置过了");
		}
		CONFIG_FLAG = true;
		MAX_NO = cps + 1;
		MAX_LEN = String.valueOf(MAX_NO).length();
		APP_NO = appNo;
	}

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

	public static LocalDateTime getDateTiemById(String id) {
		int year = Integer.parseInt("20" + id.substring(0, 2));
		int month = Integer.parseInt(id.substring(2, 4));
		int day = Integer.parseInt(id.substring(4, 6));
		int hour = Integer.parseInt(id.substring(6, 8));
		int minute = Integer.parseInt(id.substring(8, 10));
		int second = Integer.parseInt(id.substring(10, 12));

		LocalDateTime ldt = LocalDateTime.of(year, month, day, hour, minute, second);
		return ldt;
	}

	/**
	 * 时间标记的序列，因为目前的实现本来就是时间标记的序列，若今后扩展，那么 nexId 返回的将不一定是时间标记的序列
	 * 
	 * @param sequenceKey
	 *            序列key，自定义一个key，每次获取都会在对应的key首次获取序列值的基础上递增
	 * @return
	 */
	public static String nexIdForTimeStamp(String sequenceKey) {
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
					sequence = new AtomicInteger(START_NUM);
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
					sequence.set(START_NUM);
					no = sequence.incrementAndGet();
					strNo = SequenceGenerate.fill0(no);
				}
			}
		}
		return SequenceGenerate.getSecond().concat(strNo).concat(APP_NO);
	}

}
