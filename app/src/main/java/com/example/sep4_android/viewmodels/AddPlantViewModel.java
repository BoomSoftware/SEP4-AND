package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sep4_android.models.Plant;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;

public class AddPlantViewModel extends AndroidViewModel {
    private final PlantRepository plantRepository;
    private final GardenRepository gardenRepository;

    public AddPlantViewModel(@NonNull Application application) {
        super(application);
        plantRepository = PlantRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance(application);
    }

    public void addNewPlantToGarden(Plant plant){
        plantRepository.addNewPlantToGarden(plant);
    }
}
