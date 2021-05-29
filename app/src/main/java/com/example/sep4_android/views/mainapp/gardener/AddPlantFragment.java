package com.example.sep4_android.views.mainapp.gardener;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sep4_android.R;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.viewmodels.gardener.AddPlantViewModel;

import es.dmoral.toasty.Toasty;

public class AddPlantFragment extends Fragment {

    private AddPlantViewModel viewModel;
    private Button confirm;
    private EditText plantHeight;
    private Spinner stageGrowth;
    private Spinner soilType;
    private EditText soilVolume;
    private EditText commonPlantName;
    private EditText categoryName;
    private EditText gardenLocation;
    private String gardenName;
    private int plantId;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddPlantViewModel.class);
        gardenName = getArguments().getString("gardenName");
        view = inflater.inflate(R.layout.fragment_add_plant, container, false);
        plantId = getArguments().getInt("plantId", -1);
        prepareUI();
        checkConnectionStatus();
        prepareOnClickEvents();
        return view;
    }

    private void prepareUI() {
        confirm = view.findViewById(R.id.button_add_plant);
        plantHeight = view.findViewById(R.id.input_plant_height);
        stageGrowth = view.findViewById(R.id.input_plant_stage_growth);
        soilType = view.findViewById(R.id.input_plant_soil_type);
        soilVolume = view.findViewById(R.id.input_plant_soil_volume);
        commonPlantName = view.findViewById(R.id.input_common_plant_name);
        categoryName = view.findViewById(R.id.input_plant_category_name);
        gardenLocation = view.findViewById(R.id.input_plant_garden_location);

        if(plantId != -1){
            loadPlantValues();
            confirm.setText(getString(R.string.plant_update));
        }

        ArrayAdapter<String> spinnerStageAdapter = new ArrayAdapter<>(view.getContext(), R.layout.item_stage_spinner, getResources().getStringArray(R.array.stage_of_growth));
        stageGrowth.setAdapter(spinnerStageAdapter);

        ArrayAdapter<String> spinnerSoilAdapter = new ArrayAdapter<>(view.getContext(), R.layout.item_stage_spinner, getResources().getStringArray(R.array.soil_types));
        soilType.setAdapter(spinnerSoilAdapter);
    }

    private void loadPlantValues(){
        viewModel.getPlant(plantId).observe(getViewLifecycleOwner(), plant -> {
            gardenName = plant.getGardenName();
            plantHeight.setText(String.valueOf(plant.getHeight()));
            selectDefaultValueForStage(plant.getStageOfGrowth());
            selectDefaultValueForSoil(plant.getSoilType());
            soilVolume.setText(String.valueOf(plant.getOwnSoilVolume()));
            commonPlantName.setText(plant.getCommonPlantName());
            categoryName.setText(plant.getCategoryName());
            gardenLocation.setText(plant.getGardenLocation());
        });
    }

    private void prepareOnClickEvents(){
        if(plantId == -1) {
            confirm.setOnClickListener(v -> {
                if(validation()){
                    viewModel.addNewPlantToGarden(constructPlant());
                }

            });
        }
        else{
            confirm.setOnClickListener(v  -> {
                if(validation()){
                    viewModel.updatePlantInGarden(constructPlant());
                }

            });
        }
    }

    private Plant constructPlant(){
        Plant plant =  new Plant(
                gardenName,
                Integer.parseInt(plantHeight.getText().toString()),
                stageGrowth.getSelectedItem().toString(),
                soilType.getSelectedItem().toString(),
                Integer.parseInt(soilVolume.getText().toString()),
                commonPlantName.getText().toString(),
                categoryName.getText().toString(),
                gardenLocation.getText().toString()
        );

        if(plantId != -1){
            plant.setPlantID(plantId);
        }
        return plant;
    }

    private boolean validation() {
        if(plantHeight.getText().toString().equals("") || Integer.parseInt(plantHeight.getText().toString()) < 0) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_plant_height), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(stageGrowth == null) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_stage_growth), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(soilType == null) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_soil_type), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(soilVolume.getText().toString().equals("") || Integer.parseInt(soilVolume.getText().toString()) < 0) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_soil_volume), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(commonPlantName == null || commonPlantName.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_common_plant_name), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(categoryName == null || categoryName.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_category_name), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(gardenLocation == null || gardenLocation.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_common_garden_location), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void selectDefaultValueForStage(String stage){
        for(int i = 0; i < getResources().getStringArray(R.array.stage_of_growth).length; i++){
            if(stage.equals(getResources().getStringArray(R.array.stage_of_growth)[i]))
            stageGrowth.setSelection(i);
        }
    }

    private void selectDefaultValueForSoil(String soil){
        for(int i = 0; i < getResources().getStringArray(R.array.soil_types).length; i++){
            if(soil.equals(getResources().getStringArray(R.array.soil_types)[i]))
                soilType.setSelection(i);
        }
    }

    private void checkConnectionStatus(){
        viewModel.getConnectionStatus().observe(getViewLifecycleOwner(), status -> {
            if(status.equals(ConnectionStatus.ERROR)){
                Toasty.error(view.getContext(), view.getContext().getString(R.string.connection_error), Toast.LENGTH_SHORT, true).show();
            }
            if(status.equals(ConnectionStatus.SUCCESS)){
                Toasty.success(view.getContext(), view.getContext().getString(R.string.action_success), Toast.LENGTH_SHORT, true).show();
            }
        });
    }
}