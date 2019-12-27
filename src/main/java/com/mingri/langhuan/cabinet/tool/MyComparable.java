package com.mingri.langhuan.cabinet.tool;

import com.mingri.langhuan.cabinet.constant.ComparaResultEnum;

/**
 * 可以实现对比的类
 * 
 * @author ljl
 *
 * @param <T>  真正要对比类
 */
public class MyComparable<T> {
	private Comparable<T> instance;

	private MyComparable(Comparable<T> instance) {
		this.instance = instance;
	}

	public static <T> MyComparable<T> creat(Comparable<T> instance) {
		return new MyComparable<>(instance);
	}

	/**
	 * 是否大于等于
	 * @param o 要对比的目标对象
	 * @return
	 */
	public boolean isGtEq(T o) {
		return this.compareTo(o).isGtEq();
	}

	/**
	 * 是否大于
	 * @param o 要对比的目标对象
	 * @return 对比结果
	 */
	public boolean isGt(T o) {
		return this.compareTo(o).isGt();
	}

	/**
	 * 是否小于等于
	 * @param o 要对比的目标对象
	 * @return 对比结果
	 */
	public boolean isLtEq(T o) {
		return this.compareTo(o).isLtEq();
	}

	/**
	 * 是否相等
	 * @param o 要对比的目标对象
	 * @return 对比结果
	 */
	public boolean isEq(T o) {
		return this.compareTo(o).isEq();
	}

	/**
	 * 进行对比
	 * @param o 要对比的目标对象
	 * @return 对比结果枚举
	 */
	private ComparaResultEnum compareTo(T o) {
		int result = this.instance.compareTo(o);
		if (result > 0) {
			return ComparaResultEnum.GT;
		}
		if (result < 0) {
			return ComparaResultEnum.LT;
		}
		return ComparaResultEnum.EQ;
	}

}
