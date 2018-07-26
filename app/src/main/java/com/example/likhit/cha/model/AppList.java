package com.example.likhit.cha.model;

public class AppList {
    private String appName;
    private int imageId;


    public AppList() {
    }

    public AppList(String appName, int imageId) {
        this.appName = appName;
        this.imageId = imageId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
