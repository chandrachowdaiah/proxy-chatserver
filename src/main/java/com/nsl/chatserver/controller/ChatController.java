package com.nsl.chatserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.nsl.chatserver.livechat.request.MessageRequest;
import com.nsl.chatserver.livechat.request.RegisterUserRequest;
import com.nsl.chatserver.livechat.request.TranscriptRequest;
import com.nsl.chatserver.request.ConnectionRequest;
import com.nsl.chatserver.request.LiveChatConfigurationRequest;
import com.nsl.chatserver.service.ChatService;

@Controller
public class ChatController {
	@Autowired
	ChatService chatService;	

	@MessageMapping("/connect")
	@SendTo("/topic/connectResponse")
	public void registerClient(ConnectionRequest message, MessageHeaders messageHeaders) throws Exception {
		chatService.connect(message, messageHeaders);
	}

	@MessageMapping("/configuration")
	@SendTo("/topic/configurationResponse")
	public void getConfiguration(LiveChatConfigurationRequest message) throws Exception {
		chatService.getConfiguration(message);
	}
	
	@MessageMapping("/register")
	@SendTo("/topic/registerResponse")
	public void getConfiguration(RegisterUserRequest message) throws Exception {
		chatService.registerUser(message);
	}
	
	@MessageMapping("/disconnect")
	@SendTo("/topic/connectResponse")
	public void disconnect(ConnectionRequest connectionRequest) throws Exception {
		chatService.disconnect(connectionRequest);
	}
	
	@MessageMapping("/message")
	@SendTo("/topic/messageResponse")
	public void sendMessage(MessageRequest messageRequest) throws Exception {
		chatService.sendMessage(messageRequest);
	}
	
	@MessageMapping("/transcript")
	public void sendConversationTranscript(TranscriptRequest transcriptRequest) throws Exception {
		chatService.sendConversationTranscript(transcriptRequest);
	}
}
