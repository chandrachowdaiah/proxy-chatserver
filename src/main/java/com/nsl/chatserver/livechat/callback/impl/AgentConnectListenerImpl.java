package com.nsl.chatserver.livechat.callback.impl;

import com.nsl.chatserver.livechat.callback.AgentCallback.AgentConnectListener;
import com.nsl.chatserver.livechat.model.AgentObject;

public class AgentConnectListenerImpl implements AgentConnectListener{
	@Override
	public void onAgentConnect(AgentObject agentObject) {
		System.out.println(agentObject);	
	}
}
