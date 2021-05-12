package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.UserLiveData;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseUser;

public class AddNewGardenViewModel extends AndroidViewModel {
    private GardenRepository gardenRepository;
    private UserRepository userRepository;

    public AddNewGardenViewModel(Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance();
    }

    public void addNewGarden(Garden garden){
        gardenRepository.createGarden(garden);
    }

    public LiveData<Integer> getGardenId(){
        return gardenRepository.getGardenId();
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }
}
