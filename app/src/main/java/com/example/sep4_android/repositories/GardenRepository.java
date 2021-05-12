package com.example.sep4_android.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.models.Garden;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GardenRepository {
    private static GardenRepository instance;
    private MutableLiveData<Integer> gardenId;
    private DatabaseReference myRef;

    private GardenRepository(){
        gardenId = new MutableLiveData<>();
    }

    public LiveData<Integer> getGardenId(){
        return gardenId;
    }

    public static synchronized GardenRepository getInstance(){
        if(instance == null){
            instance = new GardenRepository();
        }
        return instance;
    }

    public void createGarden(Garden garden){
        myRef = FirebaseDatabase.getInstance().getReference().child("users").child(garden.getOwner_google_id()).child("garden");
        myRef.child("gardenName").setValue(garden.getName());
        myRef.child("land").setValue(garden.getLand_area());
        myRef.child("city").setValue(garden.getCity());
        myRef.child("street").setValue(garden.getStreet());
        myRef.child("number").setValue(garden.getNumber());
    }

}
