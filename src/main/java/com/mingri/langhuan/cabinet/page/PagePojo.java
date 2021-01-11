package com.mingri.langhuan.cabinet.page;

import com.github.pagehelper.Page;

/**
 * 分页属性，没有数据属性
 * @author jinlin Li
 * 2021年1月3日
 */
public class PagePojo {
	

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8798070553988740245L;

	private Integer pageNo;
	private Integer pageSize;
	private long total;

	public PagePojo() {
	}

	public PagePojo(Integer pageNo, Integer pageSize, Long total) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = (total == null ? 0 : total);
	}
	
	public static PagePojo valueOf(Page<?> page) {
		return  new PagePojo(page.getPageNum(), page.getPageSize(), page.getTotal());
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getLimit() {
		return pageSize;
	}

	public void setLimit(Integer limit) {
		this.pageSize = limit;
	}

	public long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = (total == null ? 0 : total);
	}
	
	/**
	 * 存储到到当前线程ThreadLocal
	 *@author jinlin Li
	 */
	public void toThreadLocal() {
		PageTool.LOCAL_PAGE.set(this);
	}
	

}
