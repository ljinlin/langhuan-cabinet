package com.ws.commons.constant;

/**
 *  逻辑运算符
 * @author  ljl·尘无尘
 *
 * Dec 13, 2018
 */
public enum LogicCmp {
	
	and("and"),
	or("or"),
	no("!");

	public final String code;

	LogicCmp(String code) {
		this.code = code;
	}
}
