package com.example.sep4_android.viewmodels.gardener;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class GardenerHomepageViewModel extends AndroidViewModel {
    private final GardenRepository gardenRepository;
    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public GardenerHomepageViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public LiveData<Garden> getGarden(String userGoogleId) {
        return gardenRepository.getOwnGarden(userGoogleId);
    }

    public LiveData<ConnectionStatus> getConnectionStatus() {
        return gardenRepository.getConnectionStatus();
    }

    public LiveData<FirebaseUser> getCurrentUser() {
        return userRepository.getCurrentUser();
    }

    public void removeGarden(String gardenName) {
        gardenRepository.removeGarden(gardenName);
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName) {
        return plantRepository.getPlantsForGarden(gardenName);
    }

    public void removePlant(int plantId) {
        plantRepository.removePlantFromGarden(plantId);
    }


}
