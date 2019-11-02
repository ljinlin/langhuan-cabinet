package com.mingri.commons.constant;

/**
 * 统一状态
 * 
 * @author 尘无尘
 *
 */
public enum UnifiedStatusEnum {

	SUCCESS(UnifiedCode.Oper.SUCCESS + "", "成功"),
	// <br>
	PARAM_ERROR(UnifiedCode.Oper.PARAM_ERROR + "", "参数错误"),
	// <br>
	SYS_ERROR(UnifiedCode.Oper.SYS_ERROR + "", "系统错误"),
	// <br>
	WARNING(UnifiedCode.Oper.WARNING + "", "警告");

	public final String code;
	public final String msg;

	UnifiedStatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
