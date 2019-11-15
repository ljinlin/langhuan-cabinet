package com.mingri.langhuan.cabinet.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 线程工具类
 * @author ljl
 *
 */
public class ThreadTool {
	
	private ThreadTool() {}

	private static final Logger LOGGER = LoggerFactory.getLogger(ThreadTool.class);

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			LOGGER.error("线程异常中断，Interrupted!异常信息:{}", e);
			Thread.currentThread().interrupt();
		}
	}
	
	public static Object buildLock(String lockId) {
		StringBuilder sb = new StringBuilder(lockId);
		return sb.toString().intern();
	}
	public static Object buildLock(String lockPrefix,String lockId) {
		StringBuilder sb = new StringBuilder();
		sb.append(lockPrefix);
		sb.append(lockId);
		return sb.toString().intern();
	}

}
