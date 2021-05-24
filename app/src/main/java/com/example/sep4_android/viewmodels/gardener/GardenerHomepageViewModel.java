package com.example.sep4_android.viewmodels.gardener;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.repositories.GardenRepository;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class GardenerHomepageViewModel extends AndroidViewModel {
    private GardenRepository gardenRepository;
    private UserRepository userRepository;

    public GardenerHomepageViewModel(@NonNull Application application) {
        super(application);
        userRepository = UserRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance(application);
    }

    public LiveData<Garden> getGarden(String userGoogleId){
        return gardenRepository.getGarden(userGoogleId);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public void removeGarden(String userGoogleId){
        gardenRepository.removeGarden(userGoogleId);
    }
}
