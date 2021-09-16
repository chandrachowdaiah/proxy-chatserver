package com.nsl.chatserver.livechat.internal.rpc;

import com.nsl.chatserver.common.data.rpc.RPC;
import com.nsl.chatserver.common.utils.Utils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sachin on 8/6/17.
 */

public class LiveChatBasicRPC extends RPC {

    public static String visitorToken = Utils.generateRandomHexToken(16);

    private static final String GET_INITIAL_DATA = "livechat:getInitialData";
    private static final String REGISTER_GUEST = "livechat:registerGuest";
    private static final String LOGIN = "login";
    private static final String GET_AGENT_DATA = "livechat:getAgentData";
    private static final String CLOSE_CONVERSATION = "livechat:closeByVisitor";
    private static final String SEND_OFFLINE_MESSAGE = "livechat:sendOfflineMessage";

    /**
     * Tested
     */

    public static String getInitialData(long integer) {
        return getRemoteMethodObject(integer, GET_INITIAL_DATA, visitorToken).toString();
    }

    /**
     * Tested
     */

    public static String registerGuest(long integer, String name, String email, String dept, String token) {

        JSONObject object = new JSONObject();
        try {
            object.put("token", token);
            object.put("name", name);
            object.put("email", email);
            object.put("department", dept);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteMethodObject(integer, REGISTER_GUEST, object).toString();
    }

    /**
     * Tested
     */
    public static String login(int integer, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("resume", token);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteMethodObject(integer, LOGIN, object).toString();

    }

    /**
     * Tested
     */
    public static String getAgentData(int integer, String roomId) {
        return getRemoteMethodObject(integer, GET_AGENT_DATA, roomId).toString();
    }

    /**
     * Tested
     * @param userId 
     */

    public static String closeConversation(long integer, String roomId, String token) {
    	JSONObject object = new JSONObject();
        try {
            object.put("rid", roomId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteMethodObject(integer, CLOSE_CONVERSATION, object).toString();
    }

    public static String sendOfflineMessage(int integer, String name, String email, String message) {
        JSONObject object = new JSONObject();
        try {
            object.put("name", name);
            object.put("email", email);
            object.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteMethodObject(integer, SEND_OFFLINE_MESSAGE, object).toString();
    }

}
