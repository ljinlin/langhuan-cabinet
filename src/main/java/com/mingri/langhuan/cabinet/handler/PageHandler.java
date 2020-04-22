package com.mingri.langhuan.cabinet.handler;

import java.util.Collection;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.mingri.langhuan.cabinet.interfac.PageTable;
import com.mingri.langhuan.cabinet.pojo.PagePojo;
import com.mingri.langhuan.cabinet.tool.StrTool;

/**
 * 分页相关操作
 * 
 * @author ljl
 *
 */
public final class PageHandler {

	static final ThreadLocal<PagePojo> LOCAL_PAGE = new ThreadLocal<PagePojo>();

	public static void setPage(PagePojo pagePojo) {
		LOCAL_PAGE.set(pagePojo);
	}

	/**
	 * 
	 * 开始分页，如果分页值不是空值
	 * 
	 * @param orderBy 排序sql，order by 后面的sql
	 */
	public static void startPageIfValid(String orderBy) {
		if (validPage()) {
			PageHelper.startPage(LOCAL_PAGE.get().getPageNo(), LOCAL_PAGE.get().getLimit(), orderBy);
		}
	}

	/**
	 * 开始分页，如果分页值是有效的才分页，否在不分页
	 */
	public static void startPageIfValid() {
		PageHandler.startPageIfValid(null);
	}

	/**
	 * 开始分页，如果分页值是空值，则设置默认分页值再分页
	 * @param orderBy 排序sql，order by 后面的sql 
	 */
	public static void startPageDftVal(String orderBy) {
		if (!validPage()) {
			dftNullPageVal();
		}
		startPage(orderBy);
	}

	/**
	 * 开始分页，如果分页值是空值，则设置默认分页值再分页
	 */
	public static void startPageDftVal() {
		PageHandler.startPageDftVal(null);
	}

	/**
	 * 开始分页，根据分页值
	 * 
	 * @param orderBy 排序sql，order by 后面的sql
	 */
	public static void startPage(String orderBy) {
		if (StrTool.isEmpty(orderBy)) {
			PageHelper.startPage(LOCAL_PAGE.get().getPageNo(), LOCAL_PAGE.get().getLimit());
		} else {
			PageHelper.startPage(LOCAL_PAGE.get().getPageNo(), LOCAL_PAGE.get().getLimit(), orderBy);
		}
	}

	/**
	 * 开始分页，根据分页值
	 */
	public static void startPage() {
		PageHandler.startPage(null);
	}

	/**
	 * 给分页值设置默认值，如果分页值是空值
	 */
	public static void dftNullPageVal() {
		if (LOCAL_PAGE.get() == null) {
			LOCAL_PAGE.set(new PagePojo());
		}
		if (LOCAL_PAGE.get().getLimit() == null)
			LOCAL_PAGE.get().setLimit(20);
		if (LOCAL_PAGE.get().getPageNo() == null || LOCAL_PAGE.get().getPageNo() == 0)
			LOCAL_PAGE.get().setPageNo(1);
	}

	/**
	 * 验证分页值是否有效，如果page对象是null或者pageNo是空或者0或者limit是null或者0 则是无效的
	 * 
	 * @return 有效返回true
	 */
	public static boolean validPage() {
		return LOCAL_PAGE.get() != null && LOCAL_PAGE.get().getLimit() != null && LOCAL_PAGE.get().getPageNo() != null
				&& LOCAL_PAGE.get().getPageNo() > 0 && LOCAL_PAGE.get().getLimit() > -1;
	}

	public static <T> PageTable<T> toPageTable(List<T> page) {
		PageTable<T> pgtb = null;
		if (page instanceof Page) {
			pgtb = new PageTable<>((Page<T>) page);
		} else {
			pgtb = new PageTable<>();
			pgtb.setCollection(page);
		}
		return pgtb;
	}

	public static <T> PageTable<T> toPageTable(PagePojo page, Collection<T> list) {
		return new PageTable<>(list, page);
	}

	public static <T> PageTable<T> toPageTable(Collection<T> collection, int pageNo, int pageSize, long total) {
		return new PageTable<>(collection, new PagePojo(pageNo, pageSize, total));
	}

}
