package com.example.sep4_android.views.mainapp.assistant;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.GardenAdapter;
import com.example.sep4_android.adapters.OwnGardenAdapter;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.viewmodels.assistant.OwnGardenListViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class OwnGardenListFragment extends Fragment implements OwnGardenAdapter.OnItemClickListener {

    private View view;
    private OwnGardenListViewModel viewModel;
    private FirebaseRecyclerOptions<Garden> options;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_own_garden_list, container, false);
        viewModel = new ViewModelProvider(this).get(OwnGardenListViewModel.class);
        loadData();
        prepareRecyclerView();
        return view;
    }

    private void loadData(){
        options = new FirebaseRecyclerOptions.Builder<Garden>().setQuery(viewModel.getOwnGardens(), Garden.class).build();
    }

    private void prepareRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.own_garden_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        OwnGardenAdapter adapter = new OwnGardenAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onOpenGarden(String gardenName) {
        viewModel.synchronizePlants(gardenName);
        Bundle bundle = new Bundle();
        bundle.putString("gardenName", gardenName);
        Navigation.findNavController(view).navigate(R.id.action_ownGardenListFragment_to_plantListFragment ,bundle);
    }

    @Override
    public void synchronizeGarden(Garden garden) {
        viewModel.synchronizeGarden(garden);
    }
}