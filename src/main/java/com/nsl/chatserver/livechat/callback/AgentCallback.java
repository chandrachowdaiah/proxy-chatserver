package com.nsl.chatserver.livechat.callback;

import com.nsl.chatserver.livechat.model.AgentObject;
import com.nsl.chatserver.common.listener.Callback;
import com.nsl.chatserver.common.listener.Listener;

/**
 * Created by sachin on 9/6/17.
 */

/**
 * Getting agent info. from the server
 */
public class AgentCallback {
    public interface AgentDataCallback extends Callback {
        void onAgentData(AgentObject agentObject);
    }

    public interface AgentConnectListener extends Listener {
        void onAgentConnect(AgentObject agentObject);
    }
}
