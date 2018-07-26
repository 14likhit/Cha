package com.example.likhit.cha.model;

public class NavDrawerItem {
    private boolean showNotify;
    private String title;

    public NavDrawerItem() {
    }

    public NavDrawerItem(boolean showNotify, String title) {
        this.showNotify = showNotify;
        this.title = title;
    }

    public boolean isShowNotify() {
        return showNotify;
    }

    public String getTitle() {
        return title;
    }

    public void setShowNotify(boolean showNotify) {
        this.showNotify = showNotify;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
