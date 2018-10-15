package com.hydra.model;

import com.github.pagehelper.PageInfo;

import java.util.List;

public class PageResult<T>{
	
	public PageResult(PageInfo<T> data) {
		this.currentPage = data.getPageNum();
		this.pageSize = data.getPageSize();
		this.total = data.getTotal();
		this.data = data.getList();
	}
	
	public PageResult() {
		
	}

	public int getCurrentPage() {
		return currentPage;
	}

	private int currentPage;
	
	private int pageSize;
	
	private long total;
	
	private List<T> data;
	public int getPageSize() {
		return pageSize;
	}

	public long getTotal() {
		return total;
	}

	public List<T> getData() {
		return data;
	}

}
