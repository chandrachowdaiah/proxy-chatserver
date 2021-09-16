package com.nsl.chatserver.common.listener;

public interface TypingListener extends Listener {
    void onTyping(String roomId, String user, Boolean istyping);
}
