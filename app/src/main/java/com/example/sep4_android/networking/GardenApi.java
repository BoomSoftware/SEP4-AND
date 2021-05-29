package com.example.sep4_android.networking;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GardenApi {
    @POST("/control")
    Call<Void> windowAction(@Query("isOpen") boolean status);
}
