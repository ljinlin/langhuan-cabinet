package com.mingri.langhuan.cabinet.constant;

/**
 * 对象类型
 * @author  ljl·尘无尘
 * @date Oct 12, 2018
 */
public enum ObjTypeEnum {
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
