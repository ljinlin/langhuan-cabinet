package com.mingri.langhuan.cabinet.interfac;

public class ResultException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7491742406939860664L;
	private Result result;

	public Result getResult() {
		return result;
	}

	public ResultException(Result result) {
		super("Exception status code is:" + result.status.code + "(status code Please refer to:"
				+ result.status.getClass().getCanonicalName() + "),message:"+result.status.getMsg());
		this.result=result;
	}
  
}
