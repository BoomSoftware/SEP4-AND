package com.example.sep4_android.views.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sep4_android.MainAppActivity;
import com.example.sep4_android.R;
import com.example.sep4_android.viewmodels.login.SignUpViewModel;

public class SignUpFragment extends Fragment {
    private View view;
    private SignUpViewModel loginViewModel;
    private Button buttonGardener;
    private Button buttonAssistant;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginViewModel = new ViewModelProvider(this).get(SignUpViewModel.class);
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        prepareUI();
        prepareOnClickEvents();
        return view;
    }

    private void prepareUI() {
        buttonGardener = view.findViewById(R.id.button_sign_up_gardener);
        buttonAssistant = view.findViewById(R.id.button_sign_up_assistant);
    }

    private void prepareOnClickEvents() {
        buttonGardener.setOnClickListener(v -> {
            loginViewModel.createUser(true);
            openMainActivity();
        });
        buttonAssistant.setOnClickListener(v -> {
            loginViewModel.createUser(false);
            openMainActivity();
        });
    }

    private void openMainActivity(){
        Intent intent = new Intent(getContext(), MainAppActivity.class);
        startActivity(intent);
    }
}