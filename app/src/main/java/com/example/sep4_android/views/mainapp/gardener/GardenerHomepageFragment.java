package com.example.sep4_android.views.mainapp.gardener;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.sep4_android.viewmodels.gardener.GardenerHomepageViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Map;

import es.dmoral.toasty.Toasty;

public class GardenerHomepageFragment extends Fragment {

    private View view;
    private GardenerHomepageViewModel viewModel;
    private ImageView assistantsButton;
    private ImageView notificationButton;
    private TextView notificationNumber;
    private TextView gardenNameTextView;
    private TextView descriptionTextView;
    private TextView addGardenTextView;
    private ImageView addGardenImageView;
    private ConstraintLayout buttonAddGarden;
    private Button buttonViewGarden;
    private ConstraintLayout buttonSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_gardener_homepage, container, false);
        viewModel = new ViewModelProvider(this).get(GardenerHomepageViewModel.class);
        prepareUI();
        prepareOnClickEvents();
        loadValues();
        return view;
    }

    private void prepareUI() {
        buttonAddGarden = view.findViewById(R.id.button_main_add_garden);
        buttonViewGarden = view.findViewById(R.id.button_own_garden_open);
        buttonSettings = view.findViewById(R.id.button_main_settings);
        gardenNameTextView = view.findViewById(R.id.text_own_garden_name);
        descriptionTextView = view.findViewById(R.id.text_own_garden_info);
        addGardenImageView = view.findViewById(R.id.img_main_add_garden);
        addGardenTextView = view.findViewById(R.id.text_main_add_garden);
        notificationButton = view.findViewById(R.id.img_main_notification);
        assistantsButton = view.findViewById(R.id.img_main_assistant);
        notificationNumber = view.findViewById(R.id.text_main_notifications);
    }

    private void loadValues() {
        viewModel.getGarden(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), garden -> {
            if (garden != null) {
                viewModel.initializeGarden(garden.getName());
                viewModel.getLiveGarden().observe(getViewLifecycleOwner(), liveGarden -> {
                    if(liveGarden != null){
                        int notifications = 0;
                        for (Map.Entry<String, Boolean> entry : liveGarden.getAssistantList().entrySet()) {
                            if (!entry.getValue()) {
                                notifications++;
                            }
                        }
                        notificationNumber.setText(String.valueOf(notifications));
                    }
                });
            }
        });
    }

    private void prepareOnClickEvents() {
        viewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            viewModel.getGarden(user.getUid()).observe(getViewLifecycleOwner(), garden -> {
                buttonViewGarden.setOnClickListener(v -> {
                    if (garden != null) {
                        Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_gardenListFragment);
                        return;
                    }
                    Toasty.error(view.getContext(), view.getContext().getString(R.string.no_garden), Toasty.LENGTH_SHORT, true).show();
                });

                if (garden == null) {
                    addGardenTextView.setText(getString(R.string.add_garden));
                    addGardenImageView.setImageResource(R.drawable.ic_baseline_addchart_24);
                    buttonAddGarden.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_addGardenFragment));
                    return;
                }
                String description = garden.getStreet() + " " + garden.getNumber() + "\n" + garden.getCity();
                addGardenImageView.setImageResource(R.drawable.ic_baseline_remove_circle_outline_24);
                descriptionTextView.setText(description);
                gardenNameTextView.setText(garden.getName());
                addGardenTextView.setText(getString(R.string.remove_garden));
                buttonAddGarden.setOnClickListener(v -> viewModel.removeGarden(garden.getName()));
            });
            buttonSettings.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_mainPageFragment_to_settingsFragment));
        });

        notificationButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("listType", "requests");
            Navigation.findNavController(view).navigate(R.id.action_gardenerHomepageFragment_to_assistantListFragment, bundle);
        });

        assistantsButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("listType", "all");
            Navigation.findNavController(view).navigate(R.id.action_gardenerHomepageFragment_to_assistantListFragment, bundle);
        });
    }
}