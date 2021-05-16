package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.data.PlantDAO;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.models.PlantWithSensor;
import com.example.sep4_android.models.Sensor;
import com.example.sep4_android.networking.PlantApi;
import com.example.sep4_android.networking.ServiceGenerator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlantRepository {
    private static PlantRepository instance;
    private PlantDAO plantDAO;
    private ExecutorService executorService;
    private PlantApi plantApi;
    private LiveData<Plant> loadedPlant;
    private MutableLiveData<List<Measurement>> measurements;
    private Plant plant;

    private PlantRepository(Application application) {
        GardenDatabase database = GardenDatabase.getInstance(application);
        measurements = new MutableLiveData<>();
        plantDAO = database.plantDAO();
        plantApi = ServiceGenerator.getPlantApi();
        executorService = Executors.newFixedThreadPool(2);
    }

    public static PlantRepository getInstance(Application application){
        if(instance == null){
            instance = new PlantRepository(application);
        }
        return instance;
    }

    public LiveData<List<Plant>> getPlantsForGarden(String gardenName){
        return plantDAO.getPlantsForGarden(gardenName);
    }

    public LiveData<Plant> getLoadedPlant(){
        return loadedPlant;
    }

    public MutableLiveData<List<Measurement>> getLoadedMeasurements(){
        return measurements;
    }

    public void loadPlant(int plantId){
        loadedPlant = plantDAO.getPlantById(plantId);
    }

    public void addNewPlantToGarden(Plant plant){
        this.plant = plant;
    }


    public void confirm(Sensor sensor){
        Call<Integer> call = plantApi.addNewPlant(new PlantWithSensor(plant, sensor));
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.isSuccessful() && response.body() != null && response.body() != -1){
                    plant.setPlantID(response.body());
                    executorService.execute(() -> plantDAO.addPlant(plant));
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {}
        });
    }

    public void loadAllMeasurements(int plantId, FrequencyTypes frequencyType, MeasurementTypes measurementType){
        Call<List<Measurement>> call = plantApi.getAllMeasurements(plantId, frequencyType.toString().toLowerCase() ,measurementType.toString());
        call.enqueue(new Callback<List<Measurement>>() {
            @Override
            public void onResponse(Call<List<Measurement>> call, Response<List<Measurement>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    System.out.println("XXXXXXXXXXXX" + response.body());
                    measurements.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Measurement>> call, Throwable t) {
                System.out.println("XXXXXXXXXXXX" + t.getMessage());
            }
        });
    }

    public void removePlantFromGarden(int plantID){
        Call<Void> call = plantApi.removePlant(plantID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    removePlantFromLocalDatabase(plantID);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {}
        });
    }

    public void addNewPlantToLocalDatabase(Plant plant){
    }

    public void removePlantFromLocalDatabase(int plantId){
        executorService.execute(() -> plantDAO.removePlant(plantId));
    }
}
