package com.mingri.langhuan.cabinet.cache;

import java.util.function.Function;

public interface ICache {

	/**
	 * 队列存储，在末尾添加元素
	 * 
	 * @param key
	 * @param value
	 * @param timeOutSecond 保存时间(秒)，超出时间，被清除
	 */
	void leftPush(String key, Object value);

	/**
	 * 队列存储，在末尾添加元素
	 * 
	 * @param key
	 * @param value
	 */
	void rightPush(String key, Object value);

	/**
	 * 队列存储，在开头添加元素
	 * 
	 * @param key
	 * @param value
	 */
	void rightPush(String key, Object value, int timeOutSecond);

	/**
	 * 删除队列的最后一个元素
	 * 
	 * @param key
	 * @return
	 */
	<T> T rightPop(String key);

	/**
	 * 删除队列的第一个元素
	 * 
	 * @param key
	 * @return
	 */
	<T> T leftPop(String key);

	/**
	 * 
	 * @param key
	 * @param timeOutSecond       保存时间(秒)，超出时间，被清除
	 * @param mappingFunction
	 */
	public <T> T computeIfAbsent(String key, int timeOutSecond, Function<String, Object> mappingFunction);

	void put(String key, Object value);

	/**
	 * 
	 * @param key
	 * @param value
	 * @param timeOutSecond 保存时间(秒)，超出时间，被清除
	 */
	void put(String key, Object value, int timeOutSecond);

	/**
	 * 没有就put，否则不put，
	 * 
	 * @param key
	 * @param value
	 * @return 没有返回true
	 */
	boolean putIfAbsent(String key, Object value);

	/**
	 * 
	 * @param key
	 * @param value
	 * @param timeOutSecond 保存时间(秒)，超出时间，被清除
	 * @return
	 */
	boolean putIfAbsent(String key, Object value, int timeOutSecond);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return
	 */
	<T> T get(String key);

	/**
	 * 设置指定key的超时时间
	 * 
	 * @param key
	 * @param timeOutSecond
	 */
	void expire(String key, int timeOutSecond);

	/**
	 * 是否含有
	 * 
	 * @param key
	 * @return
	 */
	boolean hasKey(String key);

	/**
	 * 删除
	 * 
	 * @param key
	 * @return
	 */
	void remove(String key);

	/**
	 * 删除并返回
	 * 
	 * @param key
	 * @return
	 */
	<T> T removeAndGet(String key);
}
