package com.mingri.commons.interfac;

public class ResultException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Result result;

	public Result getResult() {
		return result;
	}

	public ResultException(Result result) {
		super("error status code is:" + result.status.code + "(status code Please refer to:"
				+ result.status.getClass().getCanonicalName() + "),");
		this.result=result;
	}
  
}
