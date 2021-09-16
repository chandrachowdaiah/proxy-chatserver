package com.nsl.chatserver.response;

public class RegisterGuest {
	private Visitor visitor;
	private boolean success;
	
	public Visitor getVisitor() {
		return visitor;
	}
	public void setVisitor(Visitor visitor) {
		this.visitor = visitor;
	}
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
}