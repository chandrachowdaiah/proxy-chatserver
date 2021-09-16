package com.nsl.chatserver.request;

public class LiveChatConfigurationRequest {
    private String token;
    private String department;
    public LiveChatConfigurationRequest(){}
    
    public LiveChatConfigurationRequest(String token, String department) {
    	this.token = token;
    	this.department = department;
    }
    
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
}
