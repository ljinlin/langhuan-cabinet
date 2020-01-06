package com.mingri.langhuan.cabinet.container.session;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.util.Assert;

import com.mingri.langhuan.cabinet.tool.StrTool;

public class MySession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107882313903674965L;
	private final String id;
	private SessionSubject loginUser;
	private boolean isLogin;
	private MySeesionAttributes attributes;
	private LocalDateTime lastActiveTime;

	MySession() {
		this.id = StrTool.getUUId();
		this.lastActiveTime = LocalDateTime.now();
		this.attributes = new MySeesionAttributes();
	}

	public SessionSubject getLoginUser() {
		return loginUser;
	}

	public MySeesionAttributes getAttributes() {
		return attributes;
	}

	public String getId() {
		return id;
	}

	public void setAttribute(String attrName, Object val) {
		attributes.put(attrName, val);
	}

	public Object getAttribute(String attrName) {
		return attributes.get(attrName);
	}

	public LocalDateTime getLastActiveTime() {
		return lastActiveTime;
	}

	public boolean isLogin() {
		return isLogin;
	}

	void login(SessionSubject loginUser) {
		if (!this.isLogin) {
			this.isLogin = true;
			this.loginUser = loginUser;
		}
	}

	void active() {
		this.lastActiveTime = LocalDateTime.now();
	}

	/**
	 * 更新session用户信息
	 * 
	 * @param loginUser 登录的用户
	 */
	public synchronized void updateLoginUser(SessionSubject loginUser) {
		Assert.isTrue((this.loginUser != null && this.loginUser.getId().equals(loginUser.getId())), "该用户未登录，或者不是同一个用户");
		this.loginUser = loginUser;
	}

}
