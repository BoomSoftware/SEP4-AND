package com.example.sep4_android.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.repositories.GardenRepository;

public class AddNewGardenViewModel extends ViewModel {
    private GardenRepository gardenRepository;

    public AddNewGardenViewModel() {
        gardenRepository = GardenRepository.getInstance();
    }

    public void addNewGarden(Garden garden){
        gardenRepository.addNewGarden(garden);
    }

    public LiveData<Integer> getGardenId(){
        return gardenRepository.getGardenId();
    }
}
