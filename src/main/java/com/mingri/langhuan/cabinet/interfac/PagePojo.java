package com.mingri.langhuan.cabinet.interfac;

public class PagePojo {

	/**
	 * 
	 */
	@SuppressWarnings("unused")
	private static final long serialVersionUID = 8798070553988740245L;

	private Integer pageNo;
	private Integer limit;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

}
