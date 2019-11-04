package com.mingri.langhuan.cabinet.constant;

/**
 * 关系运算符
 * 
 * @author ljl·尘无尘
 *
 *         Dec 13, 2018
 */
public enum ComparaResult {

	EQ, GT, LT;

	public boolean isGtEq() {
		return this == EQ || this == GT;
	}

	public boolean isLtEq() {
		return this == EQ || this == LT;
	}

	public boolean isLt() {
		return this == LT;
	}
	public boolean isEq() {
		return this == EQ;
	}

	public boolean isGt() {
		return this == GT;
	}
}
