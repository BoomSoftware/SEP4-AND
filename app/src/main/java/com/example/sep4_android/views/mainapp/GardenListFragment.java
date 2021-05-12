package com.example.sep4_android.views.mainapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.example.sep4_android.R;
import com.example.sep4_android.adapters.PlantAdapter;
import com.example.sep4_android.models.Plant;

import java.util.ArrayList;


public class GardenListFragment extends Fragment  {

private RecyclerView recyclerView;
private View view;
private PlantAdapter plantAdapter;
private ArrayList<Plant> plantArrayList;
private Button button;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view= inflater.inflate(R.layout.fragment_garden_list, container, false);
        recyclerView = view.findViewById(R.id.recycler);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.HORIZONTAL,false));

//        ArrayList<Plant> plantArrayList = new ArrayList<>();
//        plantAdapter = new PlantAdapter(plantArrayList,this);
        recyclerView.setAdapter(plantAdapter);
        return view;
    }
//
//    @Override
//    public void onListItemClick(int plantId) {
//        button.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_gardenListFragment_to_addPlantFragment));
//    }
}