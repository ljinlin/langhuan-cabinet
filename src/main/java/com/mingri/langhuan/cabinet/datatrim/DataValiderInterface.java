package com.mingri.langhuan.cabinet.datatrim;

import com.mingri.langhuan.cabinet.constant.OperTypeEnum;
import com.mingri.langhuan.cabinet.interfac.Resp;

/**
 * 数据校验接口
 * 
 * @param <T> 数据对象类型
 * @author ljl
 *
 */
public interface DataValiderInterface<T> {

	Resp validVal(OperTypeEnum operTypeEnum, T data);

}
