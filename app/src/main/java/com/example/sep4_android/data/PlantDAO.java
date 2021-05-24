package com.example.sep4_android.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.sep4_android.models.Plant;

import java.util.List;

@Dao
public interface PlantDAO {
    @Insert
    void addPlant(Plant plant);

    @Update
    void updatePlant(Plant plant);

    @Query("DELETE FROM Plant WHERE plantID=:plantID")
    void removePlant(int plantID);

    @Query("SELECT * FROM Plant WHERE plantID=:plantId")
    LiveData<Plant> getPlantById(int plantId);

    @Query("SELECT * FROM Plant WHERE gardenName=:gardenName")
    LiveData<List<Plant>> getPlantsForGarden(String gardenName);
}
