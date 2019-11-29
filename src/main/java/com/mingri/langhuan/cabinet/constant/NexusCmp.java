package com.mingri.langhuan.cabinet.constant;

/**
 *  关系运算符
 * @author  ljl
 *
 * Dec 13, 2018
 */
public enum NexusCmp {
	
	EQ("="),
	GT(">"),
	LT("<"),
	GT_EQ(">="),
	LT_EQ("<="),
	LT_GT("<>"),
	NO_EQ("!="),
	IN("in"),
	NOT_IN("not in"),
	IS("is"),
	IS_NOT("is not"),
	LIKE("like"),
	LIKE_L("like"),
	LIKE_R("like"),
	LIKE_LR("like");

	public final String code;

	NexusCmp(String code) {
		this.code = code;
	}
}
