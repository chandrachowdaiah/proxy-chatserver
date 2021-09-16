package com.nsl.chatserver.livechat.internal.rpc;

import org.json.JSONException;
import org.json.JSONObject;

import com.nsl.chatserver.common.data.rpc.RPC;

/**
 * Created by sachin on 9/6/17.
 */

public class LiveChatSendMsgRPC extends RPC {

    private static final String SEND_MESSAGE = "sendMessageLivechat";

    /**
     * TESTED
     *
     * @param token Token is register guest visitorToken (visitorToken), not visitorToken for authentication
     */
    public static String sendMessage(long integer, String msgId, String roomId, String message, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("_id", msgId);
            object.put("rid", roomId);
            object.put("msg", message);
            object.put("token", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getRemoteMethodObject(integer, SEND_MESSAGE, object).toString();
    }
}
