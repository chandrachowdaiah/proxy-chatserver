package com.nsl.chatserver.livechat.callback.impl;

import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.nsl.chatserver.livechat.callback.MessageListener.SubscriptionListener;
import com.nsl.chatserver.livechat.response.LiveChatEvent;
import com.nsl.chatserver.livechat.response.LiveChatMessage;
import com.nsl.chatserver.livechat.response.LiveChatMessage.Ans;

public class SubScriptionListenerImpl implements SubscriptionListener{
	private SimpMessagingTemplate messagingTemplate;
	
	public SubScriptionListenerImpl() {}
	
	public SubScriptionListenerImpl(SimpMessagingTemplate messagingTemplate) {
		this.messagingTemplate = messagingTemplate;
	}

	@Override
	public void onMessage(String roomId, Map object) {
		String msg = (String) object.get("msg");
		String response_type = "text";//(String) ((Map)((ArrayList)((Map)((ArrayList)object.get("md")).get(0)).get("value")).get(0)).get("type");
		double timestamp = (double) ((Map) object.get("ts")).get("$date");
		String user =  (String) ((Map) object.get("u")).get("name");
		LiveChatMessage liveChatMessage = new LiveChatMessage();
		Ans ans =  new LiveChatMessage.Ans();
		ans.text = msg;
		liveChatMessage.ans = ans;
		liveChatMessage.response_type=response_type;
		liveChatMessage.timestamp = timestamp;
		liveChatMessage.user = user;
		liveChatMessage.roomId = roomId;
		messagingTemplate.convertAndSend("/topic/messageResponse", liveChatMessage);
	}

	@Override
	public void onAgentDisconnect(String roomId, Map object, String event) {
		double timestamp = (double) ((Map) object.get("ts")).get("$date");
		String user =  (String) ((Map) object.get("u")).get("username");
		LiveChatEvent livechatEvent = new LiveChatEvent();
		livechatEvent.setEventType(event);
		livechatEvent.setRoomId(roomId);
		livechatEvent.setTimestamp(timestamp);
		livechatEvent.setUser(user);
		messagingTemplate.convertAndSend("/topic/messageEvent", livechatEvent);
	}

	@Override
	public void onAgentConnected(String roomId, Map object, String event) {
		double timestamp = (double) ((Map) object.get("ts")).get("$date");
		String user =  (String) ((Map) object.get("u")).get("username");
		LiveChatEvent livechatEvent = new LiveChatEvent();
		livechatEvent.setEventType(event);
		livechatEvent.setRoomId(roomId);
		livechatEvent.setTimestamp(timestamp);
		livechatEvent.setUser(user);	
		messagingTemplate.convertAndSend("/topic/messageEvent", livechatEvent);
	}

	@Override
	public void onEvent(String roomId, Map object, String eventType) {
		double timestamp = (double) ((Map) object.get("ts")).get("$date");
		String user =  (String) ((Map) object.get("u")).get("username");
		LiveChatEvent livechatEvent = new LiveChatEvent();
		livechatEvent.setEventType(eventType);
		livechatEvent.setRoomId(roomId);
		livechatEvent.setTimestamp(timestamp);
		livechatEvent.setUser(user);	
		messagingTemplate.convertAndSend("/topic/messageEvent", livechatEvent);
	}
}