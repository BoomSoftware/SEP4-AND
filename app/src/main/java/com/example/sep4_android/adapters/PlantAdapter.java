package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Plant;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.ViewHolder> {

    private List<Plant> plants;
    private OnClickListener onClickListener;

    public PlantAdapter(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        plants = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_plant, parent, false);
        return new PlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Plant currentPlant = plants.get(position);
        holder.plantLocation.setText(currentPlant.getGardenLocation());
        holder.plantName.setText(currentPlant.getCategoryName());
        holder.viewPlant.setOnClickListener(v -> {
            onClickListener.onClick(currentPlant.getPlantID());
        });
    }


    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plantName;
        TextView plantLocation;
        Button viewPlant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.text_plant_item_name);
            viewPlant = itemView.findViewById(R.id.button_item_plant_view);
            plantLocation = itemView.findViewById(R.id.text_item_plant_location);
        }
    }

    public interface OnClickListener {
        void onClick(int plantId);
    }
}
