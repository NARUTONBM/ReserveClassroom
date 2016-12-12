package com.naruto.reserveclassroom.bean;
/*
 * Created by NARUTO on 2016/11/13.
 */

/**
 * 当前用户的信息
 */
public class CurrentUserInfoBean {

	private String username;
	private String mobilePhoneNumber;
	private String email;

	public String getUsername() {

		return username;
	}

	public void setUsername(String username) {

		this.username = username;
	}

	public String getMobilePhoneNumber() {

		return mobilePhoneNumber;
	}

	public void setMobilePhoneNumber(String mobilePhoneNumber) {

		this.mobilePhoneNumber = mobilePhoneNumber;
	}

	public String getEmail() {

		return email;
	}

	public void setEmail(String email) {

		this.email = email;
	}
}
