package com.ws.commons.constant;

public enum OperTypeEnum {
	ADD(1, "新增"),
	// <br>
	DEL(2, "删除"),
	// <br>
	UPDATE(3, "更新"),
	SELECT(4, "查询");

	public final int code;
	public final String msg;

	OperTypeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

}
