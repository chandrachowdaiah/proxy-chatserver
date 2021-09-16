package com.nsl.chatserver.livechat.callback.impl;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.nsl.chatserver.livechat.callback.InitialDataCallback;
import com.nsl.chatserver.response.LiveChatConfigResponse;

public class InitialDataCallbackImpl implements InitialDataCallback{
	private String token;
	private SimpMessagingTemplate messagingTemplate;	
	
	public InitialDataCallbackImpl() {}

	public InitialDataCallbackImpl(String token, SimpMessagingTemplate messagingTemplate) {
		this.token=token;
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onError(Exception error) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onInitialData(LiveChatConfigResponse data) {
		messagingTemplate.convertAndSend("/topic/configurationResponse", data);
	}

}
