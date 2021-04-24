package com.example.sep4_android.viewmodels;

import androidx.lifecycle.ViewModel;

import com.example.sep4_android.repositories.GardenRepository;

public class GardenViewModel extends ViewModel {
    private GardenRepository gardenRepository;

    public GardenViewModel() {
        gardenRepository = GardenRepository.getInstance();
    }
}
