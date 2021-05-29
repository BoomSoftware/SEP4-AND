package com.example.sep4_android.viewmodels.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.repositories.PlantRepository;
import com.example.sep4_android.repositories.UserRepository;

import java.util.List;

public class PlantOverviewViewModel extends AndroidViewModel {

    private final UserRepository userRepository;
    private final PlantRepository plantRepository;

    public PlantOverviewViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public LiveData<Plant> loadPlant(int plantId){
        return plantRepository.loadPlant(plantId);
    }

    public Plant getSelectedPlant(){
        return plantRepository.getSelectedPlant();
    }

    public LiveData<UserStatus> getUserStatus(String userGoogleId){
        return userRepository.getStatus(userGoogleId);
    }

    public LiveData<ConnectionStatus> getConnectionStatus(){
        return plantRepository.getConnectionStatus();
    }

    public MutableLiveData<List<Measurement>> getLoadedMeasurements(){
        return plantRepository.getLoadedMeasurements();
    }

    public void clearMeasurements(){
        plantRepository.clearMeasurements();
    }

    public void loadMeasurements(int plantId, FrequencyTypes frequencyType, MeasurementTypes measurementType){
        plantRepository.loadAllMeasurements(plantId, frequencyType, measurementType);
    }
}
