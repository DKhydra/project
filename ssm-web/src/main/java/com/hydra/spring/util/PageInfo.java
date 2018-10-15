package com.hydra.spring.util;

import java.io.Serializable;
import java.util.List;

public class PageInfo<T> implements Serializable {

	private Integer total = 0;

	private Integer currentPage = 1;

	private Integer pageSize = 15;

	private List<T> list;

	public PageInfo(List<T> list) {
		this.setList(list);
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}


}
