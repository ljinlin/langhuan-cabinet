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

	private static ICache FIRST_LEVE_LCACHE = null;
	private static ICache SECOND_CACHE;

	private static final String LOCK_PREFIX = "MUILCACHE_LOCK:";

	public static synchronized void init() {
		if (MultilevelCache.FIRST_LEVE_LCACHE == null) {
			MultilevelCache.FIRST_LEVE_LCACHE = LocalCache.instance();
		}
	}

	public static synchronized void init(ICache firstCache, ICache secondCache) {
		if (MultilevelCache.FIRST_LEVE_LCACHE == null && MultilevelCache.SECOND_CACHE == null) {
			MultilevelCache.FIRST_LEVE_LCACHE = firstCache;
			MultilevelCache.SECOND_CACHE = secondCache;
			LOGGER.info("开启二级缓存，SECOND_CACHE：{}", secondCache);
		}
	}

	public static synchronized void initAndSetFirstCache(ICache firstCache) {
		if (MultilevelCache.SECOND_CACHE == null) {
			MultilevelCache.FIRST_LEVE_LCACHE = firstCache;
		}
	}

	public static synchronized void initAndSetSecondCache(ICache secondCache) {
		if (MultilevelCache.SECOND_CACHE == null) {
			MultilevelCache.FIRST_LEVE_LCACHE = LocalCache.instance();
			MultilevelCache.SECOND_CACHE = secondCache;
			LOGGER.info("开启二级缓存，SECOND_CACHE：{}", SECOND_CACHE);
		}
	}

	public static void put(String key, Object value, int timeOutSecond) {
		if (SECOND_CACHE != null) {
			SECOND_CACHE.put(key, value, timeOutSecond);
			FIRST_LEVE_LCACHE.put(key, value, cmpFirstCacheTimeOutSecond(timeOutSecond));
		} else {
			FIRST_LEVE_LCACHE.put(key, value, timeOutSecond);
		}
	}

	/**
	 * 提供数据，并缓存
	 * 
	 * @param          <T> 返回的对象类型
	 * @param key      缓存键
	 * @param supplier 缓存值提供者
	 * @return 返回缓存的对象
	 */
	public static <T> T computeIfAbsent(String key, Supplier<T> supplier) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && SECOND_CACHE != null) {
			data = SECOND_CACHE.get(key);
		}
		if (data != null) {
			return data;
		}

		synchronized (ThreadTool.buildLock(LOCK_PREFIX, key)) {
			data = FIRST_LEVE_LCACHE.get(key);
			if (data == null && SECOND_CACHE != null) {
				data = SECOND_CACHE.get(key);
			}
			if (data != null) {
				return data;
			}

			data = supplier.get();
			if (SECOND_CACHE != null) {
				SECOND_CACHE.put(key, data);
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
	 * @param               <T> 返回的对象类型
	 * @param key           缓存键
	 * @param timeOutSecond 缓存超时时间（秒）
	 * @param supplier      缓存数据计算者
	 * @return 返回缓存的对象
	 */
	public static <T> T computeIfAbsent(String key, int timeOutSecond, Supplier<T> supplier) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && SECOND_CACHE != null) {
			data = SECOND_CACHE.get(key);
		}
		if (data != null) {
			return data;
		}
		synchronized (ThreadTool.buildLock(LOCK_PREFIX, key)) {
			data = FIRST_LEVE_LCACHE.get(key);
			if (data == null && SECOND_CACHE != null) {
				data = SECOND_CACHE.get(key);
			}
			if (data != null) {
				return data;
			}
			data = supplier.get();
			if (SECOND_CACHE != null) {
				SECOND_CACHE.put(key, data, timeOutSecond);
				FIRST_LEVE_LCACHE.put(key, data, cmpFirstCacheTimeOutSecond(timeOutSecond));
			} else {
				FIRST_LEVE_LCACHE.put(key, data, timeOutSecond);
			}

		}
		return data;
	}

	public static <T> T removeAndGet(String key) {
		T data = null;
		if (SECOND_CACHE != null) {
			data = SECOND_CACHE.removeAndGet(key);
		}
		T data2 = FIRST_LEVE_LCACHE.removeAndGet(key);
		if (data == null) {
			data = data2;
		}
		return data;
	}

	public static void remove(String key) {
		if (SECOND_CACHE != null) {
			SECOND_CACHE.remove(key);
		}
		FIRST_LEVE_LCACHE.remove(key);
	}

	public static <T> T get(String key) {
		T data = FIRST_LEVE_LCACHE.get(key);
		if (data == null && SECOND_CACHE != null) {
			data = SECOND_CACHE.get(key);
		}
		return data;
	}

	public static void expire(String key, int timeOutSecond) {
		FIRST_LEVE_LCACHE.expire(key, timeOutSecond);
		if (SECOND_CACHE != null) {
			SECOND_CACHE.expire(key, timeOutSecond);
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
