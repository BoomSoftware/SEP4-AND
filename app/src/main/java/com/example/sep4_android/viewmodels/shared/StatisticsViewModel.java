package com.example.sep4_android.viewmodels.shared;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.repositories.PlantRepository;

import java.util.List;

public class StatisticsViewModel extends AndroidViewModel {

    public final PlantRepository plantRepository;

    public StatisticsViewModel(@NonNull Application application) {
        super(application);
        plantRepository = PlantRepository.getInstance(application);
    }

    public MutableLiveData<List<Measurement>> getHistoricalMeasurements(){
        return plantRepository.getHistoricalMeasurements();
    }

    public void clearHistoricalMeasurements(){
        plantRepository.clearHistoricalMeasurements();
    }

    public void loadMeasurements(int plantId, FrequencyTypes frequencyType, MeasurementTypes measurementType){
        plantRepository.loadAllMeasurements(plantId, frequencyType, measurementType);
    }
}
