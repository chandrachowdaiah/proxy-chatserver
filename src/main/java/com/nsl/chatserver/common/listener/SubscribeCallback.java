package com.nsl.chatserver.common.listener;

public interface SubscribeCallback extends Listener {
    void onSubscribe(Boolean isSubscribed, String subId);
}
