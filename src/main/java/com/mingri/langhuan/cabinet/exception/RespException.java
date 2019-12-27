package com.mingri.langhuan.cabinet.exception;

import com.mingri.langhuan.cabinet.interfac.Resp;
import com.mingri.langhuan.cabinet.tool.StrTool;

/**
 * 响应异常，可以根据响应状态码{@code RespStatus}可以判断是什么异常
 * 
 * @author vn0wr5w
 *
 */
public class RespException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7491742406939860664L;
	private Resp result;

	public Resp getResult() {
		return result;
	}

	public RespException(Resp result) {
		super(StrTool.concat("Exception status code is:", result.status.code, "(status code Please refer to:",
				result.status.getClass().getCanonicalName(), "),message:", result.status.getMsg()));
		this.result = result;
	}

}
