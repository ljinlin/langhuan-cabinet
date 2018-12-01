package com.ws.commons.constant;

/**
 * 对象类型
 * @author  ljl·尘无尘
 * @date Oct 12, 2018
 */
public enum ObjTypeEnum {
	/**
	 * 长度为0的字符串对象
	 *  <br>
	 */
	BLANK(""),
	/**
	 * null对象
	 *  <br>
	 */
	NULL("null"),
	/**
	 * 有意义的对象，非BLANK，NULL
	 *  <br>
	 */
	OBJ("obj"),
	/**
	 * 所有对象，包含BLANK，NULL，OBJ
	 *  <br>
	 */
	ALL("all");

	public final String code;

	ObjTypeEnum(String code) {
		this.code = code;
	}

}
