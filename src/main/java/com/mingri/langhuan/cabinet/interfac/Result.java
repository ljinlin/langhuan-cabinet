package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 结果封装类： 内部或外部接口函数相互调用时返回的结果集全部用该类封装。<br>
 * 1、该类抽象了返回响应结果的基本属性：<br>
 *********** status:状态：统一状态码、状态码消息，支持自定义状态<br>
 *********** data：数据：直接put时，默认HashMap装载，直接add时默认ArrayList装载，也可以自定义数据类型
 * 2、该抽象类封装了基本常用的操作，静态函数：<br>
 *********** Result.success() Result.success(String msg) <br>
 *********** Result.success(Object data, String msg)<br>
 *********** Result.paramError(String msg) Result.sysError(String msg)<br>
 *********** Result.throwIfError(String errorMsg)<br>
 * ......
 * 
 * @author ljl
 */
public class Result implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = -3601315875464659690L;

	/**
	 * data：数据，可以是任意对象，默认为null,默认用hashMap装载，
	 */
	private Object data;

	/**
	 * 状态
	 */
	public final ResultStatus status;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Result(ResultStatus status) {
		this.status = status;
	}

	public Result(String status, String msg) {
		this.status = new ResultStatus(status, msg);
	}

	/**
	 * 当data为Map时，调用此函数，数据将装载到其中，如果data为null，则默认创建一个HashMap并装载到其中
	 * 
	 * @param key 键
	 * @param dataItem 值
	 * @return 返回HashMap类型的data属性
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> putToData(String key, Object dataItem) {
		this.data = this.data == null ? new HashMap<String, Object>() : this.data;
		((Map<String, Object>) data).put(key, dataItem);
		
		return (Map<String, Object>) data;
	}

	/**
	 * 当data为List时，调用此函数，数据将装载到其中，如果data为null，则默认创建一个ArrayList并装载到其中
	 * 
	 * @param dataItem 值
	 * @return 返回ArrayList类型的data属性
	 */
	@SuppressWarnings("unchecked")
	public List<Object> addToData(Object dataItem) {
		this.data = this.data == null ? new ArrayList<Object>() : this.data;
		((List<Object>) data).add(dataItem);
		return (List<Object>) data;
	}

	/**
	 * 成功状态的结果
	 * 
	 * @return 成功对象
	 */
	public static Result success() {
		Result res = new Result(ResultStatus.SUCCESS.code,ResultStatus.SUCCESS.getMsg());
		return res;
	}

	/**
	 * 成功状态的结果，并且设置成功提示语
	 * 
	 * @param msg
	 *            自定义成功提示语
	 * @return 成功对象
	 */
	public static Result success(String msg) {
		Result res = new Result(ResultStatus.SUCCESS.code, msg);
		return res;
	}

	/**
	 * 成功的结果，并且设置结果数据
	 * 
	 * @param data
	 *            结果中的数据
	 * @return 成功对象
	 */
	public static Result success(Object data) {
		Result res = Result.success();
		res.data = data;
		return res;
	}

	/**
	 * 成功的结果，并且设置结果数据、成功提示语
	 * 
	 * @param data
	 *            结果中的数据
	 * @param msg
	 *            自定义成功提示语
	 * @return 成功对象
	 */
	public static Result success(Object data, String msg) {
		Result res = new Result(ResultStatus.SUCCESS.code, msg);
		res.data = data;
		return res;
	}
	


	/**
	 * 参数异常状态的结果
	 * 
	 * @return 参数错误对象
	 */
	public static Result paramError() {
		Result res = new Result(ResultStatus.PARAM_ERROR.code,ResultStatus.PARAM_ERROR.getMsg());
		return res;
	}

	/**
	 * 参数异常状态的结果
	 * 
	 * @param msg
	 *            自定义参数异常提示语
	 * @return 参数错误对象
	 */
	public static Result paramError(String msg) {
		Result res = new Result(ResultStatus.PARAM_ERROR.code, msg);
		return res;
	}
	/**
	 * 参数异常状态的结果
	 * 
	 * @param msg
	 *            自定义参数异常提示语
	 * @return 警告对象
	 */
	public static Result warning(String msg) {
		Result res = new Result(ResultStatus.WARNING.code, msg);
		return res;
	}

	/**
	 * 系统异常状态的结果
	 * 
	 * @return 系统异常对象
	 */
	public static Result sysError() {
		Result res = new Result(ResultStatus.SYS_ERROR.code,ResultStatus.SYS_ERROR.getMsg());
		return res;
	}

	/**
	 * 系统异常状态的结果
	 * 
	 * @param msg
	 *            自定义系统异常提示语
	 * @return 系统异常对象
	 */
	public static Result sysError(String msg) {
		Result res = new Result(ResultStatus.SYS_ERROR.code, msg);
		return res;
	}



	/**
	 * 如果是错误状态(排除成功和警告的状态)，抛出ResultException异常
	 * @return 返回自己
	 */
	public Result throwIfError() throws  ResultException {
		if (this.status.code == ResultStatus.PARAM_ERROR.code || this.status.code == ResultStatus.SYS_ERROR.code) {
			throw new ResultException(this);
		}
		return this;
	}
	public Result throwIfNoSuccess() throws  ResultException   {
		if (this.status.code != ResultStatus.SUCCESS.code) {
			throw new ResultException(this);
		}
		return this;
	}
	public void throwError()  throws  ResultException  {
			throw new ResultException(this);
	}

	@Override
	public String toString() {
		return "Result [data=" + data + ", status=" + status + "]";
	}

	public boolean isSuccess() {
		if(this.status.code.equals(ResultStatus.SUCCESS.code)) {
			return true;
		}
		return false;
	}
	public boolean isNoError() {
		if(isSuccess()||this.status.code.equals(ResultStatus.WARNING.code)) {
			return true;
		}
		return false;
	}
	
}
