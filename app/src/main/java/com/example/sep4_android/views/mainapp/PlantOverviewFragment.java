package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.viewmodels.PlantOverviewViewModel;

import java.text.DecimalFormat;


public class PlantOverviewFragment extends Fragment {

    private View view;
    private PlantOverviewViewModel viewModel;
    private TextView tempValue;
    private TextView co2Value;
    private TextView lightValue;
    private DecimalFormat formatter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_overview, container, false);
        viewModel = new ViewModelProvider(this).get(PlantOverviewViewModel.class);
        formatter = new DecimalFormat("0.##");
        prepareUI();
        loadData();
        return view;
    }

    private void prepareUI(){
        tempValue = view.findViewById(R.id.text_plant_temp);
        co2Value = view.findViewById(R.id.text_plant_co2);
        lightValue = view.findViewById(R.id.text_plant_light);
    }

    private void loadData(){
        viewModel.getLoadedPlant().observe(getViewLifecycleOwner(), plant -> {

            //REMEMBER TO CHANGE THE PLANT_ID VALUE!!!!!!!
            viewModel.loadMeasurements(3, FrequencyTypes.LATEST, MeasurementTypes.ALL);
            viewModel.getLoadedMeasurements().observe(getViewLifecycleOwner(), measurements -> {
                for (Measurement measurement : measurements){
                    if(measurement.getMeasurementType().equals(MeasurementTypes.TEMP.toString())){
                        tempValue.setText(formatter.format(measurement.getMeasurementValue()));
                    }
                    if(measurement.getMeasurementType().equals(MeasurementTypes.CO2.toString())){
                        co2Value.setText(formatter.format(measurement.getMeasurementValue()));
                    }
                    if(measurement.getMeasurementType().equals(MeasurementTypes.LIGHT.toString())){
                        lightValue.setText(formatter.format(measurement.getMeasurementValue()));
                    }
                }
            });
        });
    }
}