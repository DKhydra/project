package com.hydra.exception;

public class CacheException extends XbaseException {

	private static final long serialVersionUID = -7947977510600468113L;

	public CacheException(XbaseError error) {
		super(error);
	}
	
	public CacheException(XbaseError error, Throwable e) {
		super(error, e);
	}
}
