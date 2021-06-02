package com.example.sep4_android.views.mainapp.shared;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sep4_android.R;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.FrequencyTypes;
import com.example.sep4_android.models.Measurement;
import com.example.sep4_android.models.MeasurementTypes;
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.viewmodels.shared.PlantOverviewViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;

import es.dmoral.toasty.Toasty;

public class PlantOverviewFragment extends DialogFragment {

    private View view;
    private PlantOverviewViewModel viewModel;
    private TextView tempValue;
    private TextView co2Value;
    private TextView lightValue;
    private TextView humidityValue;
    private TextView plantInfo;
    private TextView plantName;
    private TextView plantLocation;
    private ImageView plantImg;
    private DecimalFormat formatter;
    private Button statisticsButton;
    private Button buttonTakePic;
    private int plantID;
    private ActivityResultLauncher<Intent> pictureActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_overview, container, false);
        viewModel = new ViewModelProvider(this).get(PlantOverviewViewModel.class);
        viewModel.clearMeasurements();
        plantID = getArguments().getInt("plantId");
        formatter = new DecimalFormat("0.##");
        prepareUI();
        prepareOnClickEvents();
        loadData();
        registerOnActivityResultListener();
        checkConnectionStatus();
        return view;
    }

    private void prepareUI() {
        tempValue = view.findViewById(R.id.text_plant_temp);
        co2Value = view.findViewById(R.id.text_plant_co2);
        lightValue = view.findViewById(R.id.text_plant_light);
        humidityValue = view.findViewById(R.id.text_plant_humidity);
        humidityValue = view.findViewById(R.id.text_plant_humidity);
        plantLocation = view.findViewById(R.id.text_plant_location);
        plantInfo = view.findViewById(R.id.text_plant_info);
        plantName = view.findViewById(R.id.text_plant_name);
        plantImg = view.findViewById(R.id.img_plant);
        statisticsButton = view.findViewById(R.id.button_plant_statistics);
        buttonTakePic = view.findViewById(R.id.button_plant_take_pic);
    }

    private void registerOnActivityResultListener() {
        pictureActivity = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Bundle extras = result.getData().getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        savePictureInFirebase(imageBitmap);
                    }
                });
    }

    private void savePictureInFirebase(Bitmap imageBitmap) {
        String fileName = plantID + ".jpg";
        StorageReference storageReference = FirebaseStorage.getInstance().getReference(fileName);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        storageReference.putBytes(data);
    }

    private void prepareOnClickEvents() {
        statisticsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("plantID", plantID);
            Navigation.findNavController(view).navigate(R.id.action_plantOverviewFragment_to_statisticsFragment, bundle);
        });
        buttonTakePic.setOnClickListener(v -> {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            pictureActivity.launch(pictureIntent);
        });
    }

    private void loadData() {
        String fileName = plantID + ".jpg";
        FirebaseStorage.getInstance().getReference().child(fileName).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(view).load(uri).into(plantImg);
        });

        viewModel.getUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), status -> {
            if (status.isStatus()) {
                viewModel.loadPlant(plantID).observe(getViewLifecycleOwner(), plant -> {
                    setPlantInfoText(plant);
                    loadMeasurements();
                });
                return;
            }
            Plant plant = viewModel.getSelectedPlant();
            setPlantInfoText(plant);
            loadMeasurements();
        });
    }

    private void setPlantInfoText(Plant plant) {
        String plantInfoText =
                getString(R.string.plant_height) + ": " + plant.getHeight() + "\n\n" +
                        getString(R.string.plant_category_name) + ": " + plant.getCategoryName() + "\n\n" +
                        getString(R.string.plant_soil_type) + ": " + plant.getSoilType() + "\n\n" +
                        getString(R.string.plant_soil_volume) + ": " + plant.getOwnSoilVolume() + "\n\n" +
                        getString(R.string.plant_growth_stage) + ": " + plant.getStageOfGrowth() + "\n\n";
        plantInfo.setText(plantInfoText);
        plantName.setText(plant.getCategoryName());
        plantLocation.setText(plant.getGardenLocation());
    }

    private void loadMeasurements() {
        viewModel.loadMeasurements(plantID, FrequencyTypes.LATEST, MeasurementTypes.ALL);
        viewModel.getLoadedMeasurements().observe(getViewLifecycleOwner(), measurements -> {
            if (measurements != null) {
                for (Measurement measurement : measurements) {
                    if (measurement.getMeasurementType().equals(MeasurementTypes.TEMP.toString())) {
                        String value = formatter.format(measurement.getMeasurementValue()) + " " + getResources().getStringArray(R.array.units)[0];
                        tempValue.setText(value);
                    }
                    if (measurement.getMeasurementType().equals(MeasurementTypes.CO2.toString())) {
                        String value = formatter.format(measurement.getMeasurementValue()) + " " + getResources().getStringArray(R.array.units)[1];
                        co2Value.setText(value);
                    }
                    if (measurement.getMeasurementType().equals(MeasurementTypes.LIGHT.toString())) {
                        String value = formatter.format(measurement.getMeasurementValue()) + " " + getResources().getStringArray(R.array.units)[3];
                        lightValue.setText(value);
                    }
                    if (measurement.getMeasurementType().equals(MeasurementTypes.HUM.toString())) {
                        String value = formatter.format(measurement.getMeasurementValue()) + " " + getResources().getStringArray(R.array.units)[2];
                        humidityValue.setText(value);
                    }
                }
            }

        });
    }

    private void checkConnectionStatus() {
        viewModel.getConnectionStatus().observe(getViewLifecycleOwner(), status -> {
            if (status.equals(ConnectionStatus.ERROR)) {
                Toasty.error(view.getContext(), view.getContext().getString(R.string.measurements_error), Toast.LENGTH_SHORT, true).show();
            }
            if (status.equals(ConnectionStatus.SUCCESS)) {
                Toasty.success(view.getContext(), view.getContext().getString(R.string.measurement_success), Toast.LENGTH_SHORT, true).show();
            }
        });
    }

}