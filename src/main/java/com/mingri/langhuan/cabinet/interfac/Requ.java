package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;

import com.mingri.langhuan.cabinet.handler.PageHandler;
import com.mingri.langhuan.cabinet.pojo.PagePojo;

/**
 * 请求对象
 * 
 * @author ljl
 *
 * @param <T> 请求数据类型
 */
public class Requ<T extends DataModel> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8798070553988740245L;

	/**
	 * 请求对象
	 */
	private T t;

	/**
	 * 分页对象
	 */
	private PagePojo page;

	public Requ() {
		PageHandler.setPage(page);
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public PagePojo getPage() {
		return page;
	}

	public void setPage(PagePojo page) {
		PageHandler.setPage(page);
		this.page = page;
	}

}
