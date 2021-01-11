package com.mingri.langhuan.cabinet.tool;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * class工具类
 * 
 * @author ljl
 *
 */
public class ClassTool {

	private ClassTool() {
	}

	/**
	 * 获取非私有、非静态的函数(包含（非Object）父类的)
	 * 
	 * @param clazz    class对象
	 * @param getFinal 是否也获取常量函数
	 * @return 函数集合
	 */
	public static List<Method> getDecararedMethods(Class<?> clazz, boolean getFinal) {
		List<Method> methods = new ArrayList<>();
		Class<?> nextClass = clazz;
		while (nextClass != null && nextClass != Object.class) {
			Method[] mary = nextClass.getDeclaredMethods();
			for (Method m : mary) {
				int modifiers = m.getModifiers();
				if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers))
					continue;
				if (Modifier.isFinal(modifiers) && !getFinal)
					continue;
				methods.add(m);
			}
			nextClass = nextClass.getSuperclass();
		}
		return methods;
	}

	/**
	 * 获取非静态的字段(包含（非Object）父类的)
	 * 
	 * @param clazz    class对象
	 * @param getFinal 是否也获取常量字段
	 * @return 字段集合
	 */
	public static List<Field> getDecararedFields(Class<?> clazz, boolean getFinal) {
		List<Field> fields = new ArrayList<>();
		Class<?> nextClass = clazz;
		while (nextClass != null && nextClass != Object.class) {
			Field[] fary = nextClass.getDeclaredFields();
			for (Field f : fary) {
				int modifiers = f.getModifiers();
				if (Modifier.isStatic(modifiers))
					continue;
				if (Modifier.isFinal(modifiers) && !getFinal)
					continue;
				fields.add(f);
			}
			nextClass = nextClass.getSuperclass();
		}
		return fields;
	}

	/**
	 * 获取非静态的字段(包含（非Object）父类的)
	 * 
	 * @param clazz    class对象
	 * @param getFinal 是否获取常量字段
	 * @return 字段Map,键为字段名称
	 */
	public static Map<String, Field> getDecararedFieldsMap(Class<?> clazz, boolean getFinal) {
		Map<String, Field> fields = new HashMap<>();
		Class<?> nextClass = clazz;
		while (nextClass != null && nextClass != Object.class) {
			Field[] fary = nextClass.getDeclaredFields();
			for (Field f : fary) {
				int modifiers = f.getModifiers();
				if (Modifier.isStatic(modifiers))
					continue;
				if (Modifier.isFinal(modifiers) && !getFinal)
					continue;
				fields.put(f.getName(), f);
			}
			nextClass = nextClass.getSuperclass();
		}
		return fields;
	}

	/**
	 * 是否含有非私有、非静态的函数(不包含父类的)
	 * 
	 * @param clazz class对象
	 * @return 存在返回true
	 */
	public static boolean isExistSelfDeclaredMethod(Class<?> clazz) {
		Method[] mary = clazz.getDeclaredMethods();
		for (Method m : mary) {
			int modifiers = m.getModifiers();
			if (Modifier.isPrivate(modifiers) || Modifier.isStatic(modifiers)) {
				continue;
			} else {
				return true;
			}
		}
		return false;
	}

	/**
	 * 反射取值
	 * 
	 * @param obj   反射的对象
	 * @param field 反射的字段
	 * @return 字段值
	 * @throws IllegalArgumentException 非法参数
	 * @throws IllegalAccessException   非法访问
	 */
	public static Object reflexVal(Object obj, Field field) throws IllegalArgumentException, IllegalAccessException {
		field.setAccessible(true);
		return field.get(obj);
	}

	/**
	 * 获取非私有、非静态的字段(包含（非Object）父类的)
	 * 
	 * @param clazz     类
	 * @param fieldName 字段名称
	 * @return 字段对象
	 */
	public static Field searchDecararedField(Class<?> clazz, String fieldName) {
		Class<?> nextClass = clazz;
		while (nextClass != null && nextClass != Object.class) {
			Field[] fary = nextClass.getDeclaredFields();
			for (Field f : fary) {
				if (f.getName().equals(fieldName)) {
					return f;
				}
			}
			nextClass = nextClass.getSuperclass();
		}
		return null;
	}

	/**
	 * 获取指定的继承的（类或接口）泛型
	 * 
	 * @author ljl
	 * @date 2020-11-5
	 * @param clazz 子类
	 * @param superClass 父类或接口
	 * @return 所有泛型
	 */
	public static List<Type> getExtendGenericity(Class<?> clazz, Class<?> superClass) {
		AnnotatedType[] atypes = null;
		AnnotatedType atype = clazz.getAnnotatedSuperclass();
		if (atype == null) {
			if (clazz.isInterface()) {
				atypes = clazz.getAnnotatedInterfaces();
			} else {
				return Collections.emptyList();
			}
		} else {
			atypes = new AnnotatedType[] { atype };
		}

		List<Type> list = new ArrayList<>();
		for (AnnotatedType antype : atypes) {
			Type temptype = antype.getType();
			if(temptype instanceof ParameterizedType) {
				ParameterizedType pty = (ParameterizedType) antype.getType();
				
				if (!pty.getRawType().getTypeName().equals(superClass.getTypeName())) {
					continue;
				}
				Type[] tps = pty.getActualTypeArguments();
				if (tps == null || tps.length == 0) {
					continue;
				}
				for (Type type : tps) {
					list.add(type);
				}
			}
		}
		return list;
	}

	/**
	 * 获取继承的泛型
	 * 
	 * @author ljl
	 * @date 2020-11-5
	 * @param clazz 子类
	 * @return 所有泛型
	 */
	public static List<Type> getExtendGenericity(Class<?> clazz) {
		AnnotatedType[] atypes = null;
		AnnotatedType atype = clazz.getAnnotatedSuperclass();
		if (atype == null) {
			if (clazz.isInterface()) {
				atypes = clazz.getAnnotatedInterfaces();
			} else {
				return Collections.emptyList();
			}
		} else {
			atypes = new AnnotatedType[] { atype };
		}

		List<Type> list = new ArrayList<>();

		for (AnnotatedType antype : atypes) {
			ParameterizedType ty = (ParameterizedType) antype.getType();
			Type[] tps = ty.getActualTypeArguments();
			if (tps == null || tps.length == 0) {
				continue;
			}
			for (Type type : tps) {
				list.add(type);
			}
		}
		return list;
	}

	/**
	 * 获取实现的（接口或抽象类）的泛型
	 * 
	 * @author ljl
	 * @date 2020-11-5
	 * @param clazz 实现类
	 * @return 所有泛型
	 */
	public static List<Type> getImplGenericity(Class<?> clazz) {
		AnnotatedType[] atypes = clazz.getAnnotatedInterfaces();

		if (atypes == null || atypes.length == 0) {
			return Collections.emptyList();
		}
		List<Type> list = new ArrayList<>();
		for (AnnotatedType atype : atypes) {
			ParameterizedType ty = (ParameterizedType) atype.getType();
			Type[] tps = ty.getActualTypeArguments();
			if (tps == null || tps.length == 0) {
				continue;
			}
			for (Type type : tps) {
				list.add(type);
			}
		}
		return list;
	}

	/**
	 * 获取指定的实现的（接口或者抽象类）泛型
	 * 
	 * @author ljl
	 * @date 2020-11-5
	 * @param clazz 实现类
	 * @param interf 接口或者抽象类
	 * @return 所有泛型
	 */
	public static List<Type> getImplGenericity(Class<?> clazz, Class<?> interf) {
		AnnotatedType[] atypes = clazz.getAnnotatedInterfaces();
		if (atypes == null || atypes.length == 0) {
			return Collections.emptyList();
		}
		List<Type> list = new ArrayList<>();
		for (AnnotatedType atype : atypes) {
			ParameterizedType ty = (ParameterizedType) atype.getType();
			if (!ty.getRawType().getTypeName().equals(interf.getTypeName())) {
				continue;
			}
			Type[] tps = ty.getActualTypeArguments();
			if (tps == null || tps.length == 0) {
				continue;
			}
			for (Type type : tps) {
				list.add(type);
			}
		}
		return list;
	}


}
