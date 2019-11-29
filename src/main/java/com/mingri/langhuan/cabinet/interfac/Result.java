package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 内部或外部接口函数相互调用时返回的结果<br>
 * 1、 的基本属性：<br>
 *********** 1.1 status:状态<br>
 ***************** code:状态码，统一状态码，也可以自定义<br>
 ***************** msg:状态消息，有内置默认的，也可以自定义<br>
 *********** 1.2 data：数据，与之相关的有3个主要函数：<br>
 ***************** setData：设置数据<br>
 ***************** getData：获取数据<br>
 ***************** putToData：将数据put到data，就是Map的put（当data为null时，默认HashMap装载，可以通过setData定义data为Map的其他子类）,如果data不是null且不是Map，将出现异常<br>
 ***************** addToData：将数据add到data，就是List的add（当data为null时，默认ArrayList装载，可以通过setData定义data为List的其他子类）,如果data不是null且不是List，将出现异常<br>
 * 
 * @author ljl
 */
public class Result implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3601315875464659690L;

	/**
	 * 结果数据，可以是任意对象，默认为null,默认用hashMap或者arrayList装载，
	 */
	private Object data;

	/**
	 * 结果状态
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
	
	/**
	 * 自定义状态
	 * 
	 * @param statusCode 自定义状态码
	 * @param statusMsg  自定义状态消息
	 */
	public Result(String statusCode, String statusMsg) {
		this.status = new ResultStatus(statusCode, statusMsg);
	}

	/**
	 * 当data为Map时，调用此函数，数据将装载到其中，如果data为null，则默认创建一个HashMap作为data,并装载到其中
	 * 
	 * @param key      键
	 * @param dataItem 值
	 * @return 返回HashMap类型的data属性
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> putToData(String dataItemKey, Object dataItemValue) {
		this.data = this.data == null ? new HashMap<String, Object>() : this.data;
		((Map<String, Object>) data).put(dataItemKey, dataItemValue);

		return (Map<String, Object>) data;
	}

	/**
	 * 当data为List时，调用此函数，数据将装载到其中，如果data为null，则默认创建一个ArrayList并装载到其中
	 * 
	 * @param dataItem 值
	 * @return 返回ArrayList类型的data属性
	 */
	@SuppressWarnings("unchecked")
	public List<Object> addToData(Object dataItemValue) {
		this.data = this.data == null ? new ArrayList<Object>() : this.data;
		((List<Object>) data).add(dataItemValue);
		return (List<Object>) data;
	}

	/**
	 * 成功状态的结果
	 * 
	 * @return 成功对象
	 */
	public static Result success() {
		Result res = new Result(ResultStatus.SUCCESS.code, ResultStatus.SUCCESS.getMsg());
		return res;
	}

	/**
	 * 成功状态的结果，并且设置成功提示语
	 * 
	 * @param msg 自定义成功提示语
	 * @return 成功对象
	 */
	public static Result success(String msg) {
		Result res = new Result(ResultStatus.SUCCESS.code, msg);
		return res;
	}

	/**
	 * 成功的结果，并且设置结果数据
	 * 
	 * @param data 结果中的数据
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
	 * @param data 结果中的数据
	 * @param msg  自定义成功提示语
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
		Result res = new Result(ResultStatus.PARAM_ERROR.code, ResultStatus.PARAM_ERROR.getMsg());
		return res;
	}

	/**
	 * 参数异常状态的结果
	 * 
	 * @param msg 自定义参数异常提示语
	 * @return 参数错误对象
	 */
	public static Result paramError(String msg) {
		Result res = new Result(ResultStatus.PARAM_ERROR.code, msg);
		return res;
	}

	/**
	 * 半成功的结果（比如没有完全成功，或者成功结果不令人满意，如查询列表时空数据，如买车票，没抢到或者是只抢到部分）
	 * 
	 * @param msg 自定义参数异常提示语
	 * @return 警告对象
	 */
	public static Result imperfect(String msg) {
		Result res = new Result(ResultStatus.IMPERFECT.code, msg);
		return res;
	}

	/**
	 * 警告的结果（比如用于故障转移）
	 * 
	 * @param msg 自定义参数异常提示语
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
		Result res = new Result(ResultStatus.SYS_ERROR.code, ResultStatus.SYS_ERROR.getMsg());
		return res;
	}

	/**
	 * 系统异常状态的结果
	 * 
	 * @param msg 自定义系统异常提示语
	 * @return 系统异常对象
	 */
	public static Result sysError(String msg) {
		Result res = new Result(ResultStatus.SYS_ERROR.code, msg);
		return res;
	}

	/**
	 * 如果是错误状态(排除成功和警告的状态)，抛出ResultException异常
	 * 
	 * @return 返回自己
	 */
	public Result throwIfError() throws ResultException {
		if (this.status.code == ResultStatus.PARAM_ERROR.code || this.status.code == ResultStatus.SYS_ERROR.code) {
			throw new ResultException(this);
		}
		return this;
	}

	public Result throwIfNoOnlySuccess() throws ResultException {
		if (this.status.code != ResultStatus.SUCCESS.code) {
			throw new ResultException(this);
		}
		return this;
	}

	public Result throwIfNoSuccess() throws ResultException {
		if (this.status.code != ResultStatus.SUCCESS.code && this.status.code != ResultStatus.IMPERFECT.code) {
			throw new ResultException(this);
		}
		return this;
	}

	public void throwError() throws ResultException {
		throw new ResultException(this);
	}

	@Override
	public String toString() {
		return "Result [data=" + data + ", status=" + status + "]";
	}

	public boolean isOnlySuccess() {
		if (this.status.code.equals(ResultStatus.SUCCESS.code)) {
			return true;
		}
		return false;
	}

	public boolean isSuccess() {
		if (this.status.code.equals(ResultStatus.SUCCESS.code)
				|| this.status.code.equals(ResultStatus.IMPERFECT.code)) {
			return true;
		}
		return false;
	}

	public boolean isNoError() {
		if (isSuccess() || this.status.code.equals(ResultStatus.WARNING.code)) {
			return true;
		}
		return false;
	}

}
