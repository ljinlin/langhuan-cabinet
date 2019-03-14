package com.ws.commons.bean;

import java.io.InputStream;
import java.util.LinkedHashMap;


import com.ws.commons.interfac.HandleKV;
import com.ws.commons.interfac.ValidObj;
import com.ws.commons.tool.StrTool;

public class ExcelResolveBean {

	/**
	 * 字段信息： key: excel字段名称 val: 实体类字段名称
	 */
	private LinkedHashMap<String, String> fieldMap;
	/**
	 * 
	 * 数据字段所在行索引
	 */
	private int dataStartIndex;

	/**
	 * 标题字段所在行索引
	 */
	private int titleFieldIndex;

	/**
	 * 要处理的sheet
	 */
	private String sheetName;

	/**
	 * 验证数据的对象
	 */
	private ValidObj validObj;

	/**
	 * 处理数据的对象
	 */
	private HandleKV handleKV;

	/**
	 * 是否接受响应消息
	 */
	private Boolean isReceiveRespMsg = false;

	/**
	 * 针对每行数据的响应消息
	 */
	private static final ThreadLocal<LinkedHashMap<Integer, String>> RESP_ROW_MSG = new ThreadLocal<>();

	/**
	 * excel文件输入流
	 */
	private static final ThreadLocal<InputStream> EXCEL_INPUT_STREAM = new ThreadLocal<>();

	// private static final ThreadLocal<ExcelResolveBean> CURRENT_INSTANCE=new
	// ThreadLocal<>();

	/**
	 * 要映射的实体
	 */
	private Class<?> mapClass;

	public ExcelResolveBean() {
	}

	public ExcelResolveBean(LinkedHashMap<String, String> fieldMap, int dataStartIndex, int titleFieldIndex,
			String sheetName, Boolean isReceiveRespMsg, Class<?> mapClass) {
		super();
		this.fieldMap = fieldMap;
		this.dataStartIndex = dataStartIndex;
		this.titleFieldIndex = titleFieldIndex;
		this.sheetName = sheetName;
		this.isReceiveRespMsg = isReceiveRespMsg;
		this.mapClass = mapClass;
	}

	public LinkedHashMap<String, String> getFieldMap() {
		return fieldMap;
	}

	public void setFieldMap(LinkedHashMap<String, String> fieldMap) {
		this.fieldMap = fieldMap;
	}

	public int getDataStartIndex() {
		return dataStartIndex;
	}

	public void setDataStartIndex(int dataStartIndex) {
		this.dataStartIndex = dataStartIndex;
	}

	public int getTitleFieldIndex() {
		return titleFieldIndex;
	}

	public void setTitleFieldIndex(int titleFieldIndex) {
		this.titleFieldIndex = titleFieldIndex;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ValidObj getValidObj() {
		return validObj;
	}

	public void setValidObj(ValidObj validObj) {
		this.validObj = validObj;
	}

	public HandleKV getHandleKV() {
		return handleKV;
	}

	public void setHandleKV(HandleKV handleKV) {
		this.handleKV = handleKV;
	}

	public InputStream getExcelInputStream() {
		return EXCEL_INPUT_STREAM.get();
	}

	public Class<?> getMapClass() {
		return mapClass;
	}

	public void setMapClass(Class<?> mapClass) {
		this.mapClass = mapClass;
	}

	public LinkedHashMap<Integer, String> getRespRowMsg() {
		return RESP_ROW_MSG.get();
	}

	public String getRespRowMsgOfString() {
		LinkedHashMap<Integer, String> respMsgMap = RESP_ROW_MSG.get();
		if (respMsgMap != null && respMsgMap.size() > 0) {
			StringBuilder sb = new StringBuilder();
			respMsgMap.forEach((k, v) -> {
				sb.append("第" + k + "行：" + v + "</br>");
			});
			return sb.toString();
		}
		return StrTool.EMPTY;
	}

	public void startResolve(InputStream excelInputStream) {
		if (isReceiveRespMsg) {
			LinkedHashMap<Integer, String> respRowMsg = RESP_ROW_MSG.get();
			if (respRowMsg == null) {
				respRowMsg = new LinkedHashMap<>();
				RESP_ROW_MSG.set(respRowMsg);
			}
		}
		EXCEL_INPUT_STREAM.set(excelInputStream);
		// CURRENT_INSTANCE.set(this);
	}

	public Boolean getIsReceiveRespMsg() {
		return isReceiveRespMsg;
	}

	public void setIsReceiveRespMsg(Boolean isReceiveRespMsg) {
		this.isReceiveRespMsg = isReceiveRespMsg;
	}

}
