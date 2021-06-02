package com.example.sep4_android.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.sep4_android.data.GardenDatabase;
import com.example.sep4_android.data.PlantDAO;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.models.Plant;
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
    private final PlantDAO plantDAO;
    private final ExecutorService executorService;
    private final PlantApi plantApi;
    private final MutableLiveData<List<Measurement>> measurements;
    private final MutableLiveData<List<Measurement>> historicalMeasurements;
    private final MutableLiveData<List<Plant>> plants;
    private final MutableLiveData<ConnectionStatus> connectionStatus;
    private Plant selectedPlant;

    private PlantRepository(Application application) {
        GardenDatabase database = GardenDatabase.getInstance(application);
        measurements = new MutableLiveData<>();
        historicalMeasurements = new MutableLiveData<>();
        plants = new MutableLiveData<>();
        connectionStatus = new MutableLiveData<>();
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
        LiveData<List<Plant>> plants = plantDAO.getPlantsForGarden(gardenName);
        return plants;
    }

    public MutableLiveData<ConnectionStatus> getConnectionStatus() {
        return connectionStatus;
    }

    public void loadPlantsForGardenLive(String gardenName){
        Call<List<Plant>> call = plantApi.getPlantsForGarden(gardenName);
        call.enqueue(new Callback<List<Plant>>() {
            @Override
            public void onResponse(Call<List<Plant>> call, Response<List<Plant>> response) {
                if(response.isSuccessful() && response.body() != null){
                  List<Plant> plantList = response.body();
                  plants.postValue(plantList);
                }
            }

            @Override
            public void onFailure(Call<List<Plant>> call, Throwable t) {}
        });
    }

    public MutableLiveData<List<Plant>> getPlantsForGardenLive(){
        return plants;
    }

    public MutableLiveData<List<Measurement>> getLoadedMeasurements(){
        return measurements;
    }

    public LiveData<Plant> loadPlant(int plantId){
        return plantDAO.getPlantById(plantId);
    }

    public void addPlantToGarden(Plant plant){
        Call<Integer> call = plantApi.addNewPlant(plant);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                System.out.println(response.body());
                if(response.isSuccessful() && response.body() != null && response.body() != -1){
                    plant.setPlantID(response.body());
                    addPlantToLocalDatabase(plant);
                    connectionStatus.setValue(ConnectionStatus.SUCCESS);
                    connectionStatus.setValue(ConnectionStatus.NONE);
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
        });
    }

    public void addPlantToLocalDatabase(Plant plant){
        executorService.execute(() -> {
            if(!plantDAO.checkIfPlantExist(plant.getPlantID())){
                plantDAO.addPlant(plant);
            }
        });
    }

    public Plant getSelectedPlant() {
        return selectedPlant;
    }

    public MutableLiveData<List<Measurement>> getHistoricalMeasurements() {
        return historicalMeasurements;
    }

    public void setSelectedPlant(Plant selectedPlant) {
        this.selectedPlant = selectedPlant;
    }

    public void loadAllMeasurements(int plantId, FrequencyTypes frequencyType, MeasurementTypes measurementType){
        Call<List<Measurement>> call = plantApi.getAllMeasurements(plantId, frequencyType.toString().toLowerCase() ,measurementType.toString());
        call.enqueue(new Callback<List<Measurement>>() {
            @Override
            public void onResponse(Call<List<Measurement>> call, Response<List<Measurement>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    if(frequencyType.equals(FrequencyTypes.HISTORY)){
                        historicalMeasurements.setValue(response.body());
                    }else{
                        measurements.setValue(response.body());
                    }
                    connectionStatus.setValue(ConnectionStatus.SUCCESS);
                    connectionStatus.setValue(ConnectionStatus.NONE);
                    return;
                }
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
            @Override
            public void onFailure(Call<List<Measurement>> call, Throwable t) {
                connectionStatus.setValue(ConnectionStatus.ERROR);
                connectionStatus.setValue(ConnectionStatus.NONE);
            }
        });
    }

    public void clearMeasurements(){
        measurements.setValue(null);
    }

    public void clearHistoricalMeasurements(){
        historicalMeasurements.setValue(null);
    }

    public void removePlantFromGarden(int plantID){
        Call<Void> call = plantApi.removePlant(plantID);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    removePlantFromLocalDatabase(plantID);
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

    public void updatePlantInGarden(Plant plant) {
        Call<Void> call = plantApi.updatePlant(plant.getPlantID(), plant);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.isSuccessful()){
                    executorService.execute(() -> plantDAO.updatePlant(plant));
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

    private void removePlantFromLocalDatabase(int plantId){
        executorService.execute(() -> plantDAO.removePlant(plantId));
    }
}
