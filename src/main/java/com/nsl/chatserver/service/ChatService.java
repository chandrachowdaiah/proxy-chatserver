package com.nsl.chatserver.service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.nsl.chatserver.livechat.request.MessageRequest;
import com.nsl.chatserver.livechat.request.RegisterUserRequest;
import com.nsl.chatserver.livechat.request.TranscriptRequest;
import com.nsl.chatserver.request.ConnectionRequest;
import com.nsl.chatserver.request.LiveChatConfigurationRequest;

@Service
public class ChatService {
	
	private static final Map<String,ChatSession> sessions = new HashMap<>();
	private static final Map<String, String> wsSessionToToken = new HashMap<>();
	
	@Value("${chatserver.url}")
	private String url;
	@Value("${chatserver.http.url}")
	private String httpUrl;
	@Autowired
	SimpMessagingTemplate simpleMessagingTemplate;
	private RestTemplate restTemplate = new RestTemplate();
    protected static final Gson GSON = new GsonBuilder()
            // parse dates from long:
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();

	public void connect(ConnectionRequest request, MessageHeaders messageHeaders) {
		String token = request.getToken();
		String roomId = request.getRoomId();
		ChatSession session = sessions.get(token);
		if (session == null) {
			session = new ChatSession(url,token, roomId, simpleMessagingTemplate);
			sessions.put(token, session);
			wsSessionToToken.put(messageHeaders.get("simpSessionId").toString(), token);
		}
		session.connect();
	}

	public void disconnect(ConnectionRequest request) throws Exception {
		try {
		String requestStr = GSON.toJson(request);
		requestStr = requestStr.replace("roomId", "rid");
		Object object = GSON.fromJson(requestStr, Object.class);
		String endpoint="/api/v1/livechat/room.close";
		String restEndPoint = httpUrl + endpoint;
		ResponseEntity<String> response
		 			= restTemplate.postForEntity(restEndPoint, object, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			//Log Successfull close
		}
		else {
			//Log error
		}
		//We should not close the session, rather close the livechat room.
		//The Session to backend server is closed on closing the websocket connection & handled as port of close api
		} catch (Exception e) {
			//TODO
		}
	}
	
	public void close(String websocketSessionId) throws Exception {
		String token = wsSessionToToken.get(websocketSessionId);
		ChatSession session = sessions.get(token);
		if (session != null) {
			session.close();
			sessions.remove(token);
			wsSessionToToken.remove(websocketSessionId);
		}
	}

	public void getConfiguration(LiveChatConfigurationRequest request) throws Exception{
		String token = request.getToken();
		ChatSession session = sessions.get(token);
		if (session == null) {
			throw new RuntimeException("unable to find the session with token Id");
		}
		session.getConfiguration();
	}

	public void registerUser(RegisterUserRequest request) {
		String token = request.getToken();
		ChatSession session = sessions.get(token);
		if (session == null) {
			throw new RuntimeException("unable to find the session with token Id");
		}
		session.registerUser(request);
	}

	public void sendMessage(MessageRequest request) {
		String token = request.getToken();
		ChatSession session = sessions.get(token);
		if (session == null) {
			throw new RuntimeException("unable to find the session with token Id");
		}
		session.sendMessage(request);
	}

	public void sendConversationTranscript(TranscriptRequest request) {
		try {
		String requestStr = GSON.toJson(request);
		requestStr = requestStr.replace("roomId", "rid");
		Object object = GSON.fromJson(requestStr, Object.class);
		String endpoint = "/api/v1/livechat/transcript";
		String restEndPoint = httpUrl + endpoint;
		ResponseEntity<String> response
		 			= restTemplate.postForEntity(restEndPoint, object, String.class);

		if (response.getStatusCode() == HttpStatus.OK) {
			//Log Successfull close
		}
		else {
			//Log error
		}
		} catch (Exception e) {
			//TODO
		}
	}
}