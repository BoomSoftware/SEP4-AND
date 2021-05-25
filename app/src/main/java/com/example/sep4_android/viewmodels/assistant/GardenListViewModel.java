package com.example.sep4_android.viewmodels.assistant;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sep4_android.repositories.GardenRepository;
import com.google.firebase.database.Query;

public class GardenListViewModel extends AndroidViewModel {
    private final GardenRepository gardenRepository;

    public GardenListViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = GardenRepository.getInstance(application);
    }

    public Query getAllGardens(){
        return gardenRepository.getAllGardens();
    }

    public void sendAssistantRequest(String gardenName){
        gardenRepository.sendAssistantRequest(gardenName);
    }

    public void removeRequest(String gardenName, String assistantGoogleId){
        gardenRepository.removeAssistant(gardenName, assistantGoogleId);
    }
}
