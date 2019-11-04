package com.mingri.langhuan.cabinet.tool;

import java.util.Date;

import com.mingri.langhuan.cabinet.constant.ComparaResult;

@SuppressWarnings("hiding")
public class MyComparable<T> {
	private Comparable<T> instance;

	private MyComparable(Comparable<T> instance) {
		this.instance = instance;
	}
	
	public static  <T> MyComparable<T> creat( Comparable<T> instance) {
		return new MyComparable<>(instance);
	}
	

	public boolean isGtEq(T o) {
		return this.compareTo(o).isGtEq();
	}
	public boolean isGt(T o) {
		return this.compareTo(o).isGt();
	}
	public boolean isLtEq(T o) {
		return this.compareTo(o).isLtEq();
	}
	public boolean isEq(T o) {
		return this.compareTo(o).isEq();
	}
	
	private  ComparaResult compareTo(T o) {
		MyComparable.creat(new Date());
		int result = this.instance.compareTo(o);
		if (result > 0) {
			return ComparaResult.GT;
		}
		if (result < 0) {
			return ComparaResult.LT;
		}
		return ComparaResult.EQ;
	}

}
