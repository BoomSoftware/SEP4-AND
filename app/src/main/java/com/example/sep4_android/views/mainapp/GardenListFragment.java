package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.PlantAdapter;
import com.example.sep4_android.viewmodels.GardenViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import es.dmoral.toasty.Toasty;

public class GardenListFragment extends Fragment implements PlantAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private View view;
    private PlantAdapter plantAdapter;
    private FloatingActionButton addPlantButton;
    private GardenViewModel gardenViewModel;
    private String gardenName;
    private TextView emptyGarden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_garden_list, container, false);
        gardenViewModel = new ViewModelProvider(this).get(GardenViewModel.class);
        prepareUI();
        prepareRecyclerView();
        prepareOnClickEvents();
        return view;
    }


    private void prepareUI() {
        addPlantButton = view.findViewById(R.id.button_add_plant);
        recyclerView = view.findViewById(R.id.recycler);
        emptyGarden = view.findViewById(R.id.empty_garden);
        emptyGarden.setVisibility(View.GONE);
    }

    private void prepareOnClickEvents() {
        addPlantButton.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("gardenName", gardenName);
            Navigation.findNavController(view).navigate(R.id.action_gardenListFragment_to_addPlantFragment, bundle);
        });
    }


    private void prepareRecyclerView() {
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(), LinearLayoutManager.VERTICAL, false));

        plantAdapter = new PlantAdapter(this);
        gardenViewModel.getCurrentUser().observe(getViewLifecycleOwner(), user -> {
            gardenViewModel.getGardenInfo(user.getUid()).observe(getViewLifecycleOwner(), garden -> {
                if(garden != null){
                    gardenName = garden.getName();
                    gardenViewModel.getPlantsForGarden(garden.getName()).observe(getViewLifecycleOwner(), plants -> {
                        if(!plants.isEmpty()){
                            emptyGarden.setVisibility(View.GONE);
                            plantAdapter.setPlants(plants);
                            return;
                        }
                        emptyGarden.setVisibility(View.VISIBLE);
                    });
                }
            });
        });
        recyclerView.setAdapter(plantAdapter);
        setSwipeEvent();
    }

    @Override
    public void onClick(int plantId) {
        Bundle bundle = new Bundle();
        bundle.putInt("plantId", plantId);
        Navigation.findNavController(view).navigate(R.id.action_gardenListFragment_to_plantOverviewFragment, bundle);
    }

    private void setSwipeEvent(){
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                gardenViewModel.removePlantFromGarden(plantAdapter.getItemAt(viewHolder.getAdapterPosition()).getPlantID());
                Toasty.success(view.getContext(), view.getContext().getString(R.string.remove_plant), Toast.LENGTH_SHORT, true).show();
            }
        }).attachToRecyclerView(recyclerView);
    }
}