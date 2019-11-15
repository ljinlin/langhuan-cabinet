package com.mingri.langhuan.cabinet.constant;

/**
 * 统一状态码
 * 
 * @author ljl
 *
 */
public interface UnifiedCode {

	/**
	 * 通用的操作状态
	 * 
	 * @author ljl
	 */
	interface Oper {

		/**
		 * 成功，完全满足了请求
		 */
		static final int SUCCESS = 200;

		/**
		 * 参数错误，没有满足请求，虽然请求成功，但是请求参数错误导致请求无法继续执行
		 */
		static final int PARAM_ERROR = 400;

		/**
		 * 系统错误，没有满足请求，虽然请求成功，但是在请求过程中出现了未知的错误
		 */
		static final int SYS_ERROR = 500;

		/**
		 * 有点遗憾、半成功，满足了请求的部分
		 */
		static final int IMPERFECT = 250;

		/**
		 * 警告，没有满足请求，由于服务遇到了无法执行请求的问题，但是做出了友好响应或者提示，
		 */
		static final int WARNING = 350;
	}

	/**
	 * 元状态
	 * 
	 * @author ljl
	 */
	public interface Meta {
		/**
		 * 状态1：阳性、 进行中、父、男、变化、 外表、正常使用、未删除、张扬、热闹、是、真.....
		 */
		static final int YANG = 1;

		/**
		 * 状态0：阴性、 最开始、母、女、不变、里面、禁用、删除、沉默、安静、否、假......
		 */
		static final int YIN = 0;

	}
}
