package com.nsl.chatserver.livechat.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveChatMessageClientMessage {
	public String _id;
	public String rid;
	public String msg;
	public String token;
	public String alias;
	public Ts ts;
	public U u;
	public UpdatedAt _updatedAt;
	public List<Object> urls;
	public List<Object> mentions;
	public List<Object> channels;
	public List<Md> md;
	public boolean newRoom;
	public boolean showConnecting;

	public static class Ts {
		@JsonProperty("$date")
		public long date;
	}

	public static class U {
		public String _id;
		public String username;
		public String name;
	}

	public static class UpdatedAt {
		@JsonProperty("$date")
		public long date;
	}

	public static class Value {
		public String type;
		public String value;
	}

	public static class Md {
		public String type;
		public List<Value> value;
	}

}
