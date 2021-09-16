package com.nsl.chatserver.livechat.client;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.SendResult;
import javax.websocket.Session;

import org.json.JSONException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.nsl.chatserver.common.listener.SubscribeCallback;
import com.nsl.chatserver.common.listener.TypingListener;
import com.nsl.chatserver.common.utils.Utils;
import com.nsl.chatserver.listener.ConnectListener;
import com.nsl.chatserver.livechat.callback.AgentCallback;
import com.nsl.chatserver.livechat.callback.InitialDataCallback;
import com.nsl.chatserver.livechat.callback.MessageListener;
import com.nsl.chatserver.livechat.callback.MessageListener.MessageAckCallback;
import com.nsl.chatserver.livechat.callback.RegisterCallback;
import com.nsl.chatserver.livechat.callback.impl.InitialDataCallbackImpl;
import com.nsl.chatserver.livechat.callback.impl.RegisterCallbackImpl;
import com.nsl.chatserver.livechat.internal.rpc.LiveChatBasicRPC;
import com.nsl.chatserver.livechat.internal.rpc.LiveChatSendMsgRPC;
import com.nsl.chatserver.livechat.internal.rpc.LiveChatSubRPC;
import com.nsl.chatserver.livechat.response.GenericAnswer;
import com.nsl.chatserver.service.CallbackService;
import com.nsl.chatserver.service.SubCallbackService;

public class LiveChatClient implements AutoCloseable{
	private AtomicLong longValue;
    private static final Logger LOG = Logger.getLogger(LiveChatClient.class.getName());
    protected static final Gson GSON = new GsonBuilder()
            // parse dates from long:
            .registerTypeAdapter(Date.class, (JsonDeserializer<Date>) (json, typeOfT, context) -> new Date(json.getAsJsonPrimitive().getAsLong()))
            .registerTypeAdapter(Date.class, (JsonSerializer<Date>) (date, type, jsonSerializationContext) -> new JsonPrimitive(date.getTime()))
            .create();

    protected final Map<String, CompletableFutureWithMapper<?>> futureResults = new ConcurrentHashMap<>();
    protected final ConcurrentHashMap<String, ObservableSubjectWithMapper<?>> subscriptionResults = new ConcurrentHashMap<>();

    protected final CompletableFuture<Session> session = new CompletableFuture<>();
    protected final String url;
    protected final CompletableFuture<String> connectResult = new CompletableFuture<>();
    protected final AtomicBoolean connectionTerminated = new AtomicBoolean(false);
    protected final Executor executor;
    protected final String token;
    protected final String roomId;
    private CallbackService callBackService;
    private SubCallbackService subCallbackService;
    
    private ConnectListener connectListener;
    
    public LiveChatClient(String url, String token, String roomId) {
        this(url, token, roomId, ForkJoinPool.commonPool());
    }

    public LiveChatClient(String url, String token, String roomId, Executor executor) {
        this.url = url;
        this.executor = executor;
        this.token = token;
        this.roomId = roomId;
        longValue = new AtomicLong(1);
		this.callBackService = new CallbackService();
		this.subCallbackService = new SubCallbackService();
    }
  
    public CompletableFuture<String> connect() {
        if (!connectResult.isDone()) {
            try {
                LOG.info("connecting to " + url);
                WSClient clientEndpoint = new WSClient();
                clientEndpoint.setConnectListener(connectListener);
                session.complete(ContainerProvider.getWebSocketContainer().connectToServer(clientEndpoint, URI.create(url)));
                LOG.info("created session: " + session.join());
                session.join().getAsyncRemote().sendText("{\"msg\": \"connect\",\"version\": \"1\",\"support\": [\"1\"]}",
              sendResult -> LOG.fine("connect ack: " + sendResult.isOK()));
            } catch (Exception e) {
                session.completeExceptionally(e);
                throw new IllegalStateException(e);
            }
        }
        return connectResult;
    }
    
    public void subscribeRoom(SubscribeCallback subscribeCallback,
            MessageListener.SubscriptionListener listener) {

    	String uniqueID = Utils.shortUUID();
    	subCallbackService.createSubCallbacks(uniqueID, subscribeCallback);
    	subCallbackService.subscribeRoom(listener);
    	String requestStr = LiveChatSubRPC.streamRoomMessages(uniqueID, roomId, token);
    	sendRequest(requestStr);
    }

    public void subscribeLiveChatRoom(SubscribeCallback subscribeCallback,
                    AgentCallback.AgentConnectListener agentConnectListener) {

    	String uniqueID = Utils.shortUUID();
    	subCallbackService.createSubCallbacks(uniqueID, subscribeCallback);
    	subCallbackService.subscribeLiveChatRoom(agentConnectListener);
    	String requestStr = LiveChatSubRPC.streamLivechatRoom(uniqueID, roomId, token);
    	sendRequest(requestStr);
    }

    public void subscribeTyping(SubscribeCallback subscribeCallback,
              TypingListener listener) {

    	String uniqueID = Utils.shortUUID();
    	subCallbackService.createSubCallbacks(uniqueID, subscribeCallback);
    	subCallbackService.subscribeTyping(listener);
    	String requestStr = LiveChatSubRPC.subscribeTyping(uniqueID, roomId, token);
    	sendRequest(requestStr);
    }

    public void getInitialData(SimpMessagingTemplate simpleMessagingTemplate) {
    	long uniqueID = longValue.getAndIncrement();
		InitialDataCallback initialDataCallback = new InitialDataCallbackImpl(token, simpleMessagingTemplate);
		callBackService.createCallback(uniqueID, initialDataCallback, CallbackService.CallbackType.GET_INITIAL_DATA);
		String requestStr = LiveChatBasicRPC.getInitialData(uniqueID);
		sendRequest(requestStr);
    }

    public void registerGuest(String name, String email, String dept, String token, 
    		SimpMessagingTemplate simpleMessagingTemplate) {
        long uniqueID = longValue.getAndIncrement();
        RegisterCallback registerCallBack = new RegisterCallbackImpl(token, simpleMessagingTemplate);
        callBackService.createCallback(uniqueID, registerCallBack, CallbackService.CallbackType.REGISTER);
        String requestStr = LiveChatBasicRPC.registerGuest(uniqueID, name, email, dept, token);
        sendRequest(requestStr);
    }
    
    public void sendMessage(String roomID, String message, String token, MessageAckCallback messageAckListener) {
    	long uniqueID = longValue.getAndIncrement();
    	String msgId = Utils.shortUUID();
    	callBackService.createCallback(uniqueID, messageAckListener, CallbackService.CallbackType.SEND_MESSAGE);
    	String requestStr = LiveChatSendMsgRPC.sendMessage(uniqueID, msgId, roomID, message, token);
    	sendRequest(requestStr);
    }
 
    public void closeConversation(String roomId, String tokenId) {
        long uniqueID = longValue.getAndIncrement();
        String requestStr = LiveChatBasicRPC.closeConversation(uniqueID, roomId, tokenId);
        sendRequest(requestStr);
    }

	public CompletableFuture<String> sendRequest(String requetStr) {
		 return sendRequestInternal(requetStr,
	                failOnError(genericAnswer -> {
	                    JsonElement jsonElement = GSON.toJsonTree(genericAnswer.result);
	                    return GSON.fromJson(jsonElement, new TypeToken<List<String>>() {
	                    }.getType());
	                }));
	}
    
    private <T> CompletableFuture<T> sendRequestInternal(String requestStr, Function<GenericAnswer, T> answerMapper) {	
    	 CompletableFutureWithMapper<T> result = new CompletableFutureWithMapper<>(answerMapper);
    	 futureResults.put(UUID.randomUUID().toString(), result);
    	 session.join().getAsyncRemote().sendText(requestStr, sendResult -> handleSendResult(sendResult, result));
    	 return result;
    }

    protected <T> Function<GenericAnswer, T> failOnError(Function<GenericAnswer, T> mapper) {
        return result -> {
            if (result.error != null) {
                throw new RuntimeException("Send message failed: " + GSON.toJson(result.error));
            } else {
                return mapper.apply(result);
            }
        };
    }
    
    private static void handleSendResult(SendResult sendResult, CompletableFuture<?> result) {
        if (!sendResult.isOK()) {
            result.completeExceptionally(new SendFailedException(sendResult));
        }
    }
   
	@Override
	public void close() throws Exception {
		LOG.info("closing client..");
        try {
            session.join().close();
        } catch (IOException | CompletionException | CancellationException e) {
            LOG.log(Level.WARNING, "Could not close session: ", e);
        }
	}
	
	public void registerConnectListener(ConnectListener listener) {
		this.connectListener = listener;
	}
	
	public String getToken() {
		return this.token;
	}
	
	public String getRoomId() {
		return this.roomId;
	}
	
    @ClientEndpoint
    public class WSClient {

        private final List<String> messageParts = new ArrayList<>();
        private ConnectListener connectListener;

        public WSClient() {
            LOG.fine("created WSClient");
        }

        public void setConnectListener(ConnectListener connectListener) {
			this.connectListener = connectListener;			
		}

        @OnMessage
        public void onMessage(String message, boolean last) throws InterruptedException, JSONException {
            LOG.info("Received msg (last part: " + last + "): " + message);
            
            if (last) {
                String completeMessage;
                synchronized (messageParts) {
                    completeMessage = String.join("", messageParts) + message;
                    messageParts.clear();
                }

                GenericAnswer msgObject = GSON.fromJson(completeMessage, GenericAnswer.class);
                if (msgObject.server_id != null) {
                    LOG.fine("sending connect");
                } else if ("connected".equals(msgObject.msg)) {
                	connectListener.onConnected(msgObject);
                    connectResult.complete(msgObject.session);
                } else if ("result".equals(msgObject.msg)) {
                	connectListener.onResult(msgObject);
                	callBackService.processCallback(Long.parseLong(msgObject.id), msgObject);
               } else if ("changed".equals(msgObject.msg) && "stream-room-messages".equals(msgObject.collection)) {
            	   subCallbackService.processCallback(msgObject);
              } else if ("pong".equals(msgObject.msg)) {
                    session.join().getAsyncRemote().sendText("{\"msg\":\"ping\"}",
                            result -> LOG.fine("sent ping: " + result.isOK()));
                } else if ("ping".equals(msgObject.msg)) {
                    session.join().getAsyncRemote().sendText("{\"msg\":\"pong\"}",
                            result -> LOG.fine("sent pong: " + result.isOK()));
                }
            } else {
                synchronized (messageParts) {
                    messageParts.add(message);
                }
            }
        }

        @OnClose
        public void onClose(CloseReason closeReason) {
            LOG.warning("connection closed: " + closeReason);
            connectionTerminated.set(true);
        }
 
        @OnError
        public void onError(Session session, Throwable thr) {
        	//TODO
        }
    }

	public long getID() {
		return longValue.getAndIncrement();
	}
}