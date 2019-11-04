package com.mingri.langhuan.cabinet.cache;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mingri.langhuan.cabinet.tool.ThreadTool;

/**
 * 多级缓存实现
 * 
 * @author ljl
 *
 */
public class MultilevelCache {
	private static final Logger LOGGER = LoggerFactory.getLogger(MultilevelCache.class);

	private MultilevelCache() {
	}

	private static final ICache FIRST_LEVE_LCACHE = LocalCache.instance();
	private static ICache secondCache;

	private static final String LOCK_PREFIX = "MUILCACHE_LOCK:";

	public static synchronized void init(ICache secondCache) {
		if (MultilevelCache.secondCache == null) {
			MultilevelCache.secondCache = secondCache;
			LOGGER.info("开启二级缓存，secondCache：{}", secondCache);
		}
	}

	public static void put(String key, Object value, int timeOutSecond) {
		if (secondCache != null) {
			secondCache.put(key, value, timeOutSecond);
			FIRST_LEVE_LCACHE.put(key, value, cmpFirstCacheTimeOutSecond(timeOutSecond));
		} else {
			FIRST_LEVE_LCACHE.put(key, value, timeOutSecond);
		}
	}

	/**
	 * 提供数据，并缓存
	 * 
	 * @param key
	 * @param supplier
	 * @return
	 */
	public static <T> T computeIfAbsent(String key, Supplier<T> supplier) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && secondCache != null) {
			data = secondCache.get(key);
		}
		if (data != null) {
			return data;
		}

		synchronized (ThreadTool.buildLock(LOCK_PREFIX, key)) {
			data = FIRST_LEVE_LCACHE.get(key);
			if (data == null && secondCache != null) {
				data = secondCache.get(key);
			}
			if (data != null) {
				return data;
			}

			data = supplier.get();
			if (secondCache != null) {
				secondCache.put(key, data);
				FIRST_LEVE_LCACHE.put(key, data, 60);
			} else {
				FIRST_LEVE_LCACHE.put(key, data);
			}
		}
		return data;
	}

	/**
	 * 提供数据，并缓存一定的时间
	 * 
	 * @param key
	 * @param timeOutSecond
	 * @param supplier
	 * @return
	 */
	public static <T> T computeIfAbsent(String key, int timeOutSecond, Supplier<T> supplier) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && secondCache != null) {
			data = secondCache.get(key);
		}
		if (data != null) {
			return data;
		}
		synchronized (ThreadTool.buildLock(LOCK_PREFIX, key)) {
			data = FIRST_LEVE_LCACHE.get(key);
			if (data == null && secondCache != null) {
				data = secondCache.get(key);
			}
			if (data != null) {
				return data;
			}
			data = supplier.get();
			if (secondCache != null) {
				secondCache.put(key, data, timeOutSecond);
				FIRST_LEVE_LCACHE.put(key, data, cmpFirstCacheTimeOutSecond(timeOutSecond));
			} else {
				FIRST_LEVE_LCACHE.put(key, data, timeOutSecond);
			}

		}
		return data;
	}

	public static <T> T removeAndGet(String key) {
		T data = null;
		if (secondCache != null) {
			data = secondCache.removeAndGet(key);
		}
		T data2 = FIRST_LEVE_LCACHE.removeAndGet(key);
		if (data == null) {
			data = data2;
		}
		return data;
	}

	public static void remove(String key) {
		if (secondCache != null) {
			secondCache.remove(key);
		}
		FIRST_LEVE_LCACHE.remove(key);
	}

	public static <T> T get(String key) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && secondCache != null) {
			data = secondCache.get(key);
		}
		return data;
	}

	public static void expire(String key, int timeOutSecond) {
		FIRST_LEVE_LCACHE.expire(key, timeOutSecond);
		if (secondCache != null) {
			secondCache.expire(key, timeOutSecond);
		}
	}

	private static int cmpFirstCacheTimeOutSecond(int timeOutSecond) {
		if (timeOutSecond > 60) {
			return 60;
		} else if (timeOutSecond > 30) {
			return timeOutSecond / 2;
		}
		return timeOutSecond;
	}
}
