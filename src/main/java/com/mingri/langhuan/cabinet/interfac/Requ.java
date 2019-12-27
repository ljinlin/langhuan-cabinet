package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;

import com.github.pagehelper.PageHelper;
import com.mingri.langhuan.cabinet.tool.StrTool;

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

	private PageHelp pageHelp;

	public Requ() {
		pageHelp = new PageHelp();
	}

	public PageHelp pageHelp() {
		return pageHelp;
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
		this.page = page;
	}

	public class PageHelp {
		private PageHelp() {
		}

		/**
		 * 
		 * 开始分页，如果分页值不是空值
		 */
		public void startPageIfValid() {
			if (!validPage()) {
				PageHelper.startPage(page.getPageNo(), page.getLimit());
			}
		}

		/**
		 * 开始分页，如果分页值是空值，则设置默认分页值再分页
		 */
		public void startPageDftVal() {
			if (!validPage()) {
				dftNullPageVal();
			}
			startPage();
		}

		/**
		 * 开始分页，根据分页值
		 */
		public void startPage() {
			String orderBy = orderBy();
			if (StrTool.isEmpty(orderBy)) {
				PageHelper.startPage(page.getPageNo(), page.getLimit());
			} else {
				PageHelper.startPage(page.getPageNo(), page.getLimit(), orderBy);
			}
		}

		/**
		 * 给分页值设置默认值，如果分页值是空值
		 */
		public void dftNullPageVal() {
			if (page == null) {
				page = new PagePojo();
			}
			if (page.getLimit() == null)
				page.setLimit(20);
			if (page.getPageNo() == null || page.getPageNo() == 0)
				page.setPageNo(1);
		}

		/**
		 * 验证分页值是否有效，如果page对象是null或者pageNo是空或者0或者limit是null
		 * 
		 * @return 有效返回true
		 */
		public boolean validPage() {
			return page != null && page.getLimit() != null && page.getPageNo() != null && page.getPageNo() != 0;
		}

		/**
		 * 计算order by 后面的sql
		 * 
		 * @return order by 后面的sql
		 */
		public String orderBy() {
			return null;
		}
	}

}
