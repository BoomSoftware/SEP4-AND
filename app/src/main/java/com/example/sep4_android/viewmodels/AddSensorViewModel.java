package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sep4_android.models.Sensor;
import com.example.sep4_android.repositories.PlantRepository;

public class AddSensorViewModel extends AndroidViewModel {
    private PlantRepository plantRepository;


    public AddSensorViewModel(@NonNull Application application) {
        super(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public void confirmAdding(Sensor sensor){
        plantRepository.confirm(sensor);
    }
}
