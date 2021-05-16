package com.example.sep4_android.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;

@Database(entities = {Plant.class, Garden.class}, version = 7)
public abstract class GardenDatabase extends RoomDatabase {
    private static GardenDatabase instance;
    public abstract PlantDAO plantDAO();
    public abstract GardenDAO gardenDAO();

    public static synchronized GardenDatabase getInstance(Context context){
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    GardenDatabase.class, "garden_database").fallbackToDestructiveMigration().build();
        }
        return instance;
    }
}
