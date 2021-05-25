package com.example.sep4_android.viewmodels.gardener;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.sep4_android.models.GardenLiveData;
import com.example.sep4_android.repositories.GardenRepository;

public class AssistantListViewModel extends AndroidViewModel{
    private GardenRepository gardenRepository;

    public AssistantListViewModel(@NonNull Application application) {
        super(application);
        gardenRepository = GardenRepository.getInstance(application);
    }

    public GardenLiveData getLiveGarden(){
        return gardenRepository.getLiveGarden();
    }

    public void approveAssistant(String gardenName ,String assistantGoogleId){
        gardenRepository.approveAssistant(gardenName, assistantGoogleId);
    }
    public void removeAssistant(String gardenName, String assistantGoogleId){
        gardenRepository.removeAssistant(gardenName, assistantGoogleId);
    }
}
