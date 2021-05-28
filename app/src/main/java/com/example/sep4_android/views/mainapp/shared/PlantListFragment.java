package com.example.sep4_android.views.mainapp.shared;

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
import com.example.sep4_android.models.Plant;
import com.example.sep4_android.viewmodels.shared.PlantListViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class PlantListFragment extends Fragment implements PlantAdapter.OnClickListener {

    private RecyclerView recyclerView;
    private View view;
    private PlantAdapter plantAdapter;
    private FloatingActionButton addPlantButton;
    private PlantListViewModel gardenViewModel;
    private String gardenName;
    private TextView emptyGarden;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_plant_list, container, false);
        gardenViewModel = new ViewModelProvider(this).get(PlantListViewModel.class);
        gardenName = getArguments().getString("gardenName");
        prepareUI();
        prepareRecyclerView();
        prepareOnClickEvents();
        personalizeView();
        return view;
    }


    private void prepareUI() {
        addPlantButton = view.findViewById(R.id.button_add_plant);
        recyclerView = view.findViewById(R.id.recycler);
        emptyGarden = view.findViewById(R.id.empty_garden);
        emptyGarden.setVisibility(View.GONE);
    }

    private void personalizeView() {
        gardenViewModel.getUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), status -> {
            if (!status.isStatus()) {
                addPlantButton.setVisibility(View.GONE);
            } else {
                addPlantButton.setVisibility(View.VISIBLE);
                plantAdapter.setAccess(true);
                setSwipeEvent();
            }
        });
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
            gardenViewModel.getUserStatus(user.getUid()).observe(getViewLifecycleOwner(), status -> {
                if(status.isStatus()){
                    gardenViewModel.getPlantsForGarden(gardenName).observe(getViewLifecycleOwner(), this::displayInformation);
                    return;
                }
                    gardenViewModel.loadPlantsForGardenLive(gardenName);
                    gardenViewModel.getPlantsForGardenLive().observe(getViewLifecycleOwner(), this::displayInformation);
            });
        });
        recyclerView.setAdapter(plantAdapter);
    }

    private void displayInformation(List<Plant> plants){
        if (!plants.isEmpty()) {
            emptyGarden.setVisibility(View.GONE);
            System.out.println(plants);
            plantAdapter.setPlants(plants);
            return;
        }
        emptyGarden.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Plant plant) {
        gardenViewModel.getUserStatus(FirebaseAuth.getInstance().getCurrentUser().getUid()).observe(getViewLifecycleOwner(), status -> {
            if(!status.isStatus()){
                gardenViewModel.setSelectedPlant(plant);
            }
            Bundle bundle = new Bundle();
            bundle.putInt("plantId", plant.getPlantID());
            Navigation.findNavController(view).navigate(R.id.action_gardenListFragment_to_plantOverviewFragment, bundle);
        });

    }

    @Override
    public void onEdit(int plantId) {
        Bundle bundle = new Bundle();
        bundle.putInt("plantId", plantId);
        Navigation.findNavController(view).navigate(R.id.action_gardenListFragment_to_addPlantFragment, bundle);
    }

    private void setSwipeEvent() {
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