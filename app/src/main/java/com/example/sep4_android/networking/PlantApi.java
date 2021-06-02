package com.example.sep4_android.networking;

import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.Plant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PlantApi {
    @POST("/plants")
    Call<Integer> addNewPlant(@Body Plant plant);

    @DELETE("/plants/{plantId}")
    Call<Void> removePlant(@Path("plantId") int plantId);

    @GET("/plants/garden")
    Call<List<Plant>> getPlantsForGarden(@Query("gardenName") String gardenName);

    @PUT("/plants/{plantId}")
    Call<Void> updatePlant(@Path("plantId") int plantId, @Body Plant plant);


    @GET("measurements/{plantId}")
    Call<List<Measurement>> getAllMeasurements(@Path("plantId") int plantId,
                                               @Query("type") String type,
                                               @Query("measurementType") String measurementType);
}
