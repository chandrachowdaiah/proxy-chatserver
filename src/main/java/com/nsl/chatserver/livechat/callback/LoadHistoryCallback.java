package com.nsl.chatserver.livechat.callback;

import com.nsl.chatserver.common.listener.Callback;
import com.nsl.chatserver.livechat.response.LiveChatMessage;

import java.util.List;

public interface LoadHistoryCallback extends Callback {
    void onLoadHistory(List<LiveChatMessage> list, int unreadNotLoaded);
}
