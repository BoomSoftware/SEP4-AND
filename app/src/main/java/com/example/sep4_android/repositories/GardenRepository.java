package com.example.sep4_android.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.models.Garden;
import com.example.sep4_android.networking.GardenApi;
import com.example.sep4_android.networking.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GardenRepository {
    private static GardenRepository instance;
    private MutableLiveData<Integer> gardenId;

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

    public void addNewGarden(Garden garden){
        GardenApi gardenApi = ServiceGenerator.getGardenApi();
        Call<Integer> call = gardenApi.createNewGarden(garden);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful()){
                    gardenId.setValue(response.body().intValue());
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                gardenId.setValue(-1);
            }
        });
    }

}
