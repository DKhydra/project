package com.hydra.model;

public class ResultPagingModel<T> extends ResultModel<PageResult<T>> {

	private static final long serialVersionUID = -1622146806363545818L;

	
	public ResultPagingModel() {
		super();
	}
	public ResultPagingModel(PageResult<T> returnValue) {
		super(returnValue);
	}
	public ResultPagingModel(String returnCode, String returnMessage) {
		super(returnCode, returnMessage);
	}
	
	public ResultPagingModel(PageResult<T> returnValue, String returnCode, String returnMessage) {
		super(returnValue, returnCode, returnMessage);
	}
	
}
