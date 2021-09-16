package com.nsl.chatserver.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LiveChatConfigResponse {
	public boolean enabled;
	public String title;
	public String color;
	public boolean registrationForm;
	public Object room;
	public List<Object> triggers;
	public List<Department> departments;
	public boolean allowSwitchingDepartments;
	public boolean online;
	public String offlineColor;
	public String offlineMessage;
	public String offlineSuccessMessage;
	public String offlineUnavailableMessage;
	public boolean displayOfflineForm;
	public boolean videoCall;
	public boolean fileUpload;
	public String conversationFinishedMessage;
	public String conversationFinishedText;
	public boolean nameFieldRegistrationForm;
	public boolean emailFieldRegistrationForm;
	public String registrationFormMessage;
	public boolean showConnecting;
	public String offlineTitle;
	public String language;
	public boolean transcript;
	public String transcriptMessage;
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public boolean isRegistrationForm() {
		return registrationForm;
	}
	public void setRegistrationForm(boolean registrationForm) {
		this.registrationForm = registrationForm;
	}
	public Object getRoom() {
		return room;
	}
	public void setRoom(Object room) {
		this.room = room;
	}
	public List<Object> getTriggers() {
		return triggers;
	}
	public void setTriggers(List<Object> triggers) {
		this.triggers = triggers;
	}
	public List<Department> getDepartments() {
		return departments;
	}
	public void setDepartments(List<Department> departments) {
		this.departments = departments;
	}
	public boolean isAllowSwitchingDepartments() {
		return allowSwitchingDepartments;
	}
	public void setAllowSwitchingDepartments(boolean allowSwitchingDepartments) {
		this.allowSwitchingDepartments = allowSwitchingDepartments;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public String getOfflineColor() {
		return offlineColor;
	}
	public void setOfflineColor(String offlineColor) {
		this.offlineColor = offlineColor;
	}
	public String getOfflineMessage() {
		return offlineMessage;
	}
	public void setOfflineMessage(String offlineMessage) {
		this.offlineMessage = offlineMessage;
	}
	public String getOfflineSuccessMessage() {
		return offlineSuccessMessage;
	}
	public void setOfflineSuccessMessage(String offlineSuccessMessage) {
		this.offlineSuccessMessage = offlineSuccessMessage;
	}
	public String getOfflineUnavailableMessage() {
		return offlineUnavailableMessage;
	}
	public void setOfflineUnavailableMessage(String offlineUnavailableMessage) {
		this.offlineUnavailableMessage = offlineUnavailableMessage;
	}
	public boolean isDisplayOfflineForm() {
		return displayOfflineForm;
	}
	public void setDisplayOfflineForm(boolean displayOfflineForm) {
		this.displayOfflineForm = displayOfflineForm;
	}
	public boolean isVideoCall() {
		return videoCall;
	}
	public void setVideoCall(boolean videoCall) {
		this.videoCall = videoCall;
	}
	public boolean isFileUpload() {
		return fileUpload;
	}
	public void setFileUpload(boolean fileUpload) {
		this.fileUpload = fileUpload;
	}
	public String getConversationFinishedMessage() {
		return conversationFinishedMessage;
	}
	public void setConversationFinishedMessage(String conversationFinishedMessage) {
		this.conversationFinishedMessage = conversationFinishedMessage;
	}
	public String getConversationFinishedText() {
		return conversationFinishedText;
	}
	public void setConversationFinishedText(String conversationFinishedText) {
		this.conversationFinishedText = conversationFinishedText;
	}
	public boolean isNameFieldRegistrationForm() {
		return nameFieldRegistrationForm;
	}
	public void setNameFieldRegistrationForm(boolean nameFieldRegistrationForm) {
		this.nameFieldRegistrationForm = nameFieldRegistrationForm;
	}
	public boolean isEmailFieldRegistrationForm() {
		return emailFieldRegistrationForm;
	}
	public void setEmailFieldRegistrationForm(boolean emailFieldRegistrationForm) {
		this.emailFieldRegistrationForm = emailFieldRegistrationForm;
	}
	public String getRegistrationFormMessage() {
		return registrationFormMessage;
	}
	public void setRegistrationFormMessage(String registrationFormMessage) {
		this.registrationFormMessage = registrationFormMessage;
	}
	public boolean isShowConnecting() {
		return showConnecting;
	}
	public void setShowConnecting(boolean showConnecting) {
		this.showConnecting = showConnecting;
	}
	public String getOfflineTitle() {
		return offlineTitle;
	}
	public void setOfflineTitle(String offlineTitle) {
		this.offlineTitle = offlineTitle;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public boolean isTranscript() {
		return transcript;
	}
	public void setTranscript(boolean transcript) {
		this.transcript = transcript;
	}
	public String getTranscriptMessage() {
		return transcriptMessage;
	}
	public void setTranscriptMessage(String transcriptMessage) {
		this.transcriptMessage = transcriptMessage;
	}
	
	
}

class UpdatedAt {
	@JsonProperty("$date")
	public Object date;

	public Object getDate() {
		return date;
	}

	public void setDate(Object date) {
		this.date = date;
	}
	
}

class Department {
	public String _id;
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isShowOnRegistration() {
		return showOnRegistration;
	}
	public void setShowOnRegistration(boolean showOnRegistration) {
		this.showOnRegistration = showOnRegistration;
	}
	public boolean isShowOnOfflineForm() {
		return showOnOfflineForm;
	}
	public void setShowOnOfflineForm(boolean showOnOfflineForm) {
		this.showOnOfflineForm = showOnOfflineForm;
	}
	public boolean isRequestTagBeforeClosingChat() {
		return requestTagBeforeClosingChat;
	}
	public void setRequestTagBeforeClosingChat(boolean requestTagBeforeClosingChat) {
		this.requestTagBeforeClosingChat = requestTagBeforeClosingChat;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public List<String> getChatClosingTags() {
		return chatClosingTags;
	}
	public void setChatClosingTags(List<String> chatClosingTags) {
		this.chatClosingTags = chatClosingTags;
	}
	public String getOfflineMessageChannelName() {
		return offlineMessageChannelName;
	}
	public void setOfflineMessageChannelName(String offlineMessageChannelName) {
		this.offlineMessageChannelName = offlineMessageChannelName;
	}
	public String getAbandonedRoomsCloseCustomMessage() {
		return abandonedRoomsCloseCustomMessage;
	}
	public void setAbandonedRoomsCloseCustomMessage(String abandonedRoomsCloseCustomMessage) {
		this.abandonedRoomsCloseCustomMessage = abandonedRoomsCloseCustomMessage;
	}
	public String getWaitingQueueMessage() {
		return waitingQueueMessage;
	}
	public void setWaitingQueueMessage(String waitingQueueMessage) {
		this.waitingQueueMessage = waitingQueueMessage;
	}
	public String getDepartmentsAllowedToForward() {
		return departmentsAllowedToForward;
	}
	public void setDepartmentsAllowedToForward(String departmentsAllowedToForward) {
		this.departmentsAllowedToForward = departmentsAllowedToForward;
	}
	public UpdatedAt get_updatedAt() {
		return _updatedAt;
	}
	public void set_updatedAt(UpdatedAt _updatedAt) {
		this._updatedAt = _updatedAt;
	}
	public int getNumAgents() {
		return numAgents;
	}
	public void setNumAgents(int numAgents) {
		this.numAgents = numAgents;
	}
	public boolean enabled;
	public String name;
	public String description;
	public boolean showOnRegistration;
	public boolean showOnOfflineForm;
	public boolean requestTagBeforeClosingChat;
	public String email;
	public List<String> chatClosingTags;
	public String offlineMessageChannelName;
	public String abandonedRoomsCloseCustomMessage;
	public String waitingQueueMessage;
	public String departmentsAllowedToForward;
	public UpdatedAt _updatedAt;
	public int numAgents;
}
