package com.example.sep4_android.networking;

import com.example.sep4_android.models.Plant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PlantApi {
    @POST("/plant")
    Call<Void> addNewPlant(@Body Plant plant);


}
