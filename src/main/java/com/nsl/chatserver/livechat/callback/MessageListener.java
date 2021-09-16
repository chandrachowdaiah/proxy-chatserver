package com.nsl.chatserver.livechat.callback;

import com.nsl.chatserver.livechat.model.LiveChatMessageClientMessage;
import com.nsl.chatserver.livechat.response.LiveChatMessage;

import java.util.Map;

import com.nsl.chatserver.common.listener.Callback;
import com.nsl.chatserver.common.listener.Listener;

/**
 * Created by sachin on 9/6/17.
 */

/**
 * Used to get message, which is returned after SubType to particular room
 */

public class MessageListener {
    public interface SubscriptionListener extends Listener {
        void onMessage(String roomId, Map properties);

        void onAgentDisconnect(String roomId, Map properties, String eventType);
        
        void onAgentConnected(String roomId, Map properties, String eventType);
        
        void onEvent(String roomId, Map properties, String eventType);
    }

    public interface MessageAckCallback extends Callback {
        void onMessageAck(LiveChatMessageClientMessage object);
    }

    public interface OfflineMessageCallback extends Callback {
        void onOfflineMesssageSuccess(Boolean success);
    }
}
