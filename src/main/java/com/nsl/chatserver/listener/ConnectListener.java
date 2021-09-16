package com.nsl.chatserver.listener;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.nsl.chatserver.livechat.response.GenericAnswer;
import com.nsl.chatserver.response.ConnectionResponse;
import com.nsl.chatserver.service.ChatSession;

public class ConnectListener {
	private SimpMessagingTemplate messagingTemplate;	
	private ChatSession chatSession;
	private String sessionID;
	private long id;

	public ConnectListener(ChatSession chatSession, SimpMessagingTemplate messagingTemplate) {
		this.chatSession = chatSession;
		this.messagingTemplate = messagingTemplate;
	}

	public void onConnected(GenericAnswer msgObject) {
		//Start livechat connect
		this.sessionID = msgObject.session;
		id = chatSession.getLiveChatClient().getID();
		String requestStr="{\"msg\":\"method\",\"method\":\"livechat:setUpConnection\",\"params\":[{\"token\":\""+chatSession.getLiveChatClient().getToken()+"\"}],\"id\":\""+id+"\"}";
		chatSession.getLiveChatClient().sendRequest(requestStr);
	}
	
	public void onResult(GenericAnswer msgObject) {
		try {
			if (id == Long.parseLong(msgObject.id)) {
				//Connection succeeded, send back the response to client
				ConnectionResponse connectionResponse = new ConnectionResponse("connected", sessionID);
				messagingTemplate.convertAndSend("/topic/connectResponse", connectionResponse);
			}
		}
		catch (Exception e) {};
	}

	public String getSessionId() {
		return this.sessionID;
	}
}
