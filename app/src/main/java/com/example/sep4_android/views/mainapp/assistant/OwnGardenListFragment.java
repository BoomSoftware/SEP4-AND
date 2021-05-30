package com.example.sep4_android.views.mainapp.assistant;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.OwnGardenAdapter;
import com.example.sep4_android.models.Garden;
import com.example.sep4_android.viewmodels.assistant.OwnGardenListViewModel;
import com.firebase.ui.common.ChangeEventType;
import com.firebase.ui.database.ChangeEventListener;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class OwnGardenListFragment extends Fragment implements OwnGardenAdapter.OnItemClickListener {

    private View view;
    private OwnGardenListViewModel viewModel;
    private FirebaseRecyclerOptions<Garden> options;
    private TextView emptyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_own_garden_list, container, false);
        viewModel = new ViewModelProvider(this).get(OwnGardenListViewModel.class);
        emptyList = view.findViewById(R.id.no_garden);
        loadData();
        prepareRecyclerView();
        return view;
    }

    @Override
    public void onOpenGarden(String gardenName) {
        Bundle bundle = new Bundle();
        bundle.putString("gardenName", gardenName);
        Navigation.findNavController(view).navigate(R.id.action_ownGardenListFragment_to_plantListFragment, bundle);
    }

    private void loadData() {
        Query allGardens = viewModel.getOwnGardens();
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

    private void prepareRecyclerView() {
        RecyclerView recyclerView = view.findViewById(R.id.own_garden_recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        OwnGardenAdapter adapter = new OwnGardenAdapter(options, this);
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }
}