package com.mingri.langhuan.cabinet.algorithm;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.mingri.langhuan.cabinet.tool.DateTool;
import com.mingri.langhuan.cabinet.tool.StrTool;

/**
 * 序列号生成器：
 * 
 * 有序的、原子性， 并发量随便配置 时间戳+序号+应用编号
 * 
 * @author ljl
 */
public class SequenceGenerate {

	public static class Single {
		private static SequenceGenerate INSTANCE = new SequenceGenerate();
	}

	public static SequenceGenerate getInstance() {
		return Single.INSTANCE;
	}

	/**
	 * 每秒最高并发量
	 */
	private int maxNo = 1000000;

	/**
	 * 应用编号
	 */
	private String appNo = "0";

	/**
	 * 全局开始序号
	 */
	private byte startNum = 0;

	private final Map<String, AtomicInteger> SEQUENCE_MAP = new HashMap<String, AtomicInteger>();

	/**
	 * 设置续接起始序号： 使用场景：序号默认是从1开始，累计到cps（默认10w）后，或者服务重启后，又从1开始累计，
	 * 若是累计到5w断了，重启服务需要从50001开始累计，则用此函数设置
	 * 
	 * @param sequenceKey 序列key
	 * @param startNum    开始序号
	 */
	public void setStartNum(String sequenceKey, int startNum) {
		synchronized (SEQUENCE_MAP) {
			AtomicInteger sequence = SEQUENCE_MAP.get(sequenceKey);
			sequence.set(startNum - 1);
		}
	}

	/**
	 * 
	 * @param appNo 默认值0，应用编号
	 */
	public void setAppNo(String appNo) {
		this.appNo = appNo;
	}

	/**
	 * 设置最高并发量/每秒
	 * 
	 * @param cps 最高并发量/每秒
	 */
	public void setCps(Integer cps) {
		if (cps != null) {
			maxNo = cps + 1;
		}
	}

	/**
	 * 
	 * @param sequenceKey 序列key，自定义一个key，每次获取都会在对应的key首次获取序列值的基础上递增
	 * @return 返回下一个序列号
	 */
	public String nexId(String sequenceKey) {
		return nexId(sequenceKey, this.maxNo);
	}

	/**
	 * 
	 * @param sequenceKey 序列key，自定义一个key，每次获取都会在对应的key首次获取序列值的基础上递增
	 * @param cps         每秒最高并发量
	 * @return 返回下一个序列号，序列号小于并发量
	 */
	public String nexId(String sequenceKey, int cps) {
		AtomicInteger sequence = SEQUENCE_MAP.get(sequenceKey);
		if (sequence == null) {
			synchronized (SEQUENCE_MAP) {
				sequence = SEQUENCE_MAP.get(sequenceKey);
				if (sequence == null) {
					sequence = new AtomicInteger(startNum);
					SEQUENCE_MAP.put(sequenceKey, sequence);
				}
			}
		}
		int no = sequence.incrementAndGet();
		String strNo = null;
		int len = ((cps - 1) + "").length();
		if (no < cps) {
			strNo = StrTool.fill0(String.valueOf(no), len);
		} else {
			synchronized (SEQUENCE_MAP) {
				no = sequence.incrementAndGet();
				if (no < cps) {
					strNo = StrTool.fill0(String.valueOf(no), len);
				} else {
					sequence.set(startNum);
					no = sequence.incrementAndGet();
					strNo = StrTool.fill0(String.valueOf(no), len);
				}
			}
		}
		return this.currentTime().concat(strNo).concat(appNo);
	}

	/**
	 * 返回当前时间戳
	 * 
	 * @author jinlin Li
	 * @return 返回时间戳
	 */
	public String currentTime() {
		return LocalDateTime.now().format(DateTool.yyMdHms_FMTS);
	}

}
