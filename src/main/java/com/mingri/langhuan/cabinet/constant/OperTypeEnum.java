package com.mingri.langhuan.cabinet.constant;

/**
 * 操作类型枚举,CRUD
 * @author ljl
 *
 */
public enum OperTypeEnum {
	ADD(1, "新增"),
	// <br>
	DEL(2, "删除"),
	// <br>
	UPDATE(3, "更新"),
	// <br>
	SELECT(4, "查询");

	public final int code;
	public final String msg;

	OperTypeEnum(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public static OperTypeEnum ofCode(int code) {
		for (OperTypeEnum e : OperTypeEnum.values()) {
			if (e.code == code) {
				return e;
			}
		}
		return null;
	}

}
