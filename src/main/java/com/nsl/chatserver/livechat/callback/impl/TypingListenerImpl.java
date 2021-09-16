package com.nsl.chatserver.livechat.callback.impl;

import com.nsl.chatserver.common.listener.TypingListener;

public class TypingListenerImpl implements TypingListener{
	@Override
	public void onTyping(String roomId, String user, Boolean istyping) {
		System.out.println(user);
	}
}