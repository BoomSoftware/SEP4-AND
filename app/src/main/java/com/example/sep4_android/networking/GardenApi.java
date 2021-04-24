package com.example.sep4_android.networking;

import com.example.sep4_android.models.Garden;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface GardenApi {
    @POST("garden/")
    Call<Integer> createNewGarden(@Body Garden garden);
}
