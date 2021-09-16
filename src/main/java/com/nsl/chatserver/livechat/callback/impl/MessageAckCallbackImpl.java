package com.nsl.chatserver.livechat.callback.impl;

import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.nsl.chatserver.livechat.callback.MessageListener.MessageAckCallback;
import com.nsl.chatserver.livechat.model.LiveChatMessageClientMessage;
import com.nsl.chatserver.livechat.response.LiveChatMessage;
import com.nsl.chatserver.livechat.response.LiveChatMessage.Ans;
import com.nsl.chatserver.service.ChatSession;

public class MessageAckCallbackImpl  implements MessageAckCallback{
	
	private ChatSession chatSession;
//	private boolean firstMessage;
	private SimpMessagingTemplate simpleMessagingTemplate;

	public MessageAckCallbackImpl() {
		// TODO Auto-generated constructor stub
	}

	public MessageAckCallbackImpl(ChatSession chatSession, 
			SimpMessagingTemplate simpleMessagingTemplate) {
		this.chatSession=chatSession;
		this.simpleMessagingTemplate = simpleMessagingTemplate;
//		this.firstMessage = firstMessage;
	}

	@Override
	public void onError(Exception error) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void onMessageAck(LiveChatMessageClientMessage object) {
		//chatSession.subscribe();
//		if (firstMessage) {
//			firstMessage = false;
//			String msg = object.msg;
//			String response_type = "text";//(String) ((Map)((ArrayList)((Map)((ArrayList)object.get("md")).get(0)).get("value")).get(0)).get("type");
//			double timestamp = object.ts.date;
//			String user =  object.u.name;
//			LiveChatMessage liveChatMessage = new LiveChatMessage();
//			Ans ans =  new LiveChatMessage.Ans();
//			ans.text = msg;
//			liveChatMessage.ans = ans;
//			liveChatMessage.response_type=response_type;
//			liveChatMessage.timestamp = timestamp;
//			liveChatMessage.user = user;
//			liveChatMessage.roomId = object.rid;
//			simpleMessagingTemplate.convertAndSend("/topic/messageResponse", liveChatMessage);
//		}
	}
}