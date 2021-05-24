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
    private EditText plantWidth;
    private EditText stageGrowth;
    private EditText soilType;
    private EditText soilVolume;
    private EditText commonPlantName;
    private EditText categoryName;
    private EditText gardenLocation;
    private String gardenName;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(AddPlantViewModel.class);
        gardenName = getArguments().getString("gardenName");
        view = inflater.inflate(R.layout.fragment_add_plant, container, false);
        prepareUI();
        prepareOnClickEvents();
        return view;
    }

    private void prepareUI() {
        confirm = view.findViewById(R.id.button_add_plant);
        plantHeight = view.findViewById(R.id.input_plant_height);
        plantWidth = view.findViewById(R.id.input_plant_width);
        stageGrowth = view.findViewById(R.id.input_plant_stage_growth);
        soilType = view.findViewById(R.id.input_plant_soil_type);
        soilVolume = view.findViewById(R.id.input_plant_soil_volume);
        commonPlantName = view.findViewById(R.id.input_common_plant_name);
        categoryName = view.findViewById(R.id.input_plant_category_name);
        gardenLocation = view.findViewById(R.id.input_plant_garden_location);
    }

    private void prepareOnClickEvents(){
        confirm.setOnClickListener(v -> viewModel.addNewPlantToGarden(new Plant(
                gardenName,
                Integer.parseInt(plantHeight.getText().toString()),
                Integer.parseInt(plantWidth.getText().toString()),
                stageGrowth.getText().toString(),
                soilType.getText().toString(),
                Integer.parseInt(soilVolume.getText().toString()),
                commonPlantName.getText().toString(),
                categoryName.getText().toString(),
                gardenLocation.getText().toString()
                )));
    }
}