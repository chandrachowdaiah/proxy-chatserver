package com.nsl.chatserver.livechat.callback;

import com.nsl.chatserver.response.LiveChatConfigResponse;
import com.nsl.chatserver.common.listener.Callback;

public interface InitialDataCallback extends Callback {
    void onInitialData(LiveChatConfigResponse msgObject);
}