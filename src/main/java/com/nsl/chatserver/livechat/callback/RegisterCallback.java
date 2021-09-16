package com.nsl.chatserver.livechat.callback;

import com.nsl.chatserver.response.RegisterGuest;
import com.nsl.chatserver.common.listener.Callback;

public interface RegisterCallback extends Callback {
    void onRegister(RegisterGuest guestObject);
}