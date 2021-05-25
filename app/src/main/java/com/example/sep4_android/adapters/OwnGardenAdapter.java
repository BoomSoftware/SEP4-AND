package com.example.sep4_android.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sep4_android.R;
import com.example.sep4_android.models.Garden;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;

public class OwnGardenAdapter extends FirebaseRecyclerAdapter<Garden, OwnGardenAdapter.ViewHolder> {

    private final OnItemClickListener listener;

    public OwnGardenAdapter(@NonNull FirebaseRecyclerOptions<Garden> options, OnItemClickListener listener) {
        super(options);
        this.listener = listener;
    }


    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Garden model) {
        if(model.getAssistantList().containsKey(FirebaseAuth.getInstance().getCurrentUser().getUid()) && model.getAssistantList().get(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            holder.gardenName.setText(model.getName());
            String gardenInfo = R.string.area + " " + model.getLandArea() + "\n" +
                    R.string.address + " " + model.getStreet() + " " + model.getStreet() + " " + model.getCity() + "\n" +
                    R.string.assistant_no + " " + model.getAssistantList().size();
            holder.gardenInfo.setText(gardenInfo);
            holder.viewGardenButton.setOnClickListener(v -> {
                listener.onOpenGarden(model.getName());
            });
        }
        else {
            holder.itemView.setVisibility(View.GONE);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_own_garden, parent, false));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView gardenName;
        TextView gardenInfo;
        Button viewGardenButton;

        public ViewHolder(@NotNull View itemView){
            super(itemView);
            gardenName = itemView.findViewById(R.id.text_own_garden_name);
            gardenInfo = itemView.findViewById(R.id.text_own_garden_info);
            viewGardenButton = itemView.findViewById(R.id.button_own_garden_open);
        }
    }

    public interface OnItemClickListener {
        void onOpenGarden(String gardenName);
    }
}
