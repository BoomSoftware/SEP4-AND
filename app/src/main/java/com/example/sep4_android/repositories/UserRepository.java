package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.data.StatusDAO;
import com.example.sep4_android.models.User;
import com.example.sep4_android.models.UserLiveData;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.models.CurrentUserLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private static UserRepository instance;
    private final Application application;
    private final CurrentUserLiveData currentUser;
    private final ExecutorService executorService;
    private final StatusDAO statusDAO;


    private UserRepository(Application application){
        GardenDatabase database = GardenDatabase.getInstance(application);
        statusDAO = database.statusDAO();
        executorService = Executors.newFixedThreadPool(2);
        this.application = application;
        this.currentUser = new CurrentUserLiveData();
    }

    public static synchronized UserRepository getInstance(Application application){
        if(instance == null){
            instance = new UserRepository(application);
        }
        return instance;
    }

    public void createUser(UserStatus userStatus){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        User user = new User(firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), firebaseUser.getEmail(), firebaseUser.getUid());
        FirebaseDatabase.getInstance().getReference().child("users").child(userStatus.getUserGoogleId()).setValue(user);
        executorService.execute(() -> statusDAO.createNewUser(userStatus));
    }

    public UserLiveData getUser(String userGoogleId){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userGoogleId);
        return new UserLiveData(ref);
    }

    public LiveData<UserStatus> getStatus(String uid){
       return statusDAO.getStatusForUser(uid);
    }

    public LiveData<FirebaseUser> getCurrentUser(){
        return currentUser;
    }

    public void signOut(){
        AuthUI.getInstance().signOut(application.getApplicationContext());
    }
}
