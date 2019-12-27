package com.mingri.langhuan.cabinet.datatrim;

import com.mingri.langhuan.cabinet.constant.OperTypeEnum;
import com.mingri.langhuan.cabinet.interfac.Resp;

/**
 * 设置默认值接口
 * 
 * @param <T> 数据对象类型
 * @author ljl
 *
 */
public interface DataDftInterface<T> {

	Resp dftVal(OperTypeEnum operTypeEnum, T data);

}
