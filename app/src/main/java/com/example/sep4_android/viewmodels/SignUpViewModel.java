package com.example.sep4_android.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.UserStatusLiveData;
import com.example.sep4_android.repositories.UserRepository;
import com.google.firebase.auth.FirebaseUser;

public class SignUpViewModel extends AndroidViewModel {
    private UserRepository userRepository;

    public SignUpViewModel(Application application){
        super(application);
        userRepository = UserRepository.getInstance(application);
    }

    public void createUser(boolean isOwner){
        String userId = userRepository.getCurrentUser().getValue().getUid();
        userRepository.createUser(userId,isOwner);
    }
}
