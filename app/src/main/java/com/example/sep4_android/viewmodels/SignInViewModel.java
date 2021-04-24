package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.UserStatusLiveData;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class SignInViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public SignInViewModel(Application application){
        super(application);
        userRepository = UserRepository.getInstance(application);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return userRepository.getCurrentUser();
    }

    public UserStatusLiveData getStatus(){
        String userId = userRepository.getCurrentUser().getValue().getUid();
        return userRepository.getStatus(userId);
    }
}
