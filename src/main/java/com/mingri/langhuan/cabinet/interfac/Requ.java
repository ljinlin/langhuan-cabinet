package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;

import com.mingri.langhuan.cabinet.page.PagePojo;

/**
 * 请求对象
 * 
 * @author ljl
 *
 * @param <T> 请求数据类型
 */
public class Requ<T> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8798070553988740245L;

	/**
	 * 请求对象
	 */
	private T data;

	/**
	 * 分页对象
	 */
	private PagePojo page;

	public T getT() {
		return data;
	}

	public T getTOfDft(T dftData) {
		if(data==null) {
			data=dftData;
		}
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public PagePojo getPage() {
		return page;
	}

	public void setPage(PagePojo page) {
		if(page!=null) {
			page.toThreadLocal();
		}
		this.page = page;
	}
	

}
