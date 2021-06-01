package com.example.sep4_android.viewmodels.gardener;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;

import java.util.List;

public class SettingsViewModel extends AndroidViewModel {
    private final GardenRepository gardenRepository;
    private final PlantRepository plantRepository;

    public SettingsViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = GardenRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public void synchronizeGarden() {
        gardenRepository.synchronizeGarden();
    }

    public void addPlant(Plant plant) {
        plantRepository.addPlantToLocalDatabase(plant);
    }

    public void loadPlantsForGardenLive(String gardenName) {
        plantRepository.loadPlantsForGardenLive(gardenName);
    }

    public LiveData<List<Plant>> getPlantsForGardenLive() {
        return plantRepository.getPlantsForGardenLive();
    }

    public LiveData<String> getSynchronizedGardenName() {
        return gardenRepository.getSynchronizedGardenName();
    }
}
