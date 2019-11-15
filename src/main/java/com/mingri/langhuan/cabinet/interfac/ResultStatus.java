package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;

import com.mingri.langhuan.cabinet.constant.OperStatusEnum;

/**
 * 结果状态： <br>
 * 1、参数错误：客户端传给服务端的参数不符合服务端的要求，是程序的问题<br>
 * 2、 系统异常：服务器出的错误，程序问题或者数据问题或者网络等问题<br>
 * 3、 成功：请求一切正常 <br>
 * 4、 警告：<br>
 * &nbsp;4.1、半成功的警告：虽然请求一切顺利，但是不有一些不令人满意，如查询结果没有一条数据，
 * &nbsp;&nbsp;如批量提交订单时，部分提交成功了，部分失败了，而且业务上允许部分订单成功或者失败，如订购车票时，车票刚好卖完了或者是只订购成功了部分车票<br>
 * &nbsp;4.2、失败的警告：一般用于故障转移，如短时间使用人数太多，暂时拒绝了客户端的请求；如数据库数据异常，但是程序做了处理，只是不允部分业务的使用，需要时间或者程序员处理,如某个业务暂停或者暂时无法使用...<br>
 * 
 * @author ljl
 *
 */
public class ResultStatus implements Serializable {

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
	public static final ResultStatus SUCCESS = new ResultStatus(OperStatusEnum.SUCCESS.code,
			OperStatusEnum.SUCCESS.msg);

	/**
	 * 参数异常的状态
	 */
	public static final ResultStatus PARAM_ERROR = new ResultStatus(OperStatusEnum.PARAM_ERROR.code,
			OperStatusEnum.PARAM_ERROR.msg);

	/**
	 * 系统异常的状态
	 */
	public static final ResultStatus SYS_ERROR = new ResultStatus(OperStatusEnum.SYS_ERROR.code,
			OperStatusEnum.SYS_ERROR.msg);

	/**
	 * 半成功的状态
	 */
	public static final ResultStatus IMPERFECT = new ResultStatus(OperStatusEnum.IMPERFECT.code,
			OperStatusEnum.IMPERFECT.msg);

	/**
	 * 警告（未成功，有错误，但是已经故障转移或已处理错误）的状态
	 */
	public static final ResultStatus WARNING = new ResultStatus(OperStatusEnum.WARNING.code,
			OperStatusEnum.WARNING.msg);

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
