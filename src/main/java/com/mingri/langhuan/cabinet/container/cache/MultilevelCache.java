package com.mingri.langhuan.cabinet.container.cache;

import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mingri.langhuan.cabinet.tool.CacheTool;
import com.mingri.langhuan.cabinet.tool.ThreadTool;

/**
 * 多级缓存实现
 * 
 * @author ljl
 *
 */
public class MultilevelCache implements ICache {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultilevelCache.class);

	private MultilevelCache() {
	}

	private ICache firstLevelCache = LocalCache.instance();
	private ICache secondLevelCache;

	private static final String LOCK_PREFIX = "MUILCACHE_LOCK:";
	private int firstLevelCacheMaxTimeout = 120;// 默认120秒

	public ICache getFirstLevelCache() {
		return firstLevelCache;
	}

	public void setFirstLevelCache(ICache firstLevelCache) {
		this.firstLevelCache = firstLevelCache;
		LOGGER.info("开启一级缓存：{}", firstLevelCache);
	}

	public ICache getSecondCache() {
		return secondLevelCache;
	}

	public void setSecondCache(ICache secondCache) {
		this.secondLevelCache = secondCache;
		LOGGER.info("开启二级缓存：{}", secondCache);
	}

	public int getFirstLevelCacheMaxTimeout() {
		return firstLevelCacheMaxTimeout;
	}

	public void setFirstLevelCacheMaxTimeout(int firstLevelCacheMaxTimeout) {
		this.firstLevelCacheMaxTimeout = firstLevelCacheMaxTimeout;
	}

	public void put(String key, Object value) {
		put(key, value, 0);
	}

	public void put(String key, Object value, int timeOutSecond) {
		if (secondLevelCache != null) {
			secondLevelCache.put(key, value, timeOutSecond);
			firstLevelCache.put(key, value, cmpFirstCacheTimeOutSecond(timeOutSecond));
		} else {
			firstLevelCache.put(key, value, timeOutSecond);
		}
	}

	/**
	 * 提供数据，并缓存
	 * 
	 * @param <T>      返回的对象类型
	 * @param key      缓存键
	 * @param mappingFunction 缓存值提供者
	 * @return 返回缓存的对象
	 */
	public <T> T computeIfAbsent(String key, Function<String, T> mappingFunction) {
		return computeIfAbsent(key, 0, mappingFunction);
	}

	/**
	 * 提供数据，并缓存一定的时间
	 * 
	 * @param <T>           返回的对象类型
	 * @param key           缓存键
	 * @param timeOutSecond 缓存超时时间（秒）
	 * @param mappingFunction      缓存数据计算者
	 * @return 返回缓存的对象
	 */
	public <T> T computeIfAbsent(String key, int timeOutSecond, Function<String, T> mappingFunction) {
		T data = firstLevelCache.get(key);
		if (data == null && secondLevelCache != null) {
			data = secondLevelCache.get(key);
		}
		if (data != null) {
			return data;
		}
		synchronized (ThreadTool.buildLock(LOCK_PREFIX, key)) {
			data = firstLevelCache.get(key);
			if (data == null && secondLevelCache != null) {
				data = secondLevelCache.get(key);
			}
			if (data != null) {
				return data;
			}
			data = mappingFunction.apply(key);
			if (secondLevelCache != null) {
				secondLevelCache.put(key, data, timeOutSecond);
				firstLevelCache.put(key, data, cmpFirstCacheTimeOutSecond(timeOutSecond));
			} else {
				firstLevelCache.put(key, data, timeOutSecond);
			}

		}
		return data;
	}

	@SuppressWarnings("unchecked")
	public <T> T removeAndGet(String key) {
		T data = null;
		if (secondLevelCache != null) {
			data = secondLevelCache.removeAndGet(key);
		}
		T data2 = firstLevelCache.removeAndGet(key);
		if (data == null) {
			data = data2;
		}
		return (T) CacheTool.getSrcVal(data);
	}

	public void remove(String key) {
		if (secondLevelCache != null) {
			secondLevelCache.remove(key);
		}
		firstLevelCache.remove(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		T data = firstLevelCache.get(key);
		if (data == null && secondLevelCache != null) {
			data = secondLevelCache.get(key);
		}
		return (T) CacheTool.getSrcVal(data);
	}

	public void expire(String key, int timeOutSecond) {
		firstLevelCache.expire(key, cmpFirstCacheTimeOutSecond(timeOutSecond));
		if (secondLevelCache != null) {
			secondLevelCache.expire(key, timeOutSecond);
		}
	}

	public boolean hashKey(String key) {
		boolean flag = firstLevelCache.hasKey(key);
		if (!flag && secondLevelCache != null) {
			flag = secondLevelCache.hasKey(key);
		}
		return flag;
	}

	private int cmpFirstCacheTimeOutSecond(int timeOutSecond) {
		if (timeOutSecond < 1 || timeOutSecond > firstLevelCacheMaxTimeout) {
			return firstLevelCacheMaxTimeout;
		}
		return timeOutSecond;
	}

	@Override
	public void leftPush(String key, Object value) {
		firstLevelCache.leftPush(key, value);
		if (secondLevelCache != null) {
			secondLevelCache.leftPush(key, value);
		}
	}

	@Override
	public void rightPush(String key, Object value) {
		firstLevelCache.rightPush(key, value);
		if (secondLevelCache != null) {
			secondLevelCache.rightPush(key, value);
		}
	}

	@Override
	public <T> T rightPop(String key) {
		T data = firstLevelCache.rightPop(key);
		if (data == null && secondLevelCache != null) {
			data = secondLevelCache.rightPop(key);
		}
		return data;
	}

	@Override
	public <T> T leftPop(String key) {
		T data = firstLevelCache.leftPop(key);
		if (data == null && secondLevelCache != null) {
			data = secondLevelCache.leftPop(key);
		}
		return data;
	}

	@Override
	public <T> T putIfAbsent(String key, Object value) {
		return putIfAbsent(key, value, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T putIfAbsent(String key, Object value, int timeOutSecond) {
		Object data = firstLevelCache.putIfAbsent(key, value, timeOutSecond);
		if (data == null && secondLevelCache != null) {
			secondLevelCache.putIfAbsent(key, value, timeOutSecond);
		}
		return (T) data;
	}

	@Override
	public boolean hasKey(String key) {
		return firstLevelCache.hasKey(key) || (secondLevelCache != null && secondLevelCache.hasKey(key));
	}

	@Override
	public long size() {
		if (secondLevelCache != null) {
			return secondLevelCache.size();
		}
		return firstLevelCache.size();
	}

}
