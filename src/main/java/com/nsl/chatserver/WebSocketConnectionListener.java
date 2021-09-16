package com.nsl.chatserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.nsl.chatserver.service.ChatService;

import java.util.Map;
import java.util.Optional;

@Component
public class WebSocketConnectionListener implements ApplicationListener<ApplicationEvent> {

    @Autowired
	ChatService chatService;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if(event instanceof SessionDisconnectEvent) {
            handleSessionDisconnected((SessionDisconnectEvent) event);
        }
    }

    private void handleSessionDisconnected(SessionDisconnectEvent event) {
    	try {
			chatService.close(event.getSessionId());
		} catch (Exception e) {
			//TODO
		}
    }
}