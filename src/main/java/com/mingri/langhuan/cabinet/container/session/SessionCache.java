package com.mingri.langhuan.cabinet.container.session;

public interface SessionCache<K, V> {

	void put(K sessionId, V session, int timeOutSecond);

	V get(String sessionId);

	void expire(String sessionId, int timeOutSecond);

	void remove(String sessionId);
	
	boolean hasSession(K sessionId);
}
