package com.example.sep4_android.viewmodels.gardener;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.PlantRepository;

public class AddPlantViewModel extends AndroidViewModel {
    private final PlantRepository plantRepository;

    public AddPlantViewModel(@NonNull Application application) {
        super(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public void addNewPlantToGarden(Plant plant){
        plantRepository.addPlantToGarden(plant);
    }

    public LiveData<Plant> getPlant(int plantId){
        return plantRepository.loadPlant(plantId);
    }

    public void updatePlantInGarden(Plant plant) {
        plantRepository.updatePlantInGarden(plant);
    }
}
