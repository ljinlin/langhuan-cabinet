package com.ws.commons.constant;

public enum CmpChar {
	
	eq("="),
	gt(">"),
	lt("<"),
	gt_eq(">="),
	lt_eq("<="),
	lt_gt("<>"),
	like("like");

	public final String code;

	CmpChar(String code) {
		this.code = code;
	}
}
