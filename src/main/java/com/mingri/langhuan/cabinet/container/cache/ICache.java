package com.mingri.langhuan.cabinet.container.cache;

import java.util.function.Function;

public interface ICache {

	/**
	 * 队列存储，在左边添加元素
	 * 
	 * @param key 缓存键
	 * @param value 缓存值 
	 */
	void leftPush(String key, Object value);

	/**
	 * 队列存储，在右边添加元素
	 * 
	 * @param key 缓存键
	 * @param value 缓存值
	 */
	void rightPush(String key, Object value);


	/**
	 * 删除队列的最后一个元素
	 * 
	 * @param <T>返回的对象类型
	 * @param key 缓存键
	 * @return 返回删除的对象
	 */
	<T> T rightPop(String key);

	/**
	 * 删除队列的第一个元素
	 * 
	 * @param <T>返回的对象类型
	 * @param key 缓存键
	 * @return 返回删除的对象
	 */
	<T> T leftPop(String key);

	/**
	 * @param <T>返回的对象类型
	 * @param key 缓存键
	 * @param timeOutSecond 缓存超时时间（秒）   保存时间(秒)，超出时间，被清除
	 * @param mappingFunction  缓存提供者
	 * @return 缓存的对象
	 */
	public <T> T computeIfAbsent(String key, int timeOutSecond, Function<String, T> mappingFunction);

	void put(String key, Object value);

	/**
	 * 
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeOutSecond 缓存超时时间（秒） 保存时间(秒)，超出时间，被清除
	 */
	void put(String key, Object value, int timeOutSecond);

	/**
	 * 没有就put，否则不put，
	 * 
	 * @param key 缓存键
	 * @param value 缓存值
	 * @return 返回已经缓存的对象，没有缓存返回null
	 */
	 <T> T putIfAbsent(String key, Object value);

	/**
	 * 没有就put，否则不put，
	 * 
	 * @param key 缓存键
	 * @param value 缓存值
	 * @param timeOutSecond 缓存超时时间（秒） 保存时间(秒)，超出时间，被清除
	 * @return 没有返回true
	 */
	<T> T putIfAbsent(String key, Object value, int timeOutSecond);

	/**
	 * 获取缓存
	 * 
	 * @param <T>返回的对象类型
	 * @param key 缓存键
	 * @return 返回缓存的对象
	 */
	<T> T get(String key);

	/**
	 * 设置指定key的超时时间
	 * 
	 * @param key 缓存键
	 * @param timeOutSecond 缓存超时时间（秒）
	 */
	void expire(String key, int timeOutSecond);

	/**
	 * 是否含有
	 * 
	 * @param key 缓存键
	 * @return 存在返回true
	 */
	boolean hasKey(String key);

	/**
	 * 删除
	 * 
	 * @param key 缓存键
	 */
	void remove(String key);

	/**
	 * 删除并返回
	 * 
	 * @param <T>返回的对象类型
	 * @param key 缓存键
	 * @return 返回缓存的对象
	 */
	<T> T removeAndGet(String key);
	
	/**
	 * 获取数据（可能存在过期数据）总数
	 * 
	 * @return 缓存总数
	 */
	 long size();
}
