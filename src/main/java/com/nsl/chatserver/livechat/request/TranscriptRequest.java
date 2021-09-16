package com.nsl.chatserver.livechat.request;

public class TranscriptRequest {
	private String token;
	private String roomId;
	private String email;

	public TranscriptRequest() {}

	public TranscriptRequest(String email, String roomId, String token) {
		this.email = email;
		this.roomId = roomId;
		this.token = token;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
