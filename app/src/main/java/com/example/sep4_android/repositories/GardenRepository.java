package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.sep4_android.data.GardenDAO;
import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.models.Garden;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GardenRepository {
    private static GardenRepository instance;
    private GardenDAO gardenDAO;
    private DatabaseReference myRef;
    private ExecutorService executorService;


    private GardenRepository(Application application) {
        GardenDatabase database = GardenDatabase.getInstance(application);
        gardenDAO = database.gardenDAO();
        executorService = Executors.newFixedThreadPool(2);
    }

    public static synchronized GardenRepository getInstance(Application application) {
        if (instance == null) {
            instance = new GardenRepository(application);
        }
        return instance;
    }

    public void addGardenToLocalDatabase(Garden garden){
        executorService.execute(() -> gardenDAO.createGarden(garden));
    }

    public LiveData<Garden> getGarden(String userGoogleId){
        return gardenDAO.getGarden(userGoogleId);
    }

    public void createGarden(Garden garden) {
        myRef = FirebaseDatabase.getInstance().getReference().child("users");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag = false;
                for(DataSnapshot users : dataSnapshot.getChildren()){
                    if(users.child("garden").child("gardenName").getValue() != null && users.child("garden").child("gardenName").getValue().equals(garden.getName())){
                        flag = true;
                        break;
                    }
                }
                if(!flag){
                    myRef.child(garden.getOwnerGoogleId()).child("garden").child("gardenName").setValue(garden.getName());
                    myRef.child(garden.getOwnerGoogleId()).child("garden").child("land").setValue(garden.getLandArea());
                    myRef.child(garden.getOwnerGoogleId()).child("garden").child("city").setValue(garden.getCity());
                    myRef.child(garden.getOwnerGoogleId()).child("garden").child("street").setValue(garden.getStreet());
                    myRef.child(garden.getOwnerGoogleId()).child("garden").child("number").setValue(garden.getNumber());

                    addGardenToLocalDatabase(garden);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void removeGarden(String userGoogleId) {
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(userGoogleId).child("garden");
        myRef.removeValue();

        executorService.execute(() -> gardenDAO.removeGarden(userGoogleId));
    }

}
