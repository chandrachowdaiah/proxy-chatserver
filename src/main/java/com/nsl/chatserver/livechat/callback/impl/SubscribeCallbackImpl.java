package com.nsl.chatserver.livechat.callback.impl;

import com.nsl.chatserver.common.listener.SubscribeCallback;

public class SubscribeCallbackImpl implements SubscribeCallback{

	@Override
	public void onSubscribe(Boolean isSubscribed, String subId) {
		if (isSubscribed) {
			System.out.println("Subscribed :" + subId);
		}
	}
}
