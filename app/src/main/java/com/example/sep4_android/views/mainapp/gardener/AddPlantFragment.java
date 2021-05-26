package com.example.sep4_android.views.mainapp.gardener;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.sep4_android.R;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.viewmodels.gardener.AddPlantViewModel;

public class AddPlantFragment extends Fragment {

    private AddPlantViewModel viewModel;
    private Button confirm;
    private EditText plantHeight;
    private EditText stageGrowth;
    private EditText soilType;
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
    }


    private void loadPlantValues(){
        viewModel.getPlant(plantId).observe(getViewLifecycleOwner(), plant -> {
            gardenName = plant.getGardenName();
            plantHeight.setText(String.valueOf(plant.getHeight()));
            stageGrowth.setText(plant.getStageOfGrowth());
            soilType.setText(plant.getSoilType());
            soilVolume.setText(String.valueOf(plant.getOwnSoilVolume()));
            commonPlantName.setText(plant.getCommonPlantName());
            categoryName.setText(plant.getCategoryName());
            gardenLocation.setText(plant.getGardenLocation());
        });
    }

    private void prepareOnClickEvents(){
        if(plantId == -1) {
            confirm.setOnClickListener(v -> viewModel.addNewPlantToGarden(constructPlant()));
        }
        else{
            confirm.setOnClickListener(v  -> viewModel.updatePlantInGarden(constructPlant()));
        }
    }

    private Plant constructPlant(){
        Plant plant =  new Plant(
                gardenName,
                Integer.parseInt(plantHeight.getText().toString()),
                stageGrowth.getText().toString(),
                soilType.getText().toString(),
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
}