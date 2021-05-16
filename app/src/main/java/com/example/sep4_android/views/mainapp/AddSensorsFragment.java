package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Sensor;
import com.example.sep4_android.viewmodels.AddSensorViewModel;

public class AddSensorsFragment extends Fragment {

    private View view;
    private AddSensorViewModel viewModel;
    private EditText lightSensor;
    private EditText co2Sensor;
    private EditText tempSensor;
    private Button confirm;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_add_sensors, container, false);
        viewModel = new ViewModelProvider(this).get(AddSensorViewModel.class);
        prepareUI();
        prepareOnClickEvents();
        return view;
    }

    private void prepareUI(){
        lightSensor = view.findViewById(R.id.input_sensors_light);
        co2Sensor = view.findViewById(R.id.input_sensors_co2);
        tempSensor = view.findViewById(R.id.input_sensors_temp);
        confirm = view.findViewById(R.id.button_sensors_confirm);
    }

    private void prepareOnClickEvents(){
        confirm.setOnClickListener(v -> {
            viewModel.confirmAdding(new Sensor(Integer.parseInt(lightSensor.getText().toString()), Integer.parseInt(co2Sensor.getText().toString()), Integer.parseInt(tempSensor.getText().toString())));
            Navigation.findNavController(view).navigate(R.id.action_addSensorsFragment_to_gardenListFragment);
        });
    }


}