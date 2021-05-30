package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.GardenLiveData;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {
    private final UserRepository userRepository;
    private final GardenRepository gardenRepository;
    private final PlantRepository plantRepository;

    public MainActivityViewModel(Application app) {
        super(app);
        userRepository = UserRepository.getInstance(app);
        gardenRepository = GardenRepository.getInstance(app);
        plantRepository = PlantRepository.getInstance(app);
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void windowAction(boolean status) {
        gardenRepository.windowAction(status);
    }

    public LiveData<UserStatus> getUserStatus(String userGoogleID) {
        return userRepository.getStatus(userGoogleID);
    }

    public void initializeGarden(String gardenName) {
        gardenRepository.initializeGarden(gardenName);
    }

    public LiveData<Garden> getGarden(String userGoogleId) {
        return gardenRepository.getOwnGarden(userGoogleId);
    }

    public GardenLiveData getLiveGarden() {
        return gardenRepository.getLiveGarden();
    }

    public void signOut() {
        userRepository.signOut();
    }

    public void removeUser() {
        userRepository.removeUser();
    }

    public void removePlant(int plantId) {
        plantRepository.removePlantFromGarden(plantId);
    }

    public void removeGarden(String gardenName) {
        gardenRepository.removeGarden(gardenName);
    }

    public LiveData<Garden> getOwnGarden(String userGoogleId) {
        return gardenRepository.getOwnGarden(userGoogleId);
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName) {
        return plantRepository.getPlantsForGarden(gardenName);
    }

    public void removeUserStatus(String userGoogleId) {
        userRepository.removeUserStatus(userGoogleId);
    }

    public void removeUserFromOtherGardens(String userGoogleId) {
        userRepository.removeUserFromOtherGardens(userGoogleId);
    }

    public void updateUserStatus(String userGoogleId, boolean status) {
        userRepository.updateUserStatus(userGoogleId, status);
    }

    public LiveData<ConnectionStatus> getConnectionStatus() {
        return gardenRepository.getConnectionStatus();
    }
}
