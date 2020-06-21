package com.mingri.langhuan.cabinet.container.cache;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import com.googlecode.concurrentlinkedhashmap.Weighers;
import com.mingri.langhuan.cabinet.tool.CacheTool;
import com.mingri.langhuan.cabinet.tool.ThreadTool;

/**
 * JVM缓存，默认最多存储2000个数据，冲过2000个，lru策略淘汰
 * 
 * @author ljl
 *
 */
public class LocalCache implements ICache {

	private LocalCache() {
	}

	private static class InstanceHolder {
		static final LocalCache INSTANCE = new LocalCache();
	}

	public static LocalCache instance() {
		return InstanceHolder.INSTANCE;
	}

	/**
	 * 存储最大数据数量，超出该数据量时，删除旧的数据
	 */
	private static final long MAXCOUNT = 10000;

	/**
	 * 缓存容器
	 */
	private static final Map<String, Object> CONTAINER = new ConcurrentLinkedHashMap.Builder<String, Object>()
			.maximumWeightedCapacity(MAXCOUNT).weigher(Weighers.singleton()).initialCapacity(100).build();

	/**
	 * 缓存KEY 存储时间记录
	 */
	private static final Map<String, Long> KEY_TIME_CONTAINER = new ConcurrentLinkedHashMap.Builder<String, Long>()
			.maximumWeightedCapacity(MAXCOUNT).weigher(Weighers.singleton()).initialCapacity(100).build();

	/**
	 * 时间格式化对象
	 */
	public static final DateTimeFormatter yyyyMMddHHmmss_FMT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	/**
	 * 清理缓存线程,防止频繁的缓存清理 创建线程消耗性能
	 */
	private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	/**
	 * 清理缓存锁，保证只有一个线程清理
	 */
	private static final AtomicInteger TREAM_CACHE_LOCK = new AtomicInteger(0);

	/**
	 * 缓存清理 轮询一圈等待时长
	 */
	private static final int TRIM_INTERIM = 2000;

	@SuppressWarnings("unchecked")
	@Override
	public void rightPush(String key, Object value) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) getOfContainer(key);
		if (linkList == null) {
			linkList = new ConcurrentLinkedDeque<>();
			linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.putIfAbsent(key, linkList);
		}
		linkList.offer(value);
		LocalCache.streamContainer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void leftPush(String key, Object value) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) getOfContainer(key);
		if (linkList == null) {
			linkList = new ConcurrentLinkedDeque<>();
			linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.putIfAbsent(key, linkList);
		}
		linkList.offerFirst(value);
		LocalCache.streamContainer();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T rightPop(String key) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) getOfContainer(key);
		if (linkList == null) {
			return null;
		}
		return (T) linkList.pollLast();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T leftPop(String key) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) getOfContainer(key);
		if (linkList == null) {
			return null;
		}
		return (T) linkList.pollFirst();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T computeIfAbsent(String key, int timeOutSecond, Function<String, T> mappingFunction) {
		Object result = CONTAINER.computeIfAbsent(key, (k) -> {
			Object value = CacheTool.getSafeCacheVal(mappingFunction.apply(k));
			if (timeOutSecond > 0) {
				KEY_TIME_CONTAINER.put(key, cmpTimeOutSecond(timeOutSecond));
			}
			return value;
		});
		LocalCache.streamContainer();
		return (T) CacheTool.getSrcVal(result);
	}

	@Override
	public void put(String key, Object value) {
		put(key, value, 0);
	}

	@Override
	public void put(String key, Object value, int timeOutSecond) {
		putToContainer(key, value);
		if (timeOutSecond > 0) {

			KEY_TIME_CONTAINER.put(key, cmpTimeOutSecond(timeOutSecond));
		}
		LocalCache.streamContainer();
	}

	@Override
	public <T> T putIfAbsent(String key, Object value) {
		return putIfAbsent(key, value, 0);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T putIfAbsent(String key, Object value, int timeOutSecond) {
		Object result = putIfAbsentToContainer(key, value);
		if (timeOutSecond > 0 && result == null) {
			KEY_TIME_CONTAINER.put(key, cmpTimeOutSecond(timeOutSecond));
		}
		LocalCache.streamContainer();
		return (T) result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		T value = (T) getOfContainer(key);
		if (value == null) {
			return null;
		}
		if (LocalCache.isTimeOut(key)) {
			LocalCache.streamContainer();
			return null;
		} else {
			return value;
		}
	}

	@Override
	public void expire(String key, int timeOutSecond) {
		if (CONTAINER.containsKey(key)) {
			KEY_TIME_CONTAINER.put(key, cmpTimeOutSecond(timeOutSecond));
		}
	}

	@Override
	public boolean hasKey(String key) {
		return hasKeyOfContainer(key);
	}

	@Override
	public void remove(String key) {
		removeOfContainer(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T removeAndGet(String key) {
		return (T) removeOfContainer(key);
	}

	@Override
	public long size() {
		return CONTAINER.size();
	}

	/**
	 * 整理缓存：<br>
	 * 整理的缓存的线程只能一个，节约资源开销<br>
	 */
	private static void streamContainer() {

		if (TREAM_CACHE_LOCK.incrementAndGet() > 1) {
			return;
		}
		THREAD_POOL.execute(() -> {
			long now = Long.parseLong(LocalDateTime.now().format(yyyyMMddHHmmss_FMT));
			do {
				/*
				 * 1、超时缓存清除
				 */
				clearTimeoutData(CONTAINER.entrySet().iterator(), now);
				ThreadTool.sleep(TRIM_INTERIM);
				now = cmpTimeOutSecond(0);
			} while (!CONTAINER.isEmpty());
			TREAM_CACHE_LOCK.set(0);
		});
	}

	/**
	 * 判断key对比当前时间是否超时
	 * 
	 * @param key
	 * @return
	 */
	private static boolean isTimeOut(String key) {
		long now = Long.parseLong(LocalDateTime.now().format(yyyyMMddHHmmss_FMT));
		return LocalCache.isTimeOut(key, now);
	}

	/**
	 * 
	 * 判断key对比now是否超时
	 * 
	 * @param key
	 * @param now
	 * @return
	 */
	private static boolean isTimeOut(String key, long now) {
		Long saveTime = KEY_TIME_CONTAINER.get(key);
		return saveTime == null || saveTime < now;
	}

	private Object getOfContainer(String key) {
		Object value = CONTAINER.get(key);
		return CacheTool.getSrcVal(value);
	}

	private Object putToContainer(String key, Object value) {
		CONTAINER.put(key, CacheTool.getSafeCacheVal(value));
		return value;
	}

	private Object putIfAbsentToContainer(String key, Object value) {
		return CacheTool.getSrcVal(CONTAINER.putIfAbsent(key, CacheTool.getSafeCacheVal(value)));
	}

	private boolean hasKeyOfContainer(String key) {
		return CONTAINER.containsKey(key);
	}

	private Object removeOfContainer(String key) {
		Object value = CONTAINER.remove(key);
		KEY_TIME_CONTAINER.remove(key);
		return CacheTool.getSrcVal(value);
	}

	private static void clearTimeoutData(Iterator<Entry<String, Object>> instanceIt, long now) {
		while (instanceIt.hasNext()) {
			String key = instanceIt.next().getKey();
			if (LocalCache.isTimeOut(key, now)) {
				instanceIt.remove();
				KEY_TIME_CONTAINER.remove(key);
			}
		}
	}

	private static long cmpTimeOutSecond(int timeOutSecond) {
		LocalDateTime dt = LocalDateTime.now();
		if (timeOutSecond != 0) {
			dt = dt.plusSeconds(timeOutSecond);
		}
		return Long.parseLong(dt.format(yyyyMMddHHmmss_FMT));
	}

}
