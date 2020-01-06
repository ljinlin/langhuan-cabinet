package com.mingri.langhuan.cabinet.interfac;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BooleanSupplier;

import com.mingri.langhuan.cabinet.exception.RespException;
import com.mingri.langhuan.cabinet.tool.StrTool;

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
public class Resp implements Serializable {

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
	public final RespStatus status;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Resp(RespStatus status) {
		this.status = status;
	}

	/**
	 * 自定义状态
	 * 
	 * @param statusCode 自定义状态码
	 * @param statusMsg  自定义状态消息
	 */
	private Resp(String statusCode, String statusMsg) {
		this.status = new RespStatus(statusCode, statusMsg);
	}

	/**
	 * 当data为{@code Map}时，调用此函数，数据将装载到其中，如果data为null，则默认创建一个{@code HashMap}作为data,并装载到其中
	 * 
	 * @param dataItemKey      键
	 * @param dataItemValue 值
	 * @return 返回{@code HashMap}类型的data属性
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
	 * @param dataItemValue 值
	 * @return 返回ArrayList类型的data属性
	 */
	@SuppressWarnings("unchecked")
	public List<Object> addToData(Object dataItemValue) {
		this.data = this.data == null ? new ArrayList<Object>() : this.data;
		((List<Object>) data).add(dataItemValue);
		return (List<Object>) data;
	}

	/**
	 * 如果是错误状态(排除成功和警告的状态)，抛出ResultException异常
	 * 
	 * @return 返回自己
	 */
	public Resp throwIfError() {
		if (notError()) {
			return this;
		}
		throw new RespException(this);
	}

	public Resp throwIfNoOnlySuccess() {
		if (onlySuccess()) {
			return this;
		}
		throw new RespException(this);
	}

	public Resp throwIfNoSuccess() {
		if (success()) {
			return this;
		}
		throw new RespException(this);
	}

	public void throwError() {
		throw new RespException(this);
	}

	@Override
	public String toString() {
		return "Result [data=" + data + ", status=" + status + "]";
	}

	/**
	 * 
	 * 判断是否只是成功：ResultStatus.SUCCESS是成功
	 * 
	 * @return true成功,false失败
	 */
	public boolean onlySuccess() {
		if (this.status.code.equals(RespStatus.SUCCESS.code)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否成功：{@code ResultStatus.SUCCESS}和{@code ResultStatus.IMPERFECT}都是成功
	 * 
	 * @return true成功和半成功,其他false
	 */
	public boolean success() {
		if (this.status.code.equals(RespStatus.SUCCESS.code) || this.status.code.equals(RespStatus.IMPERFECT.code)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否不是异常：{@code ResultStatus.SUCCESS}、{@code ResultStatus.IMPERFECT}、{@code ResultStatus.WARNING}
	 * 都不是异常
	 * 
	 * @return true:{@code success()},警告，其他false
	 */
	public boolean notError() {
		if (success() || this.status.code.equals(RespStatus.WARNING.code)) {
			return true;
		}
		return false;
	}

	public static class Assert {

		public static void illegalArgument(BooleanSupplier booleanSupplier, String... msg) {
			if (booleanSupplier.getAsBoolean()) {
				return;
			}
			throw new IllegalArgumentException(StrTool.concat((Object[])msg));
		}

	}

	
	public static class Builder {

		/**
		 * 自定义状态
		 * 
		 * @param statusCode 自定义状态码
		 * @return 返回自定义状态对象
		 */
		public static Resp build(String statusCode) {
			return new Resp(statusCode, null);
		}

		/**
		 * 自定义状态
		 * 
		 * @param statusCode 自定义状态码
		 * @param statusMsg  自定义状态消息
		 * @return 返回自定义状态对象
		 */
		public static Resp build(String statusCode, String statusMsg) {
			return new Resp(statusCode, statusMsg);
		}

		/**
		 * 成功状态的结果
		 * 
		 * @return 成功对象
		 */
		public static Resp buildSuccess() {
			Resp res = new Resp(RespStatus.SUCCESS.code, RespStatus.SUCCESS.getMsg());
			return res;
		}

		/**
		 * 成功状态的结果，并且设置成功提示语
		 * 
		 * @param msg 自定义成功提示语
		 * @return 成功对象
		 */
		public static Resp buildSuccess(String... msg) {
			Resp res = new Resp(RespStatus.SUCCESS.code, StrTool.concat(msg));
			return res;
		}

		/**
		 * 成功的结果，并且设置结果数据
		 * 
		 * @param data 结果中的数据
		 * @return 成功对象
		 */
		public static Resp buildSuccess(Object data) {
			Resp res = Builder.buildSuccess();
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
		public static Resp buildSuccess(Object data, String... msg) {
			Resp res = new Resp(RespStatus.SUCCESS.code, StrTool.concat(msg));
			res.data = data;
			return res;
		}

		/**
		 * 参数异常状态的结果
		 * 
		 * @return 参数错误对象
		 */
		public static Resp buildParamError() {
			Resp res = new Resp(RespStatus.PARAM_ERROR.code, RespStatus.PARAM_ERROR.getMsg());
			return res;
		}

		/**
		 * 参数异常状态的结果
		 * 
		 * @param msg 自定义参数异常提示语
		 * @return 参数错误对象
		 */
		public static Resp buildParamError(String... msg) {
			Resp res = new Resp(RespStatus.PARAM_ERROR.code, StrTool.concat(msg));
			return res;
		}

		/**
		 * 半成功的结果（比如没有完全成功，或者成功结果不令人满意，如查询列表时空数据，如买车票，没抢到或者是只抢到部分）
		 * 
		 * @param msg 自定义参数异常提示语
		 * @return 警告对象
		 */
		public static Resp buildImperfect(String... msg) {
			Resp res = new Resp(RespStatus.IMPERFECT.code, StrTool.concat(msg));
			return res;
		}

		/**
		 * 半成功的结果（比如没有完全成功，或者成功结果不令人满意，如查询列表时空数据，如买车票，没抢到或者是只抢到部分）
		 * 
		 * @param data 响应数据
		 * @param msg 自定义参数异常提示语
		 * @return 半成功对象
		 */
		public static Resp buildImperfect(Object data, String... msg) {
			Resp res = new Resp(RespStatus.IMPERFECT.code, StrTool.concat(msg));
			res.data = data;
			return res;
		}

		/**
		 * 警告的结果（比如用于故障转移）
		 * 
		 * @param msg 自定义参数异常提示语
		 * @return 警告对象
		 */
		public static Resp buildWarning(String... msg) {
			Resp res = new Resp(RespStatus.WARNING.code, StrTool.concat(msg));
			return res;
		}

		/**
		 * 系统异常状态的结果
		 * 
		 * @return 系统异常对象
		 */
		public static Resp buildSysError() {
			Resp res = new Resp(RespStatus.SYS_ERROR.code, RespStatus.SYS_ERROR.getMsg());
			return res;
		}

		/**
		 * 系统异常状态的结果
		 * 
		 * @param msg 自定义系统异常提示语
		 * @return 系统异常对象
		 */
		public static Resp buildSysError(String... msg) {
			Resp res = new Resp(RespStatus.SYS_ERROR.code, StrTool.concat(msg));
			return res;
		}

	}
}
