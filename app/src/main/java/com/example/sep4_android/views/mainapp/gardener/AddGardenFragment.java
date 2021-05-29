package com.example.sep4_android.views.mainapp.gardener;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sep4_android.R;
import com.example.sep4_android.models.ConnectionStatus;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.viewmodels.gardener.AddNewGardenViewModel;

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
        checkCreationStatus();
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
           if(validation()) {
               viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
                   viewModel.addNewGarden(new Garden(gardenName.getText().toString(), Double.parseDouble(gardenLand.getText().toString()), gardenCity.getText().toString(), gardenStreet.getText().toString(), gardenNumber.getText().toString(), user.getUid()));
               });
           }
        });
    }

    private boolean validation(){
        if(gardenName == null || gardenName.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_garden_name), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(gardenLand.getText().toString().equals("") || Double.parseDouble(gardenLand.getText().toString()) < 0) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_garden_land), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(gardenStreet == null || gardenStreet.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_garden_street), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(gardenNumber == null || gardenNumber.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_garden_number), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        if(gardenCity == null || gardenCity.getText().toString().equals("")) {
            Toasty.error(view.getContext(), view.getContext().getString(R.string.empty_field_garden_city), Toast.LENGTH_SHORT, true).show();
            return false;
        }
        return true;
    }

    private void checkCreationStatus(){
        viewModel.getCreationStatus().observe(getViewLifecycleOwner(), status -> {
            if(status.equals(ConnectionStatus.ERROR)){
                Toasty.error(view.getContext(), view.getContext().getString(R.string.not_unique_name), Toasty.LENGTH_SHORT, true).show();
            }
            if(status.equals(ConnectionStatus.SUCCESS)) {
                Toasty.success(view.getContext(), view.getContext().getString(R.string.success_garden), Toasty.LENGTH_SHORT, true).show();
                Navigation.findNavController(view).navigate(R.id.action_addGardenFragment_to_gardenerHomepageFragment);
            }
        });
    }
}