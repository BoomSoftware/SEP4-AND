package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.sep4_android.data.GardenDAO;
import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.GardenLiveData;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GardenRepository {
    private static GardenRepository instance;
    private GardenDAO gardenDAO;
    private ExecutorService executorService;
    private GardenLiveData garden;


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

    public LiveData<Garden> getGarden(String gardenName){
        return gardenDAO.getGarden(gardenName);
    }

    public LiveData<Garden> getOwnGarden(String userGoogleId){
        return gardenDAO.getOwnGarden(userGoogleId);
    }

    public void synchronizeGarden(Garden garden){
        executorService.execute(() -> {
            Garden temp = gardenDAO.searchForGarden(garden.getName());
            if(temp == null){
                gardenDAO.createGarden(garden);
            }
        });
    }

    public void createGarden(Garden garden){
        FirebaseDatabase.getInstance().getReference().child("gardens").addListenerForSingleValueEvent(new ValueEventListener() {
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
                    FirebaseDatabase.getInstance().getReference().child("gardens").child(garden.getName()).setValue(garden);
                    addGardenToLocalDatabase(garden);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void removeGarden(String gardenName) {
        FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName).removeValue();
        executorService.execute(() -> gardenDAO.removeGarden(gardenName));
    }

    public Query getAllGardens(){
        return FirebaseDatabase.getInstance().getReference().child("gardens");
    }

    public void initializeGarden(String gardenName){
        garden = new GardenLiveData(FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName));
    }

    public GardenLiveData getLiveGarden(){
        return garden;
    }


    public void sendAssistantRequest(String gardenName){
        String assistantGoogleId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Map<String, Object> map = new HashMap<>();
        map.put(assistantGoogleId, false);
        FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName).child("assistantList").updateChildren(map);
    }

    public void approveAssistant(String gardenName, String assistantGoogleId){
        Map<String, Object> map = new HashMap<>();
        map.put(assistantGoogleId, true);
        FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName).child("assistantList").updateChildren(map);
    }

    public void removeAssistant(String gardenName, String assistantGoogleId){
        FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName).child("assistantList").child(assistantGoogleId).removeValue();
    }

    private void addGardenToLocalDatabase(Garden garden){
        executorService.execute(() -> gardenDAO.createGarden(garden));
    }
}
