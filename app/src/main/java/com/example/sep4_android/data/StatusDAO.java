package com.example.sep4_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sep4_android.models.UserStatus;

@Dao
public interface StatusDAO {
    @Insert
    void createNewUser(UserStatus status);

    @Query("SELECT * FROM UserStatus WHERE userGoogleId=:userGoogleId")
    LiveData<UserStatus> getStatusForUser(String userGoogleId);
}
