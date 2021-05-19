package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.viewmodels.PlantOverviewViewModel;

import org.w3c.dom.Text;

import java.text.DecimalFormat;


public class PlantOverviewFragment extends Fragment {

    private View view;
    private PlantOverviewViewModel viewModel;
    private TextView tempValue;
    private TextView co2Value;
    private TextView lightValue;
    private TextView humidityValue;
    private TextView plantName;
    private TextView plantLocation;
    private ImageView plantImg;
    private DecimalFormat formatter;
    private Button statisticsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_overview, container, false);
        viewModel = new ViewModelProvider(this).get(PlantOverviewViewModel.class);
        formatter = new DecimalFormat("0.##");
        prepareUI();
        prepareOnClickEvents();
        loadData();
        return view;
    }

    private void prepareUI(){
        tempValue = view.findViewById(R.id.text_plant_temp);
        co2Value = view.findViewById(R.id.text_plant_co2);
        lightValue = view.findViewById(R.id.text_plant_light);
        humidityValue = view.findViewById(R.id.text_plant_humidity);
        humidityValue = view.findViewById(R.id.text_plant_humidity);
        plantLocation = view.findViewById(R.id.text_plant_location);
        plantName = view.findViewById(R.id.text_plant_name);
        plantImg = view.findViewById(R.id.img_plant);
        statisticsButton = view.findViewById(R.id.button_plant_statistics);
    }

    private void prepareOnClickEvents() {
        statisticsButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_plantOverviewFragment_to_statisticsFragment));
    }

    private void loadData(){
        viewModel.getLoadedPlant().observe(getViewLifecycleOwner(), plant -> {
            plantName.setText(plant.getCategoryName());
            plantLocation.setText(plant.getGardenLocation());
            //REMEMBER TO CHANGE THE PLANT_ID VALUE!!!!!!!
            viewModel.loadMeasurements(3, FrequencyTypes.LATEST, MeasurementTypes.ALL);
            viewModel.getLoadedMeasurements().observe(getViewLifecycleOwner(), measurements -> {
                for (Measurement measurement : measurements){
                    if(measurement.getMeasurementType().equals(MeasurementTypes.TEMP.toString())){
                        String value = formatter.format(measurement.getMeasurementValue()) + " \u00B0C";
                        tempValue.setText(value);
                    }
                    if(measurement.getMeasurementType().equals(MeasurementTypes.CO2.toString())){
                        String value = formatter.format(measurement.getMeasurementValue()) + " PPM";
                        co2Value.setText(value);
                    }
                    if(measurement.getMeasurementType().equals(MeasurementTypes.LIGHT.toString())){
                        String value = formatter.format(measurement.getMeasurementValue()) + " lux";
                        lightValue.setText(value);
                    }
                    if(measurement.getMeasurementType().equals(MeasurementTypes.HUM.toString())){
                        String value = formatter.format(measurement.getMeasurementValue()) + " %";
                        humidityValue.setText(value);
                    }
                }
            });
        });
    }
}