package com.example.sep4_android.viewmodels.assistant;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.PlantRepository;
import com.google.firebase.database.Query;

public class OwnGardenListViewModel extends AndroidViewModel {
    private final GardenRepository gardenRepository;
    private final PlantRepository plantRepository;

    public OwnGardenListViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = GardenRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public Query getOwnGardens(){
        return gardenRepository.getAllGardens();
    }
}
