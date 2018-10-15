package com.hydra.exception;

public class XssoException extends XbaseException {

	private static final long serialVersionUID = -440382267488713339L;

	public XssoException(XbaseError error) {
		super(error);
	}
	
	public XssoException(XbaseError error, Throwable e) {
		super(error, e);
	}
	
	public XssoException(XbaseError error, String errMsg) {
		super(error, errMsg);
	}
}

