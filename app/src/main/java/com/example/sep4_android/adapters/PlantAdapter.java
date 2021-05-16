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
        View view = inflater.inflate(R.layout.recycleviewdesign,parent,false);
        return new PlantAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.button.setOnClickListener(v->{
           onClickListener.onClick(plants.get(position).getPlantID());
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
        TextView name;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.plantName);
            button = itemView.findViewById(R.id.buttonOpen);
        }
    }

    public interface OnClickListener {
        void onClick(int plantId);
    }
}
