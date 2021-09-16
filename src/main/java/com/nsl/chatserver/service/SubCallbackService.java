package com.nsl.chatserver.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.nsl.chatserver.common.listener.SubscribeCallback;
import com.nsl.chatserver.common.listener.TypingListener;
import com.nsl.chatserver.livechat.callback.AgentCallback;
import com.nsl.chatserver.livechat.callback.MessageListener;
import com.nsl.chatserver.livechat.response.GenericAnswer;

public class SubCallbackService {
    private MessageListener.SubscriptionListener subscriptionListener;
    private AgentCallback.AgentConnectListener agentConnectListener;
    private TypingListener typingListener;
    protected static final Gson GSON = new GsonBuilder()
            // parse dates from long:
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();
    public static String MESSAGE_TYPE_COMMAND = "command";
    public static String MESSAGE_TYPE_CLOSE = "livechat-close";
    public static String MESSAGE_TYPE_CONNECTED = "connected";
    public static String MESSAGE_TYPE_PROMPT_TRANSRIPT = "promptTranscript";
    public static String MESSAGE_TYPE_LIVECHAT_TRANSFER_HISTORY = "livechat_transfer_history";
    public static String MESSAGE_TYPE_LIVECHAT_TRANSCRIPT_HISTORY = "livechat_transcript_history";
    public static String MESSAGE_TYPE_USER_LEFT = "ul";
    private ConcurrentHashMap<String, SubscribeCallback> subcallbacks;
    
    public SubCallbackService() {
        subcallbacks = new ConcurrentHashMap<>();
    }
    
    private static SubType parse(String s) {
        if (s.equals("stream-room-messages")) {
            return SubType.STREAM_ROOM_MESSAGES;
        } else if (s.equals("stream-livechat-room")) {
            return SubType.STREAM_LIVECHAT_ROOM;
        } else {
            return SubType.NOTIFY_ROOM;
        }
    }
    
    public void subscribeRoom(MessageListener.SubscriptionListener subscription) {
        this.subscriptionListener = subscription;
    }

    public void subscribeLiveChatRoom(AgentCallback.AgentConnectListener agentConnectListener) {
        this.agentConnectListener = agentConnectListener;
    }

    public void subscribeTyping(TypingListener callback) {
        typingListener = callback;
    }

    public void createSubCallbacks(String id, SubscribeCallback callback) {
        if (callback != null) {
            subcallbacks.put(id, callback);
        }
    }
    
    public void processCallback(GenericAnswer object) throws JSONException {
    	System.out.println(object);
    	String s = object.collection;
        switch (parse(s)) {
            case STREAM_ROOM_MESSAGES:
            	try {
                if (subscriptionListener != null) {
                	Map properties = (Map)((ArrayList)object.fields.get("args")).get(0);
//					LiveChatMessage liveChatMessage = GSON.fromJson(object.fields.toString(), LiveChatMessage.class);
					String roomId = (String) object.fields.get("eventName");
					String type = (String) properties.get("t");
					if (type !=null && type.equals("command")) {
						String command = (String) properties.get("msg");
						if (MESSAGE_TYPE_CONNECTED.equals(command)) {
							subscriptionListener.onAgentConnected(roomId, properties, "user_connected");
						}else if (MESSAGE_TYPE_PROMPT_TRANSRIPT.equals(command)) {
							subscriptionListener.onEvent(roomId, properties, "prompt_transcript");
						}
					} else if (MESSAGE_TYPE_CLOSE.equals(type)) {
						subscriptionListener.onAgentDisconnect(roomId, properties, "livechat_close");
					} else if (MESSAGE_TYPE_LIVECHAT_TRANSCRIPT_HISTORY.equals(type)) {
						subscriptionListener.onEvent(roomId, properties, type);
					} else if (MESSAGE_TYPE_LIVECHAT_TRANSFER_HISTORY.equals(type)) {
						subscriptionListener.onEvent(roomId, properties, type);
					} else if (MESSAGE_TYPE_USER_LEFT.equals(type)) {
						subscriptionListener.onEvent(roomId, properties, "user_left");
					} else {
					    subscriptionListener.onMessage(roomId, properties);
					}
                }
            	}
                catch(Exception e) {
                	System.out.println(e.getMessage());
                }
                break;
            case STREAM_LIVECHAT_ROOM:
//                if (agentConnectListener != null) {
//                    AgentObject agent = new AgentObject(array.optJSONObject(0).optJSONObject("data"));
//                    agentConnectListener.onAgentConnect(agent);
//                }
            	//TODO
                break;
            case NOTIFY_ROOM:
                if (typingListener != null) {
//                    String roomId = (String) object.fields.get("eventName");
//                    String user = array.optString(0);
//                    Boolean typing = array.optBoolean(1);
//                    typingListener.onTyping(roomId, user, typing);
                }
                //TODO
                break;
        }
    }

    public void processSubSuccess(JSONObject subObj) {
        if (subObj.optJSONArray("subs") != null) {
            String id = subObj.optJSONArray("subs").optString(0);
            if (subcallbacks.containsKey(id)) {
                SubscribeCallback subscribeCallback = subcallbacks.remove(id);
                subscribeCallback.onSubscribe(true, id);
            }
        }
    }
    
    public enum SubType {
        STREAM_ROOM_MESSAGES,
        STREAM_LIVECHAT_ROOM,
        NOTIFY_ROOM
    }
}