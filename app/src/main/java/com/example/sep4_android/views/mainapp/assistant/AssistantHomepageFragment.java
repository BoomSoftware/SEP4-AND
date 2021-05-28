package com.example.sep4_android.views.mainapp.assistant;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.sep4_android.R;

public class AssistantHomepageFragment extends Fragment {

    private View view;
    private ConstraintLayout browseGardenButton;
    private ConstraintLayout ownGardensButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_assistant_homepage, container, false);
        prepareUI();
        prepareOnclickEvents();
        return view;
    }

    private void prepareUI(){
        browseGardenButton = view.findViewById(R.id.button_assistant_browse);
        ownGardensButton = view.findViewById(R.id.button_assistant_own);
    }

    private void prepareOnclickEvents(){
        ownGardensButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_assistantHomepageFragment_to_ownGardenListFragment));
        browseGardenButton.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_assistantHomepageFragment_to_gardenListFragment));
    }
}