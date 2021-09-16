package com.nsl.chatserver.response;

import java.util.Date;

public class LastChat {
	private String _id;
	private Date ts;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Date getTs() {
		return ts;
	}
	public void setTs(Date ts) {
		this.ts = ts;
	}
}
