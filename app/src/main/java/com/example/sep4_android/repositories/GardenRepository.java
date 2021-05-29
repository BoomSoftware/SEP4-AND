package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.sep4_android.data.GardenDAO;
import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.models.GardenLiveData;
import com.example.sep4_android.networking.GardenApi;
import com.example.sep4_android.networking.ServiceGenerator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GardenRepository {
    private static GardenRepository instance;
    private final GardenDAO gardenDAO;
    private final GardenApi gardenApi;
    private final ExecutorService executorService;
    private final MutableLiveData<String> synchronizedGardenName;
    private final MutableLiveData<ConnectionStatus> connectionStatus;
    private final MutableLiveData<ConnectionStatus> creationStatus;
    private GardenLiveData garden;

    private GardenRepository(Application application) {
        GardenDatabase database = GardenDatabase.getInstance(application);
        gardenDAO = database.gardenDAO();
        synchronizedGardenName = new MutableLiveData<>();
        connectionStatus = new MutableLiveData<>();
        creationStatus = new MutableLiveData<>();
        gardenApi = ServiceGenerator.getGardenApi();
        executorService = Executors.newFixedThreadPool(2);
    }

    public static synchronized GardenRepository getInstance(Application application) {
        if (instance == null) {
            instance = new GardenRepository(application);
        }
        return instance;
    }

    public LiveData<ConnectionStatus> getConnectionStatus(){
        return connectionStatus;
    }

    public LiveData<Garden> getOwnGarden(String userGoogleId){
        return gardenDAO.getOwnGarden(userGoogleId);
    }

    public LiveData<ConnectionStatus> getCreatingStatus(){
        return creationStatus;
    }

    public MutableLiveData<String> getSynchronizedGardenName(){
        return synchronizedGardenName;
    }


    public void createGarden(Garden garden){
        FirebaseDatabase.getInstance().getReference().child("gardens").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean result = true;
                for(DataSnapshot gardenFromFirebase: snapshot.getChildren()){
                    if(gardenFromFirebase.getKey().equals(garden.getName())){
                        result = false;
                        creationStatus.setValue(ConnectionStatus.ERROR);
                        creationStatus.setValue(ConnectionStatus.NONE);
                        break;
                    }
                }
                if(result){
                    FirebaseDatabase.getInstance().getReference().child("gardens").child(garden.getName()).setValue(garden);
                    addGardenToLocalDatabase(garden);
                    creationStatus.setValue(ConnectionStatus.SUCCESS);
                    creationStatus.setValue(ConnectionStatus.NONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
        });
    }

    public void windowAction(boolean status){
        Call<Void> call = gardenApi.windowAction(status);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    connectionStatus.setValue(ConnectionStatus.SUCCESS);
                    connectionStatus.setValue(ConnectionStatus.NONE);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
        });
    }

    public void removeGarden(String gardenName) {
        FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName).removeValue((error, ref) -> {
            if(error != null){
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
                return;
            }
        });
        executorService.execute(() -> gardenDAO.removeGarden(gardenName));
    }

    public Query getAllGardens(){
        return FirebaseDatabase.getInstance().getReference().child("gardens");
    }

    public Query getAllOwnsGardens(){
        return FirebaseDatabase.getInstance().getReference().child("gardens").orderByChild("assistantList/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).equalTo(true);

    }

    public void initializeGarden(String gardenName){
        garden = new GardenLiveData(FirebaseDatabase.getInstance().getReference().child("gardens").child(gardenName));
    }

    public GardenLiveData getLiveGarden(){
        return garden;
    }

    public void synchronizeGarden(){
        FirebaseDatabase.getInstance().getReference().child("gardens").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot gardenFromFirebase : snapshot.getChildren()){
                    if(gardenFromFirebase.child("ownerGoogleId").getValue().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                        Garden newGarden = new Garden(gardenFromFirebase.child("name").getValue().toString(),
                                Double.parseDouble(gardenFromFirebase.child("landArea").getValue().toString()),
                                gardenFromFirebase.child("city").getValue().toString(),
                                gardenFromFirebase.child("street").getValue().toString(),
                                gardenFromFirebase.child("number").getValue().toString(),
                                gardenFromFirebase.child("ownerGoogleId").getValue().toString());
                        addGardenToLocalDatabase(newGarden);
                        synchronizedGardenName.setValue(newGarden.getName());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
        });
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
        executorService.execute(() -> {
            if(!gardenDAO.checkIfGardenExist(garden.getName())){
                gardenDAO.createGarden(garden);
            }
        });
    }

}
