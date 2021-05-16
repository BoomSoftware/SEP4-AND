package com.example.sep4_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.sep4_android.models.Garden;

@Dao
public interface GardenDAO {
    @Insert
    void createGarden(Garden garden);

    @Query("DELETE FROM GARDEN WHERE ownerGoogleId=:userGoogleId")
    void removeGarden(String userGoogleId);

    @Query("SELECT * FROM Garden WHERE ownerGoogleId=:userGoogleId")
    LiveData<Garden> getGarden(String userGoogleId);
}
