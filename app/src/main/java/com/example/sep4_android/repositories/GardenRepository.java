package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
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

    public LiveData<Garden> getGarden(String userGoogleId){
        return gardenDAO.getGarden(userGoogleId);
    }

    private void addGardenToLocalDatabase(Garden garden){
        executorService.execute(() -> gardenDAO.createGarden(garden));
    }

    public void createGarden(Garden garden){
        myRef = FirebaseDatabase.getInstance().getReference().child("gardens");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean result = true;
                for(DataSnapshot gardenFromFirebase: snapshot.getChildren()){
                    if(gardenFromFirebase.getValue().equals(garden.getName())){
                        result = false;
                        break;
                    }
                }
                if(result){
                    myRef.child(garden.getName()).setValue(garden);
                    addGardenToLocalDatabase(garden);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void removeGarden(String gardenName) {
        myRef = FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName);
        myRef.removeValue();
        executorService.execute(() -> gardenDAO.removeGarden(gardenName));
    }

}
