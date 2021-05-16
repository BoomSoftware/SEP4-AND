package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class GardenViewModel extends AndroidViewModel {
    private GardenRepository gardenRepository;
    private PlantRepository plantRepository;
    private UserRepository userRepository;


    public GardenViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = GardenRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName){
        return plantRepository.getPlantsForGarden(gardenName);
    }

    public void loadPlant(int plantId){
        plantRepository.loadPlant(plantId);
    }

    public LiveData<Garden> getGardenInfo(String userGoogleId){
        return gardenRepository.getGarden(userGoogleId);
    }
}
