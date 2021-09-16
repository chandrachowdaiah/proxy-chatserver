package com.nsl.chatserver.livechat.internal.rpc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nsl.chatserver.common.data.rpc.SubRPC;

public class LiveChatSubRPC extends SubRPC {

    private static final String STREAM_ROOM = "stream-room-messages";
    private static final String STREAM_LIVECHAT_ROOM = "stream-livechat-room";
    private static final String NOTIFY_ROOM = "stream-notify-room";

    public static String streamRoomMessages(String uniqueid, String room_id, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("useCollection", false);
            JSONObject object1 = new JSONObject();
            object1.put("token", token);
            object1.put("visitorToken", token);
            JSONArray array = new JSONArray();
            array.put(object1);
            object.put("args", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteSubscriptionObject(uniqueid, STREAM_ROOM, room_id, object).toString();
    }

    public static String streamLivechatRoom(String uniqueid, String room_id, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("useCollection", false);
            JSONObject object1 = new JSONObject();
            object1.put("token", token);
            object1.put("visitorToken", token);
            JSONArray array = new JSONArray();
            array.put(object1);
            object.put("args", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteSubscriptionObject(uniqueid, STREAM_LIVECHAT_ROOM, room_id, object).toString();
    }

    public static String subscribeTyping(String uniqueid, String room_id, String token) {
        JSONObject object = new JSONObject();
        try {
            object.put("useCollection", false);
            JSONObject object1 = new JSONObject();
            object1.put("token", token);
            object1.put("visitorToken", token);
            JSONArray array = new JSONArray();
            array.put(object1);
            object.put("args", array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return getRemoteSubscriptionObject(uniqueid, NOTIFY_ROOM, room_id + "/typing", object).toString();
    }

}
