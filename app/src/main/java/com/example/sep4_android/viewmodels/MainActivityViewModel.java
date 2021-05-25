package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class MainActivityViewModel extends AndroidViewModel {
    private final UserRepository userRepository;

    public MainActivityViewModel(Application app){
        super(app);
        userRepository = UserRepository.getInstance(app);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public LiveData<UserStatus> getUserStatus(String userGoogleID) {
        return userRepository.getStatus(userGoogleID);
    }

    public void signOut() {
        userRepository.signOut();
    }
}
