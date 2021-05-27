package com.example.sep4_android.viewmodels.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {

    private UserRepository userRepository;
    private GardenRepository gardenRepository;
    private PlantRepository plantRepository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public void removeUser(){
        userRepository.removeUser();
    }

    public void removePlant(int plantId){
        plantRepository.removePlantFromGarden(plantId);
    }

    public void removeGarden(String gardenName){
        gardenRepository.removeGarden(gardenName);
    }

    public LiveData<Garden> getOwnGarden(String userGoogleId){
        return gardenRepository.getOwnGarden(userGoogleId);
    }

    public void signOut(){
        userRepository.signOut();
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName){
        return plantRepository.getPlantsForGarden(gardenName);
    }

    public void removeUserStatus(String userGoogleId){
        userRepository.removeUserStatus(userGoogleId);
    }

    public void removeUserFromOtherGardens(String userGoogleId){
        userRepository.removeUserFromOtherGardens(userGoogleId);
    }
}
