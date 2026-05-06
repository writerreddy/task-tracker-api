package com.example.tasktracker.dto.response;

public class InfoResponse {
    private String appName;
    private String appVersion;

    public InfoResponse() {}

    public InfoResponse(String appName, String appVersion) {
        this.appName = appName;
        this.appVersion = appVersion;
    }

    public String getAppName() { return appName; }
    public void setAppName(String appName) { this.appName = appName; }
    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }
}
