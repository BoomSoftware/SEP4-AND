package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.data.StatusDAO;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.models.UserLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private static UserRepository instance;
    private final Application application;
    private final UserLiveData currentUser;
    private final ExecutorService executorService;
    private final StatusDAO statusDAO;


    private UserRepository(Application application){
        GardenDatabase database = GardenDatabase.getInstance(application);
        statusDAO = database.statusDAO();
        executorService = Executors.newFixedThreadPool(2);
        this.application = application;
        this.currentUser = new UserLiveData();
    }

    public static synchronized UserRepository getInstance(Application application){
        if(instance == null){
            instance = new UserRepository(application);
        }
        return instance;
    }

    public void createUser(UserStatus userStatus){
        executorService.execute(() -> statusDAO.createNewUser(userStatus));
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
