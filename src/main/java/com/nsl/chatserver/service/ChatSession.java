package com.nsl.chatserver.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.client.RestTemplate;

import com.nsl.chatserver.common.listener.SubscribeCallback;
import com.nsl.chatserver.listener.ConnectListener;
import com.nsl.chatserver.livechat.callback.MessageListener;
import com.nsl.chatserver.livechat.callback.impl.AgentConnectListenerImpl;
import com.nsl.chatserver.livechat.callback.impl.MessageAckCallbackImpl;
import com.nsl.chatserver.livechat.callback.impl.SubScriptionListenerImpl;
import com.nsl.chatserver.livechat.callback.impl.SubscribeCallbackImpl;
import com.nsl.chatserver.livechat.callback.impl.TypingListenerImpl;
import com.nsl.chatserver.livechat.client.LiveChatClient;
import com.nsl.chatserver.livechat.request.MessageRequest;
import com.nsl.chatserver.livechat.request.RegisterUserRequest;
import com.nsl.chatserver.livechat.request.TranscriptRequest;
import com.nsl.chatserver.livechat.response.LiveChatMessage;
import com.nsl.chatserver.livechat.response.LiveChatMessage.Ans;
import com.nsl.chatserver.request.ConnectionRequest;

public class ChatSession {
	private String token;
	private String roomId;
	private String userId;
	private String department;
	private LiveChatClient liveChatClient;
	private SimpMessagingTemplate simpleMessagingTemplate;
	private boolean subscribed=false;
	
	public ChatSession (String url, String token, String roomId, SimpMessagingTemplate simpleMessagingTemplate) {
		this.token = token;
		this.roomId = roomId;
		liveChatClient = new LiveChatClient(url, token, roomId);
		liveChatClient.registerConnectListener(new ConnectListener(this, simpleMessagingTemplate));
		this.simpleMessagingTemplate = simpleMessagingTemplate;
	}

	public String getToken() {
		return token;
	}

	public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public void connect() {
		liveChatClient.connect();
	}

	public void subscribe() {
		SubscribeCallback subscribeCallback = new SubscribeCallbackImpl();
		liveChatClient.subscribeRoom(subscribeCallback, new SubScriptionListenerImpl(simpleMessagingTemplate));
//		liveChatClient.subscribeLiveChatRoom(subscribeCallback, new AgentConnectListenerImpl());
//		liveChatClient.subscribeTyping(subscribeCallback, new TypingListenerImpl());
	}

	public void getConfiguration() {
		liveChatClient.getInitialData(simpleMessagingTemplate);
	}

	public void registerUser(RegisterUserRequest userRequest) {
		liveChatClient.registerGuest(userRequest.getUserName(), userRequest.getEmail(), userRequest.getDepartment(),
				userRequest.getToken(), simpleMessagingTemplate);
		this.userId = userRequest.getUserName();
//		sendMessage(new MessageRequest(roomId, "", token));
//		if (subscribed == false) {
//			try {
//				Thread.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			subscribe();
//			subscribed = true;
//		}
	}

	public void sendMessage(MessageRequest request) {
		MessageListener.MessageAckCallback messageAckListener = new MessageAckCallbackImpl(this, simpleMessagingTemplate);
		liveChatClient.sendMessage(request.getRoomID(), request.getMessage(), request.getToken(), messageAckListener);	
		if (subscribed == false) {
			subscribe();
			subscribed = true;
		}
	}

	public void disconnect(ConnectionRequest request) {
		liveChatClient.closeConversation(request.getRoomId(), request.getRoomId());
	}
	
	public void close() throws Exception {
		liveChatClient.close();
	}

	public LiveChatClient getLiveChatClient() {
		return liveChatClient;
	}
}