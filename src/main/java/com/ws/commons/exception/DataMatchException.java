package com.ws.commons.exception;

import java.math.BigDecimal;

public class DataMatchException extends IllegalArgumentException{
  
	public static void throwForNotMatch(Class<?> matchClass,Object v) {
				throw new DataMatchException("data:"+v+" not match"+matchClass);
	}
	
	public DataMatchException() {
	super();
	}
	
	public DataMatchException(String msg) {
		super(msg);
	}
public static void main(String[] args) {
	double db=(double)2.0449268E7;
	System.out.println(new BigDecimal("2.0449268E7").toPlainString());
}

}
