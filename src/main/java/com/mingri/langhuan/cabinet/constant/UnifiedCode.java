package com.mingri.langhuan.cabinet.constant;

/**
 * 统一码
 * 
 * @author 尘无尘
 *
 */
public interface UnifiedCode {
	
	/**
	 * 通用的操作状态
	 * @author  ljl·尘无尘
	 * @date Oct 24, 2018
	 */
	interface Oper {

		/**
		 * 成功
		 */
		static final int SUCCESS = 200;

		/**
		 * 参数错误，虽然请求成功，但是请求参数错误 导致 无法调用接口或者无法正常执行接口
		 */
		static final int PARAM_ERROR = 400;

		/**
		 * 系统错误，虽然请求成功，但是由于系统内部问题导致无法正常处理请求
		 */
		static final int SYS_ERROR = 500;
		
		/**
		 * 警告，属于成功，但是强调警告
		 */
		static final int WARNING = 210;
	}

	/**
	 * 元状态
	 * @author  ljl·尘无尘
	 * @date Oct 24, 2018
	 */
	public interface Meta {
		
		/**
		 *  状态0：阴性、 最开始、母、女、不变、里面、禁用、删除、沉默、安静、否...... 
		 */
		static final int YIN = 0;
		
		/**
		 * 状态1：阳性、 进行中、父、男、变化、 外表、正常使用、未删除、张扬、热闹、是.....
		 */
		static final int YANG = 1;
	}
}
