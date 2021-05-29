package com.example.sep4_android.viewmodels.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class PlantListViewModel extends AndroidViewModel {
    private final PlantRepository plantRepository;
    private final UserRepository userRepository;

    public PlantListViewModel(@NonNull Application application) {
        super(application);
        plantRepository = PlantRepository.getInstance(application);
        userRepository = UserRepository.getInstance(application);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName){
        return plantRepository.getPlantsForGarden(gardenName);
    }

    public LiveData<ConnectionStatus> getConnectionStatus(){
        return plantRepository.getConnectionStatus();
    }

    public LiveData<List<Plant>> getPlantsForGardenLive(){
        return plantRepository.getPlantsForGardenLive();
    }

    public void loadPlantsForGardenLive(String gardenName){
        plantRepository.loadPlantsForGardenLive(gardenName);
    }

    public LiveData<UserStatus> getUserStatus(String userGoogleId){
        return userRepository.getStatus(userGoogleId);
    }

    public void removePlantFromGarden(int plantId){
        plantRepository.removePlantFromGarden(plantId);
    }

    public void setSelectedPlant(Plant plant){
        plantRepository.setSelectedPlant(plant);
    }
}
