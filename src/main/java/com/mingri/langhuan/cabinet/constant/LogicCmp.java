package com.mingri.langhuan.cabinet.constant;

/**
 *  逻辑运算符
 * @author  ljl
 *
 * Dec 13, 2018
 */
public enum LogicCmp {
	
	and("and"),
	or("or");

	public final String code;

	LogicCmp(String code) {
		this.code = code;
	}
}
