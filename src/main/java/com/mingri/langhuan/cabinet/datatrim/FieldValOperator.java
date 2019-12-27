package com.mingri.langhuan.cabinet.datatrim;

/**
 * 
 * 对象字段值处理接口
 * 
 * @param <T> 对象类型
 * 
 * @author ljl
 *
 */
public interface FieldValOperator<T> {

	/**
	 * 根据字段数据计算后返回一个新的数据
	 * 
	 * @param fieldName  字段名称
	 * @param fieldValue 字段值
	 * @param obj        字段所属对象
	 * @return 返回一个新的数据
	 */
	Object apply(String fieldName, Object fieldValue, T obj);

}
