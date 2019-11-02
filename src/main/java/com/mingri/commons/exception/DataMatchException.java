package com.mingri.commons.exception;

public class DataMatchException extends IllegalArgumentException{
  
	/**
	 * 
	 */
	private static final long serialVersionUID = -2564462277251963131L;
	public static void throwForNotMatch(Class<?> matchClass,Object v) {
				throw new DataMatchException("data:"+v+" not match"+matchClass);
	}
	
	public DataMatchException() {
	super();
	}
	
	public DataMatchException(String msg) {
		super(msg);
	}

}
