package com.mingri.langhuan.cabinet.interfac;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

import com.mingri.langhuan.cabinet.tool.StrTool;

public class Assert {
	/**
	 * 断言非法参数，是非法参数，抛出异常{@code IllegalArgumentException}
	 * 
	 * @author jinlin Li
	 * @param booleanSupplier 是否非法参数
	 * @param msg             多个消息段，内部拼接
	 */
	public static void illegalArg(BooleanSupplier booleanSupplier, String... msg) {
		if (booleanSupplier.getAsBoolean()) {
			throw new IllegalArgumentException(StrTool.concat((Object[]) msg));
		}
	}

	/**
	 * 断言非法参数，是非法参数，抛出异常 {@code IllegalArgumentException}
	 * 
	 * @author jinlin Li
	 * @param b   是否非法参数
	 * @param msg 多个消息段，内部拼接
	 */
	public static void illegalArg(boolean b, String... msg) {
		if (b) {
			throw new IllegalArgumentException(StrTool.concat((Object[]) msg));
		}
	}

	/**
	 * 断言是否是 null、""、" "、"null"，是，抛出异常 {@code IllegalArgumentException}
	 * 
	 * @author jinlin Li
	 * @param msg  消息
	 * @param args 要断言的参数
	 */
	public static void emptyArg(String msg, Object... args) {
		for (int i = 0; i < args.length; i++) {
			if (StrTool.checkEmpty(args[i])) {
				throw new IllegalArgumentException(msg);
			}
		}
	}

	/**
	 * 
	 * 断言是否是 null、""、" "、"null"，是，抛出异常 {@code IllegalArgumentException}
	 * 
	 * @author jinlin Li
	 * @param msg  消息
	 * @param args 要断言的参数
	 * @return 返回错误参数的索引
	 */
	public static List<Integer> emptyArg(Object... args) {
		List<Integer> indexList = null;
		for (int i = 0; i < args.length; i++) {
			if (StrTool.checkEmpty(args[i])) {
				if (indexList == null) {
					indexList = new ArrayList<>(0);
				}
				indexList.add(i);
			}
		}
		return indexList == null ? Collections.emptyList() : indexList;
	}
	
	
	/**
	 * 
	 * 断言是否是 null、""、" "、"null"，是，抛出异常 {@code IllegalArgumentException}
	 * 
	 * @author jinlin Li
	 * @param msg  消息
	 * @param args 要断言的参数和参数名称
	 * @return 返回错误参数的索引
	 */
	public static void emptyArgAndPrompt(String tailmsg,Object... args) {
		StringBuilder argNames=null;
		for (int i = 0; i < args.length; i+=2) {
			if (StrTool.checkEmpty(args[i+1])) {
				if( argNames==null) {
					argNames=new StringBuilder();
				}
				argNames.append(args[i]).append("、");
			}
		}
		if (argNames!=null) {
			argNames.deleteCharAt(argNames.length()-1);
			argNames.append(tailmsg);
			throw new IllegalArgumentException(argNames.toString());
		}
	}
	

}
