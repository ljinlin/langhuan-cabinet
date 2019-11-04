package com.mingri.langhuan.cabinet.constant;

/**
 *  关系运算符
 * @author  ljl
 *
 * Dec 13, 2018
 */
public enum NexusCmp {
	
	eq("="),
	gt(">"),
	lt("<"),
	gt_eq(">="),
	lt_eq("<="),
	lt_gt("<>"),
	no_eq("!="),
	in("in"),
	not_in("not in"),
	is("is"),
	is_not("is not"),
	like("like"),
	like_l("like"),
	like_r("like"),
	like_lr("like");

	public final String code;

	NexusCmp(String code) {
		this.code = code;
	}
}
