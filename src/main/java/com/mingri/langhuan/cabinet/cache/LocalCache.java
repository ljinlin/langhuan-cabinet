package com.mingri.langhuan.cabinet.cache;

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
import com.mingri.langhuan.cabinet.tool.ThreadTool;

/**
 * 本地缓存
 * 
 * @author ljl
 *
 */
public class LocalCache implements ICache {

	private LocalCache() {
	}

	private static class InstanceHolder{
		static final LocalCache INSTANCE=new LocalCache();
	}
	public static LocalCache instance() {
		return InstanceHolder.INSTANCE;
	}
	
	/**
	 * 存储最大数据数量，超出该数据量时，删除旧的数据
	 */
	private static final int MAXCOUNT = 2000;

	/**
	 * 缓存容器
	 */
	private static final Map<String, Object> CONTAINER =new ConcurrentLinkedHashMap.Builder<String, Object>()
			.maximumWeightedCapacity(MAXCOUNT). weigher(Weighers.singleton()).initialCapacity(100).build();

	/**
	 * 缓存KEY 存储时间记录
	 */
	private static final Map<String, Long> KEY_TIME_CONTAINER = new ConcurrentLinkedHashMap.Builder<String, Long>()
			.maximumWeightedCapacity(MAXCOUNT). weigher(Weighers.singleton()).initialCapacity(100).build();
	
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
	public void rightPush(String key, Object value, int timeOutSecond) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.get(key);
		if (linkList == null) {
			linkList = new ConcurrentLinkedDeque<>();
			CONTAINER.put(key, linkList);
		}
		KEY_TIME_CONTAINER.put(key,
				Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(yyyyMMddHHmmss_FMT)));
		linkList.offer(value);
		LocalCache.streamContainer();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void rightPush(String key, Object value) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.get(key);
		if (linkList == null) {
			linkList = new ConcurrentLinkedDeque<>();
			CONTAINER.putIfAbsent(key, linkList);
		}
		linkList.offer(value);
		LocalCache.streamContainer();
	}


	@SuppressWarnings("unchecked")
	@Override
	public void leftPush(String key, Object value) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.get(key);
		if (linkList == null) {
			linkList = new ConcurrentLinkedDeque<>();
			CONTAINER.putIfAbsent(key, linkList);
		}
		linkList.offerFirst(value);
		LocalCache.streamContainer();
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T rightPop(String key) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.get(key);
		if (linkList == null) {
			return null;
		}
		return (T) linkList.pollLast();
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T leftPop(String key) {
		ConcurrentLinkedDeque<Object> linkList = (ConcurrentLinkedDeque<Object>) CONTAINER.get(key);
		if (linkList == null) {
			return null;
		}
		return (T) linkList.pollFirst();
	}

	

	@SuppressWarnings("unchecked")
	@Override
	public <T>T computeIfAbsent(String key, int timeOutSecond, Function<String, Object> mappingFunction) {
		T t=(T) CONTAINER.computeIfAbsent(key, mappingFunction);
		KEY_TIME_CONTAINER.putIfAbsent(key,
				Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(yyyyMMddHHmmss_FMT)));
		LocalCache.streamContainer();
		return t;
	}

	@Override
	public void put(String key, Object value) {
		CONTAINER.put(key, value);
	}


	@Override
	public void put(String key, Object value, int timeOutSecond) {
		CONTAINER.put(key, value);
		KEY_TIME_CONTAINER.put(key,
				Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(yyyyMMddHHmmss_FMT)));
		LocalCache.streamContainer();
	}


	@Override
	public boolean putIfAbsent(String key, Object value) {
		Object result = null;
		result = CONTAINER.putIfAbsent(key, value);
		LocalCache.streamContainer();
		return result == null;
	}


	@Override
	public boolean putIfAbsent(String key, Object value, int timeOutSecond) {
		Object result = null;
		result = CONTAINER.putIfAbsent(key, value);
		KEY_TIME_CONTAINER.putIfAbsent(key,
				Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(yyyyMMddHHmmss_FMT)));
		LocalCache.streamContainer();
		return result == null;
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String key) {
		T value = (T) CONTAINER.get(key);
		if (value == null) {
			return null;
		}
		if (LocalCache.isTimeOut(key)) {
			CONTAINER.remove(key);
			KEY_TIME_CONTAINER.remove(key);
			return null;
		} else {
			return value;
		}
	}


	@Override
	public void expire(String key, int timeOutSecond) {
		KEY_TIME_CONTAINER.put(key,
				Long.parseLong(LocalDateTime.now().plusSeconds(timeOutSecond).format(yyyyMMddHHmmss_FMT)));
	}


	@Override
	public boolean hasKey(String key) {
		return CONTAINER.containsKey(key);
	}


	@Override
	public void remove(String key) {
		CONTAINER.remove(key);
	}


	@SuppressWarnings("unchecked")
	@Override
	public <T> T removeAndGet(String key) {
		return (T) CONTAINER.remove(key);
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
				Iterator<Entry<String, Object>> instanceIt = CONTAINER.entrySet().iterator();
				while (instanceIt.hasNext()) {
					String key = instanceIt.next().getKey();
					if (LocalCache.isTimeOut(key, now)) {
						instanceIt.remove();
						KEY_TIME_CONTAINER.remove(key);
					}
				}

//				/*
//				 * 2、 超容量,从首位开始清除
//				 */
//				if (CONTAINER.size() > MAXCOUNT) {
//					List<String> tempList = new ArrayList<>();
//					for (Entry<String, Object> en : CONTAINER.entrySet()) {
//						tempList.add(en.getKey());
//						if (CONTAINER.size() - tempList.size() <= MAXCOUNT) {
//							tempList.forEach(e -> {
//								CONTAINER.remove(e);
//								KEY_TIME_CONTAINER.remove(e);
//							});
//							break;
//						}
//					}
//				}

				ThreadTool.sleep(TRIM_INTERIM);
				now = Long.valueOf(LocalDateTime.now().format(yyyyMMddHHmmss_FMT));
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
}
