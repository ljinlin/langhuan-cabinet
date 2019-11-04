package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;

import com.mingri.langhuan.cabinet.constant.UnifiedStatusEnum;

/**
 * 结果状态
 * 
 * @author ljl
 *
 */
public class ResultStatus   implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8252427662972342210L;

	/**
	 * 状态码
	 */
	public final String code;
	
	/**
	 * 状态消息
	 */
	private String msg;

	public ResultStatus(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	/**
	 * 成功的状态
	 */
	public static final ResultStatus SUCCESS = new ResultStatus(
			UnifiedStatusEnum.SUCCESS.code, UnifiedStatusEnum.SUCCESS.msg);
	
	/**
	 * 参数异常的状态
	 */
	public static final ResultStatus PARAM_ERROR = new ResultStatus(
			UnifiedStatusEnum.PARAM_ERROR.code,
			UnifiedStatusEnum.PARAM_ERROR.msg);
	
	/**
	 * 系统异常的状态
	 */
	public static final ResultStatus SYS_ERROR = new ResultStatus(
			UnifiedStatusEnum.SYS_ERROR.code, UnifiedStatusEnum.SYS_ERROR.msg);
	
	/**
	 * 警告的状态
	 */
	public static final ResultStatus WARNING = new ResultStatus(
			UnifiedStatusEnum.WARNING.code, UnifiedStatusEnum.WARNING.msg);

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "ResultStatus [code=" + code + ", msg=" + msg + "]";
	}

}
