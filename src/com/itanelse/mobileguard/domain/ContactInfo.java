package com.itanelse.mobileguard.domain;

/**
 * 手机联系人的数据封装类
 * @author 毓添
 *
 */
public class ContactInfo {
	private String name;
	private String phone;
	private String email;
	private String qq;
	
	@Override
	public String toString() {
		return "ContactInfo [name=" + name + ", phone=" + phone + ", email="
				+ email + ", qq=" + qq + "]";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	
}
