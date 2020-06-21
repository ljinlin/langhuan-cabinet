package com.mingri.langhuan.cabinet.tool;

public class CacheTool {

	private CacheTool() {};
	
	public static final String NULL_VALUE = StrTool.getUUId();
	
	public static Object  getSafeCacheVal(Object value) {
		return value ==null? NULL_VALUE : value; 
	}
	public static Object getSrcVal(Object value) {
		return value == NULL_VALUE ? null : value; 
	}
}
