package com.mingri.langhuan.cabinet.container.session;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.mingri.langhuan.cabinet.tool.StrTool;

/**
 * Sesssion对象，loginUser为空时，相当于未登录
 * 
 * @author ljl 2020年4月10日
 */
public class MySession implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9107882313903674965L;
	private String id;
	private Object loginUser;
	private MySeesionAttributes attributes;
	private LocalDateTime lastActiveTime;

	MySession() {
		this.lastActiveTime = LocalDateTime.now();
		this.attributes = new MySeesionAttributes();
		this.id = generatorId();
	}

	public String generatorId() {
		return StrTool.getUUId();
	}

	public Object getLoginUser() {
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
		return this.loginUser != null;
	}

	void login(Object loginUser) {
		this.loginUser = loginUser;
	}

	/**
	 * 活动
	 */
	synchronized void active() {
		this.lastActiveTime = LocalDateTime.now();
	}

	/**
	 * 更新session用户信息
	 * 
	 * @param loginUser 登录的用户
	 */
	public void updateLoginUser(Object loginUser) {
		this.loginUser = loginUser;
	}

}
