package com.example.sep4_android.models;

import android.net.Uri;

public class User {
    private String name;
    private String avatarUrl;
    private String email;
    private String uid;

    public User() {}

    public User(String name, String avatarUrl, String email, String uid) {
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.email = email;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
