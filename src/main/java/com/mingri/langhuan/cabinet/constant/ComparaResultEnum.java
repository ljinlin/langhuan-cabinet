package com.mingri.langhuan.cabinet.constant;

/**
 * 关系运算符
 * 
 * @author ljl
 *
 */
public enum ComparaResultEnum {

	/*
	 * 等于
	 */
	EQ,

	/*
	 * 大于 <br>
	 */
	GT,

	/*
	 * 小于 <br>
	 */
	LT;

	/**
	 * 是否是大于或者是等于
	 * 
	 * @return 布尔值
	 */
	public boolean isGtEq() {
		return this == EQ || this == GT;
	}

	/**
	 * 是否是小于或者是等于
	 * 
	 * @return 布尔值
	 */
	public boolean isLtEq() {
		return this == EQ || this == LT;
	}

	/**
	 * 是否是小于
	 * 
	 * @return 布尔值
	 */
	public boolean isLt() {
		return this == LT;
	}

	/**
	 * 是否是等于
	 * 
	 * @return 布尔值
	 */
	public boolean isEq() {
		return this == EQ;
	}

	/**
	 * 是否是大于
	 * 
	 * @return 布尔值
	 */
	public boolean isGt() {
		return this == GT;
	}
}
