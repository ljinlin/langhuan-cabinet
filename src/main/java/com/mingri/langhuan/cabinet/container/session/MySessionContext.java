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
import com.mingri.langhuan.cabinet.tool.StrTool;
import com.mingri.langhuan.cabinet.tool.ThreadTool;

public class MySessionContext {

	/**
	 * session超时时间（单位：秒）
	 */
	private int sessionTimeout = 60 * 60;

	private int sessionMaxMarginTime = sessionTimeout + 15;

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
		sessionMaxMarginTime = this.sessionTimeout + 15;
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
	 * @param sessionSubjectId session用户id 
	 * @return session对象
	 */
	public MySession getSession(String sessionSubjectId) {
		return sessionCache.get(buildSessionCacheKey(sessionSubjectId));
	}

	/**
	 * 登录session
	 * 
	 * @param sessionSubject session用户
	 * @return session对象 
	 */
	public MySession loginSession(SessionSubject sessionSubject) {
		Assert.isTrue((sessionSubject != null && StrTool.isNotEmpty(sessionSubject.getId())),
				"sessionSubject或者其id属性 不能为null");
		String sessionKey = buildSessionCacheKey(sessionSubject.getId());
		Assert.state(!sessionCache.hasSession(sessionKey), "该用户已经是登录状态，不能重复登录");
		MySession mySession = createSession(sessionSubject);
		mySession.login(sessionSubject);
		sessionCache.put(sessionKey, mySession, sessionTimeout);
		return mySession;
	}

	/**
	 * 活动session
	 * 
	 * @param mySession session对象
	 */
	public void activeSession(MySession mySession) {
		String sessionCacheKey = buildSessionCacheKey(mySession.getId());
		MySession oldMySession = WAIT_ACTIVE_SESSION_MAP.get(sessionCacheKey);
		if (oldMySession != null) {
			oldMySession.active();
			return;
		}

		if (MyComparable.creat(mySession.getLastActiveTime().plusSeconds(sessionMaxMarginTime))
				.isLtEq(LocalDateTime.now())) {
			/*
			 * 即将过期，立刻同步缓存
			 */
			synchronized (ThreadTool.buildLock(mySession.getId())) {
				if (MyComparable.creat(mySession.getLastActiveTime().plusSeconds(15)).isGt(LocalDateTime.now())) {
					return;
				}
				mySession.active();
				sessionCache.put(sessionCacheKey, mySession, sessionTimeout);
			}
		} else {
			/*
			 * 延后同步缓存
			 */
			mySession.active();
			WAIT_ACTIVE_SESSION_MAP.put(sessionCacheKey, mySession);
			excutActiveSessionTask();
		}
	}

	/**
	 * 退出session
	 * 
	 * @param sessionSubject session用户对象
	 */
	public void logoutSession(SessionSubject sessionSubject) {
		sessionCache.remove(buildSessionCacheKey(sessionSubject.getId()));
	}

	protected String buildSessionCacheKey(String sessionId) {
		return ThreadTool.buildLock("session:", sessionId);
	}

	private MySession createSession(SessionSubject sessionSubject) {
		MySession mySession = new MySession();
		return mySession;
	}

	/**
	 * 活动session任务
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
