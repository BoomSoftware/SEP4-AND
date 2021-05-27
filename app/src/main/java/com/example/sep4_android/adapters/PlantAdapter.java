package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private boolean access;

    public PlantAdapter(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        plants = new ArrayList<>();
        access = false;
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
            onClickListener.onClick(currentPlant);
        });

        holder.editPlant.setOnClickListener(v -> {
            onClickListener.onEdit(currentPlant.getPlantID());
        });


        if(access){
            holder.editPlant.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return plants.size();
    }

    public void setPlants(List<Plant> plants) {
        this.plants = plants;
        notifyDataSetChanged();
    }

    public void setAccess(boolean access){
        this.access = access;
        notifyDataSetChanged();
    }

    public Plant getItemAt(int position){
        return plants.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView plantName;
        TextView plantLocation;
        ImageView editPlant;
        Button viewPlant;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            plantName = itemView.findViewById(R.id.text_plant_item_name);
            viewPlant = itemView.findViewById(R.id.button_item_plant_view);
            plantLocation = itemView.findViewById(R.id.text_item_plant_location);
            editPlant = itemView.findViewById(R.id.img_item_plant_edit);
        }
    }

    public interface OnClickListener {
        void onClick(Plant plant);
        void onEdit(int plantId);
    }
}
