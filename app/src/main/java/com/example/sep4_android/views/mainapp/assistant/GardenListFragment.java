package com.example.sep4_android.views.mainapp.assistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.GardenAdapter;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.viewmodels.assistant.GardenListViewModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GardenListFragment extends Fragment implements GardenAdapter.OnItemClickListener {
    private View view;
    private GardenListViewModel viewModel;
    private FirebaseRecyclerOptions<Garden> options;
    private TextView emptyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_garden_list, container, false);
        viewModel = new ViewModelProvider(this).get(GardenListViewModel.class);
        emptyList = view.findViewById(R.id.no_garden);
        loadData();
        prepareRecyclerView();
        return view;
    }

    @Override
    public void onSendRequestClick(String gardenName) {
        viewModel.sendAssistantRequest(gardenName);
    }

    @Override
    public void onCancelAssistance(String gardenName, String assistantGoogleId) {
        viewModel.removeRequest(gardenName, assistantGoogleId);
    }

    private void loadData(){
        Query allGardens = viewModel.getAllGardens();
        options = new FirebaseRecyclerOptions.Builder<Garden>().setQuery(allGardens, Garden.class).build();
        allGardens.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    emptyList.setVisibility(View.GONE);
                    return;
                }
                emptyList.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void prepareRecyclerView(){
        RecyclerView recyclerView = view.findViewById(R.id.garden_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        GardenAdapter adapter = new GardenAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}