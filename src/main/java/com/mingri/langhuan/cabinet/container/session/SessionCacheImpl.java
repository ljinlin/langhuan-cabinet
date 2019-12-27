package com.mingri.langhuan.cabinet.container.session;

import com.mingri.langhuan.cabinet.container.cache.MultilevelCache;

public class SessionCacheImpl implements SessionCache<String, MySession> {

	
	@Override
	public void put(String sessionId, MySession session, int timeOutSecond) {
		MultilevelCache.put(sessionId, session, timeOutSecond);
	}

	@Override
	public MySession get(String key) {
		return MultilevelCache.get(key);
	}

	@Override
	public void remove(String key) {
		 MultilevelCache.remove(key);
	}

	@Override
	public void expire(String key, int timeOutSecond) {
		MultilevelCache.expire(key, timeOutSecond);
	}

	@Override
	public boolean hasSession(String sessionId) {
		return MultilevelCache.hashKey(sessionId);
	}


}
