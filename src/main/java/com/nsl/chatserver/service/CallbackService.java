package com.nsl.chatserver.service;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.nsl.chatserver.common.listener.Callback;
import com.nsl.chatserver.common.utils.Pair;
import com.nsl.chatserver.livechat.callback.AgentCallback;
import com.nsl.chatserver.livechat.callback.InitialDataCallback;
import com.nsl.chatserver.livechat.callback.LoadHistoryCallback;
import com.nsl.chatserver.livechat.callback.MessageListener;
import com.nsl.chatserver.livechat.callback.RegisterCallback;
import com.nsl.chatserver.livechat.model.LiveChatMessageClientMessage;
import com.nsl.chatserver.livechat.response.GenericAnswer;
import com.nsl.chatserver.response.LiveChatConfigResponse;
import com.nsl.chatserver.response.RegisterGuest;

public class CallbackService {
    private ConcurrentHashMap<Long, Pair<? extends Callback, CallbackType>> callbacks;
    protected static final Gson GSON = new GsonBuilder()
            // parse dates from long:
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();
    
    public CallbackService() {
        callbacks = new ConcurrentHashMap<>();
    }

    public void createCallback(long i, Callback callback, CallbackType type) {
        callbacks.put(i, Pair.create(callback, type));
    }

    public void processCallback(long id, GenericAnswer message) {
    	 if (callbacks.containsKey(id)) {
             Pair<? extends Callback, CallbackType> callbackPair = callbacks.remove(id);
             Callback callback = callbackPair.first;
             CallbackType type = callbackPair.second;
             Object result = message.result;

             if (result == null) {
                 Map<String, ?> error = message.error;
                 if (error == null) {
                     String str = "Missing \"result\" or \"error\" values";
                     callback.onError(new Exception(str));
                 } else {
                     callback.onError(new Exception(error.toString()));
                 }
                 return;
             }

             try {
                 switch (type) {
                     case GET_INITIAL_DATA:
                         InitialDataCallback dataCallback = (InitialDataCallback) callback;
                         LiveChatConfigResponse msgObject = GSON.fromJson(message.result.toString(), LiveChatConfigResponse.class);
                         dataCallback.onInitialData(msgObject);
                         break;
                     case REGISTER:
                         RegisterCallback registerCallback = (RegisterCallback) callback;
                         RegisterGuest guestObject = GSON.fromJson(message.result.toString(), RegisterGuest.class);
                         registerCallback.onRegister(guestObject);
                         break;
                     case GET_CHAT_HISTORY:
                         LoadHistoryCallback historyCallback = (LoadHistoryCallback) callback;
                         JSONArray array = ((JSONObject) result).optJSONArray("messages");
//                         List<LiveChatMessage> list = getMessageListAdapter().fromJson(array.toString());
//                         int unreadNotLoaded = object.optJSONObject("result").optInt("unreadNotLoaded");
//                         historyCallback.onLoadHistory(list, unreadNotLoaded);
                         break;
                     case GET_AGENT_DATA:
                         AgentCallback.AgentDataCallback agentDataCallback = (AgentCallback.AgentDataCallback) callback;
//                         AgentObject agentObject = new AgentObject((JSONObject) result);
//                         agentDataCallback.onAgentData(agentObject);
                         break;
                     case SEND_MESSAGE:
                         MessageListener.MessageAckCallback messageAckCallback = (MessageListener.MessageAckCallback) callback;
                         LiveChatMessageClientMessage liveChatMessage = GSON.fromJson(message.result.toString(), LiveChatMessageClientMessage.class);
                         messageAckCallback.onMessageAck(liveChatMessage);
                         break;
                     case SEND_OFFLINE_MESSAGE:
                         MessageListener.OfflineMessageCallback messageCallback = (MessageListener.OfflineMessageCallback) callback;
                         messageCallback.onOfflineMesssageSuccess((Boolean) result);
                         break;
                 }
             } catch (Exception e) {
                 callback.onError(e);
                 e.printStackTrace();
             }
         }
    }
    
    public void notifyDisconnection(String message) {
        Exception error = new Exception(message);
        for (Map.Entry<Long, Pair<? extends Callback, CallbackType>> entry : callbacks.entrySet()) {
            entry.getValue().first.onError(error);
        }
        cleanup();
    }
    
    public void cleanup() {
        callbacks.clear();
    }
    
    public enum CallbackType {
        GET_INITIAL_DATA,
        REGISTER,
        LOGIN,
        GET_CHAT_HISTORY,
        GET_AGENT_DATA,
        SEND_MESSAGE,
        SEND_OFFLINE_MESSAGE
    }
}
