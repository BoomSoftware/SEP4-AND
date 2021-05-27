package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.data.StatusDAO;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.models.User;
import com.example.sep4_android.models.UserLiveData;
import com.example.sep4_android.models.UserStatus;
import com.example.sep4_android.models.CurrentUserLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UserRepository {
    private static UserRepository instance;
    private final Application application;
    private final GardenDatabase database;
    private final CurrentUserLiveData currentUser;
    private final ExecutorService executorService;
    private final StatusDAO statusDAO;
    private final PlantRepository plantRepository;
    private final GardenRepository gardenRepository;


    private UserRepository(Application application){
        database = GardenDatabase.getInstance(application);
        statusDAO = database.statusDAO();
        plantRepository = PlantRepository.getInstance(application);
        gardenRepository = GardenRepository.getInstance(application);
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

    public void removeUser(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.delete();
        FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid()).removeValue();
    }

    public void removeUserStatus(String userGoogleId){
        executorService.execute(() -> statusDAO.removeUserStatus(userGoogleId));
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
        FirebaseAuth.getInstance().signOut();
    }

    public void updateUserStatus(String userGoogleId, boolean status){
        executorService.execute(() -> statusDAO.updateStatus(userGoogleId, status));
    }

    public void removeUserFromOtherGardens(String userGoogleId){
        FirebaseDatabase.getInstance().getReference().child("gardens").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot gardenFromFirebase: snapshot.getChildren()){
                    for(DataSnapshot assistant : gardenFromFirebase.child("assistantList").getChildren()){
                        if(assistant.getKey().equals(userGoogleId)){
                            assistant.getRef().removeValue();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
