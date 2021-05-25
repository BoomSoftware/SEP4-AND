package com.example.sep4_android.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.annotations.NotNull;

@Entity
public class UserStatus {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userGoogleId;
    private boolean status;


    public UserStatus(String userGoogleId, boolean status) {
        this.userGoogleId = userGoogleId;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserGoogleId() {
        return userGoogleId;
    }

    public void setUserGoogleId(String userGoogleId) {
        this.userGoogleId = userGoogleId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
