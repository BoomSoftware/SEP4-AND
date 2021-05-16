package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sep4_android.models.liveDataModels.UserLiveData;
import com.example.sep4_android.models.liveDataModels.UserStatusLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserRepository {
    private final Application application;
    private final UserLiveData currentUser;
    private static UserRepository instance;
    private DatabaseReference myRef;


    private UserRepository(Application application){
        this.application = application;
        this.currentUser = new UserLiveData();
    }

    public static synchronized UserRepository getInstance(Application application){
        if(instance == null){
            instance = new UserRepository(application);
        }
        return instance;
    }

    public void createUser(String uid, boolean isOwner){
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        myRef.child("status").setValue(isOwner);
    }

    public UserStatusLiveData getStatus(String uid){
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(uid).child("status");
        return new UserStatusLiveData(myRef);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return currentUser;
    }

    public void signOut(){
        AuthUI.getInstance().signOut(application.getApplicationContext());
    }
}
