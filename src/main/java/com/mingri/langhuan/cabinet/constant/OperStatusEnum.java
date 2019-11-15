package com.mingri.langhuan.cabinet.constant;

/**
 * 操作状态枚举
 * 
 * @author ljl
 *
 */
public enum OperStatusEnum {

	SUCCESS(UnifiedCode.Oper.SUCCESS + "", "成功"),
	// <br>
	PARAM_ERROR(UnifiedCode.Oper.PARAM_ERROR + "", "参数错误"),
	// <br>
	SYS_ERROR(UnifiedCode.Oper.SYS_ERROR + "", "系统错误"),
	// <br>
	WARNING(UnifiedCode.Oper.WARNING + "", "警告"),
	// <br>
	IMPERFECT(UnifiedCode.Oper.IMPERFECT + "", "提醒");

	public final String code;
	public final String msg;

	OperStatusEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
