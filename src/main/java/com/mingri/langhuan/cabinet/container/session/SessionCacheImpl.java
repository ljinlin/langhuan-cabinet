package com.mingri.langhuan.cabinet.container.session;

import com.mingri.langhuan.cabinet.container.cache.ICache;

public class SessionCacheImpl implements SessionCache<String, MySession> {

	ICache cache;

	public SessionCacheImpl() {
	}
	
	
	public SessionCacheImpl(ICache cache) {
		this.cache=cache;
	}
	
	@Override
	public void put(String sessionId, MySession session, int timeOutSecond) {
		cache.put(sessionId, session, timeOutSecond);
	}

	@Override
	public MySession get(String key) {
		return cache.get(key);
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
	}

	@Override
	public void expire(String key, int timeOutSecond) {
		cache.expire(key, timeOutSecond);
	}

	@Override
	public boolean hasSession(String sessionId) {
		return cache.hasKey(sessionId);
	}


}
