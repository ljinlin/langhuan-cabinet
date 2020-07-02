package com.mingri.langhuan.cabinet.tool;

/**
 * 缓存工具类
 * 
 * @author jinlin Li 2020年7月2日
 */
public class CacheTool {

	private CacheTool() {
	}

	public static final String NULL_VALUE = StrTool.getUUId();

	public static Object getSafeCacheVal(Object value) {
		return value == null ? NULL_VALUE : value;
	}

	public static Object getSrcVal(Object value) {
		return value == NULL_VALUE ? null : value;
	}
}
