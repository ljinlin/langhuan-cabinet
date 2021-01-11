package com.mingri.langhuan.cabinet.page;

import java.util.Collection;

import com.github.pagehelper.Page;
import com.mingri.langhuan.cabinet.interfac.Resp;
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
	private Collection<T> data;

	/**
	 * 分页对象
	 */
	private PagePojo page;

	public PageTable() {
	}

	
	public PageTable(Collection<T> pageOrList) {
		if (pageOrList instanceof Page) {
			this.page = PagePojo.valueOf((Page<T>)pageOrList);
			this.data = ((Page<T>)pageOrList).getResult();
		}else {
			this.data = pageOrList;
		}
	}

	public PageTable(PagePojo page) {
		this.page = new PagePojo(page.getPageNo(), page.getLimit(), page.getTotal());
	}

	public Collection<T> getData() {
		return data;
	}

	public void setData(Collection<T> data) {
		this.data = data;
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
	 * @return 如果data有数据返回成功的响应对象，反之返回半成功的响应对象
	 */
	public Resp createResp() {
		if (CollectionTool.isEmpty(this.data)) {
			return Resp.Builder.buildImperfect(this);
		} else {
			return Resp.Builder.buildSuccess(this);
		}
	}

}
