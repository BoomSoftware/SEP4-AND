package com.example.sep4_android.views.mainapp.shared;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
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
import com.example.sep4_android.viewmodels.shared.PlantOverviewViewModel;

import java.text.DecimalFormat;


public class PlantOverviewFragment extends DialogFragment {

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
    private Button buttonTakePic;
    private int plantID;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_overview, container, false);
        viewModel = new ViewModelProvider(this).get(PlantOverviewViewModel.class);
        plantID = getArguments().getInt("plantId");
        formatter = new DecimalFormat("0.##");
        prepareUI();
        prepareOnClickEvents();
        loadData();
        dispatchTakePictureIntent();
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
        buttonTakePic = view.findViewById(R.id.button_plant_take_pic);
    }

    private void prepareOnClickEvents() {
        statisticsButton.setOnClickListener(v -> createDialog());
    }

    private void loadData(){
        viewModel.loadPlant(plantID).observe(getViewLifecycleOwner(), plant -> {
            plantName.setText(plant.getCategoryName());
            plantLocation.setText(plant.getGardenLocation());
            loadMeasurements();
        });
    }
    private void loadMeasurements(){
        viewModel.loadMeasurements(plantID, FrequencyTypes.LATEST, MeasurementTypes.ALL);
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
    }

    private void createDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.measurements)
                .setItems(R.array.measurements_entries, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create();
    }

    private void dispatchTakePictureIntent() {
        buttonTakePic.setOnClickListener(v -> {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        });
    }

    //    private void galleryAddPic() {
//        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//        File f = new File(currentPhotoPath);
//        Uri contentUri = Uri.fromFile(f);
//        mediaScanIntent.setData(contentUri);
//        this.sendBroadcast(mediaScanIntent);
//    }
}