package com.nsl.chatserver.livechat.callback.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.nsl.chatserver.livechat.callback.RegisterCallback;
import com.nsl.chatserver.response.RegisterGuest;
import com.nsl.chatserver.response.RegisterGuestResponse;

public class RegisterCallbackImpl implements RegisterCallback{
	private String token;
	private SimpMessagingTemplate messagingTemplate;
	
	public RegisterCallbackImpl() {
		// TODO Auto-generated constructor stub
	}
	public RegisterCallbackImpl(String token, SimpMessagingTemplate simpleMessagingTemplate) {
		this.token=token;
		this.messagingTemplate = simpleMessagingTemplate;
	}

	@Override
	public void onError(Exception error) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRegister(RegisterGuest data) {
		RegisterGuestResponse registerResponse = new  RegisterGuestResponse();
		if (data.getVisitor().getVisitorEmails() != null && data.getVisitor().getVisitorEmails().get(0) != null) {
			registerResponse.setEmail(data.getVisitor().getVisitorEmails().get(0).getAddress());
		}

		if (data.getVisitor().getLastChat() != null) {
			registerResponse.setLastChatId(data.getVisitor().getLastChat().get_id());
			registerResponse.setLastChatTimestamp(data.getVisitor().getLastChat().getTs().getTime());
		}

		registerResponse.setName(data.getVisitor().getName());
		
		if (data.getVisitor().getPhone() != null && data.getVisitor().getPhone().get(0) != null) {
			registerResponse.setPhone(Long.parseLong(data.getVisitor().getPhone().get(0).getPhoneNumber()));
		}
		if (data.getVisitor().getTs() != null) {
			registerResponse.setTimeStamp(data.getVisitor().getTs().getTime());
		}
		registerResponse.setToken(data.getVisitor().getToken());
		registerResponse.setUsername(data.getVisitor().getUsername());
		messagingTemplate.convertAndSend("/topic/registerResponse", registerResponse);
	}
}