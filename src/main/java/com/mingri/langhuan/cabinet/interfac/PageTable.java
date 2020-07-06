package com.mingri.langhuan.cabinet.interfac;

import java.util.Collection;

import com.github.pagehelper.Page;
import com.mingri.langhuan.cabinet.pojo.PagePojo;
import com.mingri.langhuan.cabinet.tool.CollectionTool;

/**
 * 
 * @author ljl
 * 2020年1月13日
 * @param <T>  列表数据类型
 */
public class PageTable<T> {

	/**
	 * 列表数据
	 */
	private Collection<T> collection;

	/**
	 * 分页对象
	 */
	private PagePojo page;

	public PageTable() {
	}

	public PageTable(Collection<T> collection, PagePojo page) {
		this.collection = collection;
		this.page = page;
	}

	public PageTable(PagePojo page) {
		this.page = new PagePojo(page.getPageNo(), page.getLimit(), page.getTotal());
	}

	public Collection<T> getCollection() {
		return collection;
	}

	public void setCollection(Collection<T> collection) {
		this.collection = collection;
	}

	public PagePojo getPage() {
		return page;
	}

	public void setPage(PagePojo page) {
		this.page = page;
	}

	/**
	 * 把自己封装到响应对象并返回
	 * 
	 * @return 如果colleaction有数据返回成功的响应对象，反之返回半成功的响应对象
	 */
	public Resp pottingResp() {
		if (CollectionTool.isEmpty(this.collection)) {
			return Resp.Builder.buildSuccess(this);
		} else {
			return Resp.Builder.buildImperfect(this.collection, null);
		}
	}

}
