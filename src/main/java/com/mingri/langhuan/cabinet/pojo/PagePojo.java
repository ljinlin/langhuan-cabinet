package com.mingri.langhuan.cabinet.pojo;

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

}
