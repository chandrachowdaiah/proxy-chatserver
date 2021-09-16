package com.nsl.chatserver.response;

import java.util.Date;
import java.util.List;

public class Visitor {
	private String _id;
	private String username;
	private Date ts;
	private String userAgent;
	private String host;
	private Date _updatedAt;
	private String name;
	private String token;
	private List<VisitorEmail> visitorEmails;
	private LastChat lastChat;
	private List<Phone> phone;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public Date get_updatedAt() {
		return _updatedAt;
	}
	public void set_updatedAt(Date _updatedAt) {
		this._updatedAt = _updatedAt;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public List<VisitorEmail> getVisitorEmails() {
		return visitorEmails;
	}
	public void setVisitorEmails(List<VisitorEmail> visitorEmails) {
		this.visitorEmails = visitorEmails;
	}
	public LastChat getLastChat() {
		return lastChat;
	}
	public void setLastChat(LastChat lastChat) {
		this.lastChat = lastChat;
	}
	public List<Phone> getPhone() {
		return phone;
	}
	public void setPhone(List<Phone> phone) {
		this.phone = phone;
	}
}
