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
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.viewmodels.AddNewGardenViewModel;

import es.dmoral.toasty.Toasty;

public class AddGardenFragment extends Fragment {

    private AddNewGardenViewModel viewModel;

    private Button confirm;
    private EditText gardenName;
    private EditText gardenLand;
    private EditText gardenStreet;
    private EditText gardenNumber;
    private EditText gardenCity;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewModel = new ViewModelProvider(this).get(AddNewGardenViewModel.class);
        view = inflater.inflate(R.layout.fragment_add_garden, container, false);
        prepareUI();
        prepareOnClickEvents();
        return view;
    }

    private void prepareUI(){
        confirm = view.findViewById(R.id.button_add_garden);
        gardenName = view.findViewById(R.id.input_garden_name);
        gardenLand = view.findViewById(R.id.input_land_area);
        gardenStreet = view.findViewById(R.id.input_street);
        gardenNumber = view.findViewById(R.id.input_street_number);
        gardenCity = view.findViewById(R.id.input_city);
    }

    private void prepareOnClickEvents(){
        confirm.setOnClickListener(v -> {
            viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                viewModel.addNewGarden(new Garden(gardenName.getText().toString(), Double.parseDouble(gardenLand.getText().toString()), gardenCity.getText().toString(), gardenStreet.getText().toString(), gardenNumber.getText().toString(), user.getUid()));
                Toasty.success(view.getContext(), view.getContext().getString(R.string.success_garden), Toasty.LENGTH_SHORT, true).show();
            });
        });
    }
}