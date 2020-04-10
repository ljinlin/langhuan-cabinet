package com.mingri.langhuan.cabinet.container.session;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.Assert;

import com.mingri.langhuan.cabinet.tool.MyComparable;
import com.mingri.langhuan.cabinet.tool.ThreadTool;

public class MySessionContext {

	/**
	 * session超时时间（单位：秒）
	 */
	private int sessionTimeout = 60 * 60;

	/**
	 * session缓存，默认{@code com.mingri.langhuan.cabinet.container.session.SessionCacheImpl}
	 */
	private SessionCache<String, MySession> sessionCache = new SessionCacheImpl();

	/**
	 * 待更新的session
	 */
	private final Map<String, MySession> WAIT_ACTIVE_SESSION_MAP = new ConcurrentHashMap<>();

	/**
	 * 更新session最后访问时间线程,防止频繁的更新session 创建线程消耗性能
	 */
	private final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

	/**
	 * 活动session任务开关
	 */
	private final AtomicInteger ACTIVE_SESSION_FLAG = new AtomicInteger(0);

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public void setSessionTimeout(int sessionTimeout) {
		this.sessionTimeout = sessionTimeout;
	}

	public SessionCache<String, MySession> getSessionCache() {
		return sessionCache;
	}

	public void setSessionCache(SessionCache<String, MySession> sessionCache) {
		this.sessionCache = sessionCache;
	}

	/**
	 * 获取session
	 * 
	 * @param sessionId session对象id
	 * @return session对象
	 */
	public MySession getSession(String sessionId) {
		return sessionCache.get(buildSessionCacheKey(sessionId));
	}

	/**
	 * 登录session
	 * 
	 * @param sessionUser session用户
	 * @return session对象
	 */
	public MySession loginSession(Object sessionUser) {
		Assert.isTrue(sessionUser != null, "sessionUser 不能为null");
		MySession mySession = createSession();
		String sessionKey = buildSessionCacheKey(mySession.getId());
		mySession.login(sessionUser);
		sessionCache.put(sessionKey, mySession, sessionTimeout);
		return mySession;
	}

	/**
	 * 退出session
	 * 
	 * @param sessionId  session对象id
	 */
	public void logoutSession(String sessionId) {
		sessionCache.remove(buildSessionCacheKey(sessionId));
	}

	protected String buildSessionCacheKey(String sessionId) {
		return ThreadTool.buildLock("session:", sessionId);
	}

	private MySession createSession() {
		MySession mySession = new MySession();
		return mySession;
	}

	/**
	 * 活动session
	 * 每个请求都要活动session,更新session的最后访问时间，为了提高最大并发量，保护session缓存，防止每一次请求都去
	 * 更新session缓存，此处采用策略5秒内同一个session最多更新一次缓存，但是依然保证每次请求都更新session的最后访问时间
	 * 活动session策略：
	 * 如果同一个session对象正在等待更新缓存，则该线程更新执行session.active后返回，
	 * 如果该session在超时时间减5秒内,则装入待更新缓存的容器，并且查看有没有更新session缓存执行器正在运行，没有则启动更新session缓存执行器
	 * 如果该session已经超时，此函数不判断是否超时，只要进来session都进行活动。
	 * session若超时，其实也是获取不到的，因为缓存已经进行存储时间限制，即使程序执行耗时误差，session设计允许最大超时15秒。
	 * 针对这种超时边缘的session，直接更新缓存，而不进入session缓存执行器更新
	 * 
	 * @param mySession session对象
	 */
	public void activeSession(MySession mySession) {
		String sessionCacheKey = buildSessionCacheKey(mySession.getId());
		MySession oldMySession = WAIT_ACTIVE_SESSION_MAP.get(sessionCacheKey);
		// session对象还在等待更新缓存，该线程只执行session签到，不需去更新缓存
		if (oldMySession != null) {
			oldMySession.active();
			return;
		}
		LocalDateTime now=LocalDateTime.now();
		//超时时间内访问过
		if (MyComparable.creat(mySession.getLastActiveTime().plusSeconds(sessionTimeout-5)).isGt(now)) {
			/*
			 * 延后同步缓存
			 */
			mySession.active();
			WAIT_ACTIVE_SESSION_MAP.put(sessionCacheKey, mySession);
			excutActiveSessionTask();

		} else {
			// 即将过期，立刻同步缓存
			synchronized (ThreadTool.buildLock(mySession.getId())) {
				// 双重判断，如果是刚刚15秒内才活跃，则不再更新缓存
				if (MyComparable.creat(mySession.getLastActiveTime().plusSeconds(15)).isGt(now)) {
					return;
				}
				mySession.active();
				sessionCache.put(sessionCacheKey, mySession, sessionTimeout);
			}
		}
	}

	/**
	 * 活动session任务，有任务时，保证最多只有一个线程执行
	 * 
	 */
	private void excutActiveSessionTask() {
		if (ACTIVE_SESSION_FLAG.incrementAndGet() > 1) {
			return;
		}
		THREAD_POOL.execute(() -> {
			do {
				ThreadTool.sleep(5000);
				Iterator<String> keyIt = WAIT_ACTIVE_SESSION_MAP.keySet().iterator();
				while (keyIt.hasNext()) {
					String key = keyIt.next();
					MySession mySession = WAIT_ACTIVE_SESSION_MAP.remove(key);
					sessionCache.put(buildSessionCacheKey(mySession.getId()), mySession, sessionTimeout);
				}
			} while (!WAIT_ACTIVE_SESSION_MAP.isEmpty());
			ACTIVE_SESSION_FLAG.set(0);
		});
	}

}
