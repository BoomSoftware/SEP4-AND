package com.example.sep4_android.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Plant;

import java.util.ArrayList;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private ArrayList<Plant> plantArrayList;

    public PlantAdapter(ArrayList<Plant> plantArrayList) {
        this.plantArrayList=plantArrayList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycleviewdesign,parent,false);
        return new PlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.button.setOnClickListener(v->{
           Bundle bundle = new Bundle();
           bundle.putInt("plantId", plantArrayList.get(position).getPlant_ID());
           Navigation.findNavController(holder.itemView).navigate(R.id.action_gardenListFragment_to_addPlantFragment,bundle);
       });
    }


    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plantName);
            button = itemView.findViewById(R.id.buttonOpen);
        }

    }
}
